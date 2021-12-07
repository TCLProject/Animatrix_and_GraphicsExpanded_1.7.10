package com.ldtteam.animatrix.loader.animation;

import com.ldtteam.animatrix.loader.animation.IAnimationLoader;
import com.ldtteam.animatrix.model.IModel;
import com.ldtteam.animatrix.model.animation.IAnimation;
import net.minecraft.util.ResourceLocation;

public interface IAnimationLoaderManager
{

    static IAnimationLoaderManager getInstance()
    {
        return Holder.instance;
    }

    IAnimation loadAnimation(IModel model, ResourceLocation location) throws AnimationLoadingException;

    void registerLoader(final IAnimationLoader loader);
    
    class Holder
    {
        private static IAnimationLoaderManager instance;
        
        public static void setup(final IAnimationLoaderManager manager)
        {
            instance = manager;
        }
    }
}
