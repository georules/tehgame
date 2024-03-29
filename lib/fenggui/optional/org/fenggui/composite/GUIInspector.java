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
 * Created on Feb 3, 2007
 * $Id: GUIInspector.java 611 2009-03-22 15:58:20Z marcmenghin $
 */
package org.fenggui.composite;

import java.io.ByteArrayOutputStream;

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.FengGUIOptional;
import org.fenggui.IContainer;
import org.fenggui.IWidget;
import org.fenggui.Label;
import org.fenggui.SplitContainer;
import org.fenggui.StandardWidget;
import org.fenggui.TextEditor;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.composite.tree.ITreeModel;
import org.fenggui.composite.tree.Tree;
import org.fenggui.composite.tree.TreeItem;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.GridLayout;
import org.fenggui.theme.xml.GlobalContextHandler;
import org.fenggui.theme.xml.XMLOutputStream;
import org.fenggui.util.Color;

public class GUIInspector extends Window
{
  private SplitContainer splitContainer = null;
  private Tree<IWidget>  tree           = null;
  private TextEditor     textArea       = null;
  private Label          sizeLabel      = new Label();
  private Label          minSizeLabel   = new Label();
  private Label          positionLabel  = new Label();
  private Label          resizeLabel    = new Label();

  public GUIInspector()
  {
    super(true, false, false, true);

    // FIXME: change this soon
    this.setupTheme(GUIInspector.class);

    splitContainer = FengGUI.createSplitContainer(this.getContentContainer(), false);

    Container treeContainer = FengGUI.createContainer();
    treeContainer.setLayoutManager(new BorderLayout());

    tree = FengGUIOptional.<IWidget> createTree(treeContainer);
    tree.setLayoutData(BorderLayoutData.CENTER);
    splitContainer.setFirstWidget(treeContainer);
    splitContainer.setValue(100);
    Button updateButton = FengGUI.createButton(treeContainer, "Update");
    updateButton.setLayoutData(BorderLayoutData.SOUTH);

    Container dataContainer = FengGUI.createContainer();
    dataContainer.setLayoutManager(new BorderLayout());

    Container labelContainer = FengGUI.createContainer(dataContainer);
    labelContainer.setLayoutManager(new GridLayout(2, 4));
    labelContainer.setLayoutData(BorderLayoutData.NORTH);
    FengGUI.createLabel(labelContainer, "Size:");
    sizeLabel = FengGUI.createLabel(labelContainer, "");
    FengGUI.createLabel(labelContainer, "MinSize:");
    minSizeLabel = FengGUI.createLabel(labelContainer, "");
    FengGUI.createLabel(labelContainer, "Position:");
    positionLabel = FengGUI.createLabel(labelContainer, "");
    FengGUI.createLabel(labelContainer, "Resize:");
    resizeLabel = FengGUI.createLabel(labelContainer, "");

    textArea = FengGUI.createTextEditor(dataContainer);
    textArea.setLayoutData(BorderLayoutData.CENTER);
    splitContainer.setSecondWidget(dataContainer);
    tree.getAppearance().add(new PlainBorder(Color.BLACK));

    setTitle("GUI Inspector");
    setSize(400, 400);
    updateButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        update();
      }
    });

    tree.getToggableWidgetGroup().addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (!selectionChangedEvent.isSelected())
          return;
        IWidget source = (IWidget) ((TreeItem) selectionChangedEvent.getToggableWidget()).getData();
        setupDetails(source);
      }
    });

  }

  private void setupDetails(IWidget w)
  {
    minSizeLabel.setText(w.getMinSize() + "");
    positionLabel.setText(w.getX() + ", " + w.getY());
    resizeLabel.setText(w.isExpandable() + " " + w.isShrinkable());
    sizeLabel.setText(w.getSize() + "");
    textArea.setText(w.toString());

    if (w instanceof StandardWidget)
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      final XMLOutputStream os = new XMLOutputStream(bos, w.getClass().getSimpleName(), new GlobalContextHandler());
      try
      {
        ((StandardWidget) w).process(os);
        os.close();
        textArea.setText(textArea.getText() + "\n\n" + os.getDocument().toXML());
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

  }

  private void update()
  {
    tree.setModel(new WidgetTreeModel());
    layout();
  }

  class WidgetTreeModel implements ITreeModel<IWidget>
  {

    public IWidget getNode(IWidget parent, int index)
    {
      if (parent instanceof Container || parent instanceof Display)
      {
        return ((Container) parent).getWidget(index);
      }

      return null;
    }

    public int getNumberOfChildren(IWidget node)
    {
      if (node instanceof Container || node instanceof Display)
      {
        return ((Container) node).size();
      }

      return 0;
    }

    public Pixmap getPixmap(IWidget node)
    {
      return null;
    }

    public IWidget getRoot()
    {
      return getDisplay();
    }

    public String getText(IWidget node)
    {
      return node.getClass().getSimpleName();
    }

    /* (non-Javadoc)
     * @see org.fenggui.composite.tree.ITreeModel#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(IWidget node)
    {
      if (node instanceof IContainer)
      {
        IContainer cont = (IContainer) node;
        return cont.hasChildWidgets();
      }
      return false;
    }

  }
}
