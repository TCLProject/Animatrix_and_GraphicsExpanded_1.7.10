package com.ldtteam.animatrix.loader.model;

import com.ldtteam.animatrix.model.IModel;

import net.minecraft.util.ResourceLocation;

public interface IModelLoaderManager
{

    static IModelLoaderManager getInstance()
    {
        return Holder.instance;
    }

    IModel loadModel(final ResourceLocation location, final ResourceLocation texture);

    void registerLoader(final IModelLoader loader);

    class Holder
    {
        private static IModelLoaderManager instance;

        public static void setup(final IModelLoaderManager manager)
        {
            instance = manager;
        }
    }
}
