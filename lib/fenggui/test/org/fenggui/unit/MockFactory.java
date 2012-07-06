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

import java.util.ArrayList;
import java.util.List;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.text.ITextCursorRenderer;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.part.AbstractContentPart;
import org.fenggui.text.content.part.TextPart;

/**
 * @author ava
 * 
 */
public class MockFactory implements IContentFactory
{

  private TextStyle defaultStyle     = new TextStyle();
  private int       textStyleSpacing = 5;

  /**
   * 
   */
  public MockFactory()
  {
  }

  public String getContentObject(Object obj)
  {
    return null;
  }

  public void setTextSpacing(int spacing)
  {
    textStyleSpacing = spacing;
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentFactory#getTextCursorRenderer()
   */
  public ITextCursorRenderer getTextCursorRenderer()
  {
    return new ITextCursorRenderer()
    {

      public int getHeight()
      {
        return 1;
      }

      public int getWidth()
      {
        return 1;
      }

      public void render(int x, int y, int w, int h, Graphics g)
      {
        //do nothing :)
      }

    };
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getContent(java.lang.Object[])
   */
  public Object getContent(Object[] lines)
  {
    String[] linesS = (String[]) lines;
    String content = "";
    for (String line : linesS)
      content += line;
    return content;
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getContentLines(java.lang.Object)
   */
  public Object[] getContentLines(Object content)
  {
    String contentS = (String) content;
    return contentS.split("\n", -1);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#addLineEnd(java.lang.StringBuilder)
   */
  public void addLineEnd(StringBuilder result)
  {
    result.append("\n");
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#addLineStart(java.lang.StringBuilder)
   */
  public void addLineStart(StringBuilder result)
  {

  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getContentParts(java.lang.Object, org.fenggui.appearance.TextAppearance)
   */
  public List<AbstractContentPart> getContentParts(Object content, TextAppearance appearance)
  {
    List<AbstractContentPart> result = new ArrayList<AbstractContentPart>(1);
    result.add(new TextPart(content.toString(), TextStyle.DEFAULTSTYLEKEY, textStyleSpacing, textStyleSpacing,
        appearance));
    return result;
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getEmptyContentPart(org.fenggui.appearance.TextAppearance)
   */
  public AbstractContentPart getEmptyContentPart(TextAppearance appearance)
  {
    return new TextPart("", TextStyle.DEFAULTSTYLEKEY, textStyleSpacing, textStyleSpacing, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#finalContent(java.lang.StringBuilder)
   */
  public void finalContent(StringBuilder result)
  {
    if (result.length() > 0)
      result.deleteCharAt(result.length() - 1);
  }

}
