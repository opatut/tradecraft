package de.opatut.tradecraft.common;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public abstract class TileEntityDirected extends TileEntityCore {
	public int direction;
	
	public void setDirection(float angle) {
		direction = (int)Math.round((0.5 + angle / 360.0) * 4.0) % 4;
		if(direction < 0) direction += 4;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("Direction", direction);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = tag.getInteger("Direction");
	}
	
	protected void rotateMatrix() {
        GL11.glRotatef(- 90.f * direction, 0.F, 1.0F, 0.0F);
	}
}
