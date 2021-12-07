package com.ldtteam.animatrix.loader.animation;

import com.ldtteam.animatrix.loader.data.AnimationData;
import net.minecraft.util.ResourceLocation;

public interface IAnimationLoader
{
    boolean canLoadAnimation(ResourceLocation animationLocation);

    AnimationData loadAnimation(ResourceLocation animationLocation) throws AnimationLoadingException;
}
