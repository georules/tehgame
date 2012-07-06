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
 * $Id: ComboBoxExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.ComboBox;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.composite.Window;
import org.fenggui.util.Spacing;

/**
 * Small example app to test a combo box.
 * 
 * 
 * 
 * @author Johannes Schaback 
 */
public class ComboBoxExample implements IExample
{

  /**
   * 
   */
  private static final long serialVersionUID = 4L;

  private Display           desk             = null;

  private void buildFrame()
  {
    Window frame = FengGUI.createWindow(desk, true, false, false, true);
    frame.setTitle("Pick a tea flavor:");

    Container spacer = FengGUI.createContainer(frame.getContentContainer());
    spacer.getAppearance().setMargin(new Spacing(10, 10, 10, 10));

    ComboBox list = FengGUI.createComboBox(spacer);
    list.addItem("Green Tea");
    list.addItem("Chinese Tea");
    list.addItem("English Tea");
    list.addItem("Milk Tea");
    list.addItem("Ginseng Tea");
    list.addItem("Herbal Tea");
    list.addItem("Ceylon Tea");
    list.addItem("Black Tea");
    frame.pack();
  }

  public void buildGUI(Display d)
  {
    this.desk = d;

    buildFrame();

  }

  public String getExampleName()
  {
    return "ComboBox Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates the how to use ComboBoxes";
  }

}
