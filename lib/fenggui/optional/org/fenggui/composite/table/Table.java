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
 * $Id: Table.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.fenggui.IToggable;
import org.fenggui.Item;
import org.fenggui.ModelWidget;
import org.fenggui.ToggableGroup;
import org.fenggui.appearance.EntryAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.util.Dimension;

/**
 * Implementation of a table or Item objects.
 * 
 * @author Johannes, Rainer last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 * @dedication Life of Agony - Weeds
 */
public class Table extends ModelWidget<EntryAppearance, ITableModel> implements Observer
{
  private List<TableRow>          rows                = new ArrayList<TableRow>();
  private ToggableGroup<TableRow> selectionGroup      = new ToggableGroup<TableRow>();
  private TableRow                hoveredRow          = null;
  private ColumnMetaData[]        columnMetaDataCache = new ColumnMetaData[0];

  private class ColumnMetaData
  {
    double weight      = 1;
    int    mouseX      = 0;
    int    width       = 0;
    int    cachedWidth = 0;
  }

  /**
   * Creates a new Table.
   */
  public Table()
  {
    super();
    setAppearance(new EntryAppearance(this));
    updateMinSize();
  }

  public void setColumnWeights(double... weights)
  {
    int count = columnMetaDataCache.length > weights.length ? columnMetaDataCache.length : weights.length;

    ColumnMetaData[] newData = new ColumnMetaData[count];

    for (int i = 0; i < count; i++)
    {
      newData[i] = new ColumnMetaData();
      if (i < weights.length)
        newData[i].weight = weights[i];
      if (i < columnMetaDataCache.length)
      {
        newData[i].mouseX = columnMetaDataCache[i].mouseX;
        newData[i].width = columnMetaDataCache[i].width;
        newData[i].cachedWidth = columnMetaDataCache[i].cachedWidth;
      }
    }

    columnMetaDataCache = newData;
    this.layout();
  }

  public void setColumnCount(int count)
  {
    ColumnMetaData[] newData = new ColumnMetaData[count];
    for (int i = 0; i < count; i++)
    {
      if (i < columnMetaDataCache.length)
        newData[i] = columnMetaDataCache[i];
      else
        newData[i] = new ColumnMetaData();
    }

    columnMetaDataCache = newData;
    this.layout();
  }

  public void addRow(Item... items)
  {
    addRow(rows.size(), items);
  }

  public void addRow(int index, Item... items)
  {
    if (items.length != columnMetaDataCache.length)
      throw new IllegalArgumentException("Wrong column count. (expected: " + columnMetaDataCache.length + ", was: "
          + items.length + ")");

    TableRow row = new TableRow(columnMetaDataCache.length);
    for (int i = 0; i < columnMetaDataCache.length; i++)
      row.items.add(items[i]);
    rows.add(index, row);
    updateMinSize();
  }

  public void removeRow(int row)
  {
    if (rows.size() <= row || row < 0)
      throw new IllegalArgumentException("Table doesn't container this row. (" + row + ")");

    rows.remove(row);
    updateMinSize();
  }

  public int getRowCount()
  {
    return rows.size();
  }

  public int getColumnCount()
  {
    return columnMetaDataCache.length;
  }

  public void clear()
  {
    rows.clear();
    updateMinSize();
  }

  public int getRowIndexOfItem(Item item)
  {
    int index = 0;
    for (TableRow row : rows)
    {
      if (row.items.contains(item))
        return index;
      index++;
    }

    return -1;
  }

  public void setSelected(int index, boolean b)
  {
    if (index < 0 || index > rows.size())
      return;

    this.selectionGroup.setSelected(this, rows.get(index), b);
  }

  public void updateFromModel()
  {
    buildTableFromModel();
  }

  public boolean isSelected(int row)
  {
    if (row >= 0 && row < rows.size())
      return selectionGroup.isSelected(rows.get(row));
    else
      return false;
  }

  private void buildTableFromModel()
  {
    rows.clear();
    ITableModel model = getModel();

    if (model != null)
    {
      rows = new ArrayList<TableRow>(model.getRowCount());
      for (int i = 0; i < model.getRowCount(); i++)
      {
        TableRow row = new TableRow(model.getColumnCount());
        for (int k = 0; k < model.getColumnCount(); k++)
        {
          row.items.add(model.getItem(k, i, getAppearance()));
        }
        rows.add(row);
        row.updateSize();
      }
    }
    else
    {
      rows = new ArrayList<TableRow>();
    }
    updateMinSize();
    layout();
  }

  public int getSelectionCount()
  {
    return selectionGroup.getSelectedItemsCount();
  }

  /**
   * Define the width in pixel a the specified column
   * 
   * @param columnIndex
   *            Index of the column
   * @param widthInPixel
   *            Width in pixel
   */
  public void setColumnWidth(int columnIndex, int widthInPixel)
  {
    if (columnIndex < 0 || columnIndex >= columnMetaDataCache.length)
      return;

    this.columnMetaDataCache[columnIndex].width = widthInPixel;
    this.columnMetaDataCache[columnIndex].weight = Double.NaN;
    this.layout();
  }

  /**
   * Define relative width (in %) to the specified column
   * 
   * @param columnIndex
   *            Index of the column
   * @param relativeWidth
   *            Relative width [0, 1]
   */
  public void setColumnWidth(int columnIndex, float relativeWidth)
  {
    if (columnIndex < 0 || columnIndex >= columnMetaDataCache.length)
      return;

    this.columnMetaDataCache[columnIndex].width = 1;
    this.columnMetaDataCache[columnIndex].weight = relativeWidth;
    this.layout();
  }

  /**
   * Deselects all entries in the table
   * 
   */
  public void clearSelection()
  {
    selectionGroup.clearSelection(this);
  }

  @Override
  public void layout()
  {
    //calculate meta data (fixed space, weights, ..)
    int fixedWidth = 0;
    double fullWeight = 0D;

    for (ColumnMetaData data : columnMetaDataCache)
    {
      if (data.weight != Double.NaN)
      {
        fullWeight += data.weight;
      }
      else
      {
        fixedWidth += data.width;
        data.cachedWidth = fixedWidth;
      }
    }

    int weightedWidth = getAppearance().getContentWidth() - fixedWidth;
    if (weightedWidth < 0)
      weightedWidth = 0;

    double weightRoot = weightedWidth / fullWeight;

    for (ColumnMetaData data : columnMetaDataCache)
    {
      if (data.weight != Double.NaN)
      {
        data.cachedWidth = (int) Math.floor(weightRoot * data.weight);
      }
    }
  }

  /**
   * Returns the index of the first row that is selected. Should be used for
   * getting the index when using single selection. The actual row can then be
   * retrieved by getting the value of the index in the table model.
   * 
   * @return value
   */
  public int getSelection()
  {
    return rows.indexOf(selectionGroup.getSelectedItem());
  }

  public void setNumberOfSelectableRows(int number)
  {
    selectionGroup.setNumberOfSelectableItems(number);
  }

  private TableRow isOnColumn(int y)
  {
    int currentY = this.getAppearance().getContentHeight();

    for (TableRow row : rows)
    {
      currentY -= row.getPreferredSize().getHeight();
      if (currentY <= y)
        return row;
    }

    return null;
  }

  public int getColumnWidth(int columnIndex)
  {
    if (columnIndex < 0 || columnIndex >= this.columnMetaDataCache.length)
      return 0;
    else
      return this.columnMetaDataCache[columnIndex].cachedWidth;
  }

  @Override
  public Dimension getMinContentSize()
  {
    int width = 0, height = 0;

    for (TableRow row : rows)
    {
      Dimension rowSize = row.getPreferredSize();
      width = rowSize.getWidth() > width ? rowSize.getWidth() : width;
      height += rowSize.getHeight();
    }

    return new Dimension(width, height);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    int currentY = this.getAppearance().getContentHeight();

    for (TableRow row : rows)
    {
      currentY -= row.getPreferredSize().getHeight();
      paintRowContent(row, 0, currentY, this.getAppearance().getContentWidth(), row.getPreferredSize().getHeight(),
        this.hoveredRow, getAppearance(), g);
    }
  }

  protected void paintRowContent(TableRow row, int x, int y, int width, int height, TableRow hoveredRow,
      EntryAppearance appearance, Graphics g)
  {
    if (row == hoveredRow)
    {
      g.setColor(appearance.getHoverColor());
      appearance.getHoverUnderlay().paint(g, x, y, width, height);
    }

    if (selectionGroup.isSelected(row))
    {
      g.setColor(appearance.getSelectionColor());
      appearance.getSelectionUnderlay().paint(g, x, y, width, height);
    }

    g.setColor(appearance.getColor());

    int currentX = x;
    int column = 0;
    for (Item item : row.items)
    {
      if (hoveredRow != null)
        item
            .render(currentX, y, columnMetaDataCache[column].cachedWidth, height, hoveredRow.hoveredItem, appearance, g);
      else
        item.render(currentX, y, columnMetaDataCache[column].cachedWidth, height, null, appearance, g);
      currentX += columnMetaDataCache[column].cachedWidth;
      column++;
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    if (hoveredRow != null)
      selectionGroup.setSelected(this, hoveredRow, true);

    super.mouseClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseMoved(int, int)
   */
  @Override
  public void mouseMoved(int displayX, int displayY)
  {
    hoveredRow = this.isOnColumn(displayY - this.getDisplayY());

    super.mouseMoved(displayX, displayY);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ModelWidget#ModelUpdated(org.fenggui.IModel)
   */
  @Override
  protected void ModelUpdated(ITableModel model, boolean newModel)
  {
    if (newModel)
    {
      if (model != null)
      {
        setColumnCount(model.getColumnCount());
      }
      else
      {
        setColumnCount(0);
      }
    }

    updateFromModel();
  }

  /**
   * Data Container for rows of the table
   * 
   * @author Marc Menghin, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
   * @version $Revision: 606 $
   */
  private class TableRow implements IToggable<TableRow>
  {
    public List<Item> items;
    public Item       hoveredItem;
    private boolean   selected = false;
    private Dimension size     = new Dimension(0, 0);

    public TableRow(int size)
    {
      items = new ArrayList<Item>(size);
    }

    /* (non-Javadoc)
     * @see org.fenggui.IToggable#isSelected()
     */
    public boolean isSelected()
    {
      return selected;
    }

    /* (non-Javadoc)
     * @see org.fenggui.IToggable#setSelected(boolean)
     */
    public TableRow setSelected(boolean b)
    {
      selected = b;
      return this;
    }

    public Dimension getPreferredSize()
    {
      return size;
    }

    public void updateSize()
    {
      int width = 0, height = 0;
      for (Item i : items)
      {
        Dimension iSize = i.getPreferredSize();
        width += iSize.getWidth();
        height = iSize.getHeight() > height ? iSize.getHeight() : height;
      }

      size.setSize(width, height);
    }

  }
}