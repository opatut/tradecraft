package de.opatut.tradecraft.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class Bank {
	public static final int START_AMOUNT = 2000;

	private Map<String, Integer> playerMoney = new HashMap<String, Integer>();
	private String loadFile;

	public int getBalance(String username) {
		if (playerMoney.containsKey(username))
			return playerMoney.get(username);
		else
			return 0;
	}

	private void setBalance(String username, int amount) {
		if(!playerMoney.containsKey(username))
			registerPlayer(username);
		
		playerMoney.put(username, amount);
		// TODO: try to notify the user about the change
		
		save(loadFile); // auto-backup
	}

	public boolean transact(String from, String to, int amount) {
		if (from != null && getBalance(from) < amount) {
			return false;
		}

		if (from != null)
			setBalance(from, getBalance(from) - amount);
		if (to != null)
			setBalance(to, getBalance(to) + amount);
		return true;
	}

	public boolean save(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(playerMoney);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean load(String filename) {
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			playerMoney = (HashMap<String, Integer>) in.readObject();
			in.close();
			fileIn.close();
			loadFile = filename;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void debugPrint() {
		System.out.println("The bank has " + playerMoney.size() + " registered players.");
		for (String k : playerMoney.keySet()) {
			System.out.println(k + ": " + playerMoney.get(k) + " credits");
		}
	}

	public boolean registerPlayer(String username) {
		if (playerMoney.containsKey(username))
			return false;


		playerMoney.put(username, 0);
		setBalance(username, START_AMOUNT);
		return true;
	}
	
	public void sendBalance(Player player) {
		String username = ((EntityPlayerMP) player).username;
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2 * 4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(PacketHandler.CODE_MONEY_PUSH);
			outputStream.writeInt(getBalance(username));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.CHANNEL_MONEY;
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		PacketDispatcher.sendPacketToPlayer(packet, player);
	}
}
