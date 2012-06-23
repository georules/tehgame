public class Physics implements Runnable {
	
	private float gravity = -0.02f;
	private boolean doit;

	public void update()	{
		updatePhysics();
	}
	private void updatePhysics() {
		float delta = Timer.delta;
		for(Thing t: World.things) {
			float falldown = gravity * delta;
			t.move(0,falldown);
			t.update(delta);
			System.out.println(delta);
		}
	}
	
	public void run()	{
		doit=true;
		while(doit) {
			updatePhysics();
		}
	}

	public void stop() {
		doit=false;
		
	}
}
