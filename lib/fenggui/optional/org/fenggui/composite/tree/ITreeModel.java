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
 * $Id: ITreeModel.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite.tree;

import org.fenggui.IModel;
import org.fenggui.binding.render.Pixmap;

/**
 * Interface to access the tree data model.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public interface ITreeModel<E> extends IModel
{
  /**
   * Returns the number of children the given tree node has.
   * @param node the node
   * @return number of children
   */
  public int getNumberOfChildren(E node);

  /**
   * Returns the pixmap of the given tree node.
   * @param node the node
   * @return pixmap
   */
  public Pixmap getPixmap(E node);

  /**
   * Returns the child node at the specified index of the given parent.
   * @param parent the parent
   * @param index the index
   * @return child node
   */
  public E getNode(E parent, int index);

  /**
   * Returns the text of the specified node.
   * @param node the node
   * @return the text
   */
  public String getText(E node);

  /**
   * Returns the root of the tree data model.
   * @return the root
   */
  public E getRoot();

  /**
   * Returns true if the node has children.
   * 
   * @param node
   * @return
   */
  public boolean hasChildren(E node);
}
