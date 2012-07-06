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
 * Created on 21.02.2008
 * $Id$
 */
package org.fenggui.unit;

import static org.junit.Assert.assertEquals;

import org.fenggui.text.content.ContentLine;
import org.fenggui.text.content.ContentUserLine;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ContentUserLineTest extends ContentTestBase
{

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    init();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#ContentUserLine(org.fenggui.binding.render.text.advanced.IContentFactory, boolean)}.
   */
  @Test
  public void testContentUserLine()
  {
    ContentUserLine line = new ContentUserLine(factory.getEmptyContentPart(appearance));

    assertEquals(0, line.getAtomCount());
    assertEquals(new Dimension(0, 10), line.getSize());
    assertEquals(false, line.hasActiveLine());
    assertEquals("", getContent(line));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#update()}.
   */
  @Test
  public void testUpdate()
  {
    ContentUserLine line = new ContentUserLine(factory.getEmptyContentPart(appearance));

    assertEquals(0, line.getAtomCount());
    assertEquals(new Dimension(0, 10), line.getSize());
    assertEquals(false, line.hasActiveLine());
    assertEquals("", getContent(line));
    assertEquals(true, line.isEmpty());

    ContentUserLine line2 = new ContentUserLine(factory.getEmptyContentPart(appearance));
    line2.add(new ContentLine(factory.getEmptyContentPart(appearance)));

    assertEquals(0, line2.getAtomCount());
    assertEquals(new Dimension(0, 20), line2.getSize());
    assertEquals(false, line2.hasActiveLine());
    assertEquals("", getContent(line2));
    assertEquals(false, line2.isEmpty());

    line2.add(getLine("funky X "));
    line2.add(getLine("second Y"));

    assertEquals(16, line2.getAtomCount());
    assertEquals(new Dimension(40, 40), line2.getSize());
    assertEquals(false, line2.hasActiveLine());
    assertEquals("funky X second Y", getContent(line2));
    assertEquals(false, line2.isEmpty());

    line2.UpdateContent(10, true, factory, appearance);

    assertEquals(16, line2.getAtomCount());
    assertEquals(new Dimension(10, 80), line2.getSize());
    assertEquals(false, line2.hasActiveLine());
    assertEquals("funky X second Y", getContent(line2));
    assertEquals(false, line2.isEmpty());

    line2.UpdateContent(2, true, factory, appearance);
    assertEquals(16, line2.getAtomCount());
    assertEquals(new Dimension(5, 160), line2.getSize());
    assertEquals(false, line2.hasActiveLine());
    assertEquals("funky X second Y", getContent(line2));
    assertEquals(false, line2.isEmpty());

  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#add(org.fenggui.text.content.ContentLine)}.
   */
  @Test
  public void testAdd()
  {
    ContentUserLine line2 = new ContentUserLine(factory.getEmptyContentPart(appearance));
    line2.add(getLine("line"));

    assertEquals(4, line2.getAtomCount());
    assertEquals(new Dimension(20, 20), line2.getSize());
    assertEquals(false, line2.hasActiveLine());
    assertEquals("line", getContent(line2));
    assertEquals(1, line2.getLinePartCount(0));
    assertEquals(1, line2.getLinePartCount(1));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#getSelectedLine()}.
   */
  @Test
  public void testGetSelectedLine()
  {
    ContentUserLine line = new ContentUserLine(factory.getEmptyContentPart(appearance));
    ContentLine line1 = getLine("line 1");
    ContentLine line2 = getLine("line 2");
    line.add(line1);
    line.add(line2);

    line.setActiveAtom(0);
    assertEquals("", getContent(line.getSelectedLine()));

    line.setActiveAtom(1);
    assertEquals("line 1", getContent(line.getSelectedLine()));

    line.setActiveAtom(6);
    assertEquals("line 1", getContent(line.getSelectedLine()));

    line.setActiveAtom(7);
    assertEquals("line 2", getContent(line.getSelectedLine()));

    line.setActiveAtom(12);
    assertEquals("line 2", getContent(line.getSelectedLine()));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#findAtomOnPosition(int, int)}.
   */
  @Test
  public void testFindAtomOnPosition()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });

    assertEquals(0, line.findAtomOnPosition(2, 0, appearance));
    assertEquals(0, line.findAtomOnPosition(2, 9, appearance));
    assertEquals(6, line.findAtomOnPosition(0, 10, appearance));
    assertEquals(6, line.findAtomOnPosition(2, 15, appearance));
    assertEquals(7, line.findAtomOnPosition(3, 15, appearance));
    assertEquals(14, line.findAtomOnPosition(10, 21, appearance));
    assertEquals(0, line.findAtomOnPosition(0, 0, appearance));
    assertEquals(0, line.findAtomOnPosition(-2, 0, appearance));
    assertEquals(0, line.findAtomOnPosition(0, -2, appearance));
    assertEquals(0, line.findAtomOnPosition(-2, -2, appearance));
    assertEquals(18, line.findAtomOnPosition(30, 30, appearance));
    assertEquals(18, line.findAtomOnPosition(50, 30, appearance));
    assertEquals(18, line.findAtomOnPosition(30, 50, appearance));
    assertEquals(18, line.findAtomOnPosition(50, 50, appearance));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#splitActive(org.fenggui.binding.render.text.advanced.IContentFactory, boolean)}.
   */
  @Test
  public void testSplitActive()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(0);

    assertEquals(1, line.getLinePartCount(0));

    ContentUserLine line2 = line.splitActive(factory, appearance);

    assertEquals("", getContent(line));
    assertEquals(false, line.hasActiveLine());
    assertEquals(1, line.getLinePartCount(0));

    assertEquals("line 1line 2line 3", getContent(line2));
    assertEquals(true, line2.hasActiveLine());
    assertEquals(1, line2.getLinePartCount(0));

    line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(6);

    line2 = line.splitActive(factory, appearance);

    assertEquals("line 1", getContent(line));
    assertEquals(false, line.hasActiveLine());

    assertEquals("line 2line 3", getContent(line2));
    assertEquals(true, line2.hasActiveLine());

    line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(10);

    line2 = line.splitActive(factory, appearance);

    assertEquals("line 1line", getContent(line));
    assertEquals(false, line.hasActiveLine());

    assertEquals(" 2line 3", getContent(line2));
    assertEquals(true, line2.hasActiveLine());

    line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(18);

    line2 = line.splitActive(factory, appearance);

    assertEquals("line 1line 2line 3", getContent(line));
    assertEquals(false, line.hasActiveLine());

    assertEquals("", getContent(line2));
    assertEquals(true, line2.hasActiveLine());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#mergeUserLine(org.fenggui.text.content.ContentUserLine)}.
   */
  @Test
  public void testMergeUserLine()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2" });
    ContentUserLine line2 = getUserLine(new String[] { "line 3", "line 4" });
    ContentUserLine line3 = getUserLine(new String[] { "line 5", "line 6" });

    line2.setActiveAtom(8);
    line.mergeContent(line2);

    assertEquals("line 1line 2line 3line 4", getContent(line));
    assertEquals(new Dimension(30, 40), line.getSize());
    assertEquals(24, line.getAtomCount());
    assertEquals(true, line.hasActiveLine());
    assertEquals(1, line.getLinePartCount(0));

    line.setActiveAtom(-1);
    assertEquals(false, line.hasActiveLine());

    line3.setActiveAtom(4);
    line.mergeContent(line3);

    assertEquals("line 1line 2line 3line 4line 5line 6", getContent(line));
    assertEquals(new Dimension(30, 60), line.getSize());
    assertEquals(36, line.getAtomCount());
    assertEquals(true, line.hasActiveLine());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#UpdateContent(int, boolean, org.fenggui.binding.render.text.advanced.IContentFactory)}.
   */
  @Test
  public void testUpdateContent()
  {
    ContentUserLine line = getUserLine(new String[] { "lin 1 lin 2 lin 3" });
    assertEquals(1, line.getLinePartCount(0));
    assertEquals(1, line.getContentLineCount());

    line.UpdateContent(30, true, factory, appearance);
    line.setActiveAtom(11);

    assertEquals(3, line.getContentLineCount());
    assertEquals(1, line.getLinePartCount(0));
    assertEquals(1, line.getLinePartCount(1));
    assertEquals(1, line.getLinePartCount(2));

    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(17, line.getAtomCount());
    assertEquals(true, line.hasActiveLine());

    line.UpdateContent(30, false, factory, appearance);

    assertEquals(new Dimension(85, 10), line.getSize());
    assertEquals(17, line.getAtomCount());
    assertEquals(true, line.hasActiveLine());

    assertEquals(1, line.getContentLineCount());
    assertEquals(1, line.getLinePartCount(0));

    line.UpdateContent(60, true, factory, appearance);

    assertEquals(new Dimension(60, 20), line.getSize());
    assertEquals(17, line.getAtomCount());
    assertEquals(true, line.hasActiveLine());

    assertEquals(2, line.getContentLineCount());
    assertEquals(1, line.getLinePartCount(0));
    assertEquals(1, line.getLinePartCount(1));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#addChar(char)}.
   */
  @Test
  public void testAddChar()
  {
    ContentUserLine result = new ContentUserLine(factory.getEmptyContentPart(appearance));
    result.setActiveAtom(0);
    result.addChar('_', appearance);
    result.addChar('+', appearance);
    result.addChar('_', appearance);
    result.addChar('*', appearance);
    result.addChar('_', appearance);

    assertEquals(new Dimension(25, 10), result.getSize());
    assertEquals("_+_*_", getContent(result));
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#addContent(String, IContentFactory)}.
   */
  @Test
  public void testAddContent()
  {
    ContentUserLine result = new ContentUserLine(factory.getEmptyContentPart(appearance));
    result.setActiveAtom(0);
    result.addContent("test", 1, false, factory, appearance);
    assertEquals("test", getContent(result));
    assertEquals(4, result.getAtomCount());
    assertEquals(new Dimension(20, 10), result.getSize());
  }

  /**
  * Test method for {@link org.fenggui.text.content.ContentUserLine#removeNextChar()}.
  */
  @Test
  public void testRemoveNextChar()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(0);

    line.removeNextChar(appearance);
    assertEquals("ine 1line 2line 3", getContent(line));
    assertEquals(17, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removeNextChar(appearance);
    assertEquals("ne 1line 2line 3", getContent(line));
    assertEquals(16, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.setActiveAtom(16);
    line.removeNextChar(appearance);
    assertEquals("ne 1line 2line 3", getContent(line));
    assertEquals(16, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.setActiveAtom(9);
    line.removeNextChar(appearance);
    assertEquals("ne 1line line 3", getContent(line));
    assertEquals(15, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removeNextChar(appearance);
    assertEquals("ne 1line ine 3", getContent(line));
    assertEquals(14, line.getAtomCount());
    assertEquals(new Dimension(25, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removeNextChar(appearance);
    assertEquals("ne 1line ne 3", getContent(line));
    assertEquals(13, line.getAtomCount());
    assertEquals(new Dimension(25, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#removePreviousChar()}.
   */
  @Test
  public void testRemovePreviousChar()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    line.setActiveAtom(0);

    line.removePreviousChar(appearance);
    assertEquals("line 1line 2line 3", getContent(line));
    assertEquals(18, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.setActiveAtom(18);
    line.removePreviousChar(appearance);
    assertEquals("line 1line 2line ", getContent(line));
    assertEquals(17, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removePreviousChar(appearance);
    assertEquals("line 1line 2line", getContent(line));
    assertEquals(16, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.setActiveAtom(12);

    line.removePreviousChar(appearance);
    assertEquals("line 1line line", getContent(line));
    assertEquals(15, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removePreviousChar(appearance);
    line.removePreviousChar(appearance);
    line.removePreviousChar(appearance);
    line.removePreviousChar(appearance);
    line.removePreviousChar(appearance);

    assertEquals("line 1line", getContent(line));
    assertEquals(10, line.getAtomCount());
    assertEquals(new Dimension(30, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());

    line.removePreviousChar(appearance);
    assertEquals("line line", getContent(line));
    assertEquals(9, line.getAtomCount());
    assertEquals(new Dimension(25, 30), line.getSize());
    assertEquals(true, line.hasActiveLine());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#setActiveAtom(int)}.
   */
  @Test
  public void testSetActiveAtom()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });

    line.setActiveAtom(10);
    assertEquals(true, line.hasActiveLine());
    assertEquals(10, line.getActiveAtom());

    line.setActiveAtom((int) line.getAtomCount());
    assertEquals(true, line.hasActiveLine());
    assertEquals((int) line.getAtomCount(), line.getActiveAtom());
  }

  /**
   * Test method for {@link org.fenggui.text.content.ContentUserLine#getActivePosition()}.
   */
  @Test
  public void testGetActivePosition()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });

    line.setActiveAtom(10);
    assertEquals(new Point(20, 10), line.getActivePosition(appearance));

    line.setActiveAtom(11);
    assertEquals(new Point(25, 10), line.getActivePosition(appearance));

    line.setActiveAtom(18);
    assertEquals(new Point(30, 20), line.getActivePosition(appearance));

    line.setActiveAtom(0);
    assertEquals(new Point(0, 0), line.getActivePosition(appearance));
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentUserLine#setSelection()}.
   */
  @Test
  public void testSetSelection()
  {
    ContentUserLine line = getUserLine(new String[] { "line 1", "line 2", "line 3" });
    assertEquals(false, line.hasSelection());

    line.setSelection(8, 15, appearance);
    assertEquals(true, line.hasSelection());
    assertEquals(8, line.getSelectionStart());
    assertEquals(15, line.getSelectionEnd());

    line.clearSelection(appearance);
    assertEquals(false, line.hasSelection());
    line.setSelection(15, 150, appearance);
    assertEquals(true, line.hasSelection());
    assertEquals(15, line.getSelectionStart());
    assertEquals(18, line.getSelectionEnd());

    line.removeSelection(factory, appearance);
    line.setSelection(-10, 10, appearance);
    assertEquals(true, line.hasSelection());
    assertEquals(0, line.getSelectionStart());
    assertEquals(10, line.getSelectionEnd());

  }
}
