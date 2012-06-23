import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class World {
	public static Controls controls;
	public static Physics physics;
	public static Thing myThing;
	public static CopyOnWriteArrayList<Thing> things;
	public static CopyOnWriteArrayList<Thread> threads;
	public static ExecutorService exe; 
	
	World() {
		Runtime r = Runtime.getRuntime();
		exe = Executors.newFixedThreadPool(r.availableProcessors());
		threads =  new CopyOnWriteArrayList<Thread>();
		
		controls = new Controls();
		physics = new Physics();
		
		things = new CopyOnWriteArrayList<Thing>();
		myThing = new Thing();
		things.add(myThing);
	}
}
