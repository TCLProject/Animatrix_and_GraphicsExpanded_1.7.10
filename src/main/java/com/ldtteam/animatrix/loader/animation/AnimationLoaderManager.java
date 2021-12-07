package com.ldtteam.animatrix.loader.animation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.ldtteam.animatrix.loader.data.AnimationData;
import com.ldtteam.animatrix.loader.data.JointTransformData;
import com.ldtteam.animatrix.loader.data.KeyFrameData;
import com.ldtteam.animatrix.model.IModel;
import com.ldtteam.animatrix.model.animation.AnimatrixAnimation;
import com.ldtteam.animatrix.model.animation.AnimatrixJointTransform;
import com.ldtteam.animatrix.model.animation.AnimatrixKeyFrame;
import com.ldtteam.animatrix.model.animation.IAnimation;
import com.ldtteam.animatrix.model.animation.IJointTransform;
import com.ldtteam.animatrix.model.animation.IKeyFrame;
import com.ldtteam.animatrix.util.math.QuaternionMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class AnimationLoaderManager implements IAnimationLoaderManager
{
    private final ConcurrentSet<IAnimationLoader> loaders = new ConcurrentSet<>();

    @Override
    public IAnimation loadAnimation(final IModel model, final ResourceLocation location) throws AnimationLoadingException
    {
       final AnimationData data = loaders.stream().filter(loader -> loader.canLoadAnimation(location)).findFirst().orElseThrow(() -> new IllegalArgumentException("Not supported animation file: " + location)).loadAnimation(location);
       final Integer lengthInTicks = (int) (data.getLengthSeconds() * 20);
       final IKeyFrame[] keyFrames = Arrays.stream(data.getKeyFrames()).map(AnimationLoaderManager::createKeyFrame).toArray(IKeyFrame[]::new);

       return new AnimatrixAnimation(location, model, lengthInTicks, keyFrames);
    }

    /**
     * Creates a keyframe from the data extracted from the collada file.
     *
     * @param data the data about the keyframe that was extracted from the
     *            collada file.
     * @return The keyframe.
     */
    private static IKeyFrame createKeyFrame(final KeyFrameData data) {
        final Map<String, IJointTransform> map = new HashMap<>();
        for (final JointTransformData jointData : data.getJointTransforms()) {
            final IJointTransform jointTransform = createTransform(jointData);
            map.put(jointData.getJointNameId(), jointTransform);
        }
        return new AnimatrixKeyFrame((int) (data.getTime() * 20), map);
    }

    /**
     * Creates a joint transform from the data extracted from the collada file.
     *
     * @param data
     *            - the data from the collada file.
     * @return The joint transform.
     */
    private static IJointTransform createTransform(final JointTransformData data) {
        final Matrix4f mat = data.getJointLocalTransform();
        final Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
        final Quaternion rotation = QuaternionMath.fromMatrix(mat);
        return new AnimatrixJointTransform(data.getJointNameId(), translation, rotation);
    }

    @Override
    public void registerLoader(final IAnimationLoader loader)
    {
        loaders.add(loader);
    }
}
