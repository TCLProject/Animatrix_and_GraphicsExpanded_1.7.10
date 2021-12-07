package com.ldtteam.graphicsexpanded.shader.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.ldtteam.animatrix.util.Constants;
import com.ldtteam.graphicsexpanded.shader.ShaderManager;
import com.ldtteam.graphicsexpanded.shader.uniform.Uniform;
import com.ldtteam.graphicsexpanded.util.log.Log;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ShaderProgram {

	private int programID;

	public ShaderProgram(final ResourceLocation vertexFile, final ResourceLocation fragmentFile, final String... inVariables) throws IOException
    {
        programID = GL20.glCreateProgram();

		int vertexShaderID = -1;
        if (vertexFile != null)
        {
        	try {
        		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        	} catch (Exception e) {
        		vertexShaderID = loadShader(Constants.Shaders.Vertex.ANIMATRIX_DEFAULT_FALLBACK, GL20.GL_VERTEX_SHADER);
        	}
            GL20.glAttachShader(programID, vertexShaderID);
        }

		int fragmentShaderID = -1;
		if (fragmentFile != null)
        {
            fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
            GL20.glAttachShader(programID, fragmentShaderID);
        }

		bindAttributes(inVariables);

		GL20.glLinkProgram(programID);

		if (vertexShaderID != -1)
        {
            GL20.glDetachShader(programID, vertexShaderID);
            GL20.glDeleteShader(vertexShaderID);
        }

		if (fragmentShaderID != -1)
        {
            GL20.glDetachShader(programID, fragmentShaderID);
            GL20.glDeleteShader(fragmentShaderID);
        }

        ShaderManager.getInstance().registerShader(this);
	}
	
	protected void storeAllUniformLocations(final Uniform... uniforms){
		for(final Uniform uniform : uniforms){
			uniform.storeUniformLocation(programID);
		}
		GL20.glValidateProgram(programID);
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

    public int getProgramID()
    {
        return programID;
    }

    private void bindAttributes(final String[] inVariables){
		for(int i=0;i<inVariables.length;i++){
			GL20.glBindAttribLocation(programID, i, inVariables[i]);
		}
	}
	
	private int loadShader(final ResourceLocation file, final int type) throws IOException
    {
		final String shaderSource = readFileAsString(file);
		final int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Log.getLogger().error(GL20.glGetShaderInfoLog(shaderID, 500));
            Log.getLogger().error("Could not compile shader "+ file);
            throw new IllegalStateException("Could not load shader.");
		}
		return shaderID;
	}

    private static String readFileAsString(final ResourceLocation filename) throws IOException
    {
        final InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(filename).getInputStream();

        if(in == null)
            return "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
