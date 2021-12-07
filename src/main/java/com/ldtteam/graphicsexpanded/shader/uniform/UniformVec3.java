package com.ldtteam.graphicsexpanded.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVec3 extends Uniform {
	private float currentX;
	private float currentY;
	private float currentZ;
	private boolean used = false;

	public UniformVec3(final String name) {
		super(name);
	}

	public void loadVec3(final Vector3f vector) {
		loadVec3(vector.x, vector.y, vector.z);
	}

	public void loadVec3(final float x, final float y, final float z) {
		if (!used || x != currentX || y != currentY || z != currentZ) {
			this.currentX = x;
			this.currentY = y;
			this.currentZ = z;
			used = true;
			GL20.glUniform3f(super.getLocation(), x, y, z);
		}
	}

}
