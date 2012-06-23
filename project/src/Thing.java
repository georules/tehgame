import org.lwjgl.opengl.GL11;


public class Thing {
	private float x = 400, y = 300;
	private float rot = 0, rotspeed = 0;
	private float xvel, yvel;
	private float r,g,b;
	
	public String toString() {
		return super.toString() + " " + x + " " + y + " " + rot + " " + rotspeed+ " " + xvel + " " + yvel;
	}
	
	Thing()	{
		randomColors();
	}
	Thing(float x, float y) {
		this.x = x;
		this.y = y;
		randomColors();
	}
	public boolean hit(int x, int y) {
		if ((Math.abs(this.x-x) < 50) && (Math.abs(this.y-y)<50)) {
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
		limitMove();
	}
	
	public void limitMove() {
		if (x < 0) {
			x = 0;
			xvel = 0;
		}
		if (x > 800) {
			xvel = 0;
			x = 800;
		}
		if (y < 0) {
			y = 0;
			yvel=0;
		}
		if (y > 600)  {
			y = 600;
			yvel=0;
		}
		if (rotspeed > 0.50)
			rotspeed = 0.50f;
		if (rotspeed < -0.50)
			rotspeed = -0.50f;
	}
	
	public void update(float delta)	{
		x += xvel*delta;
		y += yvel*delta;
		rot += rotspeed*delta;
		limitMove();
		System.out.println(this);
	}
	
	public void render() {
		GL11.glColor3f(r, g, b);
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glRotatef(rot, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
 
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x - 50, y - 50);
				GL11.glVertex2f(x + 50, y - 50);
				GL11.glVertex2f(x + 50, y + 50);
				GL11.glVertex2f(x - 50, y + 50);
			GL11.glEnd();
		GL11.glPopMatrix();
	}

	public void rot(float r) {
		rotspeed += r * 0.01;
	}
}
