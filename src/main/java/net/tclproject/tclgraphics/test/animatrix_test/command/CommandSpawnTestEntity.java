package net.tclproject.tclgraphics.test.animatrix_test.command;

import com.ldtteam.animatrix.loader.animation.IAnimationLoaderManager;
import com.ldtteam.animatrix.loader.model.IModelLoaderManager;
import com.ldtteam.animatrix.model.IModel;
import com.ldtteam.animatrix.util.Log;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.tclproject.tclgraphics.PortUtil;
import net.tclproject.tclgraphics.test.animatrix_test.ModAnimatrixTest;
import net.tclproject.tclgraphics.test.animatrix_test.entity.AnimatrixTestEntity;

@SideOnly(Side.CLIENT)
public class CommandSpawnTestEntity extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "at_test";
    }
    
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(final ICommandSender sender)
    {
        return "Opens the test gui";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (!(sender instanceof EntityPlayerMP))
        {
            return;
        }

        //  /at_test animatrix_test:models/entities/model.dae animatrix_test:textures/entities/diffuse.png
        //  /at_test animatrix_test:model.dae animatrix_test:diffuse.png animatrix_test:modelani.dae

        //  /at_test animatrix_test:models/entities/steve.dae minecraft:textures/entity/alex.png
        //  /at_test animatrix_test:models/entities/steve.dae minecraft:textures/entity/alex.png animatrix_test:animations/entities/steve.dae

        //  /at_test animatrix_test:models/entities/fr_biped.dae minecraft:textures/entity/alex.png
        //  /at_test animatrix_test:models/entities/fr_biped.dae minecraft:textures/entity/alex.png animatrix_test:animations/entities/fr_biped.dae

        //  /at_test animatrix_test:models/entities/human.dae animatrix_test:textures/entities/skin.png
        //  /at_test animatrix_test:human.dae animatrix_test:skin.png animatrix_test:humanani.dae
        PortUtil.getMinecraft().addScheduledTask(() -> {
        	
            final AnimatrixTestEntity entity = (AnimatrixTestEntity) EntityList.createEntityByID(ModAnimatrixTest.id, Minecraft.getMinecraft().theWorld);

            final IModel model = IModelLoaderManager.getInstance().loadModel(new ResourceLocation(args[0]), new ResourceLocation(args[1]));

            if (args.length == 3)
            {
                try
                {
                    model.getAnimator().startAnimation(IAnimationLoaderManager.getInstance().loadAnimation(model, new ResourceLocation(args[2])));
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }
            }

            entity.setModel(model);
            entity.setPositionAndUpdate(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);

            Minecraft.getMinecraft().theWorld.spawnEntityInWorld(entity);
        });
    }
}
