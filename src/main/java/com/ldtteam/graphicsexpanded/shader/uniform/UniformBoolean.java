package com.ldtteam.graphicsexpanded.shader.uniform;

import org.lwjgl.opengl.GL20;

public class UniformBoolean extends Uniform{

	private boolean currentBool;
	private boolean used = false;
	
	public UniformBoolean(final String name){
		super(name);
	}
	
	public void loadBoolean(final boolean bool){
		if(!used || currentBool != bool){
			GL20.glUniform1f(super.getLocation(), bool ? 1f : 0f);
			used = true;
			currentBool = bool;
		}
	}
	
}
