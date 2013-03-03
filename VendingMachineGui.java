package de.opatut.tradecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class VendingMachineGui extends GuiContainer {
	private final static int BUTTON_ADD_1 = 1;
	private final static int BUTTON_ADD_5 = 2;
	private final static int BUTTON_ADD_20 = 3;
	private final static int BUTTON_SUB_1 = 4;
	private final static int BUTTON_SUB_5 = 5;
	private final static int BUTTON_SUB_20 = 6;

	private VendingMachineTileEntity tileEntity;

	public VendingMachineGui(InventoryPlayer inventoryPlayer, VendingMachineTileEntity entity) {
		super(new VendingMachineGuiContainer(inventoryPlayer, entity));
		tileEntity = entity;
		xSize = 176;
		ySize = 241;
	}

	public void initGui() {
		super.initGui();
		// make buttons
		// id, x, y, width, height, text
		controlList.add(new GuiButton(BUTTON_ADD_1,  guiLeft + 109, guiTop + 54, 21, 20, "+1"));
		controlList.add(new GuiButton(BUTTON_ADD_5,  guiLeft + 129, guiTop + 54, 21, 20, "+5"));
		controlList.add(new GuiButton(BUTTON_ADD_20, guiLeft + 149, guiTop + 54, 21, 20, "+20"));
		controlList.add(new GuiButton(BUTTON_SUB_1,  guiLeft + 46,  guiTop + 54, 21, 20, "-1"));
		controlList.add(new GuiButton(BUTTON_SUB_5,  guiLeft + 26,  guiTop + 54, 21, 20, "-5"));
		controlList.add(new GuiButton(BUTTON_SUB_20, guiLeft + 6,   guiTop + 54, 21, 20, "-20"));
	}

	protected void actionPerformed(GuiButton button) {
		int change = 0;
		switch(button.id) {
			case BUTTON_ADD_1: change = 1; break;
			case BUTTON_ADD_5: change = 5; break;
			case BUTTON_ADD_20: change = 20; break;
			case BUTTON_SUB_1: change = -1; break;
			case BUTTON_SUB_5: change = -5; break;
			case BUTTON_SUB_20: change = -20; break;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(5 * 4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(PacketHandler.CODE_VENDING_MACHINE_CHANGE_PRICE);
			outputStream.writeInt(tileEntity.xCoord);
			outputStream.writeInt(tileEntity.yCoord);
			outputStream.writeInt(tileEntity.zCoord);
			outputStream.writeInt(change);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.CHANNEL_VENDING_MACHINE;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		PacketDispatcher.sendPacketToServer(packet);
		tileEntity.changePrice(change);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Vending Machine", 8, 6, 0x404040);

		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
		
		fontRenderer.drawString(tileEntity.getPriceString(), 80, 62, 0x666666, false);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int texture = mc.renderEngine
				.getTexture(CommonProxy.TEXTURE_VENDING_MACHINE_GUI);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
