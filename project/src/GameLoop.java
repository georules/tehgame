import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class GameLoop {
	
	private static long lastFPS, fps;

	private static void updateFPS() {
		if (Timer.getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void initGL()	{		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void renderGL()	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	 
		for (Thing t : World.things) {
			t.render();
		}
		
	}

	public static void run() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		renderGL();
		Display.setVSyncEnabled(true);
		Display.update();
		lastFPS = Timer.getTime();
	
		for (Thread t : World.threads) {
			World.exe.execute(t);
		}
		
		while (!Display.isCloseRequested()) {
			Timer.update();
			World.controls.update();
			World.physics.update();
			renderGL();
			updateFPS();
			Display.update();
			Display.sync(60);
		}
		
	}

}
