import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Controls implements Runnable{
	
	private long makeStat1, makeStat2; 
	private long makeLimit = 400;

	private boolean runit;
	
	public void update()	{
		updateControls();
		pollInput();
	}
	
	private void updateControls() {
		float x= 0,y=0,rot = 0;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x = -1;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x = 1;
 
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y = 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y = -1;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) rot = 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) rot = -1;
		
		World.myThing.rot(rot);
		World.myThing.move(x,y);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			World.myThing.rot(0.0f);
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
		
		for (Thing t : World.things) {
			if (t.hit(x,y)) {
				World.myThing = t;
				return true;
			}
		}
		return false;
	}
	
	public void makeThing(float x, float y)	{
		makeStat1 = Timer.getTime();
		if (makeStat1 - makeStat2 > makeLimit) {
			makeStat2 = makeStat1;
			Thing woot = new Thing(x,y);
			World.things.add(woot);
			World.myThing = woot;
			System.out.println(woot);
		}
	}
	
	public void run()	{
		runit=true;
		while(runit) {
			updateControls();
			pollInput();
		}
	}
	public void stop() {
		runit=false;
	}
	
	private void pollInput()	{

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
			    System.out.println(World.things.size());
			}
		    }
		}
	}
}