import org.lwjgl.opengl.GL11;


public class Thing {
	public Vec3 location = new Vec3(400,300,0);
	public Vec3 velocity = new Vec3(0,0,0);
	private float rot = 0, rotspeed = 0;
	private float xvel, yvel;
	private float r,g,b;
	
	public String toString() {
		return super.toString() + " " + location.v1 + " " + location.v2 + " " + rot + " " + rotspeed+ " " + xvel + " " + yvel;
	}
	
	Thing()	{
		randomColors();
	}
	Thing(float x, float y) {
		location.v1 = x;
		location.v2 = y;
		randomColors();
	}
	public boolean hit(int x, int y) {
		if ((Math.abs(location.v1-x) < 50) && (Math.abs(location.v2-y)<50)) {
			return true;
		}
		return false;
		
	}
	public void randomColors() {
		r = (float)Math.random();
		g = (float)Math.random();
		b = (float)Math.random();
	}
	
	public void move(float vx,float vy) {
		xvel += vx*0.02;
		yvel += vy*0.02;
	}
	
	public void stop()	{
		xvel = yvel = rotspeed = 0;
	}
	
	public void limitMove() {
		if (location.v1 < 0) {
			location.v1 = 0;
		}
		if (location.v1 > 800) {
			location.v1 = 800;
		}
		if (location.v2 < 0) {
			location.v2 = 0;
			yvel=0;
		}
		if (location.v2 > 600)  {
			location.v2 = 600;
			yvel=0;
		}
		if (rotspeed > 0.50)
			rotspeed = 0.50f;
		if (rotspeed < -0.50)
			rotspeed = -0.50f;
	}
	
	public void update(float delta)	{
		location.v1 += xvel*delta;
		location.v2 += yvel*delta;
		rot += rotspeed*delta;
	}
	
	public void render() {
		GL11.glColor3f(r, g, b);
		GL11.glPushMatrix();
			GL11.glTranslatef(location.v1, location.v2, location.v3);
			GL11.glRotatef(rot, 0f, 0f, 1f);
			GL11.glTranslatef(-location.v1, -location.v2, -location.v3);
 
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(location.v1 - 50, location.v2 - 50);
				GL11.glVertex2f(location.v1 + 50, location.v2 - 50);
				GL11.glVertex2f(location.v1 + 50, location.v2 + 50);
				GL11.glVertex2f(location.v1 - 50, location.v2 + 50);
			GL11.glEnd();
		GL11.glPopMatrix();
	}

	public void rot(float r) {
		rotspeed += r * 0.01;
	}

	public void dampen(float dampen) {
		if (xvel < 0) {
			xvel += dampen;
		}
		if (xvel > 0) {
			xvel -= dampen;
		}
		
	}
}
