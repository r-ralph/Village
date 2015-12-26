package com.example.examplemod;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Created by hidetsugu-tamaki on 2015/12/26.
 */
public class RenderElf extends RenderLiving {
    private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");

    public RenderElf(ModelVillager modelVillager, int i) {
        super(new ModelVillager(0.0f), 0.5f);

    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityVillager p_77032_1_, int p_77032_2_, float p_77032_3_) {
        return -1;
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityElf) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityElf) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void doRender(EntityElf p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected void renderEquippedItems(EntityLivingBase entity, float p_77029_2_) {
        this.renderEquippedItems((EntityElf) entity, p_77029_2_);
    }

    protected void renderEquippedItems(EntityElf entity, float p_77029_2_) {
        super.renderEquippedItems(entity, p_77029_2_);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is
     * rendered. Args: entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityVillager p_77041_1_, float p_77041_2_) {
        float f1 = 0.9375F;

        if (p_77041_1_.getGrowingAge() < 0) {
            f1 = (float) ((double) f1 * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }

        GL11.glScalef(f1, f1, f1);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call
     * Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return villagerTextures;
    }
}
