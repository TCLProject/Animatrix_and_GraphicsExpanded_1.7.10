package com.ldtteam.animatrix.util.animation;

import com.ldtteam.animatrix.model.animation.IJointTransform;
import com.ldtteam.animatrix.util.math.QuaternionMath;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public final class IJointTransformMath
{

    private IJointTransformMath()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    /**
     * Interpolates between two transforms based on the progression value. The
     * result is a new transform which is part way between the two original
     * transforms. The translation can simply be linearly interpolated, but the
     * rotation interpolation is slightly more complex, using a method called
     * "SLERP" to spherically-linearly interpolate between 2 quaternions
     * (rotations). This gives a much much better result than trying to linearly
     * interpolate between Euler rotations.
     *
     * @param frameA the previous transform
     * @param frameB the next transform
     * @param progression a number between 0 and 1 indicating how far between the two
     *            transforms to interpolate. A progression value of 0 would
     *            return a transform equal to "frameA", a value of 1 would
     *            return a transform equal to "frameB". Everything else gives a
     *            transform somewhere in-between the two.
     * @param constructor A callback to create a new instance after interpolation has succeeded.
     *
     * @return A interpolated {@link IJointTransform} from A and B with progression.
     * @throws IllegalArgumentException when the joint names of A and B do not match.
     */
    public static IJointTransform interpolate(final IJointTransform frameA, final IJointTransform frameB, final float progression, final IJointTransformConstructor constructor) {
        if (!frameA.getJointName().equals(frameB.getJointName()))
            throw new IllegalArgumentException("A and B have different Joints");

        final Vector3f pos = interpolate(frameA.getPosition(), frameB.getPosition(), progression);
        final Quaternion rot = QuaternionMath.interpolate(frameA.getRotation(), frameB.getRotation(), progression);
        return constructor.apply(frameA.getJointName(), pos, rot);
    }

    /**
     * Linearly interpolates between two translations based on a "progression"
     * value.
     *
     * @param start the start translation.
     * @param end the end translation.
     * @param progression value between 0 and 1 indicating how far to interpolate
     *            between the two translations.
     * @return A new interpolated vector.
     */
    public static Vector3f interpolate(final Vector3f start, final Vector3f end, final float progression) {
        final float x = start.x + (end.x - start.x) * progression;
        final float y = start.y + (end.y - start.y) * progression;
        final float z = start.z + (end.z - start.z) * progression;
        return new Vector3f(x, y, z);
    }

    /**
     * Callback interface to create a new {@link IJointTransform} after interpolation.
     */
    @FunctionalInterface
    public interface IJointTransformConstructor
    {
        /**
         * Creates a new {@link IJointTransform} from the given name, position and rotation.
         *
         * @param jointName The name of the joint.
         * @param position The position of the joint.
         * @param rotation The rotation of the joint.
         *
         * @return The new {@link IJointTransform} for the given name, position and rotation.
         */
        IJointTransform apply(final String jointName, final Vector3f position, final Quaternion rotation);
    }

}