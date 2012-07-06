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
 * $Id: LabelExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */

package org.fenggui.example;

import java.io.IOException;

import org.fenggui.*;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.composite.Window;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.ISliderMovedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.SliderMovedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.GridLayout;
import org.fenggui.util.Alignment;

/**
 * Example class for Label.
 * 
 * @author Boris Beaulant, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class LabelExample implements IExample
{

  private static final long serialVersionUID = 1L;

  private Pixmap            pixmap           = null;

  private void buildLabelFrame(Display display)
  {

    Window window = FengGUI.createWindow(display, true, false, false, true);
    window.setTitle(getExampleName());
    window.setX(50);
    window.setY(300);
    window.setSize(300, 200);
    window.getContentContainer().setLayoutManager(new BorderLayout());

    final Label label = FengGUI.createLabel(window.getContentContainer(), "Label's text");
    label.setLayoutData(BorderLayoutData.CENTER);

    Container buttons = FengGUI.createContainer(window.getContentContainer());
    buttons.setLayoutData(BorderLayoutData.EAST);
    buttons.setLayoutManager(new GridLayout(8, 1));

    CheckBox<Integer> pixmapCheckBox = FengGUI.<Integer> createCheckBox(window.getContentContainer(), "Pixmap");
    CheckBox<Integer> gapCheckBox = FengGUI.<Integer> createCheckBox(window.getContentContainer(), "Gap (10px)");

    final ToggableGroup<RadioButton<Alignment>> alignGroup = new ToggableGroup<RadioButton<Alignment>>();

    RadioButton<Alignment> leftRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Left", alignGroup);
    leftRadioButton.setValue(Alignment.LEFT);
    leftRadioButton.setSelected(true);

    RadioButton<Alignment> middleRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Middle", alignGroup);
    middleRadioButton.setValue(Alignment.MIDDLE);

    RadioButton<Alignment> rightRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Right", alignGroup);
    rightRadioButton.setValue(Alignment.RIGHT);

    RadioButton<Alignment> topRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Top", alignGroup);
    topRadioButton.setValue(Alignment.TOP);

    RadioButton<Alignment> bottomRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Bottom", alignGroup);
    bottomRadioButton.setValue(Alignment.BOTTOM);

    RadioButton<Alignment> topLeftRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Top Left", alignGroup);
    topLeftRadioButton.setValue(Alignment.TOP_LEFT);

    pixmapCheckBox.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (selectionChangedEvent.isSelected())
        {
          label.setPixmap(pixmap);
        }
        else
        {
          label.setPixmap(null);
        }
      }
    });

    gapCheckBox.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (selectionChangedEvent.isSelected())
        {
          label.getAppearance().setGap(10);
        }
        else
        {
          label.getAppearance().setGap(0);
        }
      }
    });

    alignGroup.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        label.getAppearance().setAlignment(alignGroup.getSelectedValue().getValue());
      }
    });
  }

  private void buildRotatedLabelFrame(Display display)
  {
    Window window = FengGUI.createWindow(display, true, false, false, true);
    window.setTitle("RotatedLabel Exemple");
    window.setX(50);
    window.setY(50);
    window.setSize(300, 200);
    window.getContentContainer().setLayoutManager(new BorderLayout());

    final RotatedLabel rotatedLabel = FengGUIOptional.createRotatedLabel(window.getContentContainer(),
      "RotatedLabel's text");
    rotatedLabel.setLayoutData(BorderLayoutData.CENTER);

    Container buttons = FengGUI.createContainer(window.getContentContainer());
    buttons.setLayoutData(BorderLayoutData.EAST);
    buttons.setLayoutManager(new GridLayout(8, 1));

    final Label labelAngle = FengGUI.createLabel(buttons, "Angle :");

    Slider angleSlider = FengGUI.createSlider(buttons, true);
    angleSlider.setValue(0.5);

    final ToggableGroup<RadioButton<Alignment>> alignGroup = new ToggableGroup<RadioButton<Alignment>>();

    RadioButton<Alignment> leftRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Left", alignGroup);
    leftRadioButton.setValue(Alignment.LEFT);
    leftRadioButton.setSelected(true);

    RadioButton<Alignment> middleRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Middle", alignGroup);
    middleRadioButton.setValue(Alignment.MIDDLE);

    RadioButton<Alignment> rightRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Right", alignGroup);
    rightRadioButton.setValue(Alignment.RIGHT);

    RadioButton<Alignment> topRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Top", alignGroup);
    topRadioButton.setValue(Alignment.TOP);

    RadioButton<Alignment> bottomRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Bottom", alignGroup);
    bottomRadioButton.setValue(Alignment.BOTTOM);

    RadioButton<Alignment> topLeftRadioButton = FengGUI.<Alignment> createRadioButton(buttons, "Top Left", alignGroup);
    topLeftRadioButton.setValue(Alignment.TOP_LEFT);

    angleSlider.addSliderMovedListener(new ISliderMovedListener()
    {
      public void sliderMoved(SliderMovedEvent sliderMovedEvent)
      {
        float angle = (float) (sliderMovedEvent.getPosition() - 0.5) * 180;
        labelAngle.setText("Angle : " + angle);
        rotatedLabel.setAngle(angle);
      }
    });

    alignGroup.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        rotatedLabel.getAppearance().setAlignment(alignGroup.getSelectedValue().getValue());
      }
    });
  }

  public void buildGUI(Display display)
  {
    try
    {
      ITexture texture = Binding.getInstance().getTexture("data/redCross.gif");
      pixmap = new Pixmap(texture, 1, 0, 14, 15);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    buildLabelFrame(display);
    buildRotatedLabelFrame(display);

    display.layout();
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Label Example";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    return "Label Example";
  }

}
