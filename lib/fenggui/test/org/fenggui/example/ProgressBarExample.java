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
 * $Id: ProgressBarExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.ProgressBar;
import org.fenggui.composite.Window;
import org.fenggui.layout.StaticLayout;

/**
 * Demonstrates the usage of a progress bar.
 * 
 * @author Johannes Schaback 
 */
public class ProgressBarExample implements IExample
{

  private Window  filesFrame = null;
  private Display desk;

  public void buildGUI(Display d)
  {
    desk = d;

    filesFrame = FengGUI.createWindow(desk, true, false, false, true);
    filesFrame.setSize(300, 50);
    filesFrame.setTitle("Progress Bar");

    final ProgressBar pb = FengGUI.createProgressBar(filesFrame.getContentContainer());
    pb.setText("Working");

    Thread t1 = new Thread(new Runnable()
    {

      public void run()
      {
        double value = 0;

        // simulate work
        while (value <= 1)
        {
          value += Math.random() * 0.1;
          pb.setValue(value);
          try
          {
            Thread.sleep(200);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
        }
        pb.setText("Done!");
      }
    });
    t1.start();

    desk.layout();

    StaticLayout.center(filesFrame, desk);

  }

  public String getExampleName()
  {
    return "Progress Bar Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates a Progress Bar";
  }

}
