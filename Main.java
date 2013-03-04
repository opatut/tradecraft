package de.opatut.tradecraft;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "de.opatut.tradecraft", name = "Tradecraft", version = "0.1-alpha")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { PacketHandler.CHANNEL_VENDING_MACHINE }, packetHandler = PacketHandler.class)
public class Main {
	@Instance("de.opatut.tradecraft")
	public static Main instance;

	@SidedProxy(clientSide = "de.opatut.tradecraft.ClientProxy", serverSide = "de.opatut.tradecraft.ServerProxy")
	public static CommonProxy proxy;

	// Block definitions
	public final static Block vendingMachineBlock = new VendingMachineBlock(
			3729);
	
	@ServerStopping
	public void onUnload(FMLServerStoppingEvent event) {
		proxy.onUnload();
	}
	
	@Init
	public void onLoad(FMLInitializationEvent event) {
		System.out.println(proxy.getClass().getName());
		proxy.onLoad();

		GameRegistry.registerBlock(vendingMachineBlock, "vendingMachineBlock");
		LanguageRegistry.addName(vendingMachineBlock, "Vending Machine");
		MinecraftForge.setBlockHarvestLevel(vendingMachineBlock, "pickaxe", 0);

		ModLoader.registerTileEntity(VendingMachineTileEntity.class,
				"VendingMachine");

		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
	}
}
