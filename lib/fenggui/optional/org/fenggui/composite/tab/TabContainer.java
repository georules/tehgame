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
import java.util.ArrayList;
import java.util.List;

import org.fenggui.Container;
import org.fenggui.ObservableWidget;
import org.fenggui.layout.FillLayout;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TabContainer extends Container
{
  private List<AbstractTabItem>  tabs   = new ArrayList<AbstractTabItem>();
  private List<ObservableWidget> header = new ArrayList<ObservableWidget>();
  private Container              tabContainer;
  private Container              headerContainer;

  public TabContainer(Alignment containerAlignment, Alignment headerAlignment)
  {
    super(new RowExLayout(false));
    tabContainer = new Container(new FillLayout());
    headerContainer = new Container();
    setHeaderAlignment(containerAlignment, headerAlignment);
  }

  public void setHeaderAlignment(Alignment containerAlignment, Alignment headerAlignment)
  {
    if (!(containerAlignment == Alignment.BOTTOM || containerAlignment == Alignment.TOP
        || containerAlignment == Alignment.LEFT || containerAlignment == Alignment.RIGHT))
    {
      throw new IllegalArgumentException(
          "Only TOP, RIGHT, BOTTOM and LEFT are valid arguments for containerAlignement.");
    }

    if (headerContainer.isInWidgetTree())
    {
      this.removeWidget(headerContainer);
      this.removeWidget(tabContainer);
    }

    if (containerAlignment == Alignment.TOP)
    {
      this.addWidget(headerContainer);
      this.addWidget(tabContainer);
    }
    else if (containerAlignment == Alignment.BOTTOM)
    {
      this.addWidget(tabContainer);
      this.addWidget(headerContainer);
    }
    else if (containerAlignment == Alignment.LEFT)
    {
      this.addWidget(headerContainer);
      this.addWidget(tabContainer);
    }
    else if (containerAlignment == Alignment.RIGHT)
    {
      this.addWidget(tabContainer);
      this.addWidget(headerContainer);
    }

    setLayoutForAlign(containerAlignment, headerAlignment);
  }

  private void setLayoutForAlign(Alignment containerAlignment, Alignment headerAlignment)
  {
    tabContainer.setLayoutData(new RowExLayoutData(true, true));
    headerContainer.setLayoutData(new RowExLayoutData(false, false, 1.0d, headerAlignment));

    if (containerAlignment == Alignment.BOTTOM || containerAlignment == Alignment.TOP)
    {
      headerContainer.setLayoutManager(new RowExLayout(true));
      this.setLayoutManager(new RowExLayout(false));
    }
    else
    {
      headerContainer.setLayoutManager(new RowExLayout(false));
      this.setLayoutManager(new RowExLayout(true));
    }
  }

  public void setActiveTab(int index)
  {
    tabs.get(index).activateTab(this);
  }

  protected void setAllInvisible()
  {
    for (AbstractTabItem widget : tabs)
    {
      widget.setVisible(false);
    }
  }

  public boolean addTab(AbstractTabItem tab, boolean active)
  {
    boolean result = addTab(tab);
    if (active)
      tab.activateTab(this);

    return result;
  }

  public int getIndexOfTab(AbstractTabItem tab)
  {
    return tabs.indexOf(tab);
  }

  public boolean addTab(AbstractTabItem tab)
  {
    tabContainer.addWidget(tab);
    ObservableWidget head = tab.getHeadWidget();
    headerContainer.addWidget(head);
    tab.hookHeaderEvents(this);
    header.add(head);
    head.setLayoutData(new RowExLayoutData(true, false));
    return tabs.add(tab);
  }

  public boolean removeTab(AbstractTabItem tab)
  {
    ObservableWidget head = tab.getHeadWidget();
    tabContainer.removeWidget(tab);
    headerContainer.removeWidget(head);
    header.remove(head);
    tab.unhookHeaderEvents(this);
    boolean result = tabs.remove(tab);
    return result;
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
    stream.processInherentChild("Header", this.headerContainer);
    stream.processInherentChild("Content", this.tabContainer);
  }
}
