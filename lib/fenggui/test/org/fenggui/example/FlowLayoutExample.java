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
 * Created on 17.03.2008
 * $Id$
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.composite.Window;
import org.fenggui.layout.FlowLayout;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class FlowLayoutExample implements IExample
{

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#buildGUI(org.fenggui.Display)
   */
  public void buildGUI(Display display)
  {
    Window window = FengGUI.createWindow(display, true, false, false, true);
    window.setTitle(getExampleName());
    window.getContentContainer().setLayoutManager(new FlowLayout());

    for (int i = 0; i < 10; i++)
    {
      FengGUI.createButton(window.getContentContainer(), "Button " + i);
    }
    window.setSize(200, 200);
    window.layout();

  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Example that shows a FlowLayout in action.";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    return "FlowLayout Example";
  }

}
