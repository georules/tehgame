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
 * $Id: TabItemLabel.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui;

import org.fenggui.event.FocusEvent;
import org.fenggui.event.IFocusListener;
import org.fenggui.event.key.IKeyPressedListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.mouse.*;

/**
 * Special label used for tabs in <code>TabContainer</code>s. It is a special widget
 * so that it can be adjusted through themes.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class TabItemLabel extends ObservableLabelWidget
{
  private TabContainer       tabContainer      = null;

  public static final String LABEL_DEFAULT     = "default";
  public static final String LABEL_MOUSE_HOVER = "mouseHover";
  public static final String LABEL_FOCUSED     = "active";

  public TabItemLabel(TabContainer tabContainer)
  {
    this.tabContainer = tabContainer;

    buildBehavior();
    setTraversable(true);

    getAppearance().setEnabled(LABEL_DEFAULT, true);
    getAppearance().setEnabled(LABEL_FOCUSED, false);
    getAppearance().setEnabled(LABEL_MOUSE_HOVER, false);

  }

  void buildBehavior()
  {
    final TabItemLabel THIZZ = this;

    addMouseEnteredListener(new IMouseEnteredListener()
    {

      public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
      {
        getAppearance().setEnabled(LABEL_MOUSE_HOVER, true);
      }

    });

    addMouseExitedListener(new IMouseExitedListener()
    {

      public void mouseExited(MouseExitedEvent mouseExited)
      {
        getAppearance().setEnabled(LABEL_MOUSE_HOVER, false);
        /*
        if(!THIZZ.equals(tabContainer.getSelectedTabLabel()))
        	getAppearance().setEnabled(LABEL_ACTIVE, false);
        	*/
      }

    });

    addMousePressedListener(new IMousePressedListener()
    {
      public void mousePressed(MousePressedEvent mousePressedEvent)
      {
        if (mousePressedEvent.getButton() == MouseButton.LEFT)
          tabContainer.selectTab(THIZZ);
        else if (getDisplay() != null)
          getDisplay().setFocusedWidget(null);
      }
    });

    addKeyPressedListener(new IKeyPressedListener()
    {

      public void keyPressed(KeyPressedEvent k)
      {
        if (k.getKey() == ' ' || k.getKeyClass() == Key.ENTER)
        {
          tabContainer.selectTab(THIZZ);
          getDisplay().setFocusedWidget(tabContainer.getSelectedTabWidget());
        }
      }
    });

    addFocusListener(new IFocusListener()
    {

      public void focusChanged(FocusEvent f)
      {
        if (f.isFocusGained())
        {
          getAppearance().setEnabled(LABEL_FOCUSED, true);
        }
        else
        {
          if (!THIZZ.equals(tabContainer.getSelectedTabLabel()))
            getAppearance().setEnabled(LABEL_FOCUSED, false);
        }
      }
    });
  }

}
