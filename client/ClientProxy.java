package de.opatut.tradecraft.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import de.opatut.tradecraft.common.CommonProxy;
import de.opatut.tradecraft.common.PacketHandler;
import de.opatut.tradecraft.objects.TileEntityCashRegister;
import de.opatut.tradecraft.objects.TileEntityCashRegister.Renderer;

public class ClientProxy extends CommonProxy {
	// Client memory
	public int playerMoney = -1;
	
	public void requestBalance() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2 * 4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(PacketHandler.CODE_MONEY_FETCH);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.CHANNEL_MONEY;
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		PacketDispatcher.sendPacketToServer(packet);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		
		MinecraftForgeClient.preloadTexture(TEXTURE_ICONS);

		// Do we even need this?
		// Maybe the users don't use this feature, then this only fills the RAM/VRAM....
		// .. it works without preloading anyway. 
		// MinecraftForgeClient.preloadTexture(TEXTURE_VENDING_MACHINE_GUI);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCashRegister.class, new TileEntityCashRegister.Renderer());
	}
}