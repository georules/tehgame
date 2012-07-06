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

import org.fenggui.Container;
import org.fenggui.ObservableWidget;

/**
 * An abstract Tab. A Tabheader widget is provided here as well as the Tab itself as a
 * Container.
 * 
 * @see TabItem
 * @see TabContainer
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public abstract class AbstractTabItem extends Container
{

  public AbstractTabItem()
  {

  }

  public AbstractTabItem(AbstractTabItem tabItem)
  {
    super(tabItem);
  }

  /**
   * The widget that should be used as the tab Header. This is always shown.
   * 
   * @return Widget
   */
  public abstract ObservableWidget getHeadWidget();

  /**
   * Called if this tab is activated. This should also activate the HeadWidget as the
   * container doesn't know enough to do so.
   */
  public abstract void activateTab(TabContainer container);

  /**
   * The container doesn't handle tab activation. So the tab itself has to do all the
   * needed things. This method is called when the tab is added to the container.
   * 
   * @param container
   */
  public abstract void hookHeaderEvents(TabContainer container);

  /**
   * This method is called when the tab is removed from the container.
   * 
   * @param container
   */
  public abstract void unhookHeaderEvents(TabContainer container);
}
