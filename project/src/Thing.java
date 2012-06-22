import org.lwjgl.opengl.GL11;


public class Thing {
	public float x = 400, y = 300;
	public float rot = 0, rotspeed = 0;
	public float xvec, yvec;
	public float r,g,b;
	
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
	
	public void moveTo(float newX, float newY, float rot)	{
		this.x = newX;
		this.y = newY;
		this.rot += rot;
		limitMove();
	}
	public void move(float moreX,float moreY) {
		x += moreX;
		y += moreY;
		limitMove();
	}
	
	public void limitMove() {
		if (x < 0) x = 0;
		if (x > 800) x = 800;
		if (y < 0) {
			y = 0;
			yvec = 0;
		}
		if (y > 600)  {
			y = 600;
			yvec =0;
		}
		if (rotspeed > 0.20)
			rotspeed = 0.20f;
		if (rotspeed < -0.20)
			rotspeed = -0.20f;
	}
	
	public void update(int delta)	{
		limitMove();
		rot += rotspeed*delta;
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
}
