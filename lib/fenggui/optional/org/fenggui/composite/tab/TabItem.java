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
 * Created on 03.01.2008
 * $Id$
 */
package org.fenggui.composite.tab;

import java.io.IOException;

import org.fenggui.RadioButton;
import org.fenggui.ToggableGroup;
import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.mouse.IMouseListener;
import org.fenggui.event.mouse.MouseAdapter;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TabItem extends AbstractTabItem
{
  private RadioButton<Integer> button;
  private TabContainer         container = null;

  public TabItem(TabItem tabItem)
  {
    super(tabItem);
    button = new RadioButton<Integer>(tabItem.button);
  }

  public TabItem()
  {
    button = new RadioButton<Integer>();
  }

  public TabItem(ToggableGroup<RadioButton<Integer>> group)
  {
    button = new RadioButton<Integer>(group);
  }

  public void setGroup(ToggableGroup<RadioButton<Integer>> group)
  {
    button.setRadioButtonGroup(group);
  }

  private IMouseListener mouseListener = new MouseAdapter()
                                       {

                                         public void mouseReleased(MouseReleasedEvent mouseReleasedEvent)
                                         {
                                           activateTab(container);
                                         }

                                       };

  public IKeyListener    keyListener   = new KeyAdapter()
                                       {

                                         public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                                         {
                                           activateTab(container);
                                         }

                                       };

  @Override
  public RadioButton<Integer> getHeadWidget()
  {
    if (button == null)
      button = new RadioButton<Integer>();

    return button;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.Container#process(org.fenggui.theme.xml.InputOutputStream)
   */
  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
    button = stream.processChild("TabButton", button, button, RadioButton.class);
  }

  @Override
  public void hookHeaderEvents(TabContainer container)
  {
    this.container = container;
    button.addMouseListener(mouseListener);
    button.addKeyListener(keyListener);
  }

  @Override
  public void unhookHeaderEvents(TabContainer container)
  {
    button.removeMouseListener(mouseListener);
    button.removeKeyListener(keyListener);
    this.container = null;
  }

  @Override
  public void activateTab(TabContainer container)
  {
    this.container = container;
    if (container == null)
      return;

    container.setAllInvisible();
    button.setSelected(true);
    setVisible(true);
  }
}
