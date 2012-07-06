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
 * $Id: ITableModel.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite.table;

import org.fenggui.IModel;
import org.fenggui.Item;
import org.fenggui.appearance.LabelAppearance;

/**
 * Specifies methods used by <code>Table</code> to query data.
 * 
 * @author Johannes Schaback
 *
 */
public interface ITableModel extends IModel
{

  /**
   * Returns the name of the column
   * @param columnIndex the index of the column (starting at 0).
   * @return the name
   */
  public String getColumnName(int columnIndex);

  /**
   * Returns the number of coumns in the table
   * @return the number of columns
   */
  public int getColumnCount();

  /**
   * Returns the value of a cell.
   * @param row the row of the cell
   * @param column the column of the cell
   * @return value
   */
  public Item getItem(int column, int row, LabelAppearance appearance);

  /**
   * Returns the number of rows.
   * @return rows
   */
  public int getRowCount();

}
