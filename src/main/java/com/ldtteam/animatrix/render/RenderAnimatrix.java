package com.ldtteam.animatrix.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.ldtteam.animatrix.entity.IEntityAnimatrix;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tclproject.tclgraphics.TCLGraphics;

public class RenderAnimatrix<T extends EntityLiving & IEntityAnimatrix> extends RenderLiving
{
    public RenderAnimatrix(final ModelBase modelbaseIn, final float shadowsizeIn)
    {
        super(modelbaseIn, shadowsizeIn);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ((IEntityAnimatrix)entity).getAnimatrixModel().getSkin().getTexture();
    }

    /**
     * Renders the desired Entity.
     */
    @Override
    public void doRender(EntityLiving entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks)
    {
        if (((IEntityAnimatrix)entity).getAnimatrixModel() == null)
            return;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        bindEntityTexture(entity);

        GL11.glTranslatef((float)x, (float)y, (float)z);
        //GlStateManager.rotate(90, 1f, 0, 0);
        //GlStateManager.scale(1/16f, 1/16f, 1/16f);

        TCLGraphics.getInstance().getShader().start();

        ((IEntityAnimatrix)entity).getAnimatrixModel().getSkin().getSkinModel().bind(0,1,2,3);

        TCLGraphics.getInstance().getShader().getJointTransforms().loadMatrixArray(((IEntityAnimatrix)entity).getAnimatrixModel().getSkeleton().getAnimationModelSpaceTransformsFromJoints());
        TCLGraphics.getInstance().getShader().getTextureSampler().loadTexUnit(OpenGlHelper.defaultTexUnit);

        GL11.glDrawElements(GL11.GL_TRIANGLES, ((IEntityAnimatrix)entity).getAnimatrixModel().getSkin().getSkinModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);

        ((IEntityAnimatrix)entity).getAnimatrixModel().getSkin().getSkinModel().unbind(0,1,2,3);

        TCLGraphics.getInstance().getShader().stop();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
