/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005-2009 FengGUI Project
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
 * Created on Nov 30, 2008
 * $Id$
 */
package org.fenggui.unit;

import java.util.ArrayList;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.text.content.ContentLine;
import org.fenggui.text.content.ContentUserLine;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.part.AbstractContentPart;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class ContentTestBase
{

  protected MockFactory    factory    = new MockFactory();
  protected TextAppearance appearance = new TextAppearance(null);

  public void init()
  {
    appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, new MockRenderer());
    appearance.addStyle(TextStyle.DEFAULTSTYLEKEY, new TextStyle());
  }

  public ContentUserLine getUserLine(String[] texts)
  {
    ContentLine[] lines = new ContentLine[texts.length];

    for (int i = 0; i < texts.length; i++)
    {
      lines[i] = getLine(texts[i]);
    }

    ContentUserLine result = new ContentUserLine(lines);
    return result;
  }

  public ContentLine getLine(String text)
  {
    ArrayList<AbstractContentPart> line = new ArrayList<AbstractContentPart>();
    line.addAll(factory.getContentParts(text, appearance));

    ContentLine contentLine = new ContentLine(line);
    contentLine.optimizeContent(appearance);
    return contentLine;
  }

  public ContentLine fillWithAmount(int start, int end)
  {
    ArrayList<AbstractContentPart> line = new ArrayList<AbstractContentPart>();
    for (int i = start; i < end; i++)
    {
      line.addAll(factory.getContentParts("testpart> " + i + " ", appearance));
    }
    ContentLine contentLine = new ContentLine(line);
    contentLine.optimizeContent(appearance);
    return contentLine;
  }

  public String getContent(ContentLine line)
  {
    StringBuilder content = new StringBuilder();
    line.getContent(content);
    return content.toString();
  }

  public String getContent(ContentUserLine line)
  {
    StringBuilder content = new StringBuilder();
    line.getContent(content);
    return content.toString();
  }

  public String getContent(AbstractContentPart line)
  {
    StringBuilder content = new StringBuilder();
    line.getContent(content);
    return content.toString();
  }
}
