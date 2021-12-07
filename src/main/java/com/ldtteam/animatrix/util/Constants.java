package com.ldtteam.animatrix.util;

import net.minecraft.util.ResourceLocation;

/**
 * Holds the constants for Animatrix.
 */
public final class Constants
{

    private Constants()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Contains all general constants in use by Animatrix.
     */
    public static final class General
    {
        private General() {}

        public static final String MOD_ID = "tclgraphics";
        public static final String MOD_NAME = "Animatrix";
        public static final String MOD_VERSION = "@VERSION@";
    }

    public static final class Shaders
    {
        public static final class Vertex
        {
            public static final ResourceLocation ANIMATRIX_DEFAULT = new ResourceLocation(General.MOD_ID, "shaders/animatrix_vertex.glsl");
            public static final ResourceLocation ANIMATRIX_DEFAULT_FALLBACK = new ResourceLocation(General.MOD_ID, "shaders/animatrix_vertex_fallback.glsl");
        }

        public static final class Fragment
        {
            public static final ResourceLocation ANIMATRIX_DEFAULT = new ResourceLocation(General.MOD_ID, "shaders/animatrix_fragment.glsl");
        }

        public static final class Variables
        {
            public static final String CONST_POSITION = "in_position";
            public static final String CONST_TEXTURE_COORDS = "in_textureCoords";
            public static final String CONST_JOINT_INDICES = "in_jointIndices";
            public static final String CONST_WEIGHTS = "in_weights";
        }

        public static final class ArraySizes
        {
            public static final Integer CONST_MAX_WEIGHTS_PER_VERTEX = 3;
            public static final Integer CONST_MAX_JOINT_COUNT = 50;
        }
    }

}