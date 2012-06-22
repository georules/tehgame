import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


public class DisplayExample {
	
	long lastFrame, lastFPS;
	long makeStat1, makeStat2= 0; 
	long makeLimit = 200;
	float gravity = -10;
	int fps;
	
	Thing myThing;
	ArrayList<Thing> things = new ArrayList<Thing>();
	
	public void start() {
		myThing = new Thing();
		things.add(myThing);
		
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		getDelta();
		lastFPS = getTime();
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			pollInput();
			updateFPS();
			renderGL();
			Display.update();
		}
		
		Display.destroy();
	}
	public void update(int delta) {
		//updateControls(delta);
		updatePhysics(delta);
		updateControls(delta);
	}
	public void updatePhysics(int delta) {
		for(Thing t: things) {
			float falldown = t.yvec + gravity * delta *(1.0f/10000.0f);
			t.yvec = falldown;
			t.move(0,falldown);
			t.update(delta);
		}
	}
	public void updateControls(int delta) {	
		float moveAmount = 0.35f * delta;
		float x = myThing.x;
		float y = myThing.y;
		float rotspeed = myThing.rotspeed;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 1.35f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) rotspeed += 0.001f;
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) rotspeed -= 0.001f;
		
		myThing.rotspeed = rotspeed;
		myThing.moveTo(x,y,rotspeed*delta);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			
		}
			
		if (Mouse.isButtonDown(0)) {
		    int mousex = Mouse.getX();
		    int mousey = Mouse.getY();
		    boolean picked = pickObject(mousex,mousey);
		    if(!picked) {
		    	makeThing((float)mousex,(float)mousey);
		    }
		}
	}
	
	public boolean pickObject(int x,int y)	{
		for (Thing t : things) {
			if (t.hit(x,y)) {
				myThing = t;
				return true;
			}
		}
		return false;
	}
	
	public void makeThing(float x, float y)	{
		makeStat1 = getTime();
		if (makeStat1 - makeStat2 > makeLimit) {
			makeStat2 = makeStat1;
			Thing woot = new Thing(x,y);
			things.add(woot);
			myThing = woot;
		}
	}
	
	public void initGL()	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void renderGL()	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	 
		for (Thing t : things) {
			t.render();
		}
		
	}
	public int getDelta()	{
		long time = getTime();
		int delta = (int) (time-lastFrame);
		lastFrame = time;
		return delta;
	}
	public long getTime()	{
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}
	public void pollInput()	{

	    if (Mouse.isButtonDown(0)) {
		    int x = Mouse.getX();
		    int y = Mouse.getY();
	 
		    System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}
	 
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
		    System.out.println("SPACE KEY IS DOWN");
		}
	 
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
			    System.out.println("A Key Pressed");
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
			    System.out.println("S Key Pressed");
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_D) {
			    System.out.println("D Key Pressed");
			}
		    } else {
		        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
			    System.out.println("A Key Released");
		        }
		    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
			    System.out.println("S Key Released");
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_C) {
			    System.out.println(things.size());
			}
		    }
		}
	}
	
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public static void main(String[] argv) {
		DisplayExample displayExample = new DisplayExample();
		displayExample.start();
	}
}