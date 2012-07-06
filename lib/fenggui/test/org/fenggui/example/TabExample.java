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
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.FengGUIOptional;
import org.fenggui.composite.Window;
import org.fenggui.composite.tab.TabContainer;
import org.fenggui.composite.tab.TabItem;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TabExample implements IExample
{

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#buildGUI(org.fenggui.Display)
   */
  public void buildGUI(Display display)
  {
    Window w = FengGUI.createWindow(display, true, false, false, true);
    w.getContentContainer().setLayoutManager(new RowLayout(false));
    w.setTitle(this.getExampleName() + " Left Aligned");

    TabContainer tabContainer1 = FengGUIOptional.createTab(w.getContentContainer(), Alignment.TOP, Alignment.TOP_LEFT);
    tabContainer1.getAppearance().setMargin(new Spacing(0, 0, 5, 0));
    addTabs(tabContainer1);

    TabContainer tabContainer2 = FengGUIOptional
        .createTab(w.getContentContainer(), Alignment.RIGHT, Alignment.TOP_LEFT);
    tabContainer2.getAppearance().setMargin(new Spacing(5, 0, 5, 0));
    addTabs(tabContainer2);

    TabContainer tabContainer3 = FengGUIOptional.createTab(w.getContentContainer(), Alignment.BOTTOM,
      Alignment.TOP_LEFT);
    tabContainer3.getAppearance().setMargin(new Spacing(5, 0, 5, 0));
    addTabs(tabContainer3);

    TabContainer tabContainer4 = FengGUIOptional.createTab(w.getContentContainer(), Alignment.LEFT, Alignment.TOP_LEFT);
    tabContainer4.getAppearance().setMargin(new Spacing(5, 0, 0, 0));
    addTabs(tabContainer4);

    w.setSizeToMinSize();
    w.layout();

    Window w2 = FengGUI.createWindow(display, true, false, false, true);
    w2.getContentContainer().setLayoutManager(new RowLayout(false));
    w2.setTitle(this.getExampleName() + " Right Aligned");

    TabContainer tabContainer21 = FengGUIOptional.createTab(w2.getContentContainer(), Alignment.TOP,
      Alignment.TOP_RIGHT);
    tabContainer21.getAppearance().setMargin(new Spacing(0, 0, 5, 0));
    addTabs(tabContainer21);

    TabContainer tabContainer22 = FengGUIOptional.createTab(w2.getContentContainer(), Alignment.RIGHT,
      Alignment.TOP_RIGHT);
    tabContainer22.getAppearance().setMargin(new Spacing(5, 0, 5, 0));
    addTabs(tabContainer22);

    TabContainer tabContainer23 = FengGUIOptional.createTab(w2.getContentContainer(), Alignment.BOTTOM,
      Alignment.TOP_RIGHT);
    tabContainer23.getAppearance().setMargin(new Spacing(5, 0, 5, 0));
    addTabs(tabContainer23);

    TabContainer tabContainer24 = FengGUIOptional.createTab(w2.getContentContainer(), Alignment.LEFT,
      Alignment.TOP_RIGHT);
    tabContainer24.getAppearance().setMargin(new Spacing(5, 0, 0, 0));
    addTabs(tabContainer24);

    w2.setSizeToMinSize();
    w2.layout();

  }

  private void addTabs(TabContainer tabContainer)
  {
    TabItem tab1 = FengGUIOptional.createTabItem();
    tab1.getHeadWidget().setText("Tab1");
    FengGUI.createLabel(tab1, "New Tab1 Text\nContent Text.");

    TabItem tab2 = FengGUIOptional.createTabItem();
    tab2.getHeadWidget().setText("Tab1");
    FengGUI.createButton(tab2, "button 1");
    FengGUI.createButton(tab2, "button 2");
    FengGUI.createButton(tab2, "button 3");
    FengGUI.createButton(tab2, "button 4");

    TabItem tab3 = FengGUIOptional.createTabItem();
    tab3.getHeadWidget().setText("Tab1");
    FengGUI.createCheckBox(tab3, "check 1");
    FengGUI.createCheckBox(tab3, "check 2");

    tabContainer.addTab(tab1);
    tabContainer.addTab(tab2);
    tabContainer.addTab(tab3);
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Shows the TabContainer in action";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    return "Tab Example";
  }

}
