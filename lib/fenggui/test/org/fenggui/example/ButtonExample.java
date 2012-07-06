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
 * $Id: ButtonExample.java 611 2009-03-22 15:58:20Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.*;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.mouse.*;
import org.fenggui.layout.GridLayout;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.util.Spacing;

public class ButtonExample implements IExample
{

  Display display = null;

  @SuppressWarnings("deprecation")
  public void buildGUI(Display d)
  {
    display = d;

    Window w = FengGUI.createWindow(display, true, false, false, true);
    w.setTitle("Button Example");
    w.getContentContainer().setLayoutManager(new RowExLayout(false));

    Button b = FengGUI.createWidget(Button.class);
    b.setText("TestButton 1");
    b.getAppearance().setMargin(new Spacing(5, 15, 15, 5));
    b.setLayoutData(new RowExLayoutData(true, false));

    Button b1 = FengGUI.createWidget(Button.class);
    b1.setText("TestButton 2");
    b1.getAppearance().setMargin(new Spacing(5, 15, 15, 5));
    b1.setLayoutData(new RowExLayoutData(true, false));

    Button b2 = FengGUI.createWidget(Button.class);
    b2.setText("TestButton 3");
    b2.getAppearance().setMargin(new Spacing(5, 15, 15, 5));
    b2.setLayoutData(new RowExLayoutData(true, false));

    Container container = FengGUI.createWidget(Container.class);
    container.setLayoutData(new RowExLayoutData(true, false));
    container.setLayoutManager(new GridLayout(1, 2));
    FengGUI.createLabel(container, "Last Event:");
    final Label infoEvent = FengGUI.createWidget(Label.class);
    infoEvent.setText("Information");
    container.addWidget(infoEvent);
    
    final TextEditor text = FengGUI.createTextEditor();
    text.setMultiline(true);
    text.setWordWarping(false);
    text.setLayoutData(new RowExLayoutData(true, true));
    text.setText(b.toString());

    w.getContentContainer().addWidget(b, b1, b2, container, text);

    b.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        infoEvent.setText("Button Pressed");
      }
    });

    b.addMouseReleasedListener(new IMouseReleasedListener()
    {

      public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
      {
        infoEvent.setText("Mouse Released");
      }
    });

    b.addMousePressedListener(new IMousePressedListener()
    {

      public void mousePressed(MousePressedEvent mousePressedEvent)
      {
        infoEvent.setText("Mouse Pressed");
      }
    });

    b.addMouseEnteredListener(new IMouseEnteredListener()
    {

      public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
      {
        infoEvent.setText("Mouse Entered");
      }

    });

    b.addMouseExitedListener(new IMouseExitedListener()
    {

      public void mouseExited(MouseExitedEvent mouseExited)
      {
        infoEvent.setText("Mouse Exited");
      }

    });

    w.setSize(250, 300);
  }

  public String getExampleName()
  {
    return "Button Example";
  }

  public String getExampleDescription()
  {
    return "Shows some buttons and other stuff";
  }

}
