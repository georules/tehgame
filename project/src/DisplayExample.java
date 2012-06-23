import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class DisplayExample {
	public static World state;
	
	public void start() {
			
		GameLoop.run();
	
		World.exe.shutdown();
		try {
			World.exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Display.destroy();
	}
	
	public static void main(String[] argv) {
		state = new World();
		DisplayExample displayExample = new DisplayExample();
		displayExample.start();
	}
}