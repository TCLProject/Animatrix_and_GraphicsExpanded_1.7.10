package com.ldtteam.animatrix.model.skeleton;

import org.lwjgl.util.vector.Matrix4f;

import com.ldtteam.animatrix.model.AnimatrixModel;
import com.ldtteam.animatrix.util.array.ArrayUtility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents a skeleton in a {@link AnimatrixModel}.
 */
@SideOnly(Side.CLIENT)
public class AnimatrixSkeleton implements ISkeleton
{
    //The root joint of the skeleton.
    //All child joints will be stored under this joint.
    private final IJoint rootJoint;
    //Contains the count of joints in this skeleton.
    private final int jointCount;

    public AnimatrixSkeleton(final IJoint rootJoint) {
        this.rootJoint = rootJoint;
        this.jointCount = rootJoint.getMaxJointIndex() + 1;
        this.rootJoint.calculateIMSBT(new Matrix4f());
    }

    /**
     * Returns the root Joint for the skeleton.
     * @return The root joint for the skeleton.
     */
    @Override
    public IJoint getRootJoint()
    {
        return rootJoint;
    }

    /**
     * The total amount of joints that make up this skeleton.
     * @return The amount of joints in the skeleton.
     */
    @Override
    public int getJointCount()
    {
        return jointCount;
    }

    /**
     * Gets an array of the all important model-space transforms of all the
     * joints (with the current animation pose applied) in the skelton. The
     * joints are ordered in the array based on their joint index. The position
     * of each joint's transform in the array is equal to the joint's index.
     *
     * @return The array of model-space transforms of the joints in the current
     *         animation pose.
     */
    @Override
    public Matrix4f[] getAnimationModelSpaceTransformsFromJoints() {
        final Matrix4f[] jointMatrices = new Matrix4f[jointCount];
        ArrayUtility.InitializeArray(jointMatrices, Matrix4f::new);

        addJointsToArray(rootJoint, jointMatrices);
        return jointMatrices;
    }

    /**
     * This adds the current model-space transform of a joint (and all of its
     * descendants) into an array of transforms. The joint's transform is added
     * into the array at the position equal to the joint's index.
     *
     * @param headJoint the current joint being added to the array. This method also
     *            adds the transforms of all the descendents of this joint too.
     * @param jointMatrices the array of joint transforms that is being filled.
     */
    private void addJointsToArray(final IJoint headJoint, final Matrix4f[] jointMatrices) {
        if (headJoint .getIndex() != -1)
        {
            jointMatrices[headJoint.getIndex()] = headJoint.getAnimationModelSpaceTransform();
        }
        headJoint.getChildJoints().forEach(childJoint -> addJointsToArray(childJoint, jointMatrices));
    }

    @Override
    public String toString()
    {
        return "AnimatrixSkeleton{" +
                 "rootJoint=" + rootJoint +
                 ", jointCount=" + jointCount +
                 '}';
    }
}
