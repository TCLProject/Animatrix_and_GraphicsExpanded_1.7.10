package com.ldtteam.graphicsexpanded.shader.uniform;

import com.ldtteam.graphicsexpanded.util.log.Log;
import org.lwjgl.opengl.GL20;

public abstract class Uniform {
	
	private static final int NOT_FOUND = -1;
	
	private final String name;
	private int location;
	
	public Uniform(final String name){
		this.name = name;
	}
	
	public void storeUniformLocation(final int programID){
		location = GL20.glGetUniformLocation(programID, name);
		if(location == NOT_FOUND){
            Log.getLogger().error("No uniform variable called " + name + " found!");
		}
	}
	
	public int getLocation(){
		return location;
	}

}
