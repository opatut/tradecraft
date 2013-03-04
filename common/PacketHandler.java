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
import de.opatut.tradecraft.Main;
import de.opatut.tradecraft.client.ClientProxy;
import de.opatut.tradecraft.objects.TileEntityCashRegister;

public class PacketHandler implements IPacketHandler {
	public final static int CODE_CASH_REGISTER_CHANGE_PRICE = 1;
	public final static int CODE_CASH_REGISTER_UPDATE = 2;
	public final static int CODE_MONEY_FETCH = 3;
	public final static int CODE_MONEY_PUSH = 4;

	public final static String CHANNEL_MONEY = "Money";
	public final static String CHANNEL_CASH_REGISTER = "VendingMachine";

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player playerEntity) {
		if (packet.channel.equals(CHANNEL_CASH_REGISTER)) {
			handleCashRegister(packet, playerEntity);
		} else if (packet.channel.equals(CHANNEL_MONEY)) {
			handleMoney(packet, playerEntity);
		}
	}

	private void handleMoney(Packet250CustomPayload packet, Player playerEntity) {
		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		try {
			int code = inputStream.readInt();
			if (code == CODE_MONEY_FETCH) {
				// client requests the balance
				CommonProxy.bank.sendBalance(playerEntity);
			} else if (code == CODE_MONEY_PUSH) {
				// client receives balance
				((ClientProxy) Main.proxy).playerMoney = inputStream.readInt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleCashRegister(Packet250CustomPayload packet, Player player) {

		DataInputStream inputStream = new DataInputStream(
				new ByteArrayInputStream(packet.data));

		try {

			int code = inputStream.readInt();
			int x = inputStream.readInt();
			int y = inputStream.readInt();
			int z = inputStream.readInt();

			if (code == CODE_CASH_REGISTER_CHANGE_PRICE) {
				int change = inputStream.readInt();
				TileEntity te = ((EntityPlayerMP) player).worldObj
						.getBlockTileEntity(x, y, z);
				if (te == null || !(te instanceof TileEntityCashRegister)) {
					System.err
							.println("Cannot change price at vending machine - no vending machine at "
									+ x + "|" + y + "|" + z + ".");
				} else {
					// TODO: check player
					((TileEntityCashRegister) te).changePrice(change);
				}
			} else if (code == CODE_CASH_REGISTER_UPDATE) {
				int price = inputStream.readInt();

				int ownerLength = inputStream.readInt();
				byte[] b = new byte[ownerLength];
				inputStream.read(b);
				String owner = new String(b);

				TileEntity te = ((EntityClientPlayerMP) player).worldObj
						.getBlockTileEntity(x, y, z);
				if (te == null || !(te instanceof TileEntityCashRegister)) {
					System.err
							.println("Cannot update vending machine - no vending machine at "
									+ x + "|" + y + "|" + z + ".");
				} else {
					((TileEntityCashRegister) te).update(price, owner);
				}
			} else {
				System.err.println("Invalid code for Vending Machine command: "
						+ code);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
