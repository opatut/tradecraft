package de.opatut.tradecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VendingMachineModel extends ModelBase {
    private static final int TEXTURE_HEIGHT = 64;
    private static final int TEXTURE_WIDTH = 64;

    private ModelRenderer bottom, top;

    public VendingMachineModel() {
        this.textureHeight = TEXTURE_HEIGHT;
        this.textureWidth = TEXTURE_WIDTH;
        
        bottom = new ModelRenderer(this, 0, 17);
        bottom.addBox(-7F, 0F, -7F, 14, 5, 14);

        top = new ModelRenderer(this, 0, 0);
        top.addBox(-6F, 3F, -9F, 12, 4, 12);
        top.rotateAngleX = 0.44F;
    }

    public void render(float scale) {
        top.render(scale);
        bottom.render(scale);
    }
    
    public void render(VendingMachineTileEntity entity, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        ForgeHooksClient.bindTexture(CommonProxy.TEXTURE_VENDING_MACHINE_MODEL, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.render(1.f/16.f);     
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}