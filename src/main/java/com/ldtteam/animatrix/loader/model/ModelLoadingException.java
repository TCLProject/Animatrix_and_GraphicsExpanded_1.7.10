package com.ldtteam.animatrix.loader.model;

import net.minecraft.util.ResourceLocation;

/**
 * Signals an error during the loading of a Animatrix Model.
 */
public class ModelLoadingException extends RuntimeException
{

    public ModelLoadingException(final Class<? extends IModelLoader> loaderClass, final ResourceLocation location, final Throwable cause)
    {
        super("Failed to load Model: " + location + " by: " + loaderClass.getName(), cause);
    }
}
