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
 * Created on 2005-4-10
 * $Id: MouseDraggedEvent.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.event.mouse;

import java.util.Set;

import org.fenggui.IWidget;
import org.fenggui.event.key.Key;

/**
 * 
 * Event type used when the user dragged the mouse of
 * a Widget.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class MouseDraggedEvent extends MouseEvent
{

  private int         displayX, displayY;
  private MouseButton mouseButton = MouseButton.LEFT;

  public MouseDraggedEvent(IWidget source, int x, int y, MouseButton mouseButton, Set<Key> modifiers)
  {
    super(source, modifiers);
    this.displayX = x;
    this.displayY = y;
    this.mouseButton = mouseButton;
  }

  public MouseButton getButton()
  {
    return mouseButton;
  }

  public int getDisplayX()
  {
    return displayX;
  }

  public int getDisplayY()
  {
    return displayY;
  }

}
