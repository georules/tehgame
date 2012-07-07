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


public class Game extends DesktopArea {
	
	private GUI gui;
	private LWJGLRenderer renderer;
	private final FPSCounter fpsCounter;
	private final ChatFrame chat;
	
	public static World state;
	public static Game game;


	public static void main(String[] argv) {
		state = new World();
		game = new Game();
		
		game.init();	// Get things going
		game.run();		// Run things
		game.end();		// When we're done running
	}
	
	Game()	{

		fpsCounter = new FPSCounter();
		add(fpsCounter);
		chat = new ChatFrame();
		add(chat);
		chat.setSize(500,200);
		chat.setPosition(20,50);
	}
	 @Override
	    protected boolean handleEvent(Event evt) {
	        if(super.handleEvent(evt)) {
	        //    return true;
	        }
	        
	        return false;
	    }


	public void init() {
		// Init OpenGL
		try {
			Display.setDisplayMode(new DisplayMode(1200,800));
			Display.setResizable(true);
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
		GL11.glOrtho(0, 1200, 0, 800, -500, 500);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		// Init GUI
		try {
			renderer = new LWJGLRenderer();
			gui = new GUI(game, renderer);
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
	
	public void end() {
		gui.destroy();
		
		World.controls.stop();
		World.physics.stop();
	
		World.exe.shutdown();
		try {
			World.exe.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Display.destroy();
	}


	
	public void renderGL()	{
		if (Display.wasResized()) {
			renderer.setViewport(0, 0, Display.getWidth(), Display.getHeight());
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
		
		while (!Display.isCloseRequested()) {
			Timer.update();
			World.controls.update();
			World.physics.update();
			renderGL();
			gui.update();
			Display.update();
		}
		
	}
	
	static class ChatFrame extends ResizableFrame {
        private final StringBuilder sb;
        private final HTMLTextAreaModel textAreaModel;
        private final TextArea textArea;
        private final EditField editField;
        private final ScrollPane scrollPane;
        private int curColor;

        public ChatFrame() {
            setTitle("Code");

            this.sb = new StringBuilder();
            this.textAreaModel = new HTMLTextAreaModel();
            this.textArea = new TextArea(textAreaModel);
            this.editField = new EditField();
            this.editField.setMultiLine(true);
            this.editField.setText("sdkfhsjdkf\nsdjfhsdjfds");

      

            scrollPane = new ScrollPane(editField);
            scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
          

            DialogLayout l = new DialogLayout();
            l.setTheme("content");
            
            l.setHorizontalGroup(l.createParallelGroup(scrollPane));
            l.setVerticalGroup(l.createSequentialGroup(scrollPane));

            add(l);
        }
 
    }

}
