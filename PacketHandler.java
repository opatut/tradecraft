package de.opatut.tradecraft;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	public final static int CODE_VENDING_MACHINE_CHANGE_PRICE = 1;
	public final static int CODE_VENDING_MACHINE_UPDATE = 2;

	public final static String CHANNEL_VENDING_MACHINE = "VendingMachine";
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player playerEntity) {
		if (packet.channel.equals(CHANNEL_VENDING_MACHINE)) {
			handleVendingMachine(packet, playerEntity);
		}
	}

	private void handleVendingMachine(Packet250CustomPayload packet,
			Player player) {
		
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		try {

			int code = inputStream.readInt();
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();

			if (code == CODE_VENDING_MACHINE_CHANGE_PRICE) {
				int change = inputStream.readInt();
				System.out.println("Change received at server: " + change);
				TileEntity te = ((EntityPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				if(te == null || !(te instanceof VendingMachineTileEntity)) {
					System.err.println("Cannot change price at vending machine - no vending machine at " + x + "|" + y + "|" + z + ".");
				} else {
					// TODO: check player
					((VendingMachineTileEntity)te).changePrice(change);
				}
			} else if (code == CODE_VENDING_MACHINE_UPDATE) {
				int price = inputStream.readInt();
				
				TileEntity te = ((EntityClientPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				if(te == null || !(te instanceof VendingMachineTileEntity)) {
					System.err.println("Cannot update vending machine - no vending machine at " + x + "|" + y + "|" + z + ".");
				} else {
					((VendingMachineTileEntity)te).update(price);
				}
			} else {
				System.err.println("Invalid code for Vending Machine command: " + code);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}
}
