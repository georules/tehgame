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
 * Created on Aug 1, 2007
 * $Id$
 */
package org.fenggui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.IDragAndDropListener;
import org.fenggui.event.IPaintListener;
import org.fenggui.event.ISliderMovedListener;
import org.fenggui.event.key.Key;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

/**
 * Implementation of a slider that snaps to discrete ticks.
 * Currently only as vertical slider.
 * 
 * @author Johannes Schaback, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class SnappingSlider extends StatefullWidget<TextAppearance>
{
  private Pixmap                          sliderPixmap    = null;
  private int                             ticks           = 10;
  private int                             position        = 0;
  private TextAppearance                  appearance      = null;
  private ArrayList<ISliderMovedListener> sliderMovedHook = new ArrayList<ISliderMovedListener>();
  private IDragAndDropListener            dndListener     = new SliderDnDListener();
  private String[]                        tickLabels      = null;
  private IPaintListener                  tickPainter     = new TickPainter();

  /**
   * 
   * @param nrOfTicks
   */
  public SnappingSlider(int nrOfTicks)
  {
    this.ticks = nrOfTicks;

    appearance = new TextAppearance(this);

    updateMinSize();
  }

  @Override
  public TextAppearance getAppearance()
  {
    return appearance;
  }

  public Pixmap getSliderPixmap()
  {
    return sliderPixmap;
  }

  public void setSliderPixmap(Pixmap sliderPixmap)
  {
    this.sliderPixmap = sliderPixmap;
    updateMinSize();
  }

  public int getTicks()
  {
    return ticks;
  }

  public void addSliderMovedListener(ISliderMovedListener sle)
  {
    sliderMovedHook.add(sle);
  }

  public void removeSliderMovedListener(ISliderMovedListener sle)
  {
    sliderMovedHook.remove(sle);
  }

  private void fireSliderMovedListener()
  {
    //		SliderMovedEvent sme = new SliderMovedEvent(this);
    //		for (ISliderMovedListener l : sliderMovedHook)
    //		{
    //			l.sliderMoved(sme);
    //		}
  }

  @Override
  public void addedToWidgetTree()
  {
    if (getDisplay() != null)
      getDisplay().addDndListener(dndListener);
  }

  @Override
  public void removedFromWidgetTree()
  {
    if (getDisplay() != null)
      getDisplay().removeDndListener(dndListener);
  }

  public int getValue()
  {
    return position;
  }

  public void setValue(int v)
  {
    position = v;
  }

  private class SliderDnDListener implements IDragAndDropListener
  {
    private int startX = 0;

    public void select(int x, int y, Set<Key> modifiers)
    {
      startX = getDisplayX() + getAppearance().getLeftMargins()
          - (getAppearance().getContentWidth() / (getTicks() - 1)) / 2;
    }

    public void drag(int x, int y, Set<Key> modifiers)
    {
      //			float half = ((float) getAppearance().getContentWidth() / (float) (getTicks() - 1)) / 1f;
      int deltaX = x - startX;

      int t = deltaX / (getAppearance().getContentWidth() / (getTicks() - 1));

      //System.out.println("drag: "+x+" "+y+" deltaX: "+deltaX+"  "+t);

      if (t >= 0 && t < getTicks())
      {
        int oldT = getValue();
        setValue(t);

        if (oldT != t)
          fireSliderMovedListener();
      }
    }

    public void drop(int x, int y, IWidget dropOn, Set<Key> modifiers)
    {
      //pressed = false;
    }

    public boolean isDndWidget(IWidget w, int x, int y)
    {
      x = x - getDisplayX();
      y = y - getDisplayY();

      int sPos = (int) ((float) getValue() * (float) (getAppearance().getContentWidth() / (float) (getTicks() - 1)))
          + getAppearance().getLeftMargins();

      int pixmapHeight = getSliderPixmap() == null ? 10 : getSliderPixmap().getHeight();
      int pixmapWidth = getSliderPixmap() == null ? 10 : getSliderPixmap().getWidth();

      //System.out.println(" "+x+" "+y+" sPos: "+sPos);
      return getHeight() / 2 - pixmapHeight / 2 < y && getHeight() / 2 + pixmapHeight / 2 > y
          && sPos - pixmapWidth / 2 < x && sPos + pixmapWidth / 2 > x;
    }
  }

  public String[] getTickLabels()
  {
    return tickLabels;
  }

  public void setTickLabels(String... tickLabels)
  {
    this.tickLabels = tickLabels;
    updateMinSize();
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);

    sliderPixmap = stream.processChild("SliderPixmap", sliderPixmap, Pixmap.class);
  }

  @Override
  public Dimension getMinContentSize()
  {
    int pixmapHeight = 20;
    if (getSliderPixmap() != null)
      pixmapHeight = getSliderPixmap().getHeight();

    if (getTickLabels() != null)
    {
      int sum = 0;
      for (String s : getTickLabels())
      {
        //				appearance.getData().setContent(s);
        //				sum += appearance.getRenderer().getTextWidth(appearance.getData());
      }

      return new Dimension(sum + 30, pixmapHeight + 20 + 15); // 15 -> appearance.getTextLineHeight()
    }
    else
      return new Dimension(100, pixmapHeight + 20);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    tickPainter.paint(g);

    if (getSliderPixmap() == null)
      return;

    int x = (int) ((float) getValue() * ((float) appearance.getContentWidth() / (float) (getTicks() - 1)));

    g.setColor(Color.WHITE);
    g.drawImage(getSliderPixmap(), x - getSliderPixmap().getWidth() / 2, appearance.getContentHeight() / 2
        - getSliderPixmap().getHeight() / 2);
  }

  public IPaintListener getTickPainter()
  {
    return tickPainter;
  }

  public void setTickPainter(IPaintListener tickPainter)
  {
    this.tickPainter = tickPainter;
  }

  public class TickPainter implements IPaintListener
  {
    public void paint(Graphics g)
    {
      if (getSliderPixmap() == null)
        return;

      //			g.setColor(appearance.getgetColor());
      final int y = appearance.getContentHeight() / 2 + getSliderPixmap().getHeight() / 2;

      for (int i = 0; i < getTicks(); i++)
      {
        int x = (int) ((float) i * (float) (appearance.getContentWidth() / (float) (getTicks() - 1)));

        g.drawLine(x, y - 5, x, y + 5);
      }

      String[] tickLabels = getTickLabels();

      if (tickLabels == null)
        return;
      if (tickLabels.length != getTicks())
        throw new IllegalStateException("Number of ticks and number of tick labels must be equal!");

      for (int i = 0; i < getTicks(); i++)
      {
        int x = (int) ((float) i * (float) (appearance.getContentWidth() / (float) (getTicks() - 1)));
        //				appearance.getData().setContent(tickLabels[i]);
        //				appearance.renderText(x - appearance.getRenderer().getTextWidth(appearance.getData()), y + 7, g);
      }

    }

  }

}
