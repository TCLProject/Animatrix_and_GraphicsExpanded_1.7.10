package com.ldtteam.animatrix.loader.model;

import com.ldtteam.animatrix.loader.data.AnimatedModelData;

import net.minecraft.util.ResourceLocation;

public interface IModelLoader
{
    boolean canLoadModel(ResourceLocation modelLocation);

    AnimatedModelData loadModel(ResourceLocation colladaFile) throws ModelLoadingException;
}
