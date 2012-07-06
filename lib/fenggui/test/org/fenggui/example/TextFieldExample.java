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
 * $Id: TextFieldExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.TextEditor;
import org.fenggui.composite.Window;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;

/**
 * Demonstrates a <code>TextField</code> Widget.
 * 
 * 
 * @author Johannes Schaback
 * 
 */
public class TextFieldExample implements IExample
{

  public void buildGUI(Display g)
  {
    Window w = FengGUI.createWindow(g, true, false, false, true);
    w.setTitle("TextField Example");
    w.getContentContainer().setLayoutManager(new RowExLayout(false));

    TextEditor tf = FengGUI.createTextEditor(w.getContentContainer());
    tf.setEmptyText("Enter Some Text Here...");
    tf.setLayoutData(new RowExLayoutData(true, false));
    tf.setSize(200, 26);
    tf.setMultiline(false);
    tf.setWordWarping(false);

    TextEditor tf2 = FengGUI.createTextEditor(w.getContentContainer());
    tf2.setLayoutData(new RowExLayoutData(true, false));
    tf2.setEmptyText("Password Here ..");
    tf2.setSize(200, 26);
    tf2.setMultiline(false);
    tf2.setWordWarping(false);
    tf2.setPasswordField(true);
    w.pack();
  }

  public String getExampleName()
  {
    return "Text Field Example";
  }

  public String getExampleDescription()
  {
    return "Text Field Example";
  }

}
