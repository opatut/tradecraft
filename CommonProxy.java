package de.opatut.tradecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class CommonProxy {
	public static final String IMAGE_DIR = "/de/opatut/tradecraft/";
	
	public static final String TEXTURE_ICONS = IMAGE_DIR + "icons.png";
	public static final String TEXTURE_VENDING_MACHINE_GUI = IMAGE_DIR + "vending-machine-gui.png";
	public static final String TEXTURE_VENDING_MACHINE_MODEL = IMAGE_DIR + "vending-machine-model.png";
	
	public static final Bank bank = new Bank();
	public static final String BANK_FILE = "de.opatut.tradecraft.bank-accounts.dat";

	/**
	 * This method is being called on load, and can be overwritten to do client/server specific
	 * data loading (usually used for texture loading in ClientProxy).
	 */
	public void onLoad() {
		MinecraftForge.EVENT_BUS.register(this);
		bank.load(BANK_FILE);
		bank.debugPrint();
	}
	
	@ForgeSubscribe
	public void entityJoined(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityPlayer) {
			System.out.println("Player " + event.entity.getEntityName() + " joined the world.");
			bank.registerPlayer(event.entity.getEntityName());
		}
	}

	public void onUnload() {
		bank.save(BANK_FILE);
	}
}