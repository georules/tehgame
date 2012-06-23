import org.lwjgl.Sys;

public class Timer {
	
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
