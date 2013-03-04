package de.opatut.tradecraft;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RenderTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		 /*if (Minecraft.getMinecraft().currentScreen == null) {
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution screen = new ScaledResolution(mc.gameSettings,
					mc.displayWidth, mc.displayHeight);
			int i = player.inventory.currentItem;
			ItemStack stack = player.inventory.mainInventory[i];
			if (stack != null && stack.getItem() instanceof IModularItem) {
				MuseRenderer.blendingOn();
				NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
				int swapTime = (int) Math.min(System.currentTimeMillis()
						- lastSwapTime, SWAPTIME);
				MuseIcon currentMode = null;
				MuseIcon nextMode = null;
				MuseIcon prevMode = null;
				List<String> modes = MuseItemUtils.getModes(stack, player);
				String mode = itemTag.getString("Mode");
				int modeIndex = modes.indexOf(mode);
				if (modeIndex > -1) {
					String prevModeName = modes
							.get((modeIndex + modes.size() - 1) % modes.size());
					String nextModeName = modes.get((modeIndex + 1)
							% modes.size());
					IPowerModule module = ModuleManager.getModule(mode);
					IPowerModule nextModule = ModuleManager
							.getModule(nextModeName);
					IPowerModule prevModule = ModuleManager
							.getModule(prevModeName);

					if (module != null) {
						currentMode = module.getIcon(stack);
						if (nextModeName != mode) {
							nextMode = nextModule.getIcon(stack);
							prevMode = prevModule.getIcon(stack);
						}
					}
				}

				double prevX, prevY, currX, currY, nextX, nextY, prevAlpha, currAlpha, nextAlpha;
				double sw = screen.getScaledWidth_double();
				double sh = screen.getScaledHeight_double();
				double baroffset = -40;
				if (!player.capabilities.isCreativeMode) {
					baroffset -= 16;
					if (ForgeHooks.getTotalArmorValue(player) > 0) {
						baroffset -= 8;
					}
				}

				prevX = sw / 2.0 - 105.0 + 20.0 * i;
				prevY = sh + baroffset + 10;
				currX = sw / 2.0 - 89.0 + 20.0 * i;
				currY = sh + baroffset;
				nextX = sw / 2.0 - 73.0 + 20.0 * i;
				nextY = sh + baroffset + 10;
				if (swapTime == SWAPTIME || lastSwapDirection == 0) {
					prevAlpha = 0.4;
					currAlpha = 0.8;
					nextAlpha = 0.4;
					MuseRenderer.drawIconPartial(prevX, prevY, prevMode,
							Colour.WHITE.withAlpha(prevAlpha), 0, 0, 16, sh
									+ baroffset - prevY + 16);
					MuseRenderer.drawIconPartial(currX, currY, currentMode,
							Colour.WHITE.withAlpha(currAlpha), 0, 0, 16, sh
									+ baroffset - currY + 16);
					MuseRenderer.drawIconPartial(nextX, nextY, nextMode,
							Colour.WHITE.withAlpha(nextAlpha), 0, 0, 16, sh
									+ baroffset - nextY + 16);
				} else {
					prevAlpha = 0.8;
					currAlpha = 0.8;
					nextAlpha = 0.8;
					double r1 = 1 - swapTime / (double) SWAPTIME;
					double r2 = swapTime / (double) SWAPTIME;
					if (lastSwapDirection == -1) {
						nextX = (currX * r1 + nextX * r2);
						nextY = (currY * r1 + nextY * r2);
						currX = (prevX * r1 + currX * r2);
						currY = (prevY * r1 + currY * r2);
						MuseRenderer.drawIconPartial(currX, currY, currentMode,
								Colour.WHITE.withAlpha(currAlpha), 0, 0, 16, sh
										+ baroffset - currY + 16);
						MuseRenderer.drawIconPartial(nextX, nextY, nextMode,
								Colour.WHITE.withAlpha(nextAlpha), 0, 0, 16, sh
										+ baroffset - nextY + 16);

					} else {
						prevX = (currX * r1 + prevX * r2);
						prevY = (currY * r1 + prevY * r2);
						currX = (nextX * r1 + currX * r2);
						currY = (nextY * r1 + currY * r2);
						MuseRenderer.drawIconPartial(prevX, prevY, prevMode,
								Colour.WHITE.withAlpha(prevAlpha), 0, 0, 16, sh
										+ baroffset - prevY + 16);
						MuseRenderer.drawIconPartial(currX, currY, currentMode,
								Colour.WHITE.withAlpha(currAlpha), 0, 0, 16, sh
										+ baroffset - currY + 16);

					}
				}
				// MuseRenderer.blendingOff();
				GL11.glDisable(GL11.GL_LIGHTING);
				Colour.WHITE.doGL();
			}

		}*/

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "de.opatut.tradecraft.RenderTickHandler";
	}
}