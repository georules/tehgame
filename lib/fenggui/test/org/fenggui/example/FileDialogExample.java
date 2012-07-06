/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005-2009 FengGUI Project
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
 * Created on Mar 22, 2009
 * $Id$
 */
package org.fenggui.example;

import java.io.File;

import org.fenggui.Display;
import org.fenggui.composite.filedialog.FileDialogWindow;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class FileDialogExample implements IExample
{
  Display display = null;

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#buildGUI(org.fenggui.Display)
   */
  public void buildGUI(Display display)
  {
   
   
   FileDialogWindow wnd = FileDialogWindow.displayFileDialog(display);
   wnd.getDialog().setCurrentDirectory(new File("."));
   wnd.getDialog().addFileFilter("All Files", ".*");
   wnd.getDialog().addFileFilter("Image Files", ".*[\\.png|\\.bmp|\\.jpg|\\.gif|\\.tga]");
   wnd.getDialog().addFileFilter("Text Files", ".*[\\.txt]");
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Demonstrates the use of the FileDialog.";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    // TODO Auto-generated method stub
    return "FileDialog Example";
  }
  
}
