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
 * Created on 22.10.2007
 * $Id$
 */
package org.fenggui.binding.render.jogl;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

import org.fenggui.binding.render.AWTFont;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOnlyStream;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;

import com.sun.opengl.util.j2d.TextRenderer;

/**
 * A TextRenderer that uses the text renderer included in JOGL. Of course you can only use
 * this if you use JOGL in your application. This enables features such as full Unicode
 * support.
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class JOGLTextRenderer implements ITextRenderer
{
  public class DefaultFengGUIRenderDelegate implements TextRenderer.RenderDelegate
  {
    public boolean intensityOnly()
    {
      return true;
    }

    public Rectangle2D getBounds(CharSequence str, Font font, FontRenderContext frc)
    {
      return getBounds(font.createGlyphVector(frc, new MapCharSequenceToGlyphVector(str)), frc);
    }

    public Rectangle2D getBounds(String str, Font font, FontRenderContext frc)
    {
      return getBounds(font.createGlyphVector(frc, str), frc);
    }

    public Rectangle2D getBounds(GlyphVector gv, FontRenderContext frc)
    {
      // used to calculate the correct size. JOGL seems to not use logicalBounds but the
      // visualBounds. This leads to spaces not being within the size if they are first or
      // last characters.
      return gv.getLogicalBounds();
    }

    public void drawGlyphVector(Graphics2D graphics, GlyphVector str, int x, int y)
    {
      graphics.drawGlyphVector(str, x, y);
    }

    public void draw(Graphics2D graphics, String str, int x, int y)
    {
      graphics.drawString(str, x, y);
    }
  }

  private static class MapCharSequenceToGlyphVector implements CharacterIterator
  {
    CharSequence mSequence;
    int          mLength;
    int          mCurrentIndex;

    MapCharSequenceToGlyphVector()
    {
    }

    MapCharSequenceToGlyphVector(CharSequence sequence)
    {
      initFromCharSequence(sequence);
    }

    public void initFromCharSequence(CharSequence sequence)
    {
      mSequence = sequence;
      mLength = mSequence.length();
      mCurrentIndex = 0;
    }

    public char last()
    {
      mCurrentIndex = Math.max(0, mLength - 1);

      return current();
    }

    public char current()
    {
      if ((mLength == 0) || (mCurrentIndex >= mLength))
      {
        return CharacterIterator.DONE;
      }

      return mSequence.charAt(mCurrentIndex);
    }

    public char next()
    {
      mCurrentIndex++;

      return current();
    }

    public char previous()
    {
      mCurrentIndex = Math.max(mCurrentIndex - 1, 0);

      return current();
    }

    public char setIndex(int position)
    {
      mCurrentIndex = position;

      return current();
    }

    public int getBeginIndex()
    {
      return 0;
    }

    public int getEndIndex()
    {
      return mLength;
    }

    public int getIndex()
    {
      return mCurrentIndex;
    }

    public Object clone()
    {
      MapCharSequenceToGlyphVector iter = new MapCharSequenceToGlyphVector(mSequence);
      iter.mCurrentIndex = mCurrentIndex;

      return iter;
    }

    public char first()
    {
      if (mLength == 0)
      {
        return CharacterIterator.DONE;
      }

      mCurrentIndex = 0;

      return current();
    }
  }

  private static Map<Font, TextRenderer> renderers          = new HashMap<Font, TextRenderer>();

  private String                         name;
  private TextRenderer                   renderer           = null;
  private AWTFont                        font               = null;
  private float                          lineHeight         = 0;
  private float                          lineSpacing        = 0;
  private float                          lineDecent         = 0;
  private float                          underlineOffset    = 0.0f;
  private float                          underlineThickness = 0.0f;

  public JOGLTextRenderer(String name, AWTFont font)
  {
    this.name = name;
    setFont(font);
  }

  public JOGLTextRenderer(String name, TextRenderer renderer, boolean underlined)
  {
    this.name = name;
    setTextRenderer(renderer, underlined);
  }

  public JOGLTextRenderer(InputOnlyStream stream) throws IOException, IXMLStreamableException
  {
    this.process(stream);
  }

  private void setTextRenderer(TextRenderer renderer, boolean underlined)
  {
    this.renderer = renderer;
    addTextRenderer(renderer);
    font = new AWTFont(renderer.getFont(), renderer.getFontRenderContext().isAntiAliased(), renderer
        .getFontRenderContext().usesFractionalMetrics(), underlined);
    updateValues();
  }

  private void updateValues()
  {
    Font font = this.font.getFont();
    LineMetrics metrics = font.getLineMetrics("example@|�`jfq^/_'", renderer.getFontRenderContext());
    this.lineDecent = (float) Math.ceil(metrics.getDescent());
    this.lineHeight = (float) Math.ceil(metrics.getAscent()) + lineDecent;
    this.lineSpacing = (float) Math.ceil(metrics.getLeading());
    /**
     * needs to be at least 1 as on some graphics cards if its less it will not be drawn
     * at all.
     */
    this.underlineOffset = Math.round(metrics.getUnderlineOffset());
    this.underlineThickness = Math.max(1.0f, Math.round(metrics.getUnderlineThickness()));
  }

  private TextRenderer createRendererFromAWTFont(AWTFont font)
  {
    if (font == null)
    {
      throw new IllegalArgumentException("Not able to create JOGLTextRenderer. No AWTFont set.");
    }
    if (renderers.containsKey(font.getFont()))
    {
      return renderers.get(font.getFont());
    }
    else
    {
      TextRenderer joglRender = new TextRenderer(font.getFont(), font.isAntialiased(), font.isUseFractionalMetrics(),
          new DefaultFengGUIRenderDelegate());
      addTextRenderer(joglRender);
      return joglRender;
    }
  }

  public Dimension calculateSize(String[] text)
  {
    if (text == null || text.length <= 0)
    {
      return new Dimension(0, (int) Math.ceil(lineHeight));
    }
    double maxWidth = 0;
    double height = 0;
    for (String line : text)
    {
      Rectangle2D bounds = this.renderer.getBounds(line);
      double width = bounds.getWidth();
      if (width > maxWidth)
        maxWidth = width;
      height += lineHeight + lineSpacing;
    }
    height -= lineSpacing;
    return new Dimension((int) Math.ceil(maxWidth), (int) Math.ceil(height));
  }

  public JOGLTextRenderer copy()
  {
    return new JOGLTextRenderer(name, renderer, font.isUnderlined());
  }

  public void render(int x, int y, String[] text, Color color, Graphics g)
  {
    if (text == null || text.length == 0)
      return;

    int localX = x + g.getTranslation().getX();
    float startLocalY = y + g.getTranslation().getY() - (lineHeight - lineDecent);
    float localY = startLocalY;

    if (font.isUnderlined())
    {
      IOpenGL gl = g.getOpenGL();
      g.setColor(color);
      for (String line : text)
      {
        Rectangle2D rect = this.renderer.getBounds(line);
        float posY = localY - underlineOffset;
        float posX = localX;
        gl.startQuads();
        gl.vertex(posX, posY - underlineThickness);
        gl.vertex(posX, posY);
        gl.vertex((float) (posX + rect.getWidth()), posY);
        gl.vertex((float) (posX + rect.getWidth()), posY - underlineThickness);
        gl.end();

        localY -= lineHeight + lineSpacing;
      }
    }

    localY = startLocalY;
    g.setColor(color);
    this.renderer.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    this.renderer.begin3DRendering();
    for (String line : text)
    {
      this.renderer.draw3D(line, localX, localY, 0.0f, 1.0f);
      localY -= lineHeight + lineSpacing;
    }
    this.renderer.end3DRendering();
  }

  public int getLineHeight()
  {
    return (int) Math.ceil(lineHeight);
  }

  public boolean isValidChar(char c)
  {
    // JOGL is full unicode text renderer,
    // valid chars depend on the used Font.
    if (Character.isIdentifierIgnorable(c))
      return false;
    else
      return renderer.getFont().canDisplay(c);
  }

  public int getWidth(String text)
  {
    if (text.length() == 0)
      return 0;
    double width = Math.ceil(this.renderer.getBounds(text).getWidth());
    return (int) (width);
  }

  public String getUniqueName()
  {
    return IXMLStreamable.GENERATE_NAME;
  }

  public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
  {
    name = stream.processAttribute("Name", name);
    font = stream.processChild(font, XMLTheme.FONT_REGISTRY);
    setTextRenderer(createRendererFromAWTFont(font), font.isUnderlined());
  }

  public int getWidth(char text)
  {
    return (int) Math.ceil(renderer.getBounds(Character.toString(text)).getWidth());
  }

  public static void addTextRenderer(TextRenderer renderer)
  {
    renderers.put(renderer.getFont(), renderer);
  }

  public static void removeTextRenderer(TextRenderer renderer)
  {
    renderers.remove(renderer);
  }

  public static void removeAllTextRenderers()
  {
    renderers.clear();
  }

  public static int getRegisteredRenderersCount()
  {
    return renderers.size();
  }

  /**
   * This method takes some time as a new TextRenderer needs to be created for a new Font.
   * 
   * @param font
   */
  public void setFont(AWTFont font)
  {
    renderer = createRendererFromAWTFont(font);
    this.font = font;
    updateValues();
  }

  public void setFont(IFont font)
  {
    if (font instanceof AWTFont)
    {
      this.setFont((AWTFont) font);
    }
    else
    {
      throw new IllegalArgumentException("Only an AWTFont can be used with a JOGLTextRenderer.");
    }
  }

  public IFont getFont()
  {
    return this.font;
  }

  public void render(int x, int y, String text, Color color, Graphics g)
  {
    if (text == null || text.length() == 0 || text.trim().length() == 0)
      return;

    int localX = x + g.getTranslation().getX();
    float localY = (y + g.getTranslation().getY() - (lineHeight - lineDecent));

    g.setColor(color);
    this.renderer.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    this.renderer.begin3DRendering();
    this.renderer.draw3D(text, localX, localY, 0.0f, 1.0f);

    this.renderer.end3DRendering();
  }

  public int getLineSpacing()
  {
    return (int) Math.ceil(lineSpacing);
  }

  /* (non-Javadoc)
   * @see org.fenggui.binding.render.text.ITextRenderer#getName()
   */
  public String getName()
  {
    return name;
  }
}
