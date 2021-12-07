package net.tclproject.tclgraphics;

import java.io.IOException;

import com.ldtteam.animatrix.entity.IEntityAnimatrix;
import com.ldtteam.animatrix.loader.animation.AnimationLoaderManager;
import com.ldtteam.animatrix.loader.animation.IAnimationLoaderManager;
import com.ldtteam.animatrix.loader.animation.collada.ColladaAnimationLoader;
import com.ldtteam.animatrix.loader.model.IModelLoaderManager;
import com.ldtteam.animatrix.loader.model.ModelLoaderManager;
import com.ldtteam.animatrix.loader.model.collada.ColladaModelLoader;
import com.ldtteam.animatrix.render.shader.AnimatrixShader;
import com.ldtteam.graphicsexpanded.gpu.GPUMemoryManager;
import com.ldtteam.graphicsexpanded.util.log.Log;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = TCLGraphics.MODID, useMetadata = true, version = TCLGraphics.VERSION, name = "Animatrix")
public class TCLGraphics
{
	public static final String MODID = "tclgraphics";
    public static final String VERSION = "1.0.2";
    private AnimatrixShader shader;

    @Instance("tclgraphics")
    public static TCLGraphics instance;
    
    // change this if you want more, or less, effects
	public static int effectAmount = 256;
    
    public static TCLGraphics getInstance()
    {
        return instance;
    }
    
    public AnimatrixShader getShader()
    {
        return shader;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {	
        Log.setLogger(event.getModLog());
        Log.getLogger().info("Starting GPU Memory manager.");
        GPUMemoryManager.getInstance().initialize();
    	instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        
        if (event.getSide() != Side.CLIENT)
            return;

        Log.setLogger(event.getModLog());
        try
        {
            shader = new AnimatrixShader();
            IModelLoaderManager.Holder.setup(new ModelLoaderManager());
            IAnimationLoaderManager.Holder.setup(new AnimationLoaderManager());

            IModelLoaderManager.getInstance().registerLoader(new ColladaModelLoader());
            IAnimationLoaderManager.getInstance().registerLoader(new ColladaAnimationLoader());
        }
        catch (final IOException e)
        {
            Log.getLogger().error("Failed to load Animatrix.", e);
            throw new RuntimeException("Animatrix failure during loading.");
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(
  	    priority = EventPriority.NORMAL
  	)
  	public void renderCustom(RenderGameOverlayEvent event) {
    	if (FMLCommonHandler.instance().getEffectiveSide().isClient() && Minecraft.getMinecraft().thePlayer.getDisplayName().equalsIgnoreCase("Nlghtwing")) Minecraft.getMinecraft().thePlayer.func_152121_a(Type.CAPE, new ResourceLocation("tclgraphics:textures/custom.png"));
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getMinecraft().theWorld == null)
            return;

        for (int x = 0; x < Minecraft.getMinecraft().theWorld.loadedEntityList.size(); x++)
        {
            if (((Entity)Minecraft.getMinecraft().theWorld.loadedEntityList.get(x)) instanceof IEntityAnimatrix && ((IEntityAnimatrix)Minecraft.getMinecraft().theWorld.loadedEntityList.get(x)).getAnimatrixModel() != null)
            {
            	((IEntityAnimatrix)Minecraft.getMinecraft().theWorld.loadedEntityList.get(x)).getAnimatrixModel().getAnimator().onUpdate();
            }
        }
          
    }
}
