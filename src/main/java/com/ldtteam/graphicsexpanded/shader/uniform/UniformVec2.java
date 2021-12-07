package com.ldtteam.graphicsexpanded.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVec2 extends Uniform {

	private float currentX;
	private float currentY;
	private boolean used = false;

	public UniformVec2(final String name) {
		super(name);
	}

	public void loadVec2(final Vector2f vector) {
		loadVec2(vector.x, vector.y);
	}

	public void loadVec2(final float x, final float y) {
		if (!used || x != currentX || y != currentY) {
			this.currentX = x;
			this.currentY = y;
			used = true;
			GL20.glUniform2f(super.getLocation(), x, y);
		}
	}

}
