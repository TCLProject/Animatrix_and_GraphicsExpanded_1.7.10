package com.ldtteam.animatrix.loader.model.collada;

import com.ldtteam.animatrix.loader.data.AnimatedModelData;
import com.ldtteam.animatrix.loader.data.MeshData;
import com.ldtteam.animatrix.loader.data.SkeletonData;
import com.ldtteam.animatrix.loader.data.SkinningData;
import com.ldtteam.animatrix.loader.model.IModelLoader;
import com.ldtteam.animatrix.loader.model.ModelLoadingException;
import com.ldtteam.animatrix.util.Constants;
import com.ldtteam.animatrix.util.xml.XmlNode;
import com.ldtteam.animatrix.util.xml.XmlParser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ColladaModelLoader implements IModelLoader
{

    @Override
    public boolean canLoadModel(final ResourceLocation modelLocation)
    {
        return modelLocation.getResourcePath().endsWith(".dae");
    }

    @Override
    public AnimatedModelData loadModel(final ResourceLocation colladaFile) throws ModelLoadingException {
        try
        {
            final XmlNode node = XmlParser.loadXmlFile(colladaFile);
            final ColladaSkinLoader skinLoader = new ColladaSkinLoader(node.getChild("library_controllers"), Constants.Shaders.ArraySizes.CONST_MAX_WEIGHTS_PER_VERTEX);
            final SkinningData skinningData = skinLoader.extractSkinData();

            final ColladaSkeletonLoader jointsLoader = new ColladaSkeletonLoader(node.getChild("library_visual_scenes"), skinningData.getJointOrder());
            final SkeletonData jointsData = jointsLoader.extractBoneData();

            final ColladaGeometryLoader g = new ColladaGeometryLoader(node.getChild("library_geometries"), skinningData.getVerticesSkinData());
            final MeshData meshData = g.extractModelData();

            return new AnimatedModelData(meshData, jointsData);
        }
        catch (final Exception e)
        {
            throw new ModelLoadingException(this.getClass(), colladaFile, e);
        }
	}

/*	public static AnimationData loadColladaAnimation(MyFile colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		ColladaAnimationExtractor loader = new ColladaAnimationExtractor(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}*/

}
