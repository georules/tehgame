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
 * $Id: MessageWindow.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite;

import org.fenggui.*;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

public class MessageWindow extends Window
{

  private Label  label = null;
  private Button OK    = null;

  public MessageWindow()
  {
    this(true, false, false);
  }

  public MessageWindow(String message)
  {
    this();
    label.setText(message);
  }

  public MessageWindow(boolean closeBtn, boolean maximizeBtn, boolean minimizeBtn)
  {
    super(closeBtn, maximizeBtn, minimizeBtn, true);

    //TODO: fix this stuff here
    this.setupTheme(MessageWindow.class);

    ((Container) getContentContainer()).setLayoutManager(new RowLayout(false));

    label = FengGUI.createLabel(getContentContainer());
    label.getAppearance().setAlignment(Alignment.MIDDLE);
    label.getAppearance().setMargin(new Spacing(10, 10));

    OK = FengGUI.createButton(getContentContainer(), "OK");

    OK.getAppearance().setPadding(new Spacing(3, 10));
    OK.getAppearance().setMargin(new Spacing(5, 0, 0, 5));
    OK.setExpandable(false);
    OK.setSizeToMinSize();
    setSize(300, 250);

    final Window thizz = this;
    OK.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        ((Container) getParent()).removeWidget(thizz);
      }

    });
  }

  /**
   * @return <code>Label</code> containing message
  **/
  public Label getLabel()
  {
    return label;
  }

  /**
   * @return OK <code>Button</code> for this window
  **/
  public Button getOKButton()
  {
    return OK;
  }

}
