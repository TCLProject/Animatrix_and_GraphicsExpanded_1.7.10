package com.ldtteam.graphicsexpanded.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class UniformVec4 extends Uniform {

	public UniformVec4(final String name) {
		super(name);
	}

	public void loadVec4(final Vector4f vector) {
		loadVec4(vector.x, vector.y, vector.z, vector.w);
	}

	public void loadVec4(final float x, final float y, final float z, final float w) {
		GL20.glUniform4f(super.getLocation(), x, y, z, w);
	}

}
