package net.tclproject.tclgraphics;

import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PortUtil {
	
	private final Thread mcThread = Thread.currentThread();
	public final Queue < FutureTask<? >> scheduledTasks = Queues. < FutureTask<? >> newArrayDeque();
	private static PortUtil instance = new PortUtil();
	
	public static PortUtil getMinecraft() {
		return instance;
	}

	public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule)
    {
        Validate.notNull(callableToSchedule);

        if (this.isCallingFromMinecraftThread())
        {
            try
            {
                return Futures.<V>immediateFuture(callableToSchedule.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
        else
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(callableToSchedule);

            synchronized (this.scheduledTasks)
            {
                this.scheduledTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        Validate.notNull(runnableToSchedule);
        return this.<Object>addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread()
    {
        return Thread.currentThread() == this.mcThread;
    }
    
    public static <T> T func_199748_a(Supplier<T> p_199748_0_) {
        return p_199748_0_.get();
     }
    
    @SideOnly(Side.CLIENT)
    public static float func_226165_i_(float p_226165_0_) {
       float f = 0.5F * p_226165_0_;
       int i = Float.floatToIntBits(p_226165_0_);
       i = 1597463007 - (i >> 1);
       p_226165_0_ = Float.intBitsToFloat(i);
       return p_226165_0_ * (1.5F - f * p_226165_0_ * p_226165_0_);
    }
    
    public static float func_76131_a(float p_76131_0_, float p_76131_1_, float p_76131_2_) {
        if (p_76131_0_ < p_76131_1_) {
           return p_76131_1_;
        } else {
           return p_76131_0_ > p_76131_2_ ? p_76131_2_ : p_76131_0_;
        }
     }
    
    @SideOnly(Side.CLIENT)
    public static PortUtil.EnumOS getOSType()
    {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (s.contains("win"))
        {
            return PortUtil.EnumOS.WINDOWS;
        }
        else if (s.contains("mac"))
        {
            return PortUtil.EnumOS.OSX;
        }
        else if (s.contains("solaris"))
        {
            return PortUtil.EnumOS.SOLARIS;
        }
        else if (s.contains("sunos"))
        {
            return PortUtil.EnumOS.SOLARIS;
        }
        else if (s.contains("linux"))
        {
            return PortUtil.EnumOS.LINUX;
        }
        else
        {
            return s.contains("unix") ? PortUtil.EnumOS.LINUX : PortUtil.EnumOS.UNKNOWN;
        }
    }

    @Nullable
    public static <V> V runTask(FutureTask<V> task, Logger logger)
    {
        try
        {
            task.run();
            return task.get();
        }
        catch (ExecutionException executionexception)
        {
            logger.fatal("Error executing task", (Throwable)executionexception);
        }
        catch (InterruptedException interruptedexception)
        {
            logger.fatal("Error executing task", (Throwable)interruptedexception);
        }

        return (V)null;
    }

    public static <T> T getLastElement(List<T> list)
    {
        return list.get(list.size() - 1);
    }

    @SideOnly(Side.CLIENT)
    public static enum EnumOS
    {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;
    }
    
    // in main game loop
    /* synchronized (this.scheduledTasks)
        {
            while (!this.scheduledTasks.isEmpty())
            {
                PortUtil.runTask(this.scheduledTasks.poll(), LOGGER);
            }
        } */
}
