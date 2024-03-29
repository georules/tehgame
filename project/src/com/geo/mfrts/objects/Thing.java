package com.geo.mfrts.objects;

import java.io.Serializable;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.geo.mfrts.World;
import com.geo.mfrts.util.Vec3;


public class Thing implements Serializable{
	public String hash;
	public Vec3 location = new Vec3(400,300,0);
	public Vec3 velocity = new Vec3(0,0,0);
	private float rot = 0, rotspeed = 0;
	public float xvel, yvel;
	private float r,g,b;

	
	public String toString() {
		return super.toString() + " x " + location.v1 + " y " + location.v2 + " z " + location.v3;
	}
	
	public Thing()	{
		randomColors();
	}
	public Thing(Thing x) {
		location = x.location;
		r = x.r;
		b = x.b;
		g = x.g;
	}
	public Thing(float x, float y) {
		location.v1 = x;
		location.v2 = y;
		location.v3 = 100.0f;
		randomColors();
	}
	public boolean hit(int x, int y) {
		int area = 25;
		if ((Math.abs(location.v1-x) < area) && (Math.abs(location.v2-y)<area)) {
			return true;
		}
		return false;
		
	}
	public void randomColors() {
		r = (float)Math.random();
		g = (float)Math.random();
		b = (float)Math.random();
		r = (r+0.5f) / 1.5f;
		g = (g+0.5f) / 1.5f;
		b = (b+0.5f) / 1.5f;
	}
	public void setColor(float red, float green, float blue) {
		r = red;
		g = green;
		b = blue;
	}
	
	public void move(float vx,float vy) {
		xvel += vx*0.02;
		yvel += vy*0.02;
		/*location.v1 += vx;
		location.v2 += vy;*/
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
				GL11.glVertex3f(location.v1 - 20, location.v2 - 20, location.v3);
				GL11.glVertex3f(location.v1 + 20, location.v2 - 20, location.v3);
				GL11.glVertex3f(location.v1 + 20, location.v2 + 20, location.v3);
				GL11.glVertex3f(location.v1 - 20, location.v2 + 20, location.v3);
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

	public void moveTo(float x, float y) {
		location.v1 = x;
		location.v2 = y;
		//location.v3 = 10 * World.things.size();
		yvel = 0;
		xvel = 0;
		
	}

	public void init(float x, float y) {
		location.v1 = x;
		location.v2 = y;
		
	}
}
