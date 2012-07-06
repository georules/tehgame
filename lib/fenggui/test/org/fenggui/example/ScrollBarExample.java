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
 * $Id: ScrollBarExample.java 613 2009-03-25 22:02:20Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.*;
import org.fenggui.composite.Window;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

/**
 * 
 * Little demo app for scroll bars.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class ScrollBarExample implements IExample
{

  private Display desk;

  private void buildHorizontalScrollBarFrame()
  {
    Window f = FengGUI.createWindow(desk, true, false, false, true);
    f.setTitle("Horizontal Scroll Bar Test");

    ScrollBar sc = FengGUI.createScrollBar(true);
    f.getContentContainer().addWidget(sc);
    sc.getAppearance().setMargin(new Spacing(5, 5));
    sc.setLayoutData(BorderLayoutData.SOUTH);

    Label l = FengGUI.createWidget(Label.class);
    f.getContentContainer().addWidget(l);
    l.setText("Check out the horizontal slider!");
    l.getAppearance().setAlignment(Alignment.MIDDLE);
    l.setLayoutData(BorderLayoutData.CENTER);

    f.getContentContainer().setLayoutManager(new BorderLayout());
    f.pack();
  }

  private void buildVerticalScrollBarFrame()
  {
    Window f = FengGUI.createWindow(desk, true, false, false, true);
    f.setTitle("Vertical Scroll Bar Test");

    ScrollBar sc = FengGUI.createScrollBar(false);
    f.getContentContainer().addWidget(sc);
    sc.getAppearance().setMargin(new Spacing(5, 5));
    sc.setLayoutData(BorderLayoutData.WEST);

    Label l = FengGUI.createWidget(Label.class);
    f.getContentContainer().addWidget(l);
    l.setText("Check out the vertical slider");
    l.setLayoutData(BorderLayoutData.CENTER);
    l.getAppearance().setAlignment(Alignment.MIDDLE);

    f.getContentContainer().setLayoutManager(new BorderLayout());
    f.pack();
  }

  public void buildGUI(Display d)
  {
    desk = d;

    buildHorizontalScrollBarFrame();
    buildVerticalScrollBarFrame();

    desk.layout();
  }

  public String getExampleName()
  {
    return "ScrollBar Example";
  }

  public String getExampleDescription()
  {
    return "Shows two frames with scroll bars";
  }

}
