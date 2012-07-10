package com.geo.mfrts;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.tests.TestUtils;

import com.geo.mfrts.objects.Thing;
import com.geo.mfrts.player.HUD;
import com.geo.mfrts.util.Timer;

import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.matthiasmann.twl.theme.ThemeManager;


public class Game {
	
	public static World state;
	public static Game game;
	public static Physics physics;

	public static HUD hud; // I want to change this to a player later maybe

	public static void main(String[] argv) {
		game = new Game();
		state = new World();
		physics = new Physics();
		hud = new HUD();

		game.init();	// Get things going
		hud.init();
		game.run();		// Run things
		game.end();		// When we're done running
	}

	public void init() {
		// Init OpenGL
		try {
			Display.setDisplayMode(new DisplayMode(1200,800));
			Display.setResizable(false);
			Display.setVSyncEnabled(true);
			Display.setTitle("MF GAME");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GL11.glViewport(0,0,1200,800);	
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glClearColor(0.3f,0.7f,0.9f,0.0f);
		GL11.glOrtho(0, 1200, 0, 800, -1000, 500);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	
	public void end() {
		
		World.stop();
	
		try {
			World.exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Display.destroy();
	}


	
	public void renderGL()	{
		if (Display.wasResized()) {
			hud.resize();
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glClearColor(0.3f,0.7f,0.9f,0.0f);
			GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -500, 500);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	 
		for (Thing t : World.things) {
			t.render();
		}
		
	}

	public void run() {
	
		for (Thread t : World.threads) {
			World.exe.execute(t);
		}
		while(!World.go) {
			Timer.update();
			World.update();
			renderGL();
			Display.update();
		}
		
		while (!Display.isCloseRequested()) {
			Timer.update();
			World.update();
			physics.update();
			renderGL();
			hud.update();
			Display.update();
		}
		
	}

}
