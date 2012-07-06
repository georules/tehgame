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
package org.fenggui.unit;

import java.io.IOException;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * @author ava
 *
 */
public class MockRenderer implements ITextRenderer
{

  private int lineHeight = 10;

  /**
   * 
   */
  public MockRenderer()
  {
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#calculateSize(java.lang.String)
   */
  public Dimension calculateSize(String[] text)
  {
    int height = 0;

    if (text == null || text.length <= 0)
    {
      return new Dimension(0, lineHeight);
    }

    int maxWidth = 0;
    for (String line : text)
    {
      int width = line.length() * 5;
      if (width > maxWidth)
        maxWidth = width;
      height += lineHeight;
    }

    return new Dimension(maxWidth, height);
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getFont()
   */
  public IFont getFont()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getLineHeight()
   */
  public int getLineHeight()
  {
    return lineHeight;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getWidth(java.lang.String)
   */
  public int getWidth(String text)
  {
    return text.length() * 5;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getWidth(char)
   */
  public int getWidth(char text)
  {
    return 5;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#isValidChar(char)
   */
  public boolean isValidChar(char c)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#render(int, int, java.lang.String, org.fenggui.util.Color, org.fenggui.binding.render.Graphics, org.fenggui.binding.render.IOpenGL)
   */
  public void render(int x, int y, String[] text, Color color, Graphics g, IOpenGL gl)
  {
    System.out.print(text);
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#setFont(org.fenggui.binding.render.IFont)
   */
  public void setFont(IFont font)
  {

  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#getUniqueName()
   */
  public String getUniqueName()
  {
    return MockRenderer.GENERATE_NAME;
  }

  /* (non-Javadoc)
   * @see org.fenggui.theme.xml.IXMLStreamable#process(org.fenggui.theme.xml.InputOutputStream)
   */
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
  }

  /* (non-Javadoc)
   * @see org.fenggui.util.ICopyable#copy()
   */
  public ITextRenderer copy()
  {
    return this;
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
    return ITextRenderer.DEFAULTTEXTRENDERERKEY;
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
