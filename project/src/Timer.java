import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.lwjgl.Sys;

public class Timer {
	
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public static final Lock readLock = lock.readLock();
	public static final Lock writeLock = lock.writeLock();
	
	private static long lastTime;
	public static int delta;
	
	public static void update()	{
		long time = getTime();
		delta = (int)(time-lastTime);
		lastTime = time;
	}
	
	public static long getTime()	{
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}

}
