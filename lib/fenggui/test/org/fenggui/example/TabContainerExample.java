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
 */
package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.FengGUIOptional;
import org.fenggui.Label;
import org.fenggui.TabContainer;
import org.fenggui.TextEditor;
import org.fenggui.composite.Window;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.GridLayout;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Color;
import org.fenggui.util.Spacing;

public class TabContainerExample implements IExample
{

  public void buildGUI(Display display)
  {
    Window w = FengGUI.createWindow(true, false, false, true);
    w.setTitle(this.getExampleName());
    
    TabContainer tabContainer = FengGUIOptional.createTabContainer(w.getContentContainer());

    Label l2 = FengGUI.createLabel("Other tab (3)");
    l2.getAppearance().add(new PlainBackground(Color.GREEN));

    Label l3 = FengGUI.createLabel("Yen another tab (4)");
    l3.getAppearance().add(new PlainBackground(Color.CYAN));

    tabContainer.addTab("Login", null, buildLoginTab());
    tabContainer.addTab("Buttons", null, buildButtonTab());
    tabContainer.addTab("Tab 3", null, l2);
    tabContainer.addTab("Tab 4", null, l3);

    w.setSize(250, 250);
    display.addWidget(w);
    w.layout();
  }

  private Container buildButtonTab()
  {
    Container c = FengGUI.createWidget(Container.class);
    c.setLayoutManager(new GridLayout(2, 2));
    FengGUI.createButton(c, "B1");
    FengGUI.createButton(c, "B2");
    FengGUI.createButton(c, "B3");
    FengGUI.createButton(c, "B4");

    return c;
  }

  private Container buildLoginTab()
  {

    Container k = FengGUI.createWidget(Container.class);
    k.setLayoutManager(new RowLayout(false));

    Container c = FengGUI.createWidget(Container.class);
    c.setLayoutManager(new GridLayout(2, 2));
    FengGUI.createLabel(c, "Name:");
    c.addWidget(FengGUI.createWidget(TextEditor.class));
    FengGUI.createLabel(c, "Password:");
    c.addWidget(FengGUI.createWidget(TextEditor.class));
    c.setLayoutData(BorderLayoutData.CENTER);
    c.setExpandable(false);
    c.setShrinkable(false);
    c.setSize(150,100);
    k.addWidget(c);
    
    Button b = FengGUI.createButton(k, " Login ");
    b.getAppearance().setMargin(new Spacing(5, 5));
    b.setSizeToMinSize();
    b.setExpandable(false);
    b.setShrinkable(false);
    b.setLayoutData(BorderLayoutData.SOUTH);

    return k;
  }

  public String getExampleName()
  {
    return "TabContainer Example";
  }

  public String getExampleDescription()
  {
    return "Shows the TabContainer in action";
  }

}
