package de.opatut.tradecraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import de.opatut.tradecraft.client.GuiChashRegister;
import de.opatut.tradecraft.objects.ContainerCashRegister;
import de.opatut.tradecraft.objects.TileEntityCashRegister;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityCashRegister) {
			return new ContainerCashRegister(player.inventory,
					(TileEntityCashRegister) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityCashRegister) {
			return new GuiChashRegister(player.inventory,
					(TileEntityCashRegister) tileEntity);
		}
		return null;
	}
}