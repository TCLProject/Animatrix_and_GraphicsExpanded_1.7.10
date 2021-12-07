package com.ldtteam.animatrix.model.animator;

import com.ldtteam.animatrix.model.animation.IAnimation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * Handles the animation of {@link com.ldtteam.animatrix.model.IModel}
 */
@SideOnly(Side.CLIENT)
public interface IAnimator
{
    default void startAnimation(final IAnimation animation)
    {
        startAnimation(animation, 0);
    }

    /**
     * Starts a new animation that runs an infinite amount of times with the given priority.
     * Lower priority means more influence on the model.
     *
     * @param animation The animation to start.
     * @param priority The priority. Lower means more influence.
     */
    default void startAnimation(final IAnimation animation, final int priority)
    {
        startAnimation(animation, priority, Double.POSITIVE_INFINITY);
    }

    /**
     * Starts a new animation that runs the given amount of times, with the given priority.
     * Lower priority means more influence on the model.
     *
     * @param animation The animation to start.
     * @param priority The priority.
     * @param count The count.
     */
    void startAnimation(final IAnimation animation, int priority, double count);

    /**
     * Stops a animation from running and removes its information from the animator.
     *
     * @param name The name of the animation.
     */
    void stopAnimation(final ResourceLocation name);

    /**
     * Called to update the animator and the animations that are running.
     * Applies the joint pose of all animations to the models skeleton.
     */
    void onUpdate();
}
