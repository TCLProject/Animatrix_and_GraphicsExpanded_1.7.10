package com.ldtteam.animatrix.render.shader;

import static com.ldtteam.animatrix.util.Constants.Shaders.ArraySizes.CONST_MAX_JOINT_COUNT;
import static com.ldtteam.animatrix.util.Constants.Shaders.Variables.CONST_JOINT_INDICES;
import static com.ldtteam.animatrix.util.Constants.Shaders.Variables.CONST_POSITION;
import static com.ldtteam.animatrix.util.Constants.Shaders.Variables.CONST_TEXTURE_COORDS;
import static com.ldtteam.animatrix.util.Constants.Shaders.Variables.CONST_WEIGHTS;

import java.io.IOException;

import com.ldtteam.animatrix.util.Constants;
import com.ldtteam.graphicsexpanded.shader.program.ShaderProgram;
import com.ldtteam.graphicsexpanded.shader.uniform.UniformMat4Array;
import com.ldtteam.graphicsexpanded.shader.uniform.UniformSampler;

public class AnimatrixShader extends ShaderProgram
{
    private final UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", CONST_MAX_JOINT_COUNT);
    private final UniformSampler textureSampler = new UniformSampler("texture");

    public AnimatrixShader() throws IOException
    {
        super(Constants.Shaders.Vertex.ANIMATRIX_DEFAULT, Constants.Shaders.Fragment.ANIMATRIX_DEFAULT, CONST_POSITION, CONST_TEXTURE_COORDS, CONST_JOINT_INDICES, CONST_WEIGHTS);
        super.storeAllUniformLocations(jointTransforms, textureSampler);
    }

    public UniformMat4Array getJointTransforms()
    {
        return jointTransforms;
    }

    public UniformSampler getTextureSampler()
    {
        return textureSampler;
    }
}
