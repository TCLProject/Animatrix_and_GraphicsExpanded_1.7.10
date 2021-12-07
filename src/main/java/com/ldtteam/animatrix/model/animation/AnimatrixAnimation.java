package com.ldtteam.animatrix.model.animation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.lwjgl.util.vector.Matrix4f;

import com.ldtteam.animatrix.model.IModel;
import com.ldtteam.animatrix.util.animation.IJointTransformMath;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a simple animation in Animatix.
 */
@SideOnly(Side.CLIENT)
public class AnimatrixAnimation implements IAnimation
{

    private final ResourceLocation name;

    private final IModel model;

    private final int             totalLengthInTicks;
    private       int             animationTime;

    private final IKeyFrame[] keyFrames;

    public AnimatrixAnimation(final ResourceLocation name, final IModel model, final int totalLengthInTicks, final IKeyFrame[] keyFrames) {
        this.name = name;
        this.model = model;
        this.totalLengthInTicks = totalLengthInTicks;
        this.keyFrames = keyFrames;
        this.animationTime = 0;
    }

    @Override
    public ResourceLocation getName()
    {
        return name;
    }

    /**
     * Returns to total length of the animation in ticks.
     *
     * @return The total length of the animation in ticks.
     */
    @Override
    public int getTotalLengthInTicks()
    {
        return totalLengthInTicks;
    }

    /**
     * The keyframes of this animation.
     *
     * @return The keyframes of the animation.
     */
    @Override
    public IKeyFrame[] getKeyFrames()
    {
        return keyFrames;
    }

    /**
     * This method should be called each frame to update the animation currently
     * being played. This increases the animation time (and loops it back to
     * zero if necessary), finds the pose that the entity should be in at that
     * time of the animation, and then applies that pose to all the model's
     * joints by setting the joint transforms.
     *
     * @param onAnimationCompleted Callback called when animation completes.
     */
    @Override
    public void update(final Consumer<IAnimation> onAnimationCompleted) {
        increaseAnimationTime(onAnimationCompleted);
    }

    /**
     * Increases the current animation time which allows the animation to
     * progress. If the current animation has reached the end then the timer is
     * reset, causing the animation to loop.
     */
    private void increaseAnimationTime(final Consumer<IAnimation> onAnimationCompleted) {
        animationTime += 1;
        if (animationTime >= getTotalLengthInTicks()) {
            onAnimationCompleted.accept(this);
            this.animationTime %= getTotalLengthInTicks();
        }
    }

    /**
     * This method returns the current animation pose of the entity. It returns
     * the desired local-space transforms for all the joints in a map, indexed
     * by the name of the joint that they correspond to.
     *
     * The pose is calculated based on the previous and next keyframes in the
     * current animation. Each keyframe provides the desired pose at a certain
     * time in the animation, so the animated pose for the current time can be
     * calculated by interpolating between the previous and next keyframe.
     *
     * This method first finds the preious and next keyframe, calculates how far
     * between the two the current animation is, and then calculated the pose
     * for the current animation time by interpolating between the transforms at
     * those keyframes.
     *
     * @return The current pose as a map of the desired local-space transforms
     *         for all the joints. The transforms are indexed by the name ID of
     *         the joint that they should be applied to.
     */
    @Override
    public Map<String, Matrix4f> calculateCurrentAnimationPose() {
        final IKeyFrame[] frames = getPreviousAndNextFrames();
        final float progression = calculateProgression(frames[0], frames[1]);
        return interpolatePoses(frames[0], frames[1], progression);
    }

    /**
     * Finds the previous keyframe in the animation and the next keyframe in the
     * animation, and returns them in an array of length 2. If there is no
     * previous frame (perhaps current animation time is 0.5 and the first
     * keyframe is at time 1.5) then the first keyframe is used as both the
     * previous and next keyframe. The last keyframe is used for both next and
     * previous if there is no next keyframe.
     *
     * @return The previous and next keyframes, in an array which therefore will
     *         always have a length of 2.
     */
    private IKeyFrame[] getPreviousAndNextFrames() {
        IKeyFrame previousFrame = getKeyFrames()[0];
        IKeyFrame nextFrame = getKeyFrames()[0];
        for (int i = 1; i < getKeyFrames().length; i++) {
            nextFrame = getKeyFrames()[i];
            if (nextFrame.getTicksAfterStart() > animationTime) {
                break;
            }
            previousFrame = getKeyFrames()[i];
        }
        return new IKeyFrame[] { previousFrame, nextFrame };
    }

    /**
     * Calculates how far between the previous and next keyframe the current
     * animation time is, and returns it as a value between 0 and 1.
     *
     * @param previousFrame the previous keyframe in the animation.
     * @param nextFrame the next keyframe in the animation.
     * @return A number between 0 and 1 indicating how far between the two
     *         keyframes the current animation time is.
     */
    private float calculateProgression(final IKeyFrame previousFrame, final IKeyFrame nextFrame) {
        final float totalTime = nextFrame.getTicksAfterStart() - previousFrame.getTicksAfterStart();
        final float currentTime = (animationTime + Minecraft.getMinecraft().timer.renderPartialTicks) - previousFrame.getTicksAfterStart();
        return currentTime / totalTime;
    }

    @SubscribeEvent
    public void onTickRenderTick(final TickEvent.RenderTickEvent event)
    {
    }

    /**
     * Calculates all the local-space joint transforms for the desired current
     * pose by interpolating between the transforms at the previous and next
     * keyframes.
     *
     * @param previousFrame previous keyframe in the animation.
     * @param nextFrame the next keyframe in the animation.
     * @param progression a number between 0 and 1 indicating how far between the
     *            previous and next keyframes the current animation time is.
     * @return The local-space transforms for all the joints for the desired
     *         current pose. They are returned in a map, indexed by the name of
     *         the joint to which they should be applied.
     */
    private Map<String, Matrix4f> interpolatePoses(final IKeyFrame previousFrame, final IKeyFrame nextFrame, final float progression) {
        final Map<String, Matrix4f> currentPose = new HashMap<String, Matrix4f>();
        for (final String jointName : previousFrame.getJointTransformMap().keySet()) {
            final IJointTransform previousTransform = previousFrame.getJointTransformMap().get(jointName);
            final IJointTransform nextTransform = nextFrame.getJointTransformMap().get(jointName);
            final IJointTransform currentTransform = IJointTransformMath.interpolate(previousTransform, nextTransform, progression, AnimatrixJointTransform::new);
            currentPose.put(jointName, currentTransform.getJointSpaceTransformMatrix());
        }
        return currentPose;
    }

    @Override
    public String toString()
    {
        return "AnimatrixAnimation{" +
                 "totalLengthInTicks=" + totalLengthInTicks +
                 ", animationTime=" + animationTime +
                 ", keyFrames=" + Arrays.toString(keyFrames) +
                 '}';
    }
}
