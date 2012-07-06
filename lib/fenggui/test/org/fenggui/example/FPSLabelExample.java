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
 * $Id: FPSLabelExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.experimental.FPSLabel;
import org.fenggui.util.Alphabet;
import org.fenggui.util.fonttoolkit.FontFactory;

public class FPSLabelExample implements IExample
{

  public void buildGUI(Display display)
  {
    FPSLabel label = new FPSLabel();
    display.addWidget(label);
    label.setXY(display.getWidth() - 90, 10);

    ImageFont font = FontFactory.renderStandardFont(new java.awt.Font("Serif", java.awt.Font.PLAIN, 24), true,
      Alphabet.ENGLISH);
    ITextRenderer renderer = new DirectTextRenderer(font);
    label.getAppearance().addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);
  }

  public String getExampleDescription()
  {
    return "FPS Label";
  }

  public String getExampleName()
  {
    return "FPS Label";
  }

}
