package net.tclproject.tclgraphics.test.animatrix_test;

import java.awt.Color;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;
import net.tclproject.tclgraphics.test.animatrix_test.command.CommandSpawnTestEntity;
import net.tclproject.tclgraphics.test.animatrix_test.entity.AnimatrixTestEntity;
import net.tclproject.tclgraphics.test.animatrix_test.render.AnimatrixTestRender;

@Mod(modid = "animatrix_test", name = "Animatrix - Test", version = "1.0.0")
public class ModAnimatrixTest
{
	
	public static int id = 0;

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void onFMLPreInitialization(final FMLPreInitializationEvent event)
    {
        if (event.getSide() != Side.CLIENT)
            return;

        MinecraftForge.EVENT_BUS.register(this);
        id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(AnimatrixTestEntity.class, "animatrix_test", id);
        EntityRegistry.registerModEntity(AnimatrixTestEntity.class, "animatrix_test", id, this, Short.MAX_VALUE, 1, true);
        EntityList.entityEggs.put(Integer.valueOf(id), new EntityList.EntityEggInfo(id, Color.WHITE.getRGB(), Color.BLACK.getRGB()));
        RenderingRegistry.registerEntityRenderingHandler(AnimatrixTestEntity.class, new AnimatrixTestRender());
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void onFMLServerStarting(final FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSpawnTestEntity());
    }
}
