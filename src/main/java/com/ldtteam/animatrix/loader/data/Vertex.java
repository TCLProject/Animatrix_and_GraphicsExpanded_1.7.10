package com.ldtteam.animatrix.loader.data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	
	private static final int NO_INDEX = -1;
	
	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);
	
	
	private VertexSkinData weightsData;
	
	public Vertex(final int index, final Vector3f position, final VertexSkinData weightsData){
		this.index = index;
		this.weightsData = weightsData;
		this.position = position;
		this.length = position.length();
	}
	
	public VertexSkinData getWeightsData(){
		return weightsData;
	}
	
	public void addTangent(final Vector3f tangent){
		tangents.add(tangent);
	}
	
	public void averageTangents(){
		if(tangents.isEmpty()){
			return;
		}
		for(final Vector3f tangent : tangents){
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}
	
	public Vector3f getAverageTangent(){
		return averagedTangent;
	}
	
	public int getIndex(){
		return index;
	}
	
	public float getLength(){
		return length;
	}
	
	public boolean isSet(){
		return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(final int textureIndexOther, final int normalIndexOther){
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
	public void setTextureIndex(final int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(final int normalIndex){
		this.normalIndex = normalIndex;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(final Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
