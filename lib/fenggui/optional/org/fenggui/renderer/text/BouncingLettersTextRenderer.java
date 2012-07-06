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
 * Created on Feb 26, 2007
 * $Id: BouncingLettersTextRenderer.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.renderer.text;

import java.io.IOException;

import org.fenggui.binding.render.*;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.CharacterPixmap;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * FIXME: Does not work anymore!!
 *  
 * Renders a single line of text such that the characerts jump up and down.
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class BouncingLettersTextRenderer implements ITextRenderer
{
  private ImageFont font   = ImageFont.getDefaultFont();
  private double[]  values = null;
  private double    t      = 0;

  public ImageFont getFont()
  {
    return font;
  }

  public int getHeight()
  {
    return font.getHeight() * 2;
  }

  public void setFont(ImageFont font)
  {
    this.font = font;
  }

  public BouncingLettersTextRenderer copy()
  {
    BouncingLettersTextRenderer result = new BouncingLettersTextRenderer();
    result.font = this.font;
    return result;
  }

  public Dimension calculateSize(String[] text)
  {
    int height = 0;
    int width = 0;

    for (String line : text)
    {
      Dimension lineSize = font.calculateSize(line);
      width = Math.max(width, lineSize.getWidth());
      height += lineSize.getHeight();
    }
    return new Dimension(width, height);
  }

  public int getLineHeight()
  {
    return font.getHeight();
  }

  public int getWidth(String text)
  {
    return font.getWidth(text);
  }

  public boolean isValidChar(char c)
  {
    return font.isCharacterMapped(c);
  }

  public void render(int x, int y, String[] texts, Color color, Graphics g, IOpenGL gl)
  {
    if (texts == null || texts.length == 0)
      return;

    if (values == null)
    {
      values = new double[texts.length];

      for (int i = 0; i < values.length; i++)
      {
        //values[i] = Math.PI - Math.random()*2*Math.PI;
        values[i] = (double) i * 2 * Math.PI / (double) texts.length;
      }
    }

    int localX = x + g.getTranslation().getX();
    int localY = y + g.getTranslation().getY();

    gl.enableTexture2D(true);

    CharacterPixmap pixmap = null;

    boolean init = true;

    for (String text : texts)
    {
      for (int i = 0; i < text.length(); i++)
      {
        localY = y + g.getTranslation().getY() + font.getHeight() / 2
            + (int) (Math.sin(t + values[i]) * font.getHeight() / 5 + 0.5);

        final char c = text.charAt(i);
        if (c == '\r' || c == '\f' || c == '\t')
          continue;
        pixmap = getFont().getCharPixMap(c);

        if (init)
        {
          ITexture tex = pixmap.getTexture();

          if (tex.hasAlpha())
          {
            gl.setTexEnvModeModulate();
          }

          tex.bind();
          gl.startQuads();
          init = false;
        }

        final int imgWidth = pixmap.getWidth();
        final int imgHeight = pixmap.getHeight();

        final float endY = pixmap.getEndY();
        final float endX = pixmap.getEndX();

        final float startX = pixmap.getStartX();
        final float startY = pixmap.getStartY();

        localY += values[i];

        gl.texCoord(startX, endY);
        gl.vertex(localX, localY);

        gl.texCoord(startX, startY);
        gl.vertex(localX, imgHeight + localY);

        gl.texCoord(endX, startY);
        gl.vertex(imgWidth + localX, imgHeight + localY);

        gl.texCoord(endX, endY);
        gl.vertex(imgWidth + localX, localY);

        localX += pixmap.getCharWidth();
      }
      //move to new line
      localY -= font.getHeight();
      localX = x + g.getTranslation().getX();
    }
    gl.end();
    gl.enableTexture2D(false);

    t = (t + 0.02) % (2 * Math.PI);
  }

  public String getUniqueName()
  {
    return GENERATE_NAME;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {

  }

  public int getWidth(char text)
  {
    return font.getWidth(text);
  }

  public void setFont(IFont font)
  {
    if (font instanceof ImageFont)
      this.font = (ImageFont) font;
    else
      throw new IllegalArgumentException("Only an ImageFont can be used with a BouncingLettersTextRenderer.");
  }

  public void render(int x, int y, String text, Color color, Graphics g, IOpenGL gl)
  {
  }

  public int getLineSpacing()
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getName()
   */
  public String getName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#render(int, int, java.lang.String[], org.fenggui.util.Color, org.fenggui.binding.render.Graphics)
   */
  public void render(int x, int y, String[] text, Color color, Graphics g)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#render(int, int, java.lang.String, org.fenggui.util.Color, org.fenggui.binding.render.Graphics)
   */
  public void render(int x, int y, String text, Color color, Graphics g)
  {
    // TODO Auto-generated method stub

  }

}
