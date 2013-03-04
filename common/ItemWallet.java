package de.opatut.tradecraft.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWallet extends Item {
	public ItemWallet(int id) {
		super(id);
		setTextureFile(CommonProxy.TEXTURE_ICONS);
		setIconIndex(2);
		setCreativeTab(CreativeTabs.tabTools);
		setItemName("tradecraftWallet");
		setMaxStackSize(1);
	}
	
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(entity instanceof EntityPlayerMP) {
			player.sendChatToPlayer("You will soon be able to trade with " + ((EntityPlayerMP)entity).username);
		} else {
			player.sendChatToPlayer("You can only trade with players.");
		}
		return true;
	}
}
