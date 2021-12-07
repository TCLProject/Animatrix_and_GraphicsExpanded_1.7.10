package com.ldtteam.animatrix.loader.model.collada;

import java.nio.FloatBuffer;
import java.util.List;

import com.ldtteam.animatrix.loader.data.JointData;
import com.ldtteam.animatrix.loader.data.SkeletonData;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.ldtteam.animatrix.util.xml.XmlNode;

public class ColladaSkeletonLoader
{

	private XmlNode armatureData;
	
	private List<String> boneOrder;
	
	private int jointCount = 0;
	
	private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));

	public ColladaSkeletonLoader(final XmlNode visualSceneNode, final List<String> boneOrder) {
		this.armatureData = visualSceneNode.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		this.boneOrder = boneOrder;
	}
	
	public SkeletonData extractBoneData(){
		final XmlNode headNode = armatureData.getChild("node");
		final JointData headJoint = loadJointData(headNode, true);
		return new SkeletonData(jointCount, headJoint);
	}
	
	private JointData loadJointData(final XmlNode jointNode, final boolean isRoot){
		final JointData joint = extractMainJointData(jointNode, isRoot);
		for(final XmlNode childNode : jointNode.getChildren("node")){
			joint.addChild(loadJointData(childNode, false));
		}
		return joint;
	}
	
	private JointData extractMainJointData(final XmlNode jointNode, final boolean isRoot){
		final String nameId = jointNode.getAttribute("id");
		final int index = boneOrder.indexOf(nameId);
		final String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		final Matrix4f matrix = new Matrix4f();
		matrix.load(convertData(matrixData));
		matrix.transpose();
		if(isRoot){
			//because in Blender z is up, but in our game y is up.
			Matrix4f.mul(CORRECTION, matrix, matrix);
		}
		jointCount++;
		return new JointData(index, nameId, matrix);
	}
	
	private FloatBuffer convertData(final String[] rawData){
		final float[] matrixData = new float[16];
		for(int i=0;i<matrixData.length;i++){
			matrixData[i] = Float.parseFloat(rawData[i]);
		}
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrixData);
		buffer.flip();
		return buffer;
	}

}
