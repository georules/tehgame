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
 * Created on Mar 9, 2007
 * $Id: ConsoleAppearance.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite.console;

import java.io.IOException;

import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Timer;

public class ConsoleAppearance extends DecoratorAppearance
{
  private ITextRenderer   textRenderer   = new DirectTextRenderer();
  private ITextRenderer   promtRenderer  = new DirectTextRenderer();
  private ICarretRenderer carretRenderer = null;
  private Console         widget         = null;
  private Timer           carretTimer    = new Timer(2, 400);
  private Color           textColor      = Color.BLACK;

  public ConsoleAppearance(Console w)
  {
    super(w);
    this.widget = w;

    //		textRenderer.setFont(Font.getDefaultFont());
    //		promtRenderer.setFont(Font.getDefaultFont());
    carretRenderer = new LineCarretRenderer(ImageFont.getDefaultFont().getHeight());
  }

  public ITextRenderer getTextRenderer()
  {
    return textRenderer;
  }

  public ITextRenderer getPromtRenderer()
  {
    return promtRenderer;
  }

  public void setCarretRenderer(ICarretRenderer carretRenderer)
  {
    this.carretRenderer = carretRenderer;
  }

  public Console getWidget()
  {
    return widget;
  }

  public Timer getCarretTimer()
  {
    return carretTimer;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    textColor = stream.processChild("Color", textColor, Color.BLACK, Color.class);

    if (stream.isInputStream())
      setFont(stream.processChild("Font", null, ImageFont.getDefaultFont(), ImageFont.class));
  }

  public void setFont(ImageFont f)
  {
    //		textRenderer.setFont(f);
    //		promtRenderer.setFont(f);
  }

  public ImageFont getFont()
  {
    //		return textRenderer.getFont();
    return null;
  }
}
