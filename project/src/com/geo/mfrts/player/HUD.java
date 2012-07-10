package com.geo.mfrts.player;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.tests.TestUtils;

import com.geo.mfrts.Game;
import com.geo.mfrts.World;
import com.geo.mfrts.objects.Thing;
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


public class HUD extends DesktopArea {
	private GUI gui;
	private LWJGLRenderer renderer;
	private final FPSCounter fpsCounter;
	private final InputFrame chat;

	public HUD()	{
		fpsCounter = new FPSCounter();
		add(fpsCounter);
		chat = new InputFrame();
		add(chat);
		chat.setSize(500,200);
		chat.setPosition(20,50);
	}
	public String getText() {
		return chat.getText();
	}
	public void update() {
		gui.update();
		this.keyboardFocusLost();
	}
	 @Override
	protected boolean handleEvent(Event evt) {	
	    if(super.handleEvent(evt)) {
	       return true;
	    }
	    
	    return false;
	}

	public void init() {
		// Init GUI
		try {
			renderer = new LWJGLRenderer();
			gui = new GUI(this, renderer);
			URL url = Game.class.getResource("/chat/chat.xml");
			ThemeManager theme = ThemeManager.createThemeManager(url, renderer);
			gui.applyTheme(theme);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
    @Override
    protected void layout() {
        super.layout();

        // fpsCounter is bottom right
        fpsCounter.adjustSize();
        fpsCounter.setPosition(
                getInnerWidth() - fpsCounter.getWidth(),
                getInnerHeight() - fpsCounter.getHeight());
    }
	public void resize() {
		renderer.setViewport(0, 0, Display.getWidth(), Display.getHeight());
		
	}
}
