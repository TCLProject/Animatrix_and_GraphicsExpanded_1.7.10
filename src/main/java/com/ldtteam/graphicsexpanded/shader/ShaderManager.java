package com.ldtteam.graphicsexpanded.shader;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL20;

import com.ldtteam.graphicsexpanded.shader.program.ShaderProgram;
import com.ldtteam.graphicsexpanded.util.log.Log;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.tclproject.tclgraphics.PortUtil;

@SideOnly(Side.CLIENT)
public final class ShaderManager
{

    private static ShaderManager ourInstance = new ShaderManager();

    public static ShaderManager getInstance()
    {
        return ourInstance;
    }

    private final ConcurrentMap<WeakReference<ShaderProgram>, Integer> shaders = new ConcurrentHashMap();

    private ShaderManager()
    {
    }

    public void initialize()
    {
    	PortUtil.getMinecraft().addScheduledTask(new ClearingRunnable(this));
    }

    public void registerShader(final ShaderProgram shaderProgram)
    {
        Log.getLogger().info("Created Shader: " + shaderProgram.getProgramID());
        shaders.put(new WeakReference<>(shaderProgram), shaderProgram.getProgramID());
    }

    /**
     * Clears up the GPU when VBOs and VAOs are no longer needed.
     */
    private static final class ClearingRunnable implements Runnable
    {

        private final ShaderManager managerToHandle;

        public ClearingRunnable(final ShaderManager managerToHandle) {this.managerToHandle = managerToHandle;}

        @Override
        public void run()
        {
            final List<Map.Entry<WeakReference<ShaderProgram>, Integer>> removedShaders =
              managerToHandle.shaders.entrySet().stream().filter(entry -> entry.getKey().get() == null).collect(Collectors.toList());

            removedShaders.forEach(weakReferenceIntegerEntry -> {
                Log.getLogger().info("Deleting Shader: " + weakReferenceIntegerEntry.getValue());
                managerToHandle.shaders.remove(weakReferenceIntegerEntry.getKey());
                GL20.glDeleteProgram(weakReferenceIntegerEntry.getValue());
            });

            //Reschedule clearing task.
            //We will have to wait a minimal amount of time.
            final Thread rescheduleThread = new Thread(() -> {
                try
                {
                    Thread.sleep(500);
                }
                catch (final InterruptedException ignored)
                {
                }

                try
                {
                	PortUtil.getMinecraft().addScheduledTask(this);
                }
                catch (final Exception ex)
                {
                    Log.getLogger().error("Failed to reregister the ShaderManager. GPU memory leaks will occur.", ex);
                }
            });
            rescheduleThread.start();
        }

        @Override
        public String toString()
        {
            return "ClearingRunnable{" +
                     "managerToHandle=" + managerToHandle +
                     '}';
        }
    }

    @Override
    public String toString()
    {
        return "ShaderManager{" +
                 "shaders=" + shaders +
                 '}';
    }
}
