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
 * Created on 22.02.2008
 * $Id$
 */
package org.fenggui.unit;

import static org.junit.Assert.assertEquals;

import org.fenggui.event.ISizeChangedListener;
import org.fenggui.event.SizeChangedEvent;
import org.fenggui.text.content.ContentManager;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentManagerTest extends ContentTestBase
{

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    this.init();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#ContentManager(org.fenggui.binding.render.text.advanced.IContentFactory)}.
   */
  @Test
  public void testContentManagerIContentFactory()
  {
    ContentManager manager = new ContentManager(factory, appearance);

    assertEquals(new Dimension(0, 10), manager.getSize());
    assertEquals(new Point(0, 0), manager.getActivePosition(appearance));
    assertEquals(0, manager.getAtomCount());
    assertEquals("", manager.getContent());
    assertEquals(false, manager.isAutoWarp());
    assertEquals(false, manager.isMultiline());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#addContentChangedListener(org.fenggui.event.IContentChangedListener)}.
   */
  @Test
  public void testAddContentChangedListener()
  {
    final String textAfter = "c";

    final ContentManager manager = new ContentManager(factory, appearance);

    manager.addSizeChangedListener(new ISizeChangedListener()
    {
      public void sizeChanged(SizeChangedEvent event)
      {
        assertEquals(textAfter, manager.getContent());
      }

    });

    manager.addChar('c', appearance);

  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#AddContent(String, IAppearance))}.
   */
  @Test
  public void testAddContent()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.addContent("test", appearance);
    manager.setActiveAtom(4);
    assertEquals(4, manager.getAtomCount());
    assertEquals(1, manager.getContentLineCount());
    assertEquals(4, manager.getActiveAtom());

    manager.addContent("loss", appearance);
    assertEquals(8, manager.getAtomCount());
    assertEquals(1, manager.getContentLineCount());
    assertEquals(4, manager.getActiveAtom());

    manager.removeAll(appearance);
    assertEquals(0, manager.getAtomCount());
    assertEquals(1, manager.getContentLineCount());
    assertEquals(0, manager.getActiveAtom());

    manager.addContent("loss", appearance);
    assertEquals(4, manager.getAtomCount());
    assertEquals(1, manager.getContentLineCount());

    manager.addContent(" test", appearance);
    assertEquals(9, manager.getAtomCount());
    assertEquals(1, manager.getContentLineCount());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#updateContent(int)}.
   */
  @Test
  public void testUpdateContentInt()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 10, appearance);
    manager.setMultiline(true);
    manager.addContent("test text", appearance);
    manager.setActiveAtom(5);
    assertEquals("test text", manager.getContent());
    assertEquals(true, manager.hasActiveLine());

    manager.updateContent(5, appearance);
    assertEquals("test text", manager.getContent());
    assertEquals(9, manager.getAtomCount());
    assertEquals(new Dimension(5, 90), manager.getSize());
    assertEquals(true, manager.hasActiveLine());

    manager.updateContent(10, appearance);
    assertEquals("test text", manager.getContent());
    assertEquals(9, manager.getAtomCount());
    assertEquals(new Dimension(10, 50), manager.getSize());
    assertEquals(true, manager.hasActiveLine());

    manager.updateContent(35, appearance);
    assertEquals("test text", manager.getContent());
    assertEquals(9, manager.getAtomCount());
    assertEquals(new Dimension(25, 20), manager.getSize());
    assertEquals(true, manager.hasActiveLine());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#findAtomOnPosition(int, int)}.
   */
  @Test
  public void testFindAtomOnPosition()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 10, appearance);
    manager.setMultiline(true);

    manager.addContent("test test test test test", appearance);
    manager.updateContent(45, appearance);

    assertEquals(new Dimension(45, 30), manager.getSize());

    assertEquals(0, manager.findAtomOnPosition(0, 0, appearance));
    assertEquals(0, manager.findAtomOnPosition(2, 5, appearance));
    assertEquals(0, manager.findAtomOnPosition(-5, -5, appearance));
    assertEquals(1, manager.findAtomOnPosition(3, 5, appearance));

    assertEquals(9, manager.findAtomOnPosition(80, 5, appearance));
    assertEquals(24, manager.findAtomOnPosition(80, 80, appearance));
    assertEquals(15, manager.findAtomOnPosition(0, 80, appearance));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#addChar(char)}.
   */
  @Test
  public void testAddChar()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);

    manager.addChar('x', appearance);

    assertEquals(1, manager.getAtomCount());
    assertEquals(new Dimension(5, 10), manager.getSize());
    assertEquals("x", manager.getContent());

    manager.addChar('y', appearance);
    manager.addChar('z', appearance);
    manager.addChar('a', appearance);
    manager.addChar('b', appearance);
    manager.addChar('c', appearance);

    assertEquals(6, manager.getAtomCount());
    assertEquals(new Dimension(30, 10), manager.getSize());
    assertEquals("xyzabc", manager.getContent());

    manager.addChar('p', appearance);

    assertEquals(7, manager.getAtomCount());
    assertEquals(new Dimension(30, 20), manager.getSize());
    assertEquals("xyzabcp", manager.getContent());
    assertEquals(7, manager.getActiveAtom());

    manager.addChar('\n', appearance);

    assertEquals(8, manager.getAtomCount());
    assertEquals(new Dimension(30, 30), manager.getSize());
    assertEquals("xyzabcp\n", manager.getContent());
    assertEquals(8, manager.getActiveAtom());

    manager.addChar('\n', appearance);

    assertEquals(9, manager.getAtomCount());
    assertEquals(new Dimension(30, 40), manager.getSize());
    assertEquals("xyzabcp\n\n", manager.getContent());
    assertEquals(9, manager.getActiveAtom());

    manager.addChar('\n', appearance);
    manager.addChar('\n', appearance);

    assertEquals(11, manager.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#removeNextChar()}.
   */
  @Test
  public void testRemoveNextChar()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(manager.getAtomCount());

    manager.removeNextChar(appearance);
    manager.removeNextChar(appearance);
    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(21);
    manager.removeNextChar(appearance);

    assertEquals(26, manager.getAtomCount());
    assertEquals("test\nsome super duperthing", manager.getContent());
    assertEquals(new Dimension(30, 50), manager.getSize());

    manager.removeNextChar(appearance);

    assertEquals(25, manager.getAtomCount());
    assertEquals("test\nsome super duperhing", manager.getContent());
    assertEquals(new Dimension(30, 50), manager.getSize());

    manager.setActiveAtom(15);
    manager.removeNextChar(appearance);

    assertEquals(24, manager.getAtomCount());
    assertEquals("test\nsome superduperhing", manager.getContent());
    assertEquals(new Dimension(30, 50), manager.getSize());

    manager.removeNextChar(appearance);
    manager.removeNextChar(appearance);
    manager.removeNextChar(appearance);
    manager.removeNextChar(appearance);

    assertEquals(20, manager.getAtomCount());
    assertEquals("test\nsome superrhing", manager.getContent());
    assertEquals(new Dimension(30, 40), manager.getSize());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#removePreviousChar()}.
   */
  @Test
  public void testRemovePreviousChar()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(0);
    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(22);
    manager.removePreviousChar(appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(26, manager.getAtomCount());
    assertEquals("test\nsome super duperthing", manager.getContent());

    manager.removePreviousChar(appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(25, manager.getAtomCount());
    assertEquals("test\nsome super dupething", manager.getContent());

    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);
    manager.removePreviousChar(appearance);

    assertEquals(new Dimension(30, 40), manager.getSize());
    assertEquals(20, manager.getAtomCount());
    assertEquals("test\nsome superthing", manager.getContent());

  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#moveLeft()}.
   */
  @Test
  public void testMoveLeft()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(0);
    manager.moveLeft();
    manager.moveLeft();
    manager.moveLeft();
    assertEquals(0, manager.getActiveAtom());

    manager.setActiveAtom(27);
    manager.moveLeft();
    assertEquals(26, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(25, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(24, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(23, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(22, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(21, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(20, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(19, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(18, manager.getActiveAtom());
    manager.moveLeft();
    assertEquals(17, manager.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#moveRight()}.
   */
  @Test
  public void testMoveRight()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(27);
    manager.moveRight();
    manager.moveRight();
    manager.moveRight();
    assertEquals(27, manager.getActiveAtom());

    manager.setActiveAtom(0);
    manager.moveRight();
    assertEquals(1, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(2, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(3, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(4, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(5, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(6, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(7, manager.getActiveAtom());
    manager.moveRight();
    assertEquals(8, manager.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#moveUp()}.
   */
  @Test
  public void testMoveUp()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthingd", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(28, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthingd", manager.getContent());

    manager.setActiveAtom(28);

    assertEquals(28, manager.getActiveAtom());
    manager.moveUp(appearance);
    assertEquals(21, manager.getActiveAtom());
    manager.moveUp(appearance);
    assertEquals(15, manager.getActiveAtom());
    manager.moveUp(appearance);
    assertEquals(10, manager.getActiveAtom());
    manager.moveUp(appearance);
    assertEquals(4, manager.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#moveDown()}.
   */
  @Test
  public void testMoveDown()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.setActiveAtom(0);
    manager.moveDown(appearance);
    assertEquals(5, manager.getActiveAtom());
    manager.moveDown(appearance);
    assertEquals(10, manager.getActiveAtom());
    assertEquals(new Point(25, 10), manager.getActivePosition(appearance));

    //TODO both should be one higher (reason is that active position
    //is always from end of first if between two lines)
    manager.moveDown(appearance);
    assertEquals(15, manager.getActiveAtom());
    manager.moveDown(appearance);
    assertEquals(21, manager.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentManager#getActivePosition()}.
   */
  @Test
  public void testGetActivePosition()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    manager.setActiveAtom(0);
    assertEquals(new Point(0, 0), manager.getActivePosition(appearance));

    manager.setActiveAtom(1);
    assertEquals(new Point(5, 0), manager.getActivePosition(appearance));

    manager.setActiveAtom(2);
    assertEquals(new Point(10, 0), manager.getActivePosition(appearance));

    manager.setActiveAtom(3);
    assertEquals(new Point(15, 0), manager.getActivePosition(appearance));

    manager.setActiveAtom(4);
    assertEquals(new Point(20, 0), manager.getActivePosition(appearance));

    manager.setActiveAtom(5);
    assertEquals(new Point(0, 10), manager.getActivePosition(appearance));

    manager.setActiveAtom(10);
    assertEquals(new Point(25, 10), manager.getActivePosition(appearance));

    manager.setActiveAtom(16);
    assertEquals(new Point(30, 20), manager.getActivePosition(appearance));

    manager.setActiveAtom(27);
    assertEquals(new Point(25, 40), manager.getActivePosition(appearance));
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentUserLine#setSelection()}.
   */
  @Test
  public void testSetSelection()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);
    assertEquals(false, manager.hasSelection());

    manager.setSelection(8, 15, appearance);
    assertEquals(true, manager.hasSelection());
    assertEquals(8, manager.getSelectionStart());
    assertEquals(15, manager.getSelectionEnd());

    manager.clearSelection(appearance);
    assertEquals(false, manager.hasSelection());
    manager.setSelection(15, 150, appearance);
    assertEquals(true, manager.hasSelection());
    assertEquals(15, manager.getSelectionStart());
    assertEquals(27, manager.getSelectionEnd());

    manager.removeSelection(appearance);
    manager.setSelection(-10, 10, appearance);
    assertEquals(true, manager.hasSelection());
    assertEquals(0, manager.getSelectionStart());
    assertEquals(10, manager.getSelectionEnd());

  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentUserLine#removeLastContentLine()}.
   */
  @Test
  public void testRemoveLastContentLine()
  {
    factory.setTextSpacing(0);
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.removeLastContentLine();
    assertEquals(new Dimension(30, 40), manager.getSize());
    assertEquals(21, manager.getAtomCount());
    assertEquals("test\nsome super duper", manager.getContent());

    manager.removeLastContentLine();
    assertEquals(new Dimension(20, 10), manager.getSize());
    assertEquals(4, manager.getAtomCount());
    assertEquals("test", manager.getContent());

    manager.removeLastContentLine();
    assertEquals(new Dimension(0, 0), manager.getSize());
    assertEquals(0, manager.getAtomCount());
    assertEquals("", manager.getContent());

    manager.removeLastContentLine();
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentUserLine#removeFirstContentLine()}.
   */
  @Test
  public void testRemoveFirstContentLine()
  {
    factory.setTextSpacing(0);
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 30, appearance);
    manager.setMultiline(true);
    manager.addContent("test\nsome super duper\nthing", appearance);

    assertEquals(new Dimension(30, 50), manager.getSize());
    assertEquals(27, manager.getAtomCount());
    assertEquals("test\nsome super duper\nthing", manager.getContent());

    manager.removeFirstContentLine();
    assertEquals(new Dimension(30, 40), manager.getSize());
    assertEquals(22, manager.getAtomCount());
    assertEquals("some super duper\nthing", manager.getContent());

    manager.removeFirstContentLine();
    assertEquals(new Dimension(25, 10), manager.getSize());
    assertEquals(5, manager.getAtomCount());
    assertEquals("thing", manager.getContent());

    manager.removeFirstContentLine();
    assertEquals(new Dimension(0, 0), manager.getSize());
    assertEquals(0, manager.getAtomCount());
    assertEquals("", manager.getContent());

    manager.removeFirstContentLine();
  }

  @Test
  public void testSpecialCases()
  {
    ContentManager manager = new ContentManager(factory, appearance);
    manager.setAutoWarp(true, 5, appearance);
    manager.setMultiline(true);
    manager.addContent("fun fun line here", appearance);
    manager.removeAll(appearance);
    manager.addContent("a new line that is even more crazy as the last one!", appearance);
  }
}
