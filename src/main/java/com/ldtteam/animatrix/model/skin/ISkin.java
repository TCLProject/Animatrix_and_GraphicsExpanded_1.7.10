package com.ldtteam.animatrix.model.skin;

import com.ldtteam.graphicsexpanded.gpu.VAO;
import net.minecraft.util.ResourceLocation;

public interface ISkin
{
    /**
     * Returns the {@link VAO} that represents the skin data on the GPU.
     *
     * @return The {@link VAO} containing the skin data on the GPU.
     */
    VAO getSkinModel();

    /**
     * Returns the {@link ResourceLocation} that represents the texture of this skin on disk.
     *
     * @return The texture of the skin on disk.
     */
    ResourceLocation getTexture();
}
