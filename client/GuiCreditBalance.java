package de.opatut.tradecraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import cpw.mods.fml.client.FMLClientHandler;
import de.opatut.tradecraft.common.CommonProxy;

public class GuiCreditBalance extends Gui {
	private Minecraft mc;

	public GuiCreditBalance(Minecraft minecraft) {
		mc = minecraft;
	}

	public void render(int money) {
		if (mc.isGuiEnabled() && money >= 0) {
			ScaledResolution sclRes = new ScaledResolution(mc.gameSettings,
					mc.displayWidth, mc.displayHeight);

			int w = sclRes.getScaledWidth();
			int h = sclRes.getScaledHeight();

			String s = money + "";
			mc.fontRenderer.drawStringWithShadow(s,
					w - 13 - mc.fontRenderer.getStringWidth(s), h - 4
							- mc.fontRenderer.FONT_HEIGHT, 0xffffffff);

			// Coin icon
			RenderEngine engine = FMLClientHandler.instance().getClient().renderEngine;
			engine.bindTexture(engine.getTexture(CommonProxy.TEXTURE_ICONS));
			drawTexturedModalRect(w - 15, h - 17, 0, 0, 16, 16);
		}
	}
}
