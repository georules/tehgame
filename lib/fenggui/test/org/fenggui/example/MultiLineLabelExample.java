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

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.composite.Window;

/**
 * @author charlie
 *
 */
public class MultiLineLabelExample implements IExample
{

  public void buildGUI(Display display)
  {
    String text = "This is an example showing the behavior of the multiline label. This label has even more test text. Btw. this can be done everywhere text is displayed.\n\nmore text\n\neot";

    Window w = FengGUI.createWindow(true, false, false, true);
    w.setTitle(this.getExampleName());
    Label l = FengGUI.createLabel(w.getContentContainer());
    l.setMultiline(true);
    l.setWordWarping(true);
    l.setText(text);

    l.updateMinSize();
    w.setSize(200,250);
    w.layout();
    display.addWidget(w);
  }

  public String getExampleDescription()
  {
    return "Shows a multiline label";
  }

  public String getExampleName()
  {
    return "Multiline Example";
  }

}
