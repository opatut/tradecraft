package de.opatut.tradecraft.common;

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
import de.opatut.tradecraft.objects.TileEntityCashRegister;

public class PacketHandler implements IPacketHandler {
	public final static int CODE_CASH_REGISTER_CHANGE_PRICE = 1;
	public final static int CODE_CASH_REGISTER_UPDATE = 2;

	public final static String CHANNEL_CASH_REGISTER = "VendingMachine";
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player playerEntity) {
		if (packet.channel.equals(CHANNEL_CASH_REGISTER)) {
			handleCashRegister(packet, playerEntity);
		}
	}

	private void handleCashRegister(Packet250CustomPayload packet,
			Player player) {
		
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		try {

			int code = inputStream.readInt();
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();

			if (code == CODE_CASH_REGISTER_CHANGE_PRICE) {
				int change = inputStream.readInt();
				TileEntity te = ((EntityPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				if(te == null || !(te instanceof TileEntityCashRegister)) {
					System.err.println("Cannot change price at vending machine - no vending machine at " + x + "|" + y + "|" + z + ".");
				} else {
					// TODO: check player
					((TileEntityCashRegister)te).changePrice(change);
				}
			} else if (code == CODE_CASH_REGISTER_UPDATE) {
				int price = inputStream.readInt();
				
				int ownerLength = inputStream.readInt();
				byte[] b = new byte[ownerLength];
				inputStream.read(b);
				String owner = new String(b);
				
				TileEntity te = ((EntityClientPlayerMP)player).worldObj.getBlockTileEntity(x, y, z);
				if(te == null || !(te instanceof TileEntityCashRegister)) {
					System.err.println("Cannot update vending machine - no vending machine at " + x + "|" + y + "|" + z + ".");
				} else {
					((TileEntityCashRegister)te).update(price, owner);
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
