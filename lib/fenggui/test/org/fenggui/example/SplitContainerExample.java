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
 * $Id: SplitContainerExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */

package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.SplitContainer;
import org.fenggui.composite.Window;

/**
 * Demonstrates the usage of a <code>SplitContainer</code>.
 * 
 * @author Johannes Schaback
 */
public class SplitContainerExample implements IExample
{
  public void buildGUI(Display display)
  {
    Window cont = FengGUI.createDialog(display);
    cont.setSize(300, 300);
    cont.setTitle("SplitContainer Example");
    SplitContainer centerSC = FengGUI.createSplitContainer(cont.getContentContainer(), true);

    SplitContainer northSC = FengGUI.createSplitContainer(false);
    SplitContainer southSC = FengGUI.createSplitContainer(false);

    Button northWestBtn = FengGUI.createButton("North-West");
    Button northEastBtn = FengGUI.createButton("North-East");

    Button southEastBtn = FengGUI.createButton("South-East");
    Button southWestBtn = FengGUI.createButton("South-West");

    centerSC.setFirstWidget(southSC);
    centerSC.setSecondWidget(northSC);

    northSC.setFirstWidget(northWestBtn);
    northSC.setSecondWidget(northEastBtn);

    southSC.setFirstWidget(southWestBtn);
    southSC.setSecondWidget(southEastBtn);

    cont.layout();
  }

  public String getExampleName()
  {
    return "SplitContainer Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates a SplitContainer";
  }

}
