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
 * $Id: CheckBoxExample.java 613 2009-03-25 22:02:20Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.CheckBox;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.layout.RowExLayout;
import org.fenggui.util.Spacing;

/**
 * Example for testing the <code>CheckBox</code> Widget.
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-25 23:02:20 +0100 (Mi, 25 Mär 2009) $
 * @version $Revision: 613 $
 */
public class CheckBoxExample implements IExample
{

  Window  frame = null;
  Display desk;

  private void buildFrame()
  {
    frame = FengGUI.createWindow(true, false, false, true);
    frame.setTitle("Fun With Check Boxes");

    frame.getContentContainer().setLayoutManager(new RowExLayout(false));
    frame.getContentContainer().getAppearance().setPadding(new Spacing(0, 5));

    Button b = FengGUI.createWidget(Button.class);
    b.setText("Book Flight!");

    final CheckBox<Integer> berlin = FengGUI.<Integer> createCheckBox();
    berlin.setText("Berlin");
    berlin.setValue(133);

    final CheckBox<Integer> paris = FengGUI.<Integer> createCheckBox();
    paris.setText("Paris");
    paris.setValue(431);

    final CheckBox<Integer> london = FengGUI.<Integer> createCheckBox();
    london.setText("London");
    london.setValue(234);

    final CheckBox<Integer> newYork = FengGUI.<Integer> createCheckBox();
    newYork.setText("New York");
    newYork.setValue(1022);

    final CheckBox<Integer> munich = FengGUI.<Integer> createCheckBox();
    munich.setText("Munich");
    munich.setValue(200);

    final CheckBox<Integer> sydney = FengGUI.<Integer> createCheckBox();
    sydney.setText("Sidney");
    sydney.setValue(2134);

    final CheckBox<Integer> sanFrancisco = FengGUI.<Integer> createCheckBox();
    sanFrancisco.setText("San Francisco");
    sanFrancisco.setValue(1523);

    final Label label = FengGUI.createWidget(Label.class);
    label.setText("Please select the cities you want to visit!");

    frame.getContentContainer().addWidget(b, berlin, paris, london, newYork, munich, sydney, sanFrancisco, label);
    label.getAppearance().setMargin(new Spacing(10, 10));

    berlin.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (selectionChangedEvent.isSelected())
          label.setText("A good choice!!!");
        else
          label.setText("A bad choice!!!");
      }
    });

    b.addButtonPressedListener(new IButtonPressedListener()
    {
      public void buttonPressed(ButtonPressedEvent e)
      {
        int sum = 0;

        if (london.isSelected())
          sum += london.getValue();
        if (paris.isSelected())
          sum += paris.getValue();
        if (berlin.isSelected())
          sum += berlin.getValue();
        if (newYork.isSelected())
          sum += newYork.getValue();
        if (munich.isSelected())
          sum += munich.getValue();
        if (sydney.isSelected())
          sum += sydney.getValue();
        if (sanFrancisco.isSelected())
          sum += sanFrancisco.getValue();

        label.setText("Price: $" + sum);
      }
    });

    frame.pack();
    desk.addWidget(frame);
  }

  public void buildGUI(Display f)
  {
    desk = f;
    buildFrame();
  }

  public String getExampleName()
  {
    return "CheckBox Exmaple";
  }

  public String getExampleDescription()
  {
    return "CheckBoxes are so handy!";
  }
}
