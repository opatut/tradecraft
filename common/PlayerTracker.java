package de.opatut.tradecraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.Player;

public class PlayerTracker implements IPlayerTracker {
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		CommonProxy.bank.sendBalance((Player) player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {}

}
