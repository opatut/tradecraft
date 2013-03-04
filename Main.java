package de.opatut.tradecraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemStack;
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
import de.opatut.tradecraft.common.ItemWallet;
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

	// Blocks and Items
	public final static Block blockCashRegister = new BlockCashRegister(3729);
	public final static ItemWallet itemWallet = new ItemWallet(3730);
	
	
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
		LanguageRegistry.addName(blockCashRegister, "Cash Register");
		LanguageRegistry.addName(itemWallet, "Wallet");
		MinecraftForge.setBlockHarvestLevel(blockCashRegister, "pickaxe", 0);

		ModLoader.registerTileEntity(TileEntityCashRegister.class, "CashRegister");

		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
		
		// === Crafting Recipes ===
		ItemStack W, s, l, c, C, i, r;
		W = new ItemStack(itemWallet);
		C = new ItemStack(blockCashRegister);
		l = new ItemStack(Item.leather);
		s = new ItemStack(Item.silk);
		i = new ItemStack(Item.ingotIron);
		r = new ItemStack(Item.redstone);
		c = new ItemStack(Block.chest);
		
		// wallet
		GameRegistry.addRecipe(W, "lll", "s  ", "lll", 'l', l, 's', s);
		GameRegistry.addRecipe(W, "lll", "  s", "lll", 'l', l, 's', s);
		
		// cash register
		GameRegistry.addRecipe(C, "Wcr", "iii", 'W', W, 'c', c, 'r', r, 'i', i);
		GameRegistry.addRecipe(C, "rcW", "iii", 'W', W, 'c', c, 'r', r, 'i', i);
	}
}
