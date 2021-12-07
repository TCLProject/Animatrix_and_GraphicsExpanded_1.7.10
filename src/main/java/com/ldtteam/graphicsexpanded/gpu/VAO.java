package com.ldtteam.graphicsexpanded.gpu;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents a OpenGL version 3.0 memory array on the GPU.
 */
@SideOnly(Side.CLIENT)
public class VAO
{
    private static final int             BYTES_PER_FLOAT = 4;
    private static final int             BYTES_PER_INT   = 4;
    public final         int             id;
    private              Collection<VBO> dataVBOs        = new ArrayList<>();
    private              VBO             indexVbo;
    private              int             indexCount;

    /**
     * Creates a new VAO representation instance in memory after it has been created on the GPU.
     * @param id The id of the VAO on disk.
     */
    VAO(final int id) {
        this.id = id;
    }

    /**
     * Returns the amount of elements in the array (not unique).
     *
     * @return The amount of indexi stored in the index buffer.
     */
    public int getIndexCount(){
        return indexCount;
    }

    /**
     * Enables the given set of attributes on the GPU for this VAO.
     * Also binds the VAO as the active VAO on the GPU.
     *
     * @param attributes The attributes to enable.
     */
    public void bind(final int... attributes){
        bind();
        for (final int i : attributes) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    /**
     * Disables the given set of attributes on the GPU for this VAO.
     * Also unbinds the VAO as the active VAO on the GPU.
     *
     * @param attributes The attributes to disable.
     */
    public void unbind(final int... attributes){
        for (final int i : attributes) {
            GL20.glDisableVertexAttribArray(i);
        }
        unbind();
    }

    /**
     * Creates a index buffer in memory.
     *
     * @param indices The indices to store in memory.
     */
    public void createIndexBuffer(final int[] indices){
        this.indexVbo = GPUMemoryManager.getInstance().createVBO(GL15.GL_ELEMENT_ARRAY_BUFFER);
        indexVbo.bind();
        indexVbo.storeData(indices);
        this.indexCount = indices.length;
    }

    /**
     * Creates a float-data based attribute.
     * Creates a new VBO to store the the data in and modifies this VAO to point the attribute to the given data.
     *
     * @param attribute The id of the attribute.
     * @param data The float-data to store.
     * @param attrSize The size of a float in memory (usually 4 bytes -> so 4)
     */
    public void createAttribute(final int attribute, final float[] data, final int attrSize){
        final VBO dataVbo = GPUMemoryManager.getInstance().createVBO(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
        dataVbo.unbind();
        dataVBOs.add(dataVbo);
    }

    /**
     * Creates a int-data based attribute.
     * Creates a new VBO to store the the data in and modifies this VAO to point the attribute to the given data.
     *
     * @param attribute The id of the attribute.
     * @param data The int-data to store.
     * @param attrSize The size of a int in memory (usually 4 bytes -> so 4)
     */
    public void createIntAttribute(final int attribute, final int[] data, int attrSize){
        final VBO dataVbo = GPUMemoryManager.getInstance().createVBO(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
        dataVbo.unbind();
        dataVBOs.add(dataVbo);
    }

    /**
     * Makes the current VAO the active VAO in the GPU Memory.
     */
    private void bind() {
        GL30.glBindVertexArray(id);
    }

    /**
     * Unbinds the current VAO as the active VAO in the GPU Memory.
     */
    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    @Override
    public String toString()
    {
        return "VAO{" +
                 "id=" + id +
                 '}';
    }
}
