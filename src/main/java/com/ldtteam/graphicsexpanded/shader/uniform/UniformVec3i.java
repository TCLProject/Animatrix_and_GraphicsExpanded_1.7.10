package com.ldtteam.graphicsexpanded.shader.uniform;

import com.ldtteam.graphicsexpanded.util.vector.Vector3i;
import org.lwjgl.opengl.GL20;

public class UniformVec3i extends Uniform {
    private int currentX;
    private int currentY;
    private int currentZ;
    private boolean used = false;

    public UniformVec3i(final String name) {
        super(name);
    }

    public void loadVec3i(final Vector3i vector) {
        loadVec3i(vector.x, vector.y, vector.z);
    }

    public void loadVec3i(final int x, final int y, final int z) {
        if (!used || x != currentX || y != currentY || z != currentZ) {
            this.currentX = x;
            this.currentY = y;
            this.currentZ = z;
            used = true;
            GL20.glUniform3f(super.getLocation(), x, y, z);
        }
    }
}
