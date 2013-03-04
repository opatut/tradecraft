package de.opatut.tradecraft.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import de.opatut.tradecraft.common.CommonProxy;
import de.opatut.tradecraft.objects.TileEntityCashRegister;
import de.opatut.tradecraft.objects.TileEntityCashRegister.Renderer;

public class ClientProxy extends CommonProxy {
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