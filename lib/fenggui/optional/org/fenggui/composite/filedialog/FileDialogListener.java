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
package org.fenggui.composite.filedialog;

import java.io.File;

/**
 * This interface is main way to find out when the user has selected a file, or cancelled the dialog.
 * If this dialog is not attached to a FileDialogWindow, then steps should be taken on recieving
 * an event to hide this dialog (the FileDialogWindow will do this itself).
 * 
 * @author Sam Bayless
 *
 */
public interface FileDialogListener
{
  public void fileSelected(File file);

  public void cancel();
}