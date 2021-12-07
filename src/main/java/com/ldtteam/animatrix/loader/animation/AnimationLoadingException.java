package com.ldtteam.animatrix.loader.animation;

import net.minecraft.util.ResourceLocation;

public class AnimationLoadingException extends Exception
{

    public AnimationLoadingException(final Class<? extends IAnimationLoader> loaderClass, final ResourceLocation location, final Throwable cause)
    {
        super("Failed to load Animation: " + location + " by: " + loaderClass.getName(), cause);
    }
}
