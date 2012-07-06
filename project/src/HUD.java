import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Rect;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Timer;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.matthiasmann.twl.model.SimpleIntegerModel;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.textarea.StyleSheet;
import de.matthiasmann.twl.textarea.TextAreaModel;
import de.matthiasmann.twl.textarea.Value;
import de.matthiasmann.twl.theme.ThemeManager;
import de.matthiasmann.twl.utils.TextUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Matthias Mann
 */
public class HUD extends DesktopArea {


    private final FPSCounter fpsCounter;
    private final TextFrame textFrame;

    public boolean quit;

    public HUD() {
        fpsCounter = new FPSCounter();
        add(fpsCounter);

        textFrame = new TextFrame();
        add(textFrame);

        textFrame.setSize(200, 200);
        textFrame.setPosition(20, 20);
    }

    @Override
    protected void layout() {
        super.layout();
        //textFrame.setPosition(20,50);

        // fpsCounter is bottom right
        fpsCounter.adjustSize();
        fpsCounter.setPosition(
                getInnerWidth() - fpsCounter.getWidth(),
                getInnerHeight() - fpsCounter.getHeight());
    }

    @Override
    protected boolean handleEvent(Event evt) {
        if(super.handleEvent(evt)) {
            return true;
        }
        switch (evt.getType()) {
            case KEY_PRESSED:
                switch (evt.getKeyCode()) {
                    case Event.KEY_ESCAPE:
                        quit = true;
                        return true;
                }
        }
        return false;
    }

    static class TextFrame extends ResizableFrame {
        private final EditField edit;
       // private final ScrollPane scrollPane;

        public TextFrame() {
            setTitle("Wooo");
            edit = new EditField();
            edit.setMultiLine(true);
            edit.setText("hi");
            
            edit.addCallback(new EditField.Callback() {
                public void callback(int key) {
                    if(key == Event.KEY_RETURN) {
                        edit.setText("");
                    }
                }
            });

            
            
            add(edit);
        }




   
    }
}