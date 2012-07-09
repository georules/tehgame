package com.geo.mfrts;

import com.geo.mfrts.objects.Thing;
import com.geo.mfrts.util.Timer;

public class Physics implements Runnable {
	
	private float gravity = -0.02f;
	private boolean doit;
	private int iterations = 5;

	public void update()	{
		Timer.writeLock.lock();
		try{
			updatePhysics();
			constraints();
		}
		finally{
			Timer.writeLock.unlock();
		}
	}
	
	private void updatePhysics() {
		float delta = Timer.delta / iterations;
		for(int i = 0; i <= iterations; i++) {
			for(Thing t: World.things) {
				float falldown = gravity * delta;
				float dampen = (-1)*gravity / 100 * delta;
				t.move(0,falldown);
				t.dampen(dampen);
				t.update(delta);
			}
		}
	}
	private void constraints()	{
		for (Thing t: World.things) {
			t.limitMove();
		}
	}
	public void run()	{
		doit=true;
		while(doit) {
			update();
		}
	}

	public void stop() {
		doit=false;
		
	}
}
