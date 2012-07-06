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
package org.fenggui.example;

import org.fenggui.Canvas;
import org.fenggui.Display;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.BevelBorder;
import org.fenggui.decorator.border.Border;
import org.fenggui.decorator.border.CompositeBorder;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.decorator.border.TitledBorder;
import org.fenggui.util.Color;
import org.fenggui.util.Spacing;

/**
 * @author Esa Tanskanen
 *
 */
public class BorderTest implements IExample
{
  final int SIZE = 100;

  public static int getRandomInt()
  {
    return (int) (Math.random() * 5.0) + 1;
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#buildGUI(org.fenggui.Display)
   */
  public void buildGUI(Display display)
  {
    CompositeBorder compositeBorder = new CompositeBorder(new PlainBorder(new Spacing(getRandomInt(), getRandomInt(),
        getRandomInt(), getRandomInt()), Color.RED), new PlainBorder(new Spacing(getRandomInt(), getRandomInt(),
        getRandomInt(), getRandomInt()), Color.YELLOW), new PlainBorder(new Spacing(getRandomInt(), getRandomInt(),
        getRandomInt(), getRandomInt()), Color.BLUE));

    final int i = SIZE + 20;

    createBorderedWidget(SIZE + i * 0, SIZE + i * 0, compositeBorder, display);

    PlainBorder plainBorder = new PlainBorder(2, 3, 4, 5);
    createBorderedWidget(SIZE + i * 1, SIZE + i * 0, plainBorder, display);

    TitledBorder titleBorder = new TitledBorder(ImageFont.getDefaultFont(), "Hello Border!", Color.BLACK);
    titleBorder.setColor(Color.BLACK);
    createBorderedWidget(SIZE + i * 2, SIZE + i * 0, titleBorder, display);

    BevelBorder bevelBorder = new BevelBorder(Color.DARK_RED, Color.LIGHT_RED);
    createBorderedWidget(SIZE + i * 3, SIZE + i * 0, bevelBorder, display);

    /*
    try
    {
    	CompositeBackground cb = new CompositeBackground(
    		new PlainBackground(Color.RED),
    		new GradientBackground(Color.GREEN, Color.OPAQUE, Color.BLUE, Color.CYAN),
    		new PixmapBackground(new Pixmap(Binding.getInstance().getTexture("data/redCross.gif"))));
    	
    	createBackgroundedWidget(SIZE + 0, SIZE + i*1, cb, display);
    }
    catch (IOException e)
    {
    	e.printStackTrace();
    }
    */
  }

  private void createBorderedWidget(int displayX, int displayY, Border b, Display d)
  {
    Canvas w = new Canvas();
    w.getAppearance().setMargin(Spacing.ZERO_SPACING);
    w.getAppearance().setPadding(Spacing.ZERO_SPACING);
    w.setXY(displayX, displayY);
    w.getAppearance().add(b);
    d.addWidget(w);
    w.getAppearance().add(new PlainBackground(Color.WHITE));
    w.setSize(SIZE, SIZE);
  }

  /*
  	private void createBackgroundedWidget(int displayX, int displayY, Background b, Display d)
  	{
  		Canvas w = new Canvas();
  		w.getAppearance().setMargin(Spacing.ZERO_SPACING);
  		w.getAppearance().setPadding(Spacing.ZERO_SPACING);
  		w.setXY(displayX, displayY);
  		d.addWidget(w);
  		w.getAppearance().add(b);
  		w.setSize(SIZE, SIZE);
  	}
  	*/
  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Composite border test";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    return "Border Example";
  }

}
