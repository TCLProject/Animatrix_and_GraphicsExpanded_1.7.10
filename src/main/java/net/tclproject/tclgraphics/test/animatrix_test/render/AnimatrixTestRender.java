package net.tclproject.tclgraphics.test.animatrix_test.render;

import com.ldtteam.animatrix.render.RenderAnimatrix;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.tclproject.tclgraphics.test.animatrix_test.entity.AnimatrixTestEntity;

public class AnimatrixTestRender extends RenderAnimatrix<AnimatrixTestEntity>
{
    public AnimatrixTestRender()
    {
        super(new ModelBiped(), 1f);
        this.shadowSize = 0F;
    }
}
