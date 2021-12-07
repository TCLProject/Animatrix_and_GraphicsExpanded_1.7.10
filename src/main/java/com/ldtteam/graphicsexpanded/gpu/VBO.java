package com.ldtteam.graphicsexpanded.gpu;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Represents a OpenGL version 1.5 memory buffer on the GPU.
 */
public final class VBO
{
    private final int vboId;
    private final int type;

    /**
     * Creates a new VBO for the given ID and type.
     *
     * @param vboId The id of the VBO on the GPU.
     * @param type The type of the VBO.
     */
    VBO(final int vboId, final int type){
        this.vboId = vboId;
        this.type = type;
    }

    /**
     * Binds this VBO to be read from or written to on the GPU.
     */
    public void bind(){
        GL15.glBindBuffer(type, vboId);
    }

    /**
     * Unbinds the VBO from read or writing on the GPU.
     */
    public void unbind(){
        GL15.glBindBuffer(type, 0);
    }

    /**
     * Writes the float-data given in the Array into the memory of the GPU represented by the VBO.
     * @param data The float-data to be written into the GPU memory.
     */
    public void storeData(final float[] data){
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        storeData(buffer);
    }

    /**
     * Writes the int-data given in the Array into the memory of the GPU represented by the VBO.
     * @param data The int-data to be written into the GPU memory.
     */
    public void storeData(final int[] data){
        final IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        storeData(buffer);
    }

    /**
     * Writes the int-data given in the buffer into the memory of the GPU represented by the VBO.
     * @param data The int-data to be written into the GPU memory.
     */
    public void storeData(final IntBuffer data){
        GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
    }

    /**
     * Writes the float-data given in the buffer into the memory of the GPU represented by the VBO.
     * @param data The float-data to be written into the GPU memory.
     */
    public void storeData(final FloatBuffer data){
        GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
    }

    @Override
    public String toString()
    {
        return "VBO{" +
                 "vboId=" + vboId +
                 ", type=" + type +
                 '}';
    }
}
