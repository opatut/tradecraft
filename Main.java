package de.opatut.tradecraft;

import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import de.opatut.tradecraft.client.RenderTickHandler;
import de.opatut.tradecraft.common.CommonProxy;
import de.opatut.tradecraft.common.GuiHandler;
import de.opatut.tradecraft.common.PacketHandler;
import de.opatut.tradecraft.common.PlayerTracker;
import de.opatut.tradecraft.objects.BlockCashRegister;
import de.opatut.tradecraft.objects.TileEntityCashRegister;

@Mod(modid = "de.opatut.tradecraft", name = "Tradecraft", version = "0.1-alpha")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { PacketHandler.CHANNEL_CASH_REGISTER, PacketHandler.CHANNEL_MONEY }, packetHandler = PacketHandler.class)
public class Main {
	@Instance("de.opatut.tradecraft")
	public static Main instance;

	@SidedProxy(clientSide = "de.opatut.tradecraft.client.ClientProxy", serverSide = "de.opatut.tradecraft.common.CommonProxy")
	public static CommonProxy proxy;

	// Block definitions
	public final static Block blockCashRegister = new BlockCashRegister(3729);
	
	@ServerStopping
	public void onUnload(FMLServerStoppingEvent event) {
		proxy.onUnload();
	}
	
	@Init
	public void onLoad(FMLInitializationEvent event) {
		System.out.println(proxy.getClass().getName());
		proxy.onLoad();

		GameRegistry.registerPlayerTracker(new PlayerTracker());
		
		GameRegistry.registerBlock(blockCashRegister, "blockCashRegister");
		LanguageRegistry.addName(blockCashRegister, "CashRegister");
		MinecraftForge.setBlockHarvestLevel(blockCashRegister, "pickaxe", 0);

		ModLoader.registerTileEntity(TileEntityCashRegister.class, "CashRegister");

		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		
		
	}
}
