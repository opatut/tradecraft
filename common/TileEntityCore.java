package de.opatut.tradecraft.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityCore extends TileEntity {
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.customParam1);
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	
	public void sync() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void redraw() {
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
}
