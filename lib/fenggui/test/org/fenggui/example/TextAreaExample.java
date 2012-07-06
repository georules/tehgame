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
 * $Id: TextAreaExample.java 621 2009-04-13 12:46:43Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.CheckBox;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;

/**
 * Displays a text area with a rather stupid text.
 * @author Johannes Schaback ($Author: marcmenghin $)
 */
public class TextAreaExample implements IExample
{

  private Window  filesFrame = null;
  private Display display;

  private void buildOtherFrame(Display display)
  {
    Window w = FengGUI.createDialog(display, "Different Frame");
    w.setTitle("Different Frame");

    w.setSize(200, 200);
    w.setXY(100, 150);

    ScrollContainer sc = FengGUI.createScrollContainer(w.getContentContainer());

    TextEditor te = FengGUI.createTextEditor(sc);
    te.setText("Not long ago and not far away there was a beautiful, big teddy bear who sat on a shelf in a\n"
        + "drug store waiting for someone to buy him and give him a home. His name was Wolstencroft. And \n"
        + "he was no ordinary bear. His fur was a lovely shade of light grey, and he had honey colored ears, \n"
        + "nose and feet. His eyes were warm and kind and he had a wonderfully wise look on his face. \n"
        + "Wolstencroft looked very smart in a brown plaid waistcoat with a gold satin bow tie at his \n"
        + "neck. Attached to the tie was a tag with his name written in bold, black letters: Wolstencroft.\n"
        + "He had arrived in the store just before Christmas when there had been a lovely big tree in the \n"
        + "window, all decorated with fairy lights. Yards and yards of sparkling tinsel had been draped \n"
        + "over everything, and holiday music had been playing all the time. Wolstencroft was especially \n"
        + "fond of Jingle Bells. He liked its light, tinkling sounds. It always made him feel merry. At \n"
        + "that time there had been lots of other bears to keep him company. In fact, there had been so \n"
        + "many teddy bears crowded onto that one narrow shelf that he had scarcely had room to move. \n"
        + "But, one by one they had all gone. Gleefully waving goodbye as they were carried off to their \n"
        + "new homes. Until finally, he was the only teddy bear left in the entire store.");

  }

  private void buildFileFrame()
  {

    filesFrame = FengGUI.createDialog(display, "TextArea Example");
    filesFrame.setX(50);
    filesFrame.setY(50);
    filesFrame.setSize(300, 200);

    filesFrame.getContentContainer().setLayoutManager(new RowExLayout(false));

    ScrollContainer sc = FengGUI.createScrollContainer(filesFrame.getContentContainer());
    sc.setLayoutData(new RowExLayoutData(true, true));

    final TextEditor textArea = FengGUI.createTextEditor(sc);
    //		final String text = "";
    //		final String text = "Hello there, this is super long ultra cool text to test things. Some Super long text to test stuff here and there. wow this text gets sooo long you almost cant beleave it. wounderful things can happen at the end of this string. weeeee. ";
    //		final String text = "Hello World!\n\nend";
    //		String text = "s\n\n\n\n\ne";
    //		for (int i = 0; i < 3; i++)
    //			text += text;

    //		String text1 = "MNBVCXYASD";
    //		String text2 = "";
    //		for (int i = 0; i < 90; i++)
    //			text2 += text1;

    //		System.out.println("Text: " + text.length());

    final String text = "Hello!\nThis is a text!\nPlease do this and that!\n"
        + "Alphabet: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\n\n\n" + "And make me more creative.\n"
        + "\nIncrease Decrease\n" + "Yey! and now two line breaks.\n\n" + "And another one!\n"
        + "This is a really really really long line of dumb text and other mind waste.\n\n"
        + "Once upon a time there was a little TextArea. Every evening, just before it had to "
        + "go to bed it asked his parent Container: \"Mom, when do I become a real Widget?\" "
        + "and the mother answered \"give it time, my son\" but thar hardly satisfied the small "
        + "one. So one day it decided to leave home and try to find the asnwers it was searching for. "
        + "But that was easier said than done because the NotifyList Listener guarded the parents door. "
        + "To see how this story end, please send 100 Euros to Johannes' bank account. It will be much "
        + "appreciated and is for a good purpose. Charity begins at home.";
    textArea.setText(text);

    Container container = FengGUI.createContainer(filesFrame.getContentContainer());
    container.setLayoutManager(new RowExLayout(true));
    container.setLayoutData(new RowExLayoutData(true, false));

    final CheckBox<Integer> multilineCheckBox = FengGUI.<Integer> createCheckBox(container, "Multiline");
    multilineCheckBox.setSelected(false);
    multilineCheckBox.setLayoutData(new RowExLayoutData(false, false));
    multilineCheckBox.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        textArea.setMultiline(selectionChangedEvent.isSelected());
      }

    });

    Label label = FengGUI.createLabel(container, "");
    label.setLayoutData(new RowExLayoutData(true, true));

    final CheckBox<Integer> readOnlyCheckBox = FengGUI.<Integer> createCheckBox(container, "ReadOnly");
    readOnlyCheckBox.setSelected(false);
    readOnlyCheckBox.setLayoutData(new RowExLayoutData(false, false));
    readOnlyCheckBox.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        textArea.setReadonly(selectionChangedEvent.isSelected());
      }

    });

    Label label1 = FengGUI.createLabel(container, "");
    label1.setLayoutData(new RowExLayoutData(true, true));

    final CheckBox<Integer> wordwarpCheckBox = FengGUI.<Integer> createCheckBox(container, "Word-Warp");
    wordwarpCheckBox.setSelected(false);
    wordwarpCheckBox.setLayoutData(new RowExLayoutData(false, false));
    wordwarpCheckBox.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        textArea.setWordWarping(selectionChangedEvent.isSelected());
      }

    });

    Container container2 = FengGUI.createContainer(filesFrame.getContentContainer());
    container2.setLayoutManager(new RowExLayout(true));
    container2.setLayoutData(new RowExLayoutData(true, false));

    
    Button btn = FengGUI.createWidget(Button.class);
    btn.setText("reset text");
    btn.setLayoutData(new RowExLayoutData(true, true));
    btn.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        textArea.setText(text);
      }

    });

    Button btn2 = FengGUI.createWidget(Button.class);
    btn2.setText("add to end");
    btn2.setLayoutData(new RowExLayoutData(true, true));
    btn2.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        textArea.addContentAtEnd("New Text At end\neven with a second line here");
      }

    });
    
    Button btn3 = FengGUI.createWidget(Button.class);
    btn3.setText("add to beginning");
    btn3.setLayoutData(new RowExLayoutData(true, true));
    btn3.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        textArea.addContentAtBeginning("New Text At beginning\neven with a second line here");
      }

    });
    
    container2.addWidget(btn, btn2, btn3);

    
    filesFrame.layout();
  }

  public void buildGUI(Display g)
  {
    display = g;
    //		buildOtherFrame(display);
    buildFileFrame();
  }

  public String getExampleName()
  {
    return "Text Area Example";
  }

  public String getExampleDescription()
  {
    return "Text Area Example";
  }

}
