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
 * $Id: Tree.java 611 2009-03-22 15:58:20Z marcmenghin $
 */
package org.fenggui.composite.tree;

import org.fenggui.ModelWidget;
import org.fenggui.ToggableGroup;
import org.fenggui.appearance.EntryAppearance;
import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.util.Dimension;

/**
 * Widget to display tree a structure. The data to be displayed has to be provided by an <code>ITreeModel</code>
 * implementation. Note that at the current state of development, changes to the model require to set the entire model
 * new (setModel)/ See here http://sourceforge.net/tracker/index.php?func=detail&aid=1570998&group_id=178317&atid=884747
 * <br/> When the tree is used in a <code>ScrollContainer</code>, changes to the model may require to be commited
 * with a <code>layout</code> call in the scroll container.
 * 
 * @author Johannes Schaback aka Schabby, last edited by $Author: marcmenghin $, $LastChangedDate: 2009-03-22 16:58:20 +0100 (So, 22 Mär 2009) $
 * @version $Revision: 611 $
 * @see ITreeModel
 */
public class Tree<E> extends ModelWidget<EntryAppearance, ITreeModel<E>>
{
  /**
   * The root node of the tree
   */
  private TreeItem                root                = null;
  private TreeItem                hoveredItem         = null;

  private ToggableGroup<TreeItem> toggableWidgetGroup = new ToggableGroup<TreeItem>(ToggableGroup.SINGLE_SELECTION);

  /**
   * Creates a new <code>Tree</code> instance with an empty data model.
   * 
   */
  public Tree()
  {
    this(null);
  }

  /**
   * Creates a new <code>Tree</code> instance that visualized the given data model.
   * 
   * @param model
   *            the data model
   */
  public Tree(ITreeModel<E> model)
  {
    setAppearance(new EntryAppearance(this));

    setModel(model);
  }

  private void expandLevel(TreeItem root)
  {
    buildNodeLevel(root, getAppearance());
    root.expand(getAppearance());
    updateMinSize();
  }

  private void buildNodeLevel(TreeItem root, LabelAppearance appearance)
  {
    ITreeModel<E> model = getModel();
    if (root == null)
    {
      this.root = createTreeItem(model.getRoot(), null, appearance);
    }
    else
    {
      root.removeAllChilds(getAppearance());

      @SuppressWarnings("unchecked")
      E dataRoot = (E) root.getData();
      if (model.hasChildren(dataRoot))
      {
        int childCount = model.getNumberOfChildren(dataRoot);
        TreeItem[] childs = new TreeItem[childCount];
        for (int i = 0; i < childCount; i++)
        {
          E child = model.getNode(dataRoot, i);
          childs[i] = createTreeItem(child, root, appearance);
        }
        root.addChilds(getAppearance(), childs);
      }
    }
  }

  /**
   * Creates a new TreeItem from the data tree.
   * 
   * @param node
   * @param parent
   * @param appearance
   * @return
   */
  protected TreeItem createTreeItem(E node, TreeItem parent, LabelAppearance appearance)
  {
    TreeItem item = new TreeItem(this.getModel().getText(node), getModel().getPixmap(node), appearance, parent,
        getModel().hasChildren(node));
    item.setData(node);
    return item;
  }

  public ToggableGroup<TreeItem> getToggableWidgetGroup()
  {
    return toggableWidgetGroup;
  }

  /**
   * Returns the root node of the visible Tree.
   * 
   * @return
   */
  public TreeItem getRoot()
  {
    return root;
  }

  public void setRoot(TreeItem root)
  {
    this.root = root;
    getMinContentSize();
  }

  public void AddNode(TreeItem parent, TreeItem... items)
  {
    parent.addChilds(getAppearance(), items);
  }

  @Override
  public Dimension getMinContentSize()
  {
    if (root == null)
      return new Dimension(1, 1);
    else
      return root.getPreferredSize();
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    if (getRoot() == null)
      return;

    EntryAppearance appearance = getAppearance();

    int y = appearance.getContentHeight();
    int x = 0;
    int width = appearance.getContentWidth();

    g.setColor(appearance.getColor());

    getRoot().render(x, y, width, getRoot().getPreferredSize().getHeight(), hoveredItem, this.getAppearance(), g);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseMoved(int, int)
   */
  @Override
  public void mouseMoved(int displayX, int displayY)
  {
    if (getRoot() != null)
    {
      int frombottom = getAppearance().getContentHeight() - getRoot().getPreferredSize().getHeight();
      int currentY = frombottom > 0 ? frombottom : 0;

      hoveredItem = getRoot().getItemOnPosition(0, currentY, displayX - this.getDisplayX(),
        displayY - this.getDisplayY(), getAppearance().getContentWidth(), getAppearance());
    }
    else
    {
      hoveredItem = null;
    }

    super.mouseMoved(displayX, displayY);
  }

  /* (non-Javadoc)
   * @see org.fenggui.Widget#sizeChanged(org.fenggui.event.SizeChangedEvent)
   */
  @Override
  public void sizeChanged(SizeChangedEvent event)
  {
    super.sizeChanged(event);

    if (getRoot() != null)
      getRoot().adaptChange(this.getAppearance().getContentWidth(), this.getAppearance().getContentHeight(),
        getAppearance());
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseClicked(org.fenggui.event.mouse.MouseClickedEvent)
   */
  @Override
  public void mouseClicked(MouseClickedEvent event)
  {
    if (hoveredItem != null)
    {
      toggableWidgetGroup.setSelected(this, hoveredItem, true);
      hoveredItem.setSelected(true);
    }

    super.mouseClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ObservableWidget#mouseDoubleClicked(org.fenggui.event.mouse.MouseDoubleClickedEvent)
   */
  @Override
  public void mouseDoubleClicked(MouseDoubleClickedEvent event)
  {
    if (hoveredItem != null)
    {
      buildNodeLevel(hoveredItem, getAppearance());
      hoveredItem.flip(getAppearance());
      updateMinSize();
    }

    super.mouseDoubleClicked(event);
  }

  /* (non-Javadoc)
   * @see org.fenggui.ModelWidget#ModelUpdated(org.fenggui.IModel)
   */
  @Override
  protected void ModelUpdated(ITreeModel<E> model, boolean newModel)
  {
    if (newModel)
    {
      if (this.getModel() != null)
      {
        // create first two levels
        buildNodeLevel(null, this.getAppearance());
        expandLevel(root);
      }
      else
      {
        this.root = null;
      }
    }

    //TODO: add updating of nodes

    updateMinSize();
  }
}
