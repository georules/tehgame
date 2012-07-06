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
 * $Id: TableExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */

package org.fenggui.example;

import org.fenggui.Button;
import org.fenggui.CheckBox;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.FengGUIOptional;
import org.fenggui.Item;
import org.fenggui.ScrollContainer;
import org.fenggui.ToggableGroup;
import org.fenggui.appearance.LabelAppearance;
import org.fenggui.composite.Window;
import org.fenggui.composite.table.ITableModel;
import org.fenggui.composite.table.Table;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.GridLayout;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Spacing;

/**
 * Example class for tables.
 * 
 * 
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class TableExample implements IExample
{

  private static final long serialVersionUID = 1L;

  private Window            window           = null;
  private Display           desk;

  @SuppressWarnings("unchecked")
  private void buildTableFrame()
  {

    window = FengGUI.createDialog(desk, "Table Test");
    window.setX(50);
    window.setY(50);
    window.setSize(400, 300);

    final ScrollContainer sc = FengGUI.createScrollContainer(window.getContentContainer());
    sc.setLayoutData(BorderLayoutData.CENTER);
    window.getContentContainer().setLayoutManager(new BorderLayout());

    final Table table = FengGUIOptional.createTable(sc);

    table.setModel(new MyTableModel());

    Container buttons = FengGUI.createContainer();
    buttons.setLayoutData(BorderLayoutData.SOUTH);
    buttons.setLayoutManager(new GridLayout(3, 2));
    window.getContentContainer().addWidget(buttons);

    Button createTableModelButton = FengGUI.createButton("Generate a New Table Model");
    createTableModelButton.getAppearance().setMargin(new Spacing(0, 5));
    buttons.addWidget(createTableModelButton);

    CheckBox multiSelection = FengGUI.createCheckBox(buttons, "Multiselection");

    Button updateTableModel = FengGUI.createButton("Update Table Model");
    updateTableModel.getAppearance().setMargin(new Spacing(0, 5));
    buttons.addWidget(updateTableModel);

    CheckBox drawGrid = FengGUI.createCheckBox(buttons, "Draw Grid");
    drawGrid.setSelected(true);

    Button setRelativeWidth = FengGUI.createButton("Set Relative width");
    setRelativeWidth.getAppearance().setMargin(new Spacing(0, 5));
    buttons.addWidget(setRelativeWidth);

    CheckBox drawHeader = FengGUI.createCheckBox(buttons, "Draw Table Header");
    drawHeader.setSelected(true);

    multiSelection.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (selectionChangedEvent.isSelected())
          table.setNumberOfSelectableRows(ToggableGroup.MULTIPLE_SELECTION);
        else
          table.setNumberOfSelectableRows(ToggableGroup.SINGLE_SELECTION);
      }

    });

    drawGrid.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        //table.getAppearance().setGridVisible(selectionChangedEvent.isSelected());
      }

    });

    createTableModelButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        table.setModel(new MyTableModel());
        sc.layout();
      }

    });

    updateTableModel.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        ((MyTableModel) table.getModel()).update();
        table.updateMinSize();
        sc.layout();
      }

    });

    setRelativeWidth.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        table.setColumnWidth(0, 0.1f);
        table.setColumnWidth(1, 0.1f);
        table.setColumnWidth(2, 0.5f);
        table.setColumnWidth(3, 0.3f);
      }

    });
  }

  public void buildGUI(Display g)
  {
    desk = g;

    buildTableFrame();

    desk.layout();
  }

  public static String generateRandomString()
  {
    int length = (int) (Math.random() * 10) + 5;
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < length; i++)
      sb.append(Alphabet.getDefaultAlphabet().getAlphabet()[(int) (Math.random() * Alphabet.getDefaultAlphabet()
          .getAlphabet().length)]);

    return sb.toString();
  }

  class MyTableModel implements ITableModel
  {
    String[][] matrix = null;

    public MyTableModel()
    {
      matrix = new String[10 + (int) (Math.random() * 20d)][4];
    }

    public void update()
    {
      matrix = new String[10 + (int) (Math.random() * 20d)][4];
    }

    public String getColumnName(int columnIndex)
    {
      return "column" + columnIndex;
    }

    public int getColumnCount()
    {
      return matrix[0].length;
    }

    public String getValue(int row, int column)
    {
      if (matrix[row][column] == null)
      {
        if (column == 0)
          matrix[row][column] = "" + row;
        else if (column == 2)
          matrix[row][column] = generateRandomString();
        else
          matrix[row][column] = "" + (int) (Math.random() * 100);

      }

      return matrix[row][column];
    }

    public int getRowCount()
    {
      return matrix.length;
    }

    /* (non-Javadoc)
    * @see org.fenggui.composite.table.ITableModel#getItem(int, int)
    */
    public Item getItem(int column, int row, LabelAppearance appearance)
    {
      Item item = new Item(getValue(row, column), appearance);

      return item;
    }
  }

  public String getExampleName()
  {
    return "Table Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates the Table Widget";
  }

}
