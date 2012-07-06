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
 * Created on Dec 11, 2006
 * $Id: RotatedLabelAppearance.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.appearance;

import org.fenggui.RotatedLabel;

@Deprecated
public class RotatedLabelAppearance extends LabelAppearance
{
  private RotatedLabel label    = null;

  /**
   * Prcalculated trigonometric values 
   */
  public double        sinAngle = 0;
  public double        cosAngle = 0;

  /**
   * x and y offset of the point to paint the text due to the rotation 
   */
  private int          xOffset  = 0;
  private int          yOffset  = 0;

  public RotatedLabelAppearance(RotatedLabel w)
  {
    super(w);
    label = w;
  }

  //	@Override
  //	public Dimension getContentMinSizeHint()
  //	{
  //		int width = 0;
  //		int height = 0;
  //
  //		String text = label.getText();
  //		if (text != null)
  //		{
  //			Font font = getFont();
  //			int textWidth = font.getWidth(text);
  //			int textHeight = font.getHeight();
  //
  //			xOffset = 0;
  //			yOffset = 0;
  //
  //			if (label.getAngle() == 0)
  //			{
  //				width = textWidth;
  //				height = textHeight;
  //			}
  //			else if (label.getAngle() == 90)
  //			{
  //				width = textHeight;
  //				height = textWidth;
  //				xOffset = textHeight;
  //			}
  //			else if (label.getAngle() == -90)
  //			{
  //				width = textHeight;
  //				height = textWidth;
  //				yOffset = textWidth;
  //			}
  //			else
  //			{
  //				double x1 = Math.abs(textHeight * sinAngle);
  //				double x2 = textWidth * cosAngle;
  //				double y1 = textHeight * cosAngle;
  //				double y2 = Math.abs(textWidth * sinAngle);
  //
  //				width = (int) (x1 + x2);
  //				height = (int) (y1 + y2);
  //
  //				if (label.getAngle() > 0)
  //				{
  //					xOffset = (int) x1;
  //				}
  //				else if (label.getAngle() < 0)
  //				{
  //					yOffset = (int) y2;
  //				}
  //			}
  //		}
  //
  //		return new Dimension(width, height);
  //	}
  //
  //
  //	@Override
  //	public void paintContent(Graphics g, IOpenGL gl)
  //	{
  //		int width = label.getMinSize().getWidth();
  //		int height = label.getMinSize().getHeight();
  //
  //		int x = getAlignment().alignX(getContentWidth(), width);
  //		int y = getAlignment().alignY(getContentHeight(), height);
  //
  //		if (getTextColor() != null)
  //		{
  //			g.setColor(getTextColor());
  //		}
  //		g.setFont(getFont());
  //		g.drawRotatedString(label.getText(), x + xOffset, y + yOffset, label.getAngle());
  //	}

}
