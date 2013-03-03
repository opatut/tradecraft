package de.opatut.tradecraft;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VendingMachineModel extends ModelBase {
    private static final int TEXTURE_HEIGHT = 128;
    private static final int TEXTURE_WIDTH = 128;

    private ModelRenderer bottom, top;

    public VendingMachineModel() {
        this.textureHeight = TEXTURE_HEIGHT;
        this.textureWidth = TEXTURE_WIDTH;

        this.top = new ModelRenderer(this, 0, 0);
        this.top.addBox(0.f, 1.f, 0.f, 100, 50, 100, 0.01f);
        
        this.bottom = new ModelRenderer(this, 0, 0);
        this.bottom.addBox(0.f, 2.5f, 0.05f, 90, 25, 70, 0.01f);
        this.bottom.setRotationPoint(0, 0, 0);
        this.bottom.rotateAngleX = -(float) (30.f * Math.PI / 180.f);
    }

    public void render(float scale) {
        top.render(scale);
        bottom.render(scale);
    }

    public void render(VendingMachineTileEntity entity, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslated(x, y, z);
        ForgeHooksClient.bindTexture(CommonProxy.TEXTURE_VENDING_MACHINE_MODEL, 0);
        this.render(.01f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}