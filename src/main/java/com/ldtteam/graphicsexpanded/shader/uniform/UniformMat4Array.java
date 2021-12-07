package com.ldtteam.graphicsexpanded.shader.uniform;

import org.lwjgl.util.vector.Matrix4f;

public class UniformMat4Array extends Uniform{
	
	private UniformMatrix[] matrixUniforms;
	
	public UniformMat4Array(final String name, final int size) {
		super(name);
		matrixUniforms = new UniformMatrix[size];
		for(int i=0;i<size;i++){
			matrixUniforms[i] = new UniformMatrix(name + "["+i+"]");
		}
	}
	
	@Override
	public void storeUniformLocation(final int programID) {
		for(final UniformMatrix matrixUniform : matrixUniforms){
			matrixUniform.storeUniformLocation(programID);
		}
	}

	public void loadMatrixArray(final Matrix4f[] matrices){
		for(int i=0;i<matrices.length;i++){
			matrixUniforms[i].loadMatrix(matrices[i]);
		}
	}
	
	

}
