/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author mam
 */
public class ModeSelectTest implements CallbackWithReason<VideoSettings.CallbackReason> {

    public static void main(String ... args) {
        ModeSelectTest modeSel = new ModeSelectTest();
        VideoMode mode = modeSel.selectMode();
        if(mode != null) {
            SimpleTest test = new SimpleTest();
            test.run(mode);
        }
    }

    protected DisplayMode desktopMode;
    protected VideoSettings.CallbackReason reason;

    public  ModeSelectTest() {
        System.out.println("LWJGL Version: " + Sys.getVersion());
        desktopMode = Display.getDisplayMode();
        System.out.println("Desktop mode: " + desktopMode);

        try {
            for(DisplayMode mode : Display.getAvailableDisplayModes()) {
                System.out.println("Available mode: " + mode);
            }
        } catch(LWJGLException ex) {
            TestUtils.showErrMsg(ex);
        }
    }

    public VideoMode selectMode() {
        try {
            Display.setDisplayMode(new DisplayMode(400, 300));
            Display.create();
            Display.setVSyncEnabled(true);

            LWJGLRenderer renderer = new LWJGLRenderer();
            renderer.setUseSWMouseCursors(true);
            GUI gui = new GUI(renderer);

            ThemeManager theme = ThemeManager.createThemeManager(
                    SimpleTest.class.getResource("guiTheme.xml"), renderer);
            gui.applyTheme(theme);

            final VideoSettings settings = new VideoSettings(
                    AppletPreferences.userNodeForPackage(VideoSettings.class),
                    desktopMode);
            settings.setTheme("settings");
            settings.addCallback(this);
            settings.readSettings();

            Widget frame = new SimpleFrame(settings);
            frame.setTheme("settingdialog");

            gui.getRootPane().add(frame);
            frame.adjustSize();
            frame.setPosition(
                    (gui.getWidth()-frame.getWidth())/2,
                    (gui.getHeight()-frame.getHeight())/2);

            while(!Display.isCloseRequested() && reason == null) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                gui.update();
                Display.update();
                TestUtils.reduceInputLag();
            }

            gui.destroy();
            theme.destroy();
            Display.destroy();
            
            if(reason == VideoSettings.CallbackReason.ACCEPT) {
                settings.storeSettings();
                return settings.getSelectedVideoMode();
            }
        } catch (Exception ex) {
            TestUtils.showErrMsg(ex);
        }

        Display.destroy();
        try {
            Display.setDisplayMode(desktopMode);
        } catch(LWJGLException ex) {
            TestUtils.showErrMsg(ex);
        }

        return null;
    }

    public void callback(VideoSettings.CallbackReason reason) {
        this.reason = reason;
    }

    static class SimpleFrame extends Widget {
        private final Widget child;

        public SimpleFrame(Widget child) {
            this.child = child;
            add(child);
        }

        @Override
        public int getPreferredInnerWidth() {
            return child.getPreferredWidth();
        }

        @Override
        public int getPreferredInnerHeight() {
            return child.getPreferredHeight();
        }

        @Override
        protected void layout() {
            child.setPosition(getInnerX(), getInnerY());
            child.setSize(getInnerWidth(), getInnerHeight());
        }
    }
}
