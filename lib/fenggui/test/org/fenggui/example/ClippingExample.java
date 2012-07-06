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
 * $Id: ClippingExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import java.io.IOException;

import org.fenggui.Canvas;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.appearance.DecoratorAppearance;
import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.ITexture;
import org.fenggui.composite.Window;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Tests the basic clipping.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class ClippingExample implements IExample
{

  private Window  frame = null;
  private Display desk;

  private void buildTestFrame()
  {

    frame = FengGUI.createWindow(desk, true, false, false, true);
    frame.setTitle("Clipping Example");

    Label l = FengGUI.createLabel(frame.getContentContainer(), "This text is too long"
        + " to be displayed and has to be clipped!");

    Label l1 = FengGUI.createLabel(frame.getContentContainer());
    l1.setMultiline(true);
    l1.setText("Hallo! Dies ist ein \n mehrzeilen Text, der total\ntoll ist!!!");

    try
    {
      final ITexture tex = Binding.getInstance().getTexture("data/redCross.gif");

      final Canvas canvas = new Canvas();
      frame.getContentContainer().addWidget(canvas);
      canvas.setSize(100, 100);
      canvas.setExpandable(false);
      canvas.setShrinkable(false);
      DecoratorAppearance pl = new DefaultAppearance(canvas)
      {
        Color background = new Color(0, 0, 0);

        public void paintContent(Graphics g, IOpenGL gl)
        {
          g.setColor(background);
          g.drawFilledRectangle(0, 0, canvas.getWidth(), canvas.getHeight());
          g.setColor(1, 1, 1);
          g.drawImage(tex, 90, 90);
          g.drawImage(tex, -10, -10);

          g.drawImage(tex, 90, -10);
          g.drawImage(tex, -10, 90);

          g.drawImage(tex, 45, 45);

          for (int i = 0; i < 10; i++)
          {
            g.setColor((float) Math.random(), (float) Math.random(), (float) Math.random());
            g.drawLine((int) (Math.random() * 200) - 50, (int) (Math.random() * 200) - 50,
              (int) (Math.random() * 200) - 50, (int) (Math.random() * 200) - 50);
          }

        }

        public Dimension getContentMinSizeHint()
        {
          return new Dimension(20, 20);
        }

      };
      canvas.setAppearance(pl);
      ((Container) frame.getContentContainer()).setLayoutManager(new RowLayout(false));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    frame.pack();
  }

  public void buildGUI(Display d)
  {
    desk = d;

    buildTestFrame();

  }

  public String getExampleName()
  {
    return "Clipping Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates that the clipping works";
  }

}
