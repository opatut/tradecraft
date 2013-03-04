package de.opatut.tradecraft.common;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public abstract class TileEntityDirected extends TileEntityCore {
	public int direction;
	
	public void setDirection(float yaw, float pitch, EntityLiving player) {
		int facing = MathHelper.floor_double((double) (yaw / 360) + 0.5D) % 4;
		switch (facing) {
			case 0: direction = 2; break;
			case 1: direction = 5; break;
			case 2: direction = 3; break;
			case 3: direction = 4; break;
		}
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
		float angle = 0;
		switch (direction) {
			case 2: angle = 0; break;
			case 5: angle = 90; break;
			case 3: angle = 180; break;
			case 4: angle = 270; break;
		}
        GL11.glRotatef(angle, 0.F, 1.0F, 0.0F);
	}
}
