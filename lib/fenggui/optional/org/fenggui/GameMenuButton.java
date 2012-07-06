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
 * Created on 30.01.2007
 * $Id$
 */
package org.fenggui;

import java.io.IOException;

import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.util.Dimension;

/**
 * A simple way to create a game menu.
 * 
 * @author charlierby, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class GameMenuButton extends Button
{
  Dimension size = new Dimension(5, 5);

  /**
   * Constructs a new GameMenuButton. Needs to be called in the OpenGL thread.
   * 
   * @param lowlightFile
   * @param highlightFile
   * @param focusFile
   */
  public GameMenuButton(String lowlightFile, String highlightFile, String focusFile)
  {
    try
    {

      Pixmap lowlight = new Pixmap(Binding.getInstance().getTexture(lowlightFile));
      Pixmap highlight = new Pixmap(Binding.getInstance().getTexture(highlightFile));
      Pixmap focus = new Pixmap(Binding.getInstance().getTexture(focusFile));

      initButton(lowlight, highlight, focus, focus, lowlight);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Constructs a new GameMenuButton. Needs to be called in the OpenGL thread.
   * 
   * @param lowlightFile
   * @param highlightFile
   */
  public GameMenuButton(String lowlightFile, String highlightFile)
  {
    try
    {

      Pixmap lowlight = new Pixmap(Binding.getInstance().getTexture(lowlightFile));
      Pixmap highlight = new Pixmap(Binding.getInstance().getTexture(highlightFile));

      // this solution doesn't scale
      // getAppearance().add(new SetPixmapSwitch(STATE_DEFAULT, lowlight));
      // getAppearance().add(new SetPixmapSwitch(STATE_HOVERED, highlight));
      // getAppearance().add(new SetPixmapSwitch(STATE_FOCUSED, highlight));

      initButton(lowlight, highlight, highlight, highlight, lowlight);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Constructs a new GameMenuButton.
   * 
   * @param defaultState
   * @param hoverState
   * @param focusState
   */
  public GameMenuButton(Pixmap defaultState, Pixmap hoverState, Pixmap focusState, Pixmap pressedState, Pixmap disabledState)
  {
    initButton(defaultState, hoverState, focusState, pressedState, disabledState);
  }

  private void initButton(Pixmap defaultState, Pixmap hoverState, Pixmap focusState, Pixmap pressedState,
      Pixmap disabledState)
  {
    getAppearance().removeAll();
    getAppearance().clearSpacings();

    size.setSize(defaultState.getWidth(), defaultState.getHeight());

    getAppearance().add(STATE_NONE, new PixmapBackground(defaultState, true));
    getAppearance().add(STATE_HOVERED, new PixmapBackground(hoverState, true));
    getAppearance().add(STATE_FOCUSED, new PixmapBackground(focusState, true));
    getAppearance().add(STATE_PRESSED, new PixmapBackground(pressedState, true));
    getAppearance().add(STATE_DISABLED, new PixmapBackground(disabledState, true));
    updateState();
    updateMinSize();
  }

  @Override
  public Dimension getMinContentSize()
  {
    return size;
  }

  public void setMinContentSize(Dimension size)
  {
    this.size = size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.fenggui.StatefullWidget#updateState(java.lang.String)
   */
  @Override
  protected void updateState(String newActiveState)
  {
    // change update state to only activate one state at a time
    // only default state is active as usual
    // re-enable default state so switches get called
    getAppearance().setEnabled(STATE_DEFAULT, true);

    if (newActiveState != null)
    {
      getAppearance().setEnabled(STATE_DISABLED, false);
      getAppearance().setEnabled(STATE_FOCUSED, false);
      getAppearance().setEnabled(STATE_NONE, false);
      getAppearance().setEnabled(STATE_ERROR, false);
      getAppearance().setEnabled(STATE_HOVERED, false);
      getAppearance().setEnabled(STATE_PRESSED, false);
      getAppearance().setEnabled(newActiveState, true);
    }
    else
    {
      if (isEnabled())
      {
        if (hasError())
        {
          getAppearance().setEnabled(STATE_ERROR, true);
          getAppearance().setEnabled(STATE_DISABLED, false);
          getAppearance().setEnabled(STATE_FOCUSED, false);
          getAppearance().setEnabled(STATE_NONE, false);
          getAppearance().setEnabled(STATE_HOVERED, false);
          getAppearance().setEnabled(STATE_PRESSED, false);
        }
        else
        {
          if (isPressed())
          {
            getAppearance().setEnabled(STATE_DISABLED, false);
            getAppearance().setEnabled(STATE_FOCUSED, false);
            getAppearance().setEnabled(STATE_NONE, false);
            getAppearance().setEnabled(STATE_ERROR, false);
            getAppearance().setEnabled(STATE_HOVERED, false);
            getAppearance().setEnabled(STATE_PRESSED, true);
          }
          else
          {
            if (isFocused())
            {
              getAppearance().setEnabled(STATE_DISABLED, false);
              getAppearance().setEnabled(STATE_FOCUSED, true);
              getAppearance().setEnabled(STATE_NONE, false);
              getAppearance().setEnabled(STATE_ERROR, false);
              getAppearance().setEnabled(STATE_HOVERED, false);
              getAppearance().setEnabled(STATE_PRESSED, false);
            }
            else
            {
              if (isHovered())
              {
                getAppearance().setEnabled(STATE_DISABLED, false);
                getAppearance().setEnabled(STATE_FOCUSED, false);
                getAppearance().setEnabled(STATE_NONE, false);
                getAppearance().setEnabled(STATE_ERROR, false);
                getAppearance().setEnabled(STATE_PRESSED, false);
                getAppearance().setEnabled(STATE_HOVERED, true);
              }
              else
              {
                getAppearance().setEnabled(STATE_DISABLED, false);
                getAppearance().setEnabled(STATE_FOCUSED, false);
                getAppearance().setEnabled(STATE_NONE, true);
                getAppearance().setEnabled(STATE_ERROR, false);
                getAppearance().setEnabled(STATE_HOVERED, false);
                getAppearance().setEnabled(STATE_PRESSED, false);
              }
            }
          }
        }
      }
      else
      {
        getAppearance().setEnabled(STATE_DISABLED, true);
        getAppearance().setEnabled(STATE_FOCUSED, false);
        getAppearance().setEnabled(STATE_ERROR, false);
        getAppearance().setEnabled(STATE_NONE, false);
        getAppearance().setEnabled(STATE_PRESSED, false);
        getAppearance().setEnabled(STATE_HOVERED, false);
      }

    }
  }
}
