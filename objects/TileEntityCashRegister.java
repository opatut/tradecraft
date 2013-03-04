package de.opatut.tradecraft.objects;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.opatut.tradecraft.client.ModelCashRegister;
import de.opatut.tradecraft.common.PacketHandler;
import de.opatut.tradecraft.common.TileEntityDirected;

public class TileEntityCashRegister extends TileEntityDirected implements
		IInventory {
	private ItemStack mainSlot;
	private ItemStack[] refillSlots;
	private int price;
	private String owner = "*nobody*";

	public TileEntityCashRegister() {
		refillSlots = new ItemStack[27];
	}

	public int getSizeInventory() {
		return 28;
	}

	public String getOwner() {
		return owner;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0)
			return mainSlot;
		return refillSlots[slot - 1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot == 0) {
			mainSlot = stack;
		} else {
			refillSlots[slot - 1] = stack;
		}

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "de.opatut.tradecraft.cashregister";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot,
						ItemStack.loadItemStackFromNBT(tag));
			}
		}
		price = tagCompound.getInteger("Price");
		owner = tagCompound.getString("Owner");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		tagCompound.setTag("Inventory", itemList);
		tagCompound.setInteger("Price", price);
		tagCompound.setString("Owner", owner.isEmpty() ? "*nobody*" : owner);
	}

	public void changePrice(int amount) {
		price += amount;
		if (price < 0)
			price = 0;

		if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT) {
			sync();
		}
	}

	public void update(int newPrice, String newOwner) {
		price = newPrice;
		owner = newOwner;
		sync();
	}

	public String getPriceString() {
		return price == 0 ? "free" : "" + price;
	}

	/*public void sendUpdate() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(
				6 * 4 + owner.getBytes().length);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(PacketHandler.CODE_CASH_REGISTER_UPDATE);
			outputStream.writeInt(xCoord);
			outputStream.writeInt(yCoord);
			outputStream.writeInt(zCoord);
			outputStream.writeInt(price);

			outputStream.writeInt(owner.getBytes().length);
			outputStream.write(owner.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.CHANNEL_CASH_REGISTER;
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		PacketDispatcher.sendPacketToAllPlayers(packet);
	}*/

	@SideOnly(Side.CLIENT)
	public static class Renderer extends TileEntitySpecialRenderer {
		private ModelCashRegister model = new ModelCashRegister();

		@Override
		public void renderTileEntityAt(TileEntity tileEntity, double x,
				double y, double z, float tick) {
			TileEntityCashRegister cashRegister = (TileEntityCashRegister) tileEntity;

			GL11.glPushMatrix();
			cashRegister.rotateMatrix();

			model.render(cashRegister, x, y, z);

			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glRotatef(-90.f, 0.F, 1.0F, 0.0F);
			float sc = 2.f / 3.f * 1.f / 64.f;
			GL11.glScalef(sc, -sc, -sc);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glDepthMask(false);

			FontRenderer fontRenderer = this.getFontRenderer();

			String s = cashRegister.owner + "/" + cashRegister.getPriceString();
			fontRenderer.drawString(s, -fontRenderer.getStringWidth(s) / 2, 0,
					0);

			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();

			GL11.glPopMatrix(); // rotation
		}

	}

	public void setOwner(String username) {
		owner = username;
	}
}
