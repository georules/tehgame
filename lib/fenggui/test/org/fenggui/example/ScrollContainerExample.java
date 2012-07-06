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
 * Created on Jul 15, 2005
 * $Id: ScrollContainerExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.ScrollContainer;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.Window;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

/**
 * Small example app that uses <code>ScrollContainer</code>s.
 *  
 * @author Johannes Schaback 
 */
public class ScrollContainerExample implements IExample
{

  private Display desk;

  private void buildFrame()
  {

    final Window filesFrame = FengGUI.createWindow(desk, true, false, false, true);
    filesFrame.setX(50);
    filesFrame.setY(50);
    filesFrame.setSize(300, 300);
    filesFrame.setTitle("ScrollContainer");

    ScrollContainer sc = FengGUI.createScrollContainer(filesFrame.getContentContainer());
    sc.setShowScrollbars(true);
    Label l = FengGUI.createLabel(sc, "This text is far too long to"
        + " be displayed in one single line. But fortunately, Johannes "
        + "implemented scrolling so that we all can enjoy the pleasure"
        + " to read this text!   Ahhhh. I'm getting sick of these colors.");

    ImageFont serif = FontFactory.renderStandardFont(new java.awt.Font("Times", java.awt.Font.PLAIN, 45), true,
      Alphabet.getDefaultAlphabet());

    TextStyle style = new TextStyle();
    style.getTextStyleEntry(TextStyleEntry.DEFAULTSTYLESTATEKEY).setColor(Color.WHITE);
    ITextRenderer renderer = new DirectTextRenderer(serif);
    l.getAppearance().addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);
    l.getAppearance().addStyle(TextStyle.DEFAULTSTYLEKEY, style);
    l.getAppearance().add(new PlainBackground(Color.DARK_GREEN));
    filesFrame.layout();
  }

  public void buildGUI(Display d)
  {

    desk = d;
    buildFrame();
  }

  public String getExampleName()
  {
    return "ScrollContainer Example";
  }

  public String getExampleDescription()
  {
    return "Shows a ScrollContainer";
  }

}
