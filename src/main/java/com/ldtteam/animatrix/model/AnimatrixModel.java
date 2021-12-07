package com.ldtteam.animatrix.model;

import com.ldtteam.animatrix.model.animator.AnimatrixAnimator;
import com.ldtteam.animatrix.model.animator.IAnimator;
import com.ldtteam.animatrix.model.skeleton.ISkeleton;
import com.ldtteam.animatrix.model.skin.ISkin;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents a single model in Animatrix.
 */
@SideOnly(Side.CLIENT)
public class AnimatrixModel implements IModel
{
    private final ISkeleton skeleton;
    private final ISkin skin;
    private final IAnimator animator;

    public AnimatrixModel(final ISkeleton skeleton, final ISkin skin) {
        this.skeleton = skeleton;
        this.skin = skin;
        this.animator = new AnimatrixAnimator(this);
    }

    /**
     * The skeleton of the model.
     *
     * @return The skeleton.
     */
    @Override
    public ISkeleton getSkeleton()
    {
        return skeleton;
    }

    /**
     * The skin of the model.
     *
     * @return The skin.
     */
    @Override
    public ISkin getSkin()
    {
        return skin;
    }

    /**
     * The animator for the model.
     *
     * @return The animator.
     */
    @Override
    public IAnimator getAnimator()
    {
        return animator;
    }
}
