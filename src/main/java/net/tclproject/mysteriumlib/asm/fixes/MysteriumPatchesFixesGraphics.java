package net.tclproject.mysteriumlib.asm.fixes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.tclproject.mysteriumlib.asm.annotations.Fix;
import net.tclproject.tclgraphics.PortUtil;

public class MysteriumPatchesFixesGraphics {
	
	private static final Logger LOGGER = LogManager.getLogger("Animatrix");
	
	@Fix
	public static void runGameLoop(Minecraft mc)
    {
		synchronized (PortUtil.getMinecraft().scheduledTasks)
        {
            while (!PortUtil.getMinecraft().scheduledTasks.isEmpty())
            {
            	PortUtil.runTask(PortUtil.getMinecraft().scheduledTasks.poll(), LOGGER);
            }
        }
    }

}
