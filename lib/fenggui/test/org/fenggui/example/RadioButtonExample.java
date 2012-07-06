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
 * $Id: RadioButtonExample.java 613 2009-03-25 22:02:20Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.RadioButton;
import org.fenggui.ToggableGroup;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

/**
 * Exmaple class for demonstrating the use of Radio Buttons.
 * 
 * @todo Comment this class... #
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-25 23:02:20 +0100 (Mi, 25 Mär 2009) $
 * @version $Revision: 613 $
 */
public class RadioButtonExample implements IExample
{

  private Window  filesFrame = null;
  private Display desk;

  private void buildFrame()
  {

    filesFrame = FengGUI.createWindow(true, false, false, true);
    filesFrame.setSize(350, 400);
    filesFrame.setTitle("Radio Buttons");
    filesFrame.getContentContainer().setLayoutManager(new RowExLayout(false));
    filesFrame.getContentContainer().getAppearance().setPadding(new Spacing(10, 10));
    Button btn = FengGUI.createWidget(Button.class);
    btn.setLayoutData(new RowExLayoutData(true, false));
    btn.setText("Apply");

    Label question = FengGUI.createWidget(Label.class);
    question.setLayoutData(new RowExLayoutData(true, false));
    question.setText("How many legs do bugs have?");

    final ToggableGroup<RadioButton<String>> group = new ToggableGroup<RadioButton<String>>();
    RowExLayoutData layoutData = new RowExLayoutData(false, false, 1.0d, Alignment.LEFT);

    RadioButton<String> threeLegs = FengGUI.<String> createRadioButton();
    threeLegs.setLayoutData(layoutData);
    threeLegs.setText("Three legs");
    threeLegs.setRadioButtonGroup(group);
    threeLegs.setValue("Nope!");

    RadioButton<String> fourLegs = FengGUI.<String> createRadioButton();
    fourLegs.setLayoutData(layoutData);
    fourLegs.setText("Four legs");
    fourLegs.setRadioButtonGroup(group);
    fourLegs.setValue("Close!");

    RadioButton<String> oneLeg = FengGUI.<String> createRadioButton();
    oneLeg.setLayoutData(layoutData);
    oneLeg.setText("One leg");
    oneLeg.setRadioButtonGroup(group);
    oneLeg.setValue("Wrong!");

    RadioButton<String> nineLegs = FengGUI.<String> createRadioButton();
    nineLegs.setLayoutData(layoutData);
    nineLegs.setText("Nine legs");
    nineLegs.setRadioButtonGroup(group);
    nineLegs.setValue("Not quite right");

    RadioButton<String> noLegs = FengGUI.<String> createRadioButton();
    noLegs.setLayoutData(layoutData);
    noLegs.setText("No legs");
    noLegs.setRadioButtonGroup(group);
    noLegs.setValue("Not really!");

    RadioButton<String> halfLegs = FengGUI.<String> createRadioButton();
    halfLegs.setLayoutData(layoutData);
    halfLegs.setText("3 1/2 legs");
    halfLegs.setRadioButtonGroup(group);
    halfLegs.setValue("Hmmmmmmm... no!");

    final Label label = FengGUI.createWidget(Label.class);
    label.setLayoutData( new RowExLayoutData(true, true));
    label.setText("Make your choice!");
    label.getAppearance().setAlignment(Alignment.MIDDLE);

    btn.addButtonPressedListener(new IButtonPressedListener()
    {
      public void buttonPressed(ButtonPressedEvent e)
      {
        if (group.getSelectedItem() != null)
          label.setText(group.getSelectedValue().getValue() + " Please try again");
        else
          label.setText("Please make your choice!");
      }
    });

    filesFrame.getContentContainer().addWidget(btn, question, threeLegs, fourLegs, oneLeg, nineLegs, noLegs, halfLegs,
      label);
    filesFrame.pack();
    desk.addWidget(filesFrame);
  }

  public void buildGUI(Display d)
  {
    desk = d;

    buildFrame();
  }

  public String getExampleName()
  {
    return "RadioButton Example";
  }

  public String getExampleDescription()
  {
    return "Frame with RadioButtons";
  }

}
