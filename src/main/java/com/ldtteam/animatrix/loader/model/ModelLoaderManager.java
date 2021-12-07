package com.ldtteam.animatrix.loader.model;

import java.util.Collection;
import java.util.stream.Collectors;

import com.ldtteam.animatrix.loader.data.AnimatedModelData;
import com.ldtteam.animatrix.loader.data.JointData;
import com.ldtteam.animatrix.loader.data.MeshData;
import com.ldtteam.animatrix.model.AnimatrixModel;
import com.ldtteam.animatrix.model.IModel;
import com.ldtteam.animatrix.model.skeleton.AnimatrixJoint;
import com.ldtteam.animatrix.model.skeleton.AnimatrixSkeleton;
import com.ldtteam.animatrix.model.skeleton.IJoint;
import com.ldtteam.animatrix.model.skeleton.ISkeleton;
import com.ldtteam.animatrix.model.skin.AnimatrixSkin;
import com.ldtteam.animatrix.model.skin.ISkin;
import com.ldtteam.graphicsexpanded.gpu.GPUMemoryManager;
import com.ldtteam.graphicsexpanded.gpu.VAO;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ModelLoaderManager implements IModelLoaderManager
{
    private final ConcurrentSet<IModelLoader> loaders = new ConcurrentSet<>();

    @Override
    public IModel loadModel(final ResourceLocation location, final ResourceLocation texture)
    {
        final AnimatedModelData data = loaders.stream().filter(l -> l.canLoadModel(location)).findFirst().orElseThrow(() -> new IllegalArgumentException("Not supported model file: " + location)).loadModel(location);
        final ISkin skin = new AnimatrixSkin(createVAO(data.getMeshData()), texture);
        final ISkeleton skeleton = new AnimatrixSkeleton(createJoints(data.getJointsData().getHeadJoint()));

        return new AnimatrixModel(skeleton, skin);
    }

    /**
     * Constructs the joint-hierarchy skeleton from the data extracted from the
     * collada file.
     *
     * @param data
     *            - the joints data from the collada file for the head joint.
     * @return The created joint, with all its descendants added.
     */
    private static IJoint createJoints(final JointData data) {
        final Collection<IJoint> childJoints = data.getChildren().stream().map(ModelLoaderManager::createJoints).collect(Collectors.toList());
        return new AnimatrixJoint(data.getIndex(), data.getNameId(), data.getBindLocalTransform(), childJoints);
    }

    /**
     * Stores the mesh data in a VAO.
     *
     * @param data
     *            - all the data about the mesh that needs to be stored in the
     *            VAO.
     * @return The VAO containing all the mesh data for the model.
     */
    private static VAO createVAO(final MeshData data) {
        final VAO meshVao = GPUMemoryManager.getInstance().createVAO();
        meshVao.bind();
        meshVao.createIndexBuffer(data.getIndices());
        meshVao.createAttribute(0, data.getVertices(), 3);
        meshVao.createAttribute(1, data.getTextureCoords(), 2);
        meshVao.createIntAttribute(2, data.getJointIds(), 3);
        meshVao.createAttribute(3, data.getVertexWeights(), 3);
        meshVao.unbind();
        return meshVao;
    }

    @Override
    public void registerLoader(final IModelLoader loader)
    {
        loaders.add(loader);
    }
}
