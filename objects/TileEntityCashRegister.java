package de.opatut.tradecraft.objects;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.opatut.tradecraft.common.CommonProxy;
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

	@SideOnly(Side.CLIENT)
	public static class Renderer extends TileEntitySpecialRenderer {
		private static class Model extends ModelBase {
			private ModelRenderer bottom, top;
			
			public Model() {
				textureHeight = 64;
				textureWidth = 64;
				
				bottom = new ModelRenderer(this, 0, 17);
		        bottom.addBox(-7F, 0F, -7F, 14, 5, 14);
		
		        top = new ModelRenderer(this, 0, 0);
		        top.addBox(-6F, 3F, -9F, 12, 4, 12);
		        top.rotateAngleX = 0.44F;
			}	
			
			public void render() {
		        top.render(1.f/16.f);
		        bottom.render(1.f/16.f);
			}
		}
		
		private Model model = new Model();
		
		@Override
		public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
			TileEntityCashRegister cashRegister = (TileEntityCashRegister) tileEntity;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glPushMatrix();
	        GL11.glTranslated(x + 0.5, y, z + 0.5);
			cashRegister.rotateMatrix();

	        GL11.glPushMatrix();
	        ForgeHooksClient.bindTexture(CommonProxy.TEXTURE_CASH_REGISTER_MODEL, 0);
	        model.render();
	        GL11.glPopMatrix();

			GL11.glPushMatrix();
			float sc = 2.f / 3.f * 1.f / 64.f;
			GL11.glTranslated(0, 0.3, 0.5 - 1.f/16.f + 0.001f);
			GL11.glScalef(sc, -sc, -sc);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glDepthMask(false);

			FontRenderer fontRenderer = this.getFontRenderer();
			String s = cashRegister.owner + "/" + cashRegister.getPriceString() + "/" + cashRegister.direction;
			fontRenderer.drawString(s, - fontRenderer.getStringWidth(s) / 2, 0, 0);

			//cashRegister.mainSlot.getItem();
			
			GL11.glDepthMask(true);
			GL11.glPopMatrix();

			GL11.glPopMatrix(); // rotation

			// GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        // GL11.glEnable(GL11.GL_LIGHTING);
		}

	}

	public void setOwner(String username) {
		owner = username;
	}
}
