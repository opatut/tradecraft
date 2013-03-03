package de.opatut.tradecraft;

public class CommonProxy {
	public static final String IMAGE_DIR = "/de/opatut/tradecraft/";
	
	public static final String TEXTURE_ICONS = IMAGE_DIR + "icons.png";
	public static final String TEXTURE_VENDING_MACHINE_GUI = IMAGE_DIR + "vending-machine-gui.png";
	public static final String TEXTURE_VENDING_MACHINE_MODEL = IMAGE_DIR + "vending-machine-model.png";

	/**
	 * This method is being called on load, and can be overwritten to do client/server specific
	 * data loading (usually used for texture loading in ClientProxy).
	 */
	public void onLoad() {}
}