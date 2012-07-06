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
 * $Id: TextViewExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.ScrollContainer;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.composite.Window;
import org.fenggui.composite.text.TextStyle;
import org.fenggui.composite.text.TextView;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

/**
 * Displays a text area with a rather stupid text.
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class TextViewExample implements IExample
{

  private Window      dialog = null;
  private Display     display;

  private ImageFont[] fonts;

  private void buildFileFrame()
  {

    fonts = new ImageFont[] {
        FontFactory.renderStandardFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 24), true, Alphabet
            .getDefaultAlphabet()),
        FontFactory.renderStandardFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 12), true, Alphabet
            .getDefaultAlphabet()),
        FontFactory.renderStandardFont(new java.awt.Font("Verdana", java.awt.Font.ITALIC, 40), true, Alphabet
            .getDefaultAlphabet()) };

    dialog = FengGUI.createDialog(display, "TextView word-wrap");
    dialog.setX(50);
    dialog.setY(50);
    dialog.setSize(300, 300);

    dialog.getContentContainer().setLayoutManager(new BorderLayout());

    ScrollContainer sc = new ScrollContainer();
    sc.setLayoutData(BorderLayoutData.CENTER);

    dialog.getContentContainer().addWidget(sc);

    final TextView textView = new TextView();
    textView.setText("Not long ago and not far away there was a beautiful, big teddy bear who sat on a shelf in a "
        + "drug store waiting for someone to buy him and give him a home. His name was Wolstencroft. And "
        + "he was no ordinary bear. His fur was a lovely shade of light grey, and he had honey colored ears, "
        + "nose and feet. His eyes were warm and kind and he had a wonderfully wise look on his face.");

    textView.appendText(" White", new TextStyle(fonts[0], Color.WHITE));
    textView.appendText(" RED", new TextStyle(fonts[1], Color.RED));
    textView.appendText(" GREEN", new TextStyle(fonts[2], Color.GREEN));

    sc.setInnerWidget(textView);

    Container buttonsContainer = new Container();
    buttonsContainer.setLayoutData(BorderLayoutData.SOUTH);
    dialog.getContentContainer().addWidget(buttonsContainer);

    Button button = FengGUI.createButton("Append 'Hello'");
    button.setLayoutData(BorderLayoutData.SOUTH);
    button.addButtonPressedListener(new IButtonPressedListener()
    {
      public void buttonPressed(ButtonPressedEvent e)
      {
        textView.appendText(" Hello", new TextStyle(fonts[(int) Math.ceil(Math.random() * fonts.length) - 1],
            Color.BLUE));
      }
    });
    buttonsContainer.addWidget(button);

    Button button2 = FengGUI.createButton("addTextLine 'Hello world !'");
    button2.setLayoutData(BorderLayoutData.SOUTH);
    button2.addButtonPressedListener(new IButtonPressedListener()
    {
      public void buttonPressed(ButtonPressedEvent e)
      {
        textView.addTextLine("Hello world !", new TextStyle(fonts[(int) Math.ceil(Math.random() * fonts.length) - 1],
            Color.RED));
      }
    });
    buttonsContainer.addWidget(button2);

    dialog.layout();
  }

  public void buildGUI(Display g)
  {
    display = g;
    buildFileFrame();
  }

  public String getExampleName()
  {
    return "TextView Example";
  }

  public String getExampleDescription()
  {
    return "TextView Example";
  }

}
