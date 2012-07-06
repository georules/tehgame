/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2009 FengGUI Project
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 * 
 * Created on Jul 15, 2005
 * $Id: Everything.java 633 2009-04-25 09:54:13Z marcmenghin $
 */
package org.fenggui.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.FengGUIOptional;
import org.fenggui.IWidget;
import org.fenggui.Label;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.Widget;
import org.fenggui.actor.SimpleWindowPositioningActor;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.GUIInspector;
import org.fenggui.composite.MessageWindow;
import org.fenggui.composite.Window;
import org.fenggui.composite.menu.Menu;
import org.fenggui.composite.menu.MenuBar;
import org.fenggui.composite.menu.MenuItem;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.BevelBorder;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.DisplayResizedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.IDisplayResizedListener;
import org.fenggui.event.IMenuItemPressedListener;
import org.fenggui.event.MenuItemPressedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.RowLayout;
import org.fenggui.layout.StaticLayout;
import org.fenggui.theme.DefaultTheme;
import org.fenggui.theme.ITheme;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.GlobalContextHandler;
import org.fenggui.theme.xml.XMLInputStream;
import org.fenggui.theme.xml.XMLOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Point;
import org.fenggui.util.Spacing;
import org.fenggui.util.fonttoolkit.FontFactory;

/**
 * Builds a simple frame with an ugly layout. Is mainly used to testing
 * purposes. This class will either disappear in the near future or will serves
 * as a realy simple entry point for newbies.
 * 
 * @todo Comment this class... #
 * @todo "The plates don't show up, I don't know 
 * what they are but I don't see anything." #
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-04-25 11:54:13 +0200 (Sa, 25 Apr 2009) $
 * @version $Revision: 633 $
 */
public class Everything implements IExample
{
  private Display   display       = null;
  private boolean   runAsWebstart = false;
  private ImageFont teleType      = null;

  public Everything()
  {
    this(false);
  }

  public Everything(boolean runAsWebstart)
  {
    this.runAsWebstart = runAsWebstart;
  }

  public void registerExample(final IExample example, Menu parent, boolean activate)
  {
    MenuItem item = new MenuItem(example.getExampleName(), parent.getAppearance());
    parent.addItem(item);

    item.setEnabled(activate);

    item.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        buildExampleGUIinRenderThread(example);
      }

    });

  }

  private void buildTextMenu(MenuBar menuBar)
  {
    Menu textMenu = FengGUI.createMenu(menuBar, "Text", false);

    registerExample(new TextFieldExample(), textMenu, true);
    registerExample(new TextAreaExample(), textMenu, true);
    registerExample(new TextViewExample(), textMenu, true);
    registerExample(new MultiLineLabelExample(), textMenu, true);
    //registerExample(new LabelExample(), textMenu, true);
    registerExample(new FontExample(), textMenu, true);
  }

  private void buildBasicWidgetsMenu(MenuBar menuBar)
  {
    Menu widgetMenu = FengGUI.createMenu(menuBar, "Widgets", false);

    registerExample(new ListExample(), widgetMenu, true);
    registerExample(new ComboBoxExample(), widgetMenu, true);
    registerExample(new ButtonExample(), widgetMenu, true);
    registerExample(new CheckBoxExample(), widgetMenu, true);
    registerExample(new ProgressBarExample(), widgetMenu, true);
    registerExample(new SliderExample(), widgetMenu, true);
    registerExample(new RadioButtonExample(), widgetMenu, true);
    registerExample(new TableExample(), widgetMenu, true);
    registerExample(new TableExample2(), widgetMenu, true);
    registerExample(new ScrollBarExample(), widgetMenu, true);
    registerExample(new TreeExample(), widgetMenu, true);
  }

  private void buildContainersMenu(MenuBar menuBar)
  {
    Menu containerMenu = FengGUI.createMenu(menuBar, "Containers", false);

    registerExample(new SplitContainerExample(), containerMenu, true);
    registerExample(new ScrollContainerExample(), containerMenu, true);
    registerExample(new TabContainerExample(), containerMenu, true);
    registerExample(new TabExample(), containerMenu, true);
  }

  private void buildMiscMenu(MenuBar menuBar)
  {
    Menu miscMenu = FengGUI.createMenu(menuBar, "Misc", false);

    registerExample(new ClippingExample(), miscMenu, true);
    registerExample(new CursorExample(), miscMenu, true);
    // registerExample(new SnappingSliderExample(), miscMenu, true);

    Menu submen1Menu = FengGUI.createMenu(miscMenu, "Submenus", false);
    submen1Menu.addItem(new MenuItem("Submenu Item 1", false, menuBar.getAppearance()));
    submen1Menu.addItem(new MenuItem("Submenu Item 2", false, menuBar.getAppearance()));
    submen1Menu.addItem(new MenuItem("Submenu Item 3", false, menuBar.getAppearance()));

    Menu submen2Menu = FengGUI.createMenu(submen1Menu, "Another Submenu", false);
    submen2Menu.addItem(new MenuItem("Subsubmenu Item 1", false, menuBar.getAppearance()));
    submen2Menu.addItem(new MenuItem("Subsubmenu Item 2", false, menuBar.getAppearance()));
    submen2Menu.addItem(new MenuItem("Subsubmenu Item 3", false, menuBar.getAppearance()));
    submen2Menu.addItem(new MenuItem("Subsubmenu Item 4", false, menuBar.getAppearance()));
    submen1Menu.addItem(new MenuItem("Submenu Item 4", false, menuBar.getAppearance()));

    registerExample(new ConnectionWindowExample(), miscMenu, true);
    registerExample(new FPSLabelExample(), miscMenu, true);
    //registerExample(new TextRendererExample(), miscMenu, true);
    //registerExample(new ConsoleExample(), miscMenu, true);
    registerExample(new SVGExample(), miscMenu, true);
    registerExample(new PixmapDecoratorExample(), miscMenu, true);
    registerExample(new PopupMenuExample(), miscMenu, true);
    registerExample(new FileDialogExample(), miscMenu, true);

    Menu layoutMenu = FengGUI.createMenu(miscMenu, "Layouts", false);
    registerExample(new GridLayoutExample(), layoutMenu, true);
    registerExample(new LayoutExample(), layoutMenu, true);
    registerExample(new FlowLayoutExample(), layoutMenu, true);

    Menu decoratorMenu = FengGUI.createMenu(miscMenu, "Decorators", false);
    registerExample(new BorderTest(), decoratorMenu, true);
    registerExample(new PixmapBorderExample(), decoratorMenu, true);

    registerExample(new GameMenuExample(), miscMenu, true);
    //registerExample(new ChatExample(), miscMenu, true);

    MenuItem guiInspectorItem = new MenuItem("GUI Inspector", menuBar.getAppearance());
    //    miscMenu.addItem(guiInspectorItem);
    guiInspectorItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        GUIInspector inspector = new GUIInspector();
        inspector.layout();
        display.addWidget(inspector);
      }
    });

  }

  private void buildHelpMenu(MenuBar menuBar)
  {
    Menu helpMenu = FengGUI.createMenu(menuBar, "Help", false);
    MenuItem about = new MenuItem("About", menuBar.getAppearance());
    helpMenu.addItem(about);

    about.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        String t = "FengGUI version " + org.fenggui.FengGUI.VERSION + "\n" + org.fenggui.FengGUI.WEB;
        MessageWindow mw = new MessageWindow(t);
        mw.setTitle("About");
        mw.updateMinSize();
        mw.setSize(mw.getMinWidth() + 100, mw.getMinHeight());
        mw.layout();
        display.addWidget(mw);
        StaticLayout.center(mw, display);
      }

    });
  }

  private void buildThemeMenu(MenuBar menuBar)
  {
    final Menu themeMenu = FengGUI.createMenu(menuBar, "Theme", false);

    MenuItem noThemeItem = new MenuItem("No Theme", menuBar.getAppearance());
    themeMenu.addItem(noThemeItem);
    noThemeItem.setEnabled(!runAsWebstart);

    MenuItem standardThemeItem = new MenuItem("DefaultTheme", menuBar.getAppearance());
    themeMenu.addItem(standardThemeItem);
    standardThemeItem.setEnabled(!runAsWebstart);

    MenuItem qtCurveThemeItem = new MenuItem("QtCurve", menuBar.getAppearance());
    themeMenu.addItem(qtCurveThemeItem);
    qtCurveThemeItem.setEnabled(!runAsWebstart);

    noThemeItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        loadTheme(null, true);
      }

    });

    standardThemeItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        loadTheme(null, false);
      }

    });

    qtCurveThemeItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        loadTheme("data/themes/QtCurve/QtCurve.xml", false);
      }

    });
  }

  private void buildProgramMenu(final MenuBar menuBar)
  {
    Menu programMenu = FengGUI.createMenu(menuBar, "Program", false);
    MenuItem clearScreenItem = new MenuItem("Clear Display", menuBar.getAppearance());
    programMenu.addItem(clearScreenItem);

    if (!runAsWebstart)
    {
      programMenu.addItem(buildXMLDumper(menuBar));
    }

    MenuItem exitItem = new MenuItem("Exit", menuBar.getAppearance());
    programMenu.addItem(exitItem);

    // register "Exit" item to quit the app
    exitItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        quit();
      }

    });

    // remove everything except the MenuBar from the display
    clearScreenItem.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {
        ArrayList<IWidget> toBeRemoved = new ArrayList<IWidget>();
        for (IWidget w : display.getWidgets())
        {
          if (!w.equals(menuBar))
            toBeRemoved.add(w);
        }
        display.removeWidget(toBeRemoved.toArray(new IWidget[toBeRemoved.size()]));
      }

    });
  }

  private void buildMenuBar()
  {

    final MenuBar menuBar = FengGUI.createMenuBar(display);

    buildProgramMenu(menuBar);
    buildBasicWidgetsMenu(menuBar);
    buildTextMenu(menuBar);
    buildContainersMenu(menuBar);
    buildMiscMenu(menuBar);
    buildThemeMenu(menuBar);
    buildHelpMenu(menuBar);

    // position MenuBar in Display
    menuBar.updateMinSize(); // we have not layouted anything yet...
    menuBar.setX(0);
    menuBar.setY(display.getHeight() - menuBar.getMinHeight());
    menuBar.setSize(display.getWidth(), menuBar.getMinHeight());
    menuBar.setShrinkable(false);

    // if the OpenGL screen is resized, put the MenuBar back in place
    Binding.getInstance().addDisplayResizedListener(new IDisplayResizedListener()
    {

      public void displayResized(DisplayResizedEvent displayResizedEvent)
      {
        menuBar.setX(0);
        menuBar.setY(displayResizedEvent.getHeight() - menuBar.getMinHeight());
        menuBar.setSize(displayResizedEvent.getWidth(), menuBar.getMinHeight());
      }

    });
  }

  //	private ImageFont teletype = null;

  private MenuItem buildXMLDumper(MenuBar menu)
  {
    MenuItem xmlDump = new MenuItem("XML Dump", menu.getAppearance());

    xmlDump.addMenuItemPressedListener(new IMenuItemPressedListener()
    {

      public void menuItemPressed(MenuItemPressedEvent menuItemPressedEvent)
      {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final XMLOutputStream os = new XMLOutputStream(bos, "Display", new GlobalContextHandler());
        try
        {
          display.process(os);
          os.close();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

        Window w = FengGUI.createWindow(display, true, false, false, true);
        w.getContentContainer().setLayoutManager(new BorderLayout());
        ScrollContainer sc = FengGUI.createWidget(ScrollContainer.class);
        w.getContentContainer().addWidget(sc);
        sc.setLayoutData(BorderLayoutData.CENTER);
        sc.setShowScrollbars(true);
        final TextEditor te = FengGUI.createWidget(TextEditor.class);
        sc.setInnerWidget(te);
        te.setMultiline(true);
        te.setWordWarping(false);
        te.setText(os.getDocument().toXML());

        Container btnContainer = FengGUI.createWidget(Container.class);
        w.getContentContainer().addWidget(btnContainer);
        btnContainer.setLayoutManager(new RowLayout(true));
        Button dumpToConsole = FengGUI.createButton("Print on System.out");
        Button clear = FengGUI.createButton("Clear");
        Button apply = FengGUI.createButton("Apply on Display");
        btnContainer.addWidget(dumpToConsole, clear, apply);
        w.getContentContainer().addWidget(btnContainer);
        btnContainer.setLayoutData(BorderLayoutData.SOUTH);
        dumpToConsole.addButtonPressedListener(new IButtonPressedListener()
        {
          public void buttonPressed(ButtonPressedEvent e)
          {
            System.out.println(te.getText());
          }
        });
        clear.addButtonPressedListener(new IButtonPressedListener()
        {
          public void buttonPressed(ButtonPressedEvent e)
          {
            te.setText("");
          }
        });
        apply.addButtonPressedListener(new IButtonPressedListener()
        {
          public void buttonPressed(ButtonPressedEvent e)
          {
            try
            {
              XMLInputStream xis = new XMLInputStream(new ByteArrayInputStream(te.getText().getBytes()));
              display.process(xis);
              xis.close();
              display.layout();
            }
            catch (Exception e1)
            {
              e1.printStackTrace();
            }
            //System.out.println(os.getDocument().toXML());
          }
        });

        w.setSize(500, 500);
        w.setTitle("XML Dump of Current Widget Tree");
        w.layout();
        display.addWidget(w);
        StaticLayout.center(w, display);
        sc.scrollVertical(1.0d);
        //System.out.println(os.getDocument().toXML());

        //add better font
        if (teleType == null)
        {
          Widget wid = new Widget()
          {

            public void paint(Graphics g)
            {
              display.removeWidget(this);

              if (teleType == null)
                teleType = FontFactory.renderStandardFont(new java.awt.Font("Monospace", java.awt.Font.PLAIN, 11));

              te.getAppearance().addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, new DirectTextRenderer(teleType));
              te.updateMinSize();
            }
          };
          display.addWidget(wid);
        }
        else
        {
          te.getAppearance().addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, new DirectTextRenderer(teleType));
          te.updateMinSize();
        }

      }
    });

    return xmlDump;
  }

  public void quit()
  {
    System.exit(0);
  }

  private void buildExampleGUIinRenderThread(final IExample example)
  {
    Widget w = new Widget()
    {

      public void paint(Graphics g)
      {
        display.removeWidget(this);

        example.buildGUI(display);

      }
    };

    display.addWidget(w);
  }

  private void loadThemeDirect(final String filename, final boolean noTheme)
  {
    long time = System.currentTimeMillis();
    ITheme theme = null;

    if (!noTheme)
    {
      if (filename == null)
        theme = new DefaultTheme();
      else
      {
        try
        {
          theme = new XMLTheme(filename);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }

    FengGUI.setTheme(theme);
    FengGUI.initPrototypes();
    FengGUIOptional.initOptional();

    long tookTime = System.currentTimeMillis() - time;
    System.out.println("FengGUI Theme Loading: " + tookTime);
    /*
    FengGUI.setUpAppearance(helpMenu);
    FengGUI.setUpAppearance(miscMenu);
    FengGUI.setUpAppearance(themeMenu);
    FengGUI.setUpAppearance(widgetMenu);
    FengGUI.setUpAppearance(programMenu);
    FengGUI.setUpAppearance(nonsenseMenu);
    FengGUI.setUpAppearance(compositesMenu);
    FengGUI.setUpAppearance(containersMenu); */
  }

  /**
   * Workaround to load the theme within the opengl render thread. (texture uploads)
   * 
   * @param filename
   */
  private void loadTheme(final String filename, final boolean noTheme)
  {
    display.removeAllWidgets();
    Widget w = new Widget()
    {

      @Override
      public void paint(Graphics g)
      {
        display.removeWidget(this);
        loadThemeDirect(filename, noTheme);
        buildMenuBar();
        display.layout();

        if (FengGUI.VERSION.indexOf("SVN") > 0)
        {
          showSVNInfoWindow();
        }
      }
    };

    display.addWidget(w);
  }

  public void buildGUI(Display g)
  {
    display = g;

    loadThemeDirect("data/themes/QtCurve/QtCurve.xml", false);

    buildMenuBar();

    display.layout();

    SimpleWindowPositioningActor actor = new SimpleWindowPositioningActor(new Point(10, -25), new Spacing(20, 5, 5, 5));
    actor.hook(display);

    //		for (int i = 0; i < 50; i++)
    //		{
    //			buildExampleGUIinRenderThread(new ButtonExample());
    //		}
    //		buildExampleGUIinRenderThread(new FPSLabelExample());

    //		buildExampleGUIinRenderThread(new PixmapBorderExample());

    //		if from SVN show information
    if (FengGUI.VERSION.indexOf("SVN") > 0)
    {
      showSVNInfoWindow();
    }
  }

  private void showSVNInfoWindow()
  {
    final Label label = FengGUI
        .createLabel(
          display,
          "This is a version from SVN. Some things maybe don't work as they used to in the last release. Don't expect every example to work, our time is limited.");
    label.setWordWarping(true);
    label.setMultiline(true);
    label.getAppearance().removeAll();
    label.getAppearance().setPadding(new Spacing(5, 5, 5, 5));
    label.getAppearance().setMargin(new Spacing(10, 10, 10, 10));
    label.getAppearance().add(new PlainBackground(new Color(255, 0, 0, 30)));
    label.getAppearance().add(new BevelBorder());

    display.getBinding().addDisplayResizedListener(new IDisplayResizedListener()
    {

      public void displayResized(DisplayResizedEvent displayResizedEvent)
      {
        label.setX(Alignment.BOTTOM_RIGHT.alignX(display.getWidth(), label.getWidth()));
        label.setY(Alignment.BOTTOM_RIGHT.alignY(display.getHeight(), label.getHeight()));
      }

    });

    label.setSize(340, 95);
    label.setX(Alignment.BOTTOM_RIGHT.alignX(display.getWidth(), label.getWidth()));
    label.setY(Alignment.BOTTOM_RIGHT.alignY(display.getHeight(), label.getHeight()));
    label.updateMinSize();
    label.layout();
  }

  public String getExampleName()
  {
    return "Test Almost Everything";
  }

  public String getExampleDescription()
  {
    return "Shows almost every Widget in FengGUI";
  }

}
