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
 * Created on Jan 13, 2009
 * $Id$
 */
package org.fenggui.composite.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.Item;
import org.fenggui.appearance.EntryAppearance;
import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Rectangle;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class TreeItem extends Item
{
  // appearance keys
  public static final String PLUSICONKEY  = "PlusIconPixmap";
  public static final String MINUSICONKEY = "MinusIconPixmap";

  private List<TreeItem>     childs       = null;
  private TreeItem           parent       = null;
  private boolean            expanded     = false;

  /**
   * 
   * @param text
   * @param pixmap
   * @param appearance
   * @param parent
   * @param childs
   */
  public TreeItem(String text, Pixmap pixmap, LabelAppearance appearance, TreeItem parent, boolean hasChilds)
  {
    this(text, pixmap, appearance, hasChilds);
    this.parent = parent;
  }

  /**
    * @param text
    * @param pixmap
    * @param appearance
    * @param childs
    */
  public TreeItem(String text, Pixmap pixmap, LabelAppearance appearance, boolean hasChilds)
  {
    super(text, pixmap, appearance);
    if (hasChilds)
      childs = new ArrayList<TreeItem>();
  }

  /**
   * @param text
   * @param pixmap
   * @param appearance
   */
  public TreeItem(String text, Pixmap pixmap, LabelAppearance appearance)
  {
    this(text, pixmap, appearance, false);
  }

  /**
   * @param stream
   * @throws IOException
   * @throws IXMLStreamableException
   */
  public TreeItem(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    process(stream);
  }

  /**
   * @param text
   * @param appearance
   */
  public TreeItem(String text, LabelAppearance appearance)
  {
    this(text, null, appearance);
  }

  @Override
  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    super.process(stream);
  }

  /**
   * Adds one or more children to this tree node
   * 
   * @param items
   */
  public void addChilds(LabelAppearance appearance, TreeItem... items)
  {
    if (childs == null)
      childs = new ArrayList<TreeItem>(items.length);

    for (TreeItem item : items)
      childs.add(item);

    updatePreferredSize(appearance);
  }

  /**
   * Removes one or more items for this tree node
   * 
   * @param items
   */
  public void removeChilds(LabelAppearance appearance, TreeItem... items)
  {
    if (childs == null)
      return;

    for (TreeItem item : items)
      childs.remove(item);

    updatePreferredSize(appearance);
  }

  public void removeAllChilds(LabelAppearance appearance)
  {
    if (childs == null)
      return;

    childs.clear();
    updatePreferredSize(appearance);
  }

  /**
   * Returns true if this node has children
   * 
   * @return
   */
  public boolean hasChilds()
  {
    return childs != null;
  }

  public boolean isRoot()
  {
    return parent == null;
  }

  public TreeItem getParent()
  {
    return parent;
  }

  public boolean isExpanded()
  {
    return expanded;
  }

  public void expand(LabelAppearance appearance)
  {
    this.expanded = true;
    updatePreferredSize(appearance);
  }

  public void collapse(LabelAppearance appearance)
  {
    this.expanded = false;
    updatePreferredSize(appearance);
  }

  public void flip(LabelAppearance appearance)
  {
    if (isExpanded())
      collapse(appearance);
    else
      expand(appearance);
  }

  public TreeItem getItemOnPosition(int currentX, int currentY, int x, int y, int width, LabelAppearance appearance)
  {
    Dimension prefSize = super.calculatePreferredSize(appearance);

    Rectangle localRect = new Rectangle(currentX, currentY, width, this.getPreferredSize().getHeight());
    if (localRect.contains(x, y))
    {
      //self or childs
      if (this.hasChilds() && this.isExpanded())
      {
        int currentYPos = currentY + getPreferredSize().getHeight() - prefSize.getHeight();
        for (TreeItem item : childs)
        {
          currentYPos -= item.getPreferredSize().getHeight();
          TreeItem result = item.getItemOnPosition(currentX + 15, currentYPos, x, y, width - 15, appearance);
          if (result != null)
            return result;
        }
      }
      return this;
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Item#calculatePreferredSize(org.fenggui.appearance.LabelAppearance)
   */
  @Override
  protected Dimension calculatePreferredSize(LabelAppearance appearance)
  {
    Dimension size = super.calculatePreferredSize(appearance);
    int offsetX = 0;

    if (this.hasChilds())
    {
      Pixmap icon;
      if (this.isExpanded())
      {
        icon = appearance.getPixmap(MINUSICONKEY);
      }
      else
      {
        icon = appearance.getPixmap(PLUSICONKEY);
      }
      if (icon != null)
        offsetX = icon.getWidth() + 2;

      size.setWidth(size.getWidth() + offsetX);

      if (this.isExpanded())
      {
        size.setWidth(size.getWidth());
        for (TreeItem item : childs)
        {
          Dimension itemSize = item.getPreferredSize();
          size.setSize(Math.max(size.getWidth(), offsetX + itemSize.getWidth()), size.getHeight()
              + itemSize.getHeight());
        }
      }
    }

    return size;
  }

  /* (non-Javadoc)
   * @see org.fenggui.Item#render(int, int, int, int, org.fenggui.appearance.EntryAppearance, org.fenggui.binding.render.Graphics)
   */
  @Override
  public void render(int x, int y, int width, int height, Item hoveredItem, EntryAppearance appearance, Graphics g)
  {
    Dimension contentSize = super.calculatePreferredSize(appearance);

    int offsetX = 0;

    //draw icon if childs
    if (this.hasChilds())
    {
      Pixmap icon;

      if (this.isExpanded())
      {
        icon = appearance.getPixmap(MINUSICONKEY);
      }
      else
      {
        icon = appearance.getPixmap(PLUSICONKEY);
      }
      if (icon != null)
      {
        g.setColor(Color.WHITE);
        g.drawImage(icon, x, y
            - (Alignment.MIDDLE.alignY(contentSize.getHeight(), icon.getHeight()) + icon.getHeight()));
        offsetX = icon.getWidth() + 2;
      }
    }

    g.setColor(appearance.getColor());
    super.render(x + offsetX, y - contentSize.getHeight(), width - offsetX, contentSize.getHeight(), hoveredItem,
      appearance, g);

    //draw childs
    if (this.hasChilds() && this.expanded)
    {
      renderChilds(x + offsetX, y - contentSize.getHeight(), width - offsetX, height - contentSize.getHeight(),
        hoveredItem, appearance, g);
    }
  }

  protected void renderChilds(int x, int y, int width, int height, Item hoveredItem, EntryAppearance appearance,
      Graphics g)
  {
    for (TreeItem child : childs)
    {
      Dimension childSize = child.getPreferredSize();

      child.render(x, y, width, childSize.getHeight(), hoveredItem, appearance, g);
      y -= childSize.getHeight();
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.Item#adaptChange(int, int, org.fenggui.appearance.LabelAppearance)
   */
  @Override
  public void adaptChange(int width, int height, LabelAppearance appearance)
  {
    super.adaptChange(width, height, appearance);

    if (this.hasChilds())
    {
      width -= 15;

      for (TreeItem child : childs)
      {
        child.adaptChange(width, height, appearance);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.fenggui.Item#updatePreferredSize(org.fenggui.appearance.LabelAppearance)
   */
  @Override
  public void updatePreferredSize(LabelAppearance appearance)
  {
    super.updatePreferredSize(appearance);
    if (this.parent != null)
      this.parent.updatePreferredSize(appearance);
  }

}
