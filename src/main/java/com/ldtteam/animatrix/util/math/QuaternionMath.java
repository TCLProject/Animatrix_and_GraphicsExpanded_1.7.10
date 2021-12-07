package com.ldtteam.animatrix.util.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;

public final class QuaternionMath
{

    private QuaternionMath()
    {
        throw new IllegalArgumentException("Utility Class");
    }


    /**
     * Converts the quaternion to a 4x4 matrix representing the exact same
     * rotation as this quaternion. (The rotation is only contained in the
     * top-left 3x3 part, but a 4x4 matrix is returned here for convenience
     * seeing as it will be multiplied with other 4x4 matrices).
     *
     * More detailed explanation here:
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/
     *
     * @return The rotation matrix which represents the exact same rotation as
     *         this quaternion.
     */
    public static Matrix4f toRotationMatrix(final Quaternion quaternion) {
        final Matrix4f matrix = new Matrix4f();
        final float xy = quaternion.x * quaternion.y;
        final float xz = quaternion.x * quaternion.z;
        final float xw = quaternion.x * quaternion.w;
        final float yz = quaternion.y * quaternion.z;
        final float yw = quaternion.y * quaternion.w;
        final float zw = quaternion.z * quaternion.w;
        final float xSquared = quaternion.x * quaternion.x;
        final float ySquared = quaternion.y * quaternion.y;
        final float zSquared = quaternion.z * quaternion.z;
        matrix.m00 = 1 - 2 * (ySquared + zSquared);
        matrix.m01 = 2 * (xy - zw);
        matrix.m02 = 2 * (xz + yw);
        matrix.m03 = 0;
        matrix.m10 = 2 * (xy + zw);
        matrix.m11 = 1 - 2 * (xSquared + zSquared);
        matrix.m12 = 2 * (yz - xw);
        matrix.m13 = 0;
        matrix.m20 = 2 * (xz - yw);
        matrix.m21 = 2 * (yz + xw);
        matrix.m22 = 1 - 2 * (xSquared + ySquared);
        matrix.m23 = 0;
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        matrix.m33 = 1;
        return matrix;
    }

    /**
     * Extracts the rotation part of a transformation matrix and converts it to
     * a quaternion using the magic of maths.
     *
     * More detailed explanation here:
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
     *
     * @param matrix
     *            - the transformation matrix containing the rotation which this
     *            quaternion shall represent.
     */
    public static Quaternion fromMatrix(final Matrix4f matrix) {
        final float w;
        final float x;
        final float y;
        final float z;
        final float diagonal = matrix.m00 + matrix.m11 + matrix.m22;
        if (diagonal > 0) {
            final float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
            w = w4 / 4f;
            x = (matrix.m21 - matrix.m12) / w4;
            y = (matrix.m02 - matrix.m20) / w4;
            z = (matrix.m10 - matrix.m01) / w4;
        } else if ((matrix.m00 > matrix.m11) && (matrix.m00 > matrix.m22)) {
            final float x4 = (float) (Math.sqrt(1f + matrix.m00 - matrix.m11 - matrix.m22) * 2f);
            w = (matrix.m21 - matrix.m12) / x4;
            x = x4 / 4f;
            y = (matrix.m01 + matrix.m10) / x4;
            z = (matrix.m02 + matrix.m20) / x4;
        } else if (matrix.m11 > matrix.m22) {
            final float y4 = (float) (Math.sqrt(1f + matrix.m11 - matrix.m00 - matrix.m22) * 2f);
            w = (matrix.m02 - matrix.m20) / y4;
            x = (matrix.m01 + matrix.m10) / y4;
            y = y4 / 4f;
            z = (matrix.m12 + matrix.m21) / y4;
        } else {
            final float z4 = (float) (Math.sqrt(1f + matrix.m22 - matrix.m00 - matrix.m11) * 2f);
            w = (matrix.m10 - matrix.m01) / z4;
            x = (matrix.m02 + matrix.m20) / z4;
            y = (matrix.m12 + matrix.m21) / z4;
            z = z4 / 4f;
        }
        return new Quaternion(x, y, z, w);
    }
    
    /**
     * Interpolates between two quaternion rotations and returns the resulting
     * quaternion rotation. The interpolation method here is "nlerp", or
     * "normalized-lerp". Another mnethod that could be used is "slerp", and you
     * can see a comparison of the methods here:
     * https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
     *
     * and here:
     * http://number-none.com/product/Understanding%20Slerp,%20Then%20Not%20Using%20It/
     *
     * @param a
     * @param b
     * @param blend
     *            - a value between 0 and 1 indicating how far to interpolate
     *            between the two quaternions.
     * @return The resulting interpolated rotation in quaternion format.
     */
    public static Quaternion interpolate(final Quaternion a, final Quaternion b, final float blend) {
        final Quaternion result = new Quaternion(0, 0, 0, 1);
        final float dot = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
        final float blendI = 1f - blend;
        if (dot < 0) {
            result.w = blendI * a.w + blend * -b.w;
            result.x = blendI * a.x + blend * -b.x;
            result.y = blendI * a.y + blend * -b.y;
            result.z = blendI * a.z + blend * -b.z;
        } else {
            result.w = blendI * a.w + blend * b.w;
            result.x = blendI * a.x + blend * b.x;
            result.y = blendI * a.y + blend * b.y;
            result.z = blendI * a.z + blend * b.z;
        }
        result.normalise();
        return result;
    }
}