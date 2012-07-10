package com.geo.mfrts;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.geo.mfrts.objects.Thing;
import com.geo.mfrts.player.Controls;

// This should be implemented as a singleton?
public class World {
	public static Controls controls;
	public static Thing myThing;
	public static CopyOnWriteArrayList<Thing> things;
	public static CopyOnWriteArrayList<Thread> threads;
	public static ExecutorService exe;
	public static boolean go = false;
	
	World() {
		Runtime r = Runtime.getRuntime();
		exe = Executors.newFixedThreadPool(r.availableProcessors());
		threads =  new CopyOnWriteArrayList<Thread>();
		
		controls = new Controls();
	
		things = new CopyOnWriteArrayList<Thing>();
		myThing = new Thing();
		for (int i = 0; i < 50; i++) {
			Random rand = new Random();
			float x = rand.nextFloat()*800;
			float y = rand.nextFloat()*600;
			things.add(new Thing(x,y));
		}
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
