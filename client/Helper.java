package de.opatut.tradecraft.client;

import net.minecraft.client.renderer.Tessellator;

public class Helper {
	public static void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
    {
		double z = 0;
        float uvSize = 1.F / 256.F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV(x + 0, y + h, z, (u + 0) * uvSize, (v + h) * uvSize);
        var9.addVertexWithUV(x + w, y + h, z, (u + w) * uvSize, (v + h) * uvSize);
        var9.addVertexWithUV(x + w, y + 0, z, (u + w) * uvSize, (v + 0) * uvSize);
        var9.addVertexWithUV(x + 0, y + 0, z, (u + 0) * uvSize, (v + 0) * uvSize);
        var9.draw();
    }
}
