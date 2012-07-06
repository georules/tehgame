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
 * Created on Apr 18, 2005
 * $Id: JOGLBinding.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.binding.render.jogl;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;

import org.fenggui.binding.clipboard.AWTClipboard;
import org.fenggui.binding.clipboard.IClipboard;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.ITexture;
import org.fenggui.theme.XMLTheme;

/**
 * Binds FengGUI to JOGL.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class JOGLBinding extends Binding
{
  private JOGLCursorFactory cursorFactory = null;
  private Component         canvas;
  private GL                gl            = null;
  private IClipboard        clipboard     = new AWTClipboard();

  public static void EnableDebugingOnCanvas(GLCanvas canvas)
  {
    canvas.setGL(new DebugGL(canvas.getGL()));
  }

  public JOGLBinding(GLCanvas canvas)
  {
    this(canvas, canvas.getGL());
  }

  public JOGLBinding(final Component component, GL gl)
  {
    this(component, gl, new JOGLOpenGL(gl));
  }

  public JOGLBinding(final Component component, GL gl, IOpenGL opengl)
  {
    super(opengl);

    this.gl = gl;

    if (component == null)
      throw new NullPointerException("component == null");
    if (gl == null)
      throw new NullPointerException("gl == null");

    this.canvas = component;

    XMLTheme.TYPE_REGISTRY.register("Texture", JOGLTexture.class);

    cursorFactory = new JOGLCursorFactory(canvas.getParent());

    canvas.addComponentListener(new ComponentListener()
    {
      public void componentResized(ComponentEvent ce)
      {
        fireDisplayResizedEvent(canvas.getWidth(), canvas.getHeight());
      }

      public void componentMoved(ComponentEvent arg0)
      {
      }

      public void componentShown(ComponentEvent arg0)
      {
      }

      public void componentHidden(ComponentEvent arg0)
      {
      }
    });

  }

  /* (non-Javadoc)
   * @see joglui.binding.Binding#getTexture(java.lang.String)
   */
  public ITexture getTexture(InputStream stream) throws IOException
  {
    if (!this.getOpenGL().isOpenGLThread())
      throw new RuntimeException("getTexture called not in OpenGL Thread!");

    return JOGLTexture.uploadTextureToVideoRAM(gl, ImageIO.read(stream));
  }

  /* (non-Javadoc)
   * @see joglui.binding.Binding#getTexture(java.awt.image.BufferedImage)
   */
  public ITexture getTexture(BufferedImage bi)
  {
    if (!this.getOpenGL().isOpenGLThread())
      throw new RuntimeException("getTexture called not in OpenGL Thread!");

    return JOGLTexture.uploadTextureToVideoRAM(gl, bi);
  }

  /*
  	 * (non-Javadoc)
  	 * 
  	 * @see joglui.binding.Binding#getCanvasWidth()
  	 */
  public int getCanvasWidth()
  {
    return canvas.getWidth();
  }

  /* (non-Javadoc)
   * @see joglui.binding.Binding#getCanvasHeight()
   */
  public int getCanvasHeight()
  {
    return canvas.getHeight();
  }

  @Override
  public JOGLCursorFactory getCursorFactory()
  {
    return cursorFactory;
  }

  @Override
  public IClipboard getClipboard()
  {
    return clipboard;
  }

}
