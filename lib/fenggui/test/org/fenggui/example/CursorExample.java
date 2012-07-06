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
 */
package org.fenggui.example;

import org.fenggui.Display;
import org.fenggui.Widget;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Cursor;
import org.fenggui.binding.render.CursorFactory;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.Graphics;
import org.fenggui.decorator.background.Background;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.decorator.border.Border;
import org.fenggui.decorator.border.PlainBorder;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.util.Color;
import org.fenggui.util.Point;

public class CursorExample implements IExample
{

  public void buildGUI(Display display)
  {
    CursorFactory cf = Binding.getInstance().getCursorFactory();

    final int x = 150;
    final int y = 150;

    CursorWidget textCursorWidget = new CursorWidget(cf.getTextCursor(), display, "text");
    textCursorWidget.setXY(x + 20, y + 20);

    CursorWidget handCursorWidget = new CursorWidget(cf.getHandCursor(), display, "hand");
    handCursorWidget.setXY(x + 40 + 50, y + 20);

    CursorWidget moveCursorWidget = new CursorWidget(cf.getMoveCursor(), display, "move");
    moveCursorWidget.setXY(x + 60 + 100, y + 20);

    CursorWidget horizontalResizeCursor = new CursorWidget(cf.getHorizontalResizeCursor(), display, "E");
    horizontalResizeCursor.setXY(x + 80 + 150, y + 20);

    CursorWidget verticalResizeCursor = new CursorWidget(cf.getVerticalResizeCursor(), display, "N");
    verticalResizeCursor.setXY(x + 20, y + 40 + 50);

    CursorWidget NWResizeCursor = new CursorWidget(cf.getNWResizeCursor(), display, "NW");
    NWResizeCursor.setXY(x + 40 + 50, y + 40 + 50);

    CursorWidget SWResizeCursor = new CursorWidget(cf.getSWResizeCursor(), display, "SW");
    SWResizeCursor.setXY(x + 60 + 100, y + 40 + 50);

    display.layout();
  }

  public String getExampleName()
  {
    return "Cursor Example";
  }

  public String getExampleDescription()
  {
    return "Shows Various Cursors";
  }

  private class CursorWidget extends Widget
  {
    private Cursor     cursor     = null;
    private String     text       = "Bla";
    private Point      pressed    = new Point(10, 10);

    private Border     border     = null;
    private Background background = null;

    public CursorWidget(Cursor cursor, Display display, String text)
    {
      this.cursor = cursor;
      setSize(50, 50);
      setExpandable(false);
      setShrinkable(false);
      display.addWidget(this);
      this.text = text;

      border = new PlainBorder(Color.BLACK);
      background = new PlainBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void mouseEntered(MouseEnteredEvent mouseEnteredEvent)
    {
      cursor.show();
    }

    @Override
    public void mouseExited(MouseExitedEvent mouseExitedEvent)
    {
      Binding.getInstance().getCursorFactory().getDefaultCursor().show();
    }

    @Override
    public void paint(Graphics g)
    {
      background.paint(g, 0, 0, getWidth(), getHeight());
      border.paint(g, 0, 0, getWidth(), getHeight());
      g.setColor(Color.BLACK);
      g.setFont(ImageFont.getDefaultFont());
      g
          .drawString(text, getWidth() / 2 - g.getFont().getWidth(text) / 2, getHeight() / 2 - g.getFont().getHeight()
              / 2);
      g.setColor(Color.RED);
      g.drawLine(pressed.getX() - 2, pressed.getY() - 2, pressed.getX() + 2, pressed.getY() + 2);
      g.drawLine(pressed.getX() + 2, pressed.getY() - 2, pressed.getX() - 2, pressed.getY() + 2);
    }

    @Override
    public void mousePressed(MousePressedEvent mp)
    {
      pressed = new Point(mp.getDisplayX() - this.getX(), mp.getDisplayY() - this.getY());
    }

  }

}
