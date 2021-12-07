package com.ldtteam.graphicsexpanded.shader.uniform;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class UniformMatrix extends Uniform{
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(final String name) {
		super(name);
	}
	
	public void loadMatrix(final Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(super.getLocation(), false, matrixBuffer);
	}
	
	

}
