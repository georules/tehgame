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
package org.fenggui.composite.text;

import java.io.IOException;

import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;

public class TextViewAppearance extends DecoratorAppearance
{
  private Color     textColor = Color.BLACK;
  private ImageFont font      = ImageFont.getDefaultFont();

  public TextViewAppearance(TextView w)
  {
    super(w);
  }

  public ImageFont getFont()
  {
    return font;
  }

  public void setFont(ImageFont font)
  {
    this.font = font;
  }

  public Color getTextColor()
  {
    return textColor;
  }

  public void setTextColor(Color textColor)
  {
    this.textColor = textColor;
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    if (stream.isInputStream()) // XXX: only support read-in at the moment :(
      setFont(stream.processChild("Font", getFont(), ImageFont.getDefaultFont(), ImageFont.class));

    textColor = stream.processChild("Color", textColor, Color.BLACK, Color.class);
  }
}