package com.geo.mfrts;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.geo.mfrts.objects.Thing;
import com.geo.mfrts.player.Controls;

// This should be implemented as a singleton?
public class World {
	public static Controls controls;
	public static Thing myThing;
	public static ArrayList<Thing> things;
	public static CopyOnWriteArrayList<Thread> threads;
	public static ExecutorService exe;
	
	World() {
		Runtime r = Runtime.getRuntime();
		exe = Executors.newFixedThreadPool(r.availableProcessors());
		threads =  new CopyOnWriteArrayList<Thread>();
		
		controls = new Controls();
	
		things = new ArrayList<Thing>();
		myThing = new Thing();
		things.add(myThing);
	}
	
	public static void stop() {
		controls.stop();
		exe.shutdown();
	}
	
	public static void addThread(Thread t) {
		threads.add(t);
	}

	public static void update() {
		controls.update();
	}
}
