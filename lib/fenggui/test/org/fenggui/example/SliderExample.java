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
 * $Id: SliderExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.*;
import org.fenggui.composite.Window;
import org.fenggui.event.ISliderMovedListener;
import org.fenggui.event.SliderMovedEvent;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

/**
 * 
 * Small example app that demonstrates a Slider Widget.
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class SliderExample implements IExample
{

  private Display desk;

  private void buildHorizontalSliderFrame()
  {
    Window f = FengGUI.createWindow(desk, true, false, false, true);
    f.setTitle("Horizontal Slider Test");
    f.getContentContainer().setLayoutManager(new RowLayout(false));
    f.getContentContainer().getAppearance().setPadding(new Spacing(10, 10));

    Slider slider = FengGUI.createSlider(f.getContentContainer(), true);
    slider.updateMinSize();

    final Label label = FengGUI.createLabel(f.getContentContainer(), "Please move the slider a bit! Thanks!");
    label.getAppearance().setAlignment(Alignment.MIDDLE);
    slider.addSliderMovedListener(new ISliderMovedListener()
    {
      public void sliderMoved(SliderMovedEvent sliderMovedEvent)
      {
        label.setText("Slider at " + (int) (sliderMovedEvent.getPosition() * 100.0) + "% !");
      }
    });

    f.pack();
  }

  private void buildVerticalSliderFrame()
  {
    Window f = FengGUI.createWindow(desk, true, false, false, true);
    f.setTitle("Vertical Slider Test");

    f.getContentContainer().setLayoutManager(new RowExLayout(true));
    f.getContentContainer().getAppearance().setPadding(new Spacing(10, 10));

    Slider slider = FengGUI.createSlider(f.getContentContainer(), false);
    slider.setLayoutData(new RowExLayoutData(true, false));
    slider.updateMinSize();

    final Label label = FengGUI.createLabel(f.getContentContainer(), "Please move the slider a bit!");
    label.getAppearance().setAlignment(Alignment.MIDDLE);
    label.setLayoutData(new RowExLayoutData(true, true));
    slider.addSliderMovedListener(new ISliderMovedListener()
    {
      public void sliderMoved(SliderMovedEvent sliderMovedEvent)
      {
        label.setText("Slider at " + (int) (sliderMovedEvent.getPosition() * 100.0) + "% !");
      }
    });

    f.pack();
  }

  public void buildGUI(Display d)
  {
    desk = d;

    buildHorizontalSliderFrame();
    buildVerticalSliderFrame();

    desk.layout();
  }

  public String getExampleName()
  {
    return "Slider Example";
  }

  public String getExampleDescription()
  {
    return "Shows two frames with Sliders";
  }

}
