import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


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
		// to do 3d, will need to implement raytracing for picking objects
		// http://schabby.de/picking-opengl-ray-tracing/
		
		GL11.glViewport(0,0,800,600);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glClearColor(0.3f,0.7f,0.9f,0.0f);
		GL11.glOrtho(0, 800, 0, 600, -500, 500);
		//GLU.gluPerspective(45f,(800.0f/600.0f),1f,1000f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		//GLU.gluLookAt(800/2,600/2,1000, 800/2,600/2,0, 0,1,1);
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
