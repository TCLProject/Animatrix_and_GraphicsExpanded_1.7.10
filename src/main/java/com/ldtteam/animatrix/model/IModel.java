package com.ldtteam.animatrix.model;

import com.ldtteam.animatrix.model.animator.IAnimator;
import com.ldtteam.animatrix.model.skeleton.ISkeleton;
import com.ldtteam.animatrix.model.skin.ISkin;

/**
 * Represents a single model in Animatrix.
 */
public interface IModel
{
    /**
     * The skeleton of the model.
     *
     * @return The skeleton.
     */
    ISkeleton getSkeleton();

    /**
     * The skin of the model.
     *
     * @return The skin.
     */
    ISkin getSkin();

    /**
     * The animator for the model.
     *
     * @return The animator.
     */
    IAnimator getAnimator();
}
