package com.ldtteam.graphicsexpanded.util.vector;

import org.lwjgl.util.vector.Vector;

import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Vector3i extends Vector implements Serializable
{

    private static final long serialVersionUID = 1L;

    public int x, y, z;

    /**
     * Constructor for Vector3i.
     */
    public Vector3i() {
        super();
    }

    /**
     * Constructor
     */
    public Vector3i(final int x, final int y, final int z) {
        set(x, y, z);
    }

    public void set(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public void set(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return the length squared of the vector
     */
    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Load this vector from a FloatBuffer
     *
     * @param buf The buffer to load it from, at the current position
     * @return this
     */
    @Override
    public Vector load(final FloatBuffer buf)
    {
        x = (int) buf.get();
        y = (int) buf.get();
        z = (int) buf.get();
        return this;
    }

    /**
     * Translate a vector
     * @param x The translation in x
     * @param y the translation in y
     * @return this
     */
    public Vector3i translate(final int x, final int y, final int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Add a vector to another vector and place the result in a destination
     * vector.
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination vector, or null if a new vector is to be created
     * @return the sum of left and right in dest
     */
    public static Vector3i add(final Vector3i left, final Vector3i right, final Vector3i dest) {
        if (dest == null)
            return new Vector3i(left.x + right.x, left.y + right.y, left.z + right.z);
        else {
            dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
            return dest;
        }
    }

    /**
     * Subtract a vector from another vector and place the result in a destination
     * vector.
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination vector, or null if a new vector is to be created
     * @return left minus right in dest
     */
    public static Vector3i sub(final Vector3i left, final Vector3i right, final Vector3i dest) {
        if (dest == null)
            return new Vector3i(left.x - right.x, left.y - right.y, left.z - right.z);
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }

    /**
     * The cross product of two vectors.
     *
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination result, or null if a new vector is to be created
     * @return left cross right
     */
    public static Vector3i cross(
      final Vector3i left,
      final Vector3i right,
      Vector3i dest)
    {

        if (dest == null)
            dest = new Vector3i();

        dest.set(
          left.y * right.z - left.z * right.y,
          right.x * left.z - right.z * left.x,
          left.x * right.y - left.y * right.x
        );

        return dest;
    }



    /**
     * Negate a vector
     * @return this
     */
    public Vector3i negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Store this vector in a FloatBuffer
     *
     * @param buf The buffer to store it in, at the current position
     * @return this
     */
    @Override
    public Vector store(final FloatBuffer buf)
    {
        return null;
    }

    /**
     * Scale this vector
     *
     * @param scale The scale factor
     * @return this
     */
    @Override
    public Vector scale(final float scale)
    {
        return null;
    }

    /**
     * Negate a vector and place the result in a destination vector.
     * @param dest The destination vector or null if a new vector is to be created
     * @return the negated vector
     */
    public Vector3i negate(Vector3i dest) {
        if (dest == null)
            dest = new Vector3i();
        dest.x = -x;
        dest.y = -y;
        dest.z = -z;
        return dest;
    }


    /**
     * Normalise this vector and place the result in another vector.
     * @param dest The destination vector, or null if a new vector is to be created
     * @return the normalised vector
     */
    public Vector3i normalise(Vector3i dest) {
        final float l = length();

        if (dest == null)
            dest = new Vector3i((int) (x / l), (int) (y / l), (int) (z / l));
        else
            dest.set((int) (x / l), (int) (y / l),(int) (z / l));

        return dest;
    }

    /**
     * The dot product of two vectors is calculated as
     * v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
     * @param left The LHS vector
     * @param right The RHS vector
     * @return left dot right
     */
    public static int dot(final Vector3i left, final Vector3i right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    /**
     * Calculate the angle between two vectors, in radians
     * @param a A vector
     * @param b The other vector
     * @return the angle between the two vectors, in radians
     */
    public static float angle(final Vector3i a, final Vector3i b) {
        float dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1f)
            dls = -1f;
        else if (dls > 1.0f)
            dls = 1.0f;
        return (int)Math.acos(dls);
    }

    /* (non-Javadoc)
     * @see org.lwjgl.vector.Vector#load(intBuffer)
     */
    public Vector3i load(final IntBuffer buf) {
        x = buf.get();
        y = buf.get();
        z = buf.get();
        return this;
    }

    /* (non-Javadoc)
     * @see org.lwjgl.vector.Vector#scale(int)
     */
    public Vector3i scale(final int scale) {

        x *= scale;
        y *= scale;
        z *= scale;

        return this;

    }

    /* (non-Javadoc)
     * @see org.lwjgl.vector.Vector#store(intBuffer)
     */
    public Vector3i store(final IntBuffer buf) {

        buf.put(x);
        buf.put(y);
        buf.put(z);

        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);

        sb.append("Vector3i[");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append(']');
        return sb.toString();
    }

    /**
     * @return x
     */
    public final int getX() {
        return x;
    }

    /**
     * @return y
     */
    public final int getY() {
        return y;
    }

    /**
     * Set X
     * @param x
     */
    public final void setX(final int x) {
        this.x = x;
    }

    /**
     * Set Y
     * @param y
     */
    public final void setY(final int y) {
        this.y = y;
    }

    /**
     * Set Z
     * @param z
     */
    public void setZ(final int z) {
        this.z = z;
    }

    /* (Overrides)
     * @see org.lwjgl.vector.ReadableVector3i#getZ()
     */
    public int getZ() {
        return z;
    }

    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Vector3i other = (Vector3i)obj;

        if (x == other.x && y == other.y && z == other.z) return true;

        return false;
    }
}
