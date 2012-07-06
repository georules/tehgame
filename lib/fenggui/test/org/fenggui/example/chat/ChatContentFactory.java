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
 * Created on Oct 21, 2008
 * $Id$
 */
package org.fenggui.example.chat;

import java.util.ArrayList;
import java.util.List;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.example.chat.ChatManager.ChatMessage;
import org.fenggui.text.ITextCursorRenderer;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.part.AbstractContentPart;
import org.fenggui.text.content.part.TextPart;
import org.fenggui.util.Color;

/**
 * 
 * @author Johannes, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ChatContentFactory implements IContentFactory
{
  public static final String  TEXTRENDERER_DEFAULT = "<TextRenderer_Default>";
  public static final String  TEXTRENDERER_BOLD    = "<TextRenderer_Bold>";

  public static final String  TEXTSTYLEKEY_BOLD    = "boldStyle";

  private TextStyle           defaultStyle;
  private TextStyle           boldStyle;

  private ITextCursorRenderer textCursorRenderer   = null;

  public ChatContentFactory()
  {
    defaultStyle = new TextStyle();

    textCursorRenderer = new ITextCursorRenderer()
    {

      public void render(int x, int y, int w, int h, Graphics g)
      {
        g.setColor(Color.BLACK);
        g.drawFilledRectangle(x, y, w, h);
      }

      public int getHeight()
      {
        return DYNAMICSIZE;
      }

      public int getWidth()
      {
        return 2;
      }

    };
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentFactory#getContentObject(java.lang.Object)
   */
  public Object getContentObject(Object obj)
  {
    throw new UnsupportedOperationException("This is not supported.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.advanced.IContentFactory#getTextCursorRenderer()
   */
  public ITextCursorRenderer getTextCursorRenderer()
  {
    return textCursorRenderer;
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
    if (content instanceof ChatMessage)
    {
      ChatMessage message = (ChatMessage) content;
      List<AbstractContentPart> result = new ArrayList<AbstractContentPart>(3);
      TextPart tpTime = new TextPart("[" + message.time.getHours() + ":" + message.time.getMinutes() + "]",
          TextStyle.DEFAULTSTYLEKEY, appearance);
      TextPart tpUsername = new TextPart(message.userName + ": ", TEXTSTYLEKEY_BOLD, appearance);
      TextPart tpMessage = new TextPart(message.message, TextStyle.DEFAULTSTYLEKEY, appearance);
      result.add(tpTime);
      result.add(tpUsername);
      result.add(tpMessage);
      return result;
    }
    else
    {
      throw new IllegalArgumentException("content needs to be of type ChatMessage");
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getEmptyContentPart(org.fenggui.appearance.TextAppearance)
   */
  public AbstractContentPart getEmptyContentPart(TextAppearance appearance)
  {
    return new TextPart("", TextStyle.DEFAULTSTYLEKEY, appearance);
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#getContentLines(java.lang.Object)
   */
  public Object[] getContentLines(Object content)
  {
    throw new UnsupportedOperationException("This is not supported.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.text.content.IContentFactory#finalContent(java.lang.StringBuilder)
   */
  public void finalContent(StringBuilder result)
  {

  }

}
