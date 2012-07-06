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
 * Created on Feb 25, 2007
 * $Id: FPSLabel.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.experimental;

import org.fenggui.Label;
import org.fenggui.binding.render.Graphics;

public class FPSLabel extends Label
{
  private long end          = System.currentTimeMillis() + 1000;
  private int  frameCounter = 0;

  @Override
  public void paint(Graphics g)
  {
    frameCounter++;

    long the_mooooooooment = System.currentTimeMillis();

    if (end <= the_mooooooooment)
    {
      setText(frameCounter + " fps");
      setSizeToMinSize();
      end = the_mooooooooment + 1000;
      frameCounter = 0;
    }

    super.paint(g);
  }
}
