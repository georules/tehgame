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
package org.fenggui.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.fenggui.text.content.ContentLine;
import org.fenggui.text.content.IContentFactory;
import org.fenggui.text.content.part.AbstractContentPart;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marc Menghin
 * 
 */
public class ContentLineTest extends ContentTestBase
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
  * Test method for
  * {@link org.fenggui.text.content.ContentLine#ContentLine(org.fenggui.text.content.IContentFactory, boolean)}.
  */
  @Test
  public void testContentLineIContentFactoryBoolean()
  {

    ContentLine line = new ContentLine(factory.getEmptyContentPart(appearance));
    assertEquals(0, line.getAtomCount());
    assertEquals("", getContent(line));
    assertEquals(1, line.getPartCount());
    assertEquals(new Dimension(0, 10), line.getSize());
    assertEquals(false, line.hasActivePart());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#ContentLine(java.util.List)}.
   */
  @Test
  public void testContentLineListOfAbstractContentPart()
  {
    ArrayList<AbstractContentPart> empty = new ArrayList<AbstractContentPart>();
    ArrayList<AbstractContentPart> line = new ArrayList<AbstractContentPart>();
    line.add(factory.getEmptyContentPart(appearance));

    ContentLine emptyLine = new ContentLine(empty);
    assertTrue(emptyLine.getAtomCount() == 0);
    assertTrue(getContent(emptyLine).equals(""));
    assertTrue(emptyLine.getSize().equals(new Dimension(0, 0)));

    ContentLine lineLine = new ContentLine(line);
    assertTrue(lineLine.getAtomCount() == 0);
    assertTrue(getContent(lineLine).equals(""));
    assertTrue(lineLine.getSize().equals(new Dimension(0, 10)));
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#getContent()}.
   */
  @Test
  public void testGetContent()
  {
    ContentLine emptyLine = fillWithAmount(0, 0);
    assertEquals(0, emptyLine.getAtomCount());
    assertEquals("", getContent(emptyLine));
    assertEquals(new Dimension(0, 0), emptyLine.getSize());

    ContentLine lineLine1 = fillWithAmount(0, 1);
    assertEquals(12, lineLine1.getAtomCount());
    assertEquals("testpart> 0 ", getContent(lineLine1));
    assertEquals(1, lineLine1.getPartCount());
    assertEquals(new Dimension(60, 10), lineLine1.getSize());

    ContentLine lineLine10 = fillWithAmount(0, 10);
    assertEquals(120, lineLine10.getAtomCount());
    assertEquals(
      "testpart> 0 testpart> 1 testpart> 2 testpart> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ",
      getContent(lineLine10));
    assertEquals(new Dimension(600, 10), lineLine10.getSize());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#isEmpty()}.
   */
  @Test
  public void testIsEmpty()
  {
    ContentLine emptyLine = new ContentLine(factory.getEmptyContentPart(appearance));
    assertEquals(true, emptyLine.isEmpty());

    ContentLine fullLine = fillWithAmount(0, 1);
    assertEquals(false, fullLine.isEmpty());
    fullLine.optimizeContent(appearance);
    assertEquals(false, fullLine.isEmpty());

    ContentLine fullLine2 = fillWithAmount(0, 5);
    assertEquals(false, fullLine2.isEmpty());

    fullLine2.optimizeContent(appearance);
    assertEquals(false, fullLine2.isEmpty());
    fullLine2.setActiveAtom(20);
    ContentLine rest = fullLine2.splitActive(factory, appearance);
    assertEquals(false, fullLine2.isEmpty());
    assertEquals(false, rest.isEmpty());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#splitActive(org.fenggui.text.content.IContentFactory, boolean)}.
   */
  @Test
  public void testSplitActive()
  {
    ContentLine lineLine10 = fillWithAmount(0, 10);
    lineLine10.setActiveAtom(30);

    assertEquals(lineLine10.getAtomCount(), 120);
    assertEquals(
      "testpart> 0 testpart> 1 testpart> 2 testpart> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ",
      getContent(lineLine10));
    assertEquals(new Dimension(600, 10), lineLine10.getSize());
    assertEquals(true, lineLine10.hasActivePart());

    //-- split at end of a textpart --
    assertNotNull(lineLine10.setActiveAtom(12));
    assertEquals(true, lineLine10.hasActivePart());
    ContentLine rest = lineLine10.splitActive(factory, appearance);

    assertEquals("testpart> 0 ", getContent(lineLine10));
    assertEquals(new Dimension(60, 10), lineLine10.getSize());
    assertEquals(12, lineLine10.getAtomCount());
    assertEquals(false, lineLine10.hasActivePart());

    assertEquals(
      "testpart> 1 testpart> 2 testpart> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ",
      getContent(rest));
    assertEquals(new Dimension(540, 10), rest.getSize());
    assertEquals(108, rest.getAtomCount());
    assertEquals(true, rest.hasActivePart());

    //-- split middle in a textpart --		
    assertNotNull(rest.setActiveAtom(29));
    assertEquals(true, rest.hasActivePart());
    ContentLine rest2 = rest.splitActive(factory, appearance);

    //cant split at that position and not before so full textpart is removed
    assertEquals("testpart> 1 testpart> 2 testp", getContent(rest));
    assertEquals(new Dimension(145, 10), rest.getSize());
    assertEquals(29, rest.getAtomCount());
    assertEquals(false, rest.hasActivePart());

    assertEquals("art> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ", getContent(rest2));
    assertEquals(new Dimension(395, 10), rest2.getSize());
    assertEquals(79, rest2.getAtomCount());
    assertEquals(true, rest2.hasActivePart());

    //-- split at start --
    assertNotNull(rest2.setActiveAtom(0));
    assertEquals(true, rest2.hasActivePart());
    ContentLine rest3 = rest2.splitActive(factory, appearance);

    assertEquals("", getContent(rest2));
    assertEquals(new Dimension(0, 10), rest2.getSize());
    assertEquals(true, rest2.isEmpty());
    assertEquals(0, rest2.getAtomCount());
    assertEquals(false, rest2.hasActivePart());

    assertEquals("art> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ", getContent(rest3));
    assertEquals(new Dimension(395, 10), rest3.getSize());
    assertEquals(false, rest3.isEmpty());
    assertEquals(79, rest3.getAtomCount());
    assertEquals(true, rest3.hasActivePart());

    //-- split at end --
    assertNotNull(rest3.setActiveAtom((int) rest3.getAtomCount()));
    assertEquals(true, rest3.hasActivePart());
    ContentLine rest4 = rest3.splitActive(factory, appearance);

    assertEquals("art> 3 testpart> 4 testpart> 5 testpart> 6 testpart> 7 testpart> 8 testpart> 9 ", getContent(rest3));
    assertEquals(new Dimension(395, 10), rest3.getSize());
    assertEquals(false, rest3.isEmpty());
    assertEquals(79, rest3.getAtomCount());
    assertEquals(false, rest3.hasActivePart());

    assertEquals("", getContent(rest4));
    assertEquals(true, rest4.isEmpty());
    assertEquals(new Dimension(0, 10), rest4.getSize());
    assertEquals(0, rest4.getAtomCount());
    assertEquals(true, rest4.hasActivePart());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#removeAll()}.
   */
  @Test
  public void testRemoveAll()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);
    lineLine05.removeAll(this.factory, appearance);
    assertEquals("", getContent(lineLine05));
    assertEquals(true, lineLine05.isEmpty());
    assertEquals(new Dimension(0, 10), lineLine05.getSize());
    assertEquals(0, lineLine05.getAtomCount());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#splitAt(int)}.
   */
  @Test
  public void testSplitAt()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);

    assertEquals(1, lineLine05.getPartCount());

    //splits too early rest should be all content
    ContentLine rest = lineLine05.splitAt(10, factory, appearance);

    assertEquals("te", getContent(lineLine05));
    assertEquals(new Dimension(10, 10), lineLine05.getSize());
    assertEquals(2, lineLine05.getAtomCount());
    assertEquals(1, lineLine05.getPartCount());

    assertEquals("stpart> 0 testpart> 1 testpart> 2 testpart> 3 testpart> 4 ", getContent(rest));
    assertEquals(new Dimension(290, 10), rest.getSize());
    assertEquals(58, rest.getAtomCount());
    assertEquals(1, rest.getPartCount());

    //splits between to parts
    lineLine05 = fillWithAmount(0, 5);
    ContentLine rest2 = lineLine05.splitAt(60, factory, appearance);

    assertEquals("testpart> 0 ", getContent(lineLine05));
    assertEquals(new Dimension(60, 10), lineLine05.getSize());
    assertEquals(12, lineLine05.getAtomCount());

    assertEquals("testpart> 1 testpart> 2 testpart> 3 testpart> 4 ", getContent(rest2));
    assertEquals(new Dimension(240, 10), rest2.getSize());
    assertEquals(48, rest2.getAtomCount());

    //splits part (before space)
    lineLine05 = fillWithAmount(0, 5);
    rest2 = lineLine05.splitAt(105, factory, appearance);

    assertEquals("testpart> 0 testpart>", getContent(lineLine05));
    assertEquals(new Dimension(105, 10), lineLine05.getSize());
    assertEquals(21, lineLine05.getAtomCount());

    assertEquals(" 1 testpart> 2 testpart> 3 testpart> 4 ", getContent(rest2));
    assertEquals(new Dimension(195, 10), rest2.getSize());
    assertEquals(39, rest2.getAtomCount());

    //splits part (after space)
    lineLine05 = fillWithAmount(0, 5);
    rest2 = lineLine05.splitAt(110, factory, appearance);

    assertEquals("testpart> 0 testpart> ", getContent(lineLine05));
    assertEquals(new Dimension(110, 10), lineLine05.getSize());
    assertEquals(22, lineLine05.getAtomCount());

    assertEquals("1 testpart> 2 testpart> 3 testpart> 4 ", getContent(rest2));
    assertEquals(new Dimension(190, 10), rest2.getSize());
    assertEquals(38, rest2.getAtomCount());

    //splits at end
    lineLine05 = fillWithAmount(0, 5);
    rest2 = lineLine05.splitAt(300, factory, appearance);

    assertEquals("testpart> 0 testpart> 1 testpart> 2 testpart> 3 testpart> 4 ", getContent(lineLine05));
    assertEquals(new Dimension(300, 10), lineLine05.getSize());
    assertEquals(60, lineLine05.getAtomCount());

    assertEquals("", getContent(rest2));
    assertEquals(new Dimension(0, 10), rest2.getSize());
    assertEquals(0, rest2.getAtomCount());

    //try to splitt too long word
    lineLine05 = fillWithAmount(0, 1);
    rest2 = lineLine05.splitAt(20, factory, appearance);

    assertEquals("test", getContent(lineLine05));
    assertEquals(new Dimension(20, 10), lineLine05.getSize());
    assertEquals(4, lineLine05.getAtomCount());

    assertEquals("part> 0 ", getContent(rest2));
    assertEquals(new Dimension(40, 10), rest2.getSize());
    assertEquals(8, rest2.getAtomCount());

    //try to splitt too long word
    lineLine05 = fillWithAmount(0, 1);
    rest2 = lineLine05.splitAt(20, factory, appearance);

    assertEquals("test", getContent(lineLine05));
    assertEquals(new Dimension(20, 10), lineLine05.getSize());
    assertEquals(4, lineLine05.getAtomCount());

    //check splitting of first part
    lineLine05 = getLine("test test");
    rest2 = lineLine05.splitAt(35, factory, appearance);

    assertEquals("test ", getContent(lineLine05));
    assertEquals(new Dimension(25, 10), lineLine05.getSize());
    assertEquals(5, lineLine05.getAtomCount());

    assertEquals("test", getContent(rest2));
    assertEquals(new Dimension(20, 10), rest2.getSize());
    assertEquals(4, rest2.getAtomCount());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#calculatePositionInAtoms(int)}.
   */
  @Test
  public void testCalculatePositionInAtoms()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);

    assertEquals(2, lineLine05.calculatePositionInAtoms(10, appearance)); //somewhere
    assertEquals(58, lineLine05.calculatePositionInAtoms(292, appearance));//other2 max
    assertEquals(59, lineLine05.calculatePositionInAtoms(293, appearance));//other min
    assertEquals(59, lineLine05.calculatePositionInAtoms(295, appearance));//other middle
    assertEquals(59, lineLine05.calculatePositionInAtoms(297, appearance));//other max
    assertEquals(60, lineLine05.calculatePositionInAtoms(298, appearance));//end min
    assertEquals(60, lineLine05.calculatePositionInAtoms(300, appearance));//end
    assertEquals(60, lineLine05.calculatePositionInAtoms(301, appearance));//over end
    assertEquals(60, lineLine05.calculatePositionInAtoms(360, appearance));//over end
    assertEquals(0, lineLine05.calculatePositionInAtoms(0, appearance)); //start
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#clearActiveContentPart()}.
   */
  @Test
  public void testClearActiveContentPart()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);

    assertEquals(false, lineLine05.hasActivePart());
    lineLine05.clearActiveContentPart(); //call with no contentPart active
    assertEquals(false, lineLine05.hasActivePart());
    lineLine05.setActiveAtom(10);
    assertEquals(true, lineLine05.hasActivePart());
    lineLine05.clearActiveContentPart(); //call with an active contentPart
    assertEquals(false, lineLine05.hasActivePart());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#addChar(char)}.
   */
  @Test
  public void testAddChar()
  {
    ContentLine lineLine05 = fillWithAmount(0, 3);

    lineLine05.setActiveAtom(0);
    lineLine05.addChar('_', appearance);

    assertEquals("_testpart> 0 testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(37, lineLine05.getAtomCount());
    assertEquals(new Dimension(185, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(13);
    lineLine05.addChar('_', appearance);

    assertEquals("_testpart> 0 _testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(38, lineLine05.getAtomCount());
    assertEquals(new Dimension(190, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(10);
    lineLine05.addChar('_', appearance);

    assertEquals("_testpart>_ 0 _testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(39, lineLine05.getAtomCount());
    assertEquals(new Dimension(195, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(16);
    lineLine05.addChar('_', appearance);

    assertEquals("_testpart>_ 0 _t_estpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(40, lineLine05.getAtomCount());
    assertEquals(new Dimension(200, 10), lineLine05.getSize());

    lineLine05.addChar('+', appearance);

    assertEquals("_testpart>_ 0 _t_+estpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(41, lineLine05.getAtomCount());
    assertEquals(new Dimension(205, 10), lineLine05.getSize());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#addContent(string, IContentFactory)}.
   */
  @Test
  public void testAddContent()
  {
    ContentLine line = fillWithAmount(0, 3);
    line.setActiveAtom(0);

    line.addContent("[0]", 1, false, factory, appearance);
    assertEquals("[0]testpart> 0 testpart> 1 testpart> 2 ", getContent(line));
    assertEquals(39, line.getAtomCount());
    assertEquals(3, line.getActivePosition());
    assertEquals(new Dimension(195, 10), line.getSize());

    line.addContent("[1]", 1, false, factory, appearance);
    assertEquals("[0][1]testpart> 0 testpart> 1 testpart> 2 ", getContent(line));
    assertEquals(42, line.getAtomCount());
    assertEquals(6, line.getActivePosition());
    assertEquals(new Dimension(210, 10), line.getSize());

    line.setActiveAtom(25);
    line.addContent("[2]", 1, false, factory, appearance);
    assertEquals("[0][1]testpart> 0 testpar[2]t> 1 testpart> 2 ", getContent(line));
    assertEquals(45, line.getAtomCount());
    assertEquals(new Dimension(225, 10), line.getSize());

    line.setActiveAtom(33);
    line.addContent("[3]", 1, false, factory, appearance);
    assertEquals("[0][1]testpart> 0 testpar[2]t> 1 [3]testpart> 2 ", getContent(line));
    assertEquals(48, line.getAtomCount());
    assertEquals(new Dimension(240, 10), line.getSize());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#removeNextChar()}.
   */
  @Test
  public void testRemoveNextChar()
  {
    ContentLine lineLine05 = fillWithAmount(0, 3);

    lineLine05.setActiveAtom(0);
    lineLine05.removeNextChar(appearance);

    assertEquals("estpart> 0 testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(35, lineLine05.getAtomCount());
    assertEquals(new Dimension(175, 10), lineLine05.getSize());

    lineLine05.removeNextChar(appearance);

    assertEquals("stpart> 0 testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(34, lineLine05.getAtomCount());
    assertEquals(new Dimension(170, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(10);
    lineLine05.removeNextChar(appearance);

    assertEquals("stpart> 0 estpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(33, lineLine05.getAtomCount());
    assertEquals(new Dimension(165, 10), lineLine05.getSize());

    lineLine05.removeNextChar(appearance);

    assertEquals("stpart> 0 stpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(32, lineLine05.getAtomCount());
    assertEquals(new Dimension(160, 10), lineLine05.getSize());

    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);
    lineLine05.removeNextChar(appearance);

    assertEquals("stpart> 0 testpart> 2 ", getContent(lineLine05));
    assertEquals(22, lineLine05.getAtomCount());
    assertEquals(new Dimension(110, 10), lineLine05.getSize());

    lineLine05.removeNextChar(appearance);

    assertEquals("stpart> 0 estpart> 2 ", getContent(lineLine05));
    assertEquals(21, lineLine05.getAtomCount());
    assertEquals(new Dimension(105, 10), lineLine05.getSize());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#removePreviousChar()}.
   */
  @Test
  public void testRemovePreviousChar()
  {
    ContentLine lineLine05 = fillWithAmount(0, 3);

    lineLine05.setActiveAtom(0);
    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> 0 testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(36, lineLine05.getAtomCount());
    assertEquals(new Dimension(180, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(12);
    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> 0testpart> 1 testpart> 2 ", getContent(lineLine05));
    assertEquals(35, lineLine05.getAtomCount());
    assertEquals(new Dimension(175, 10), lineLine05.getSize());

    lineLine05.setActiveAtom(24);
    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> 0testpart> 1 estpart> 2 ", getContent(lineLine05));
    assertEquals(34, lineLine05.getAtomCount());
    assertEquals(new Dimension(170, 10), lineLine05.getSize());

    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> 0testpart> 1estpart> 2 ", getContent(lineLine05));
    assertEquals(33, lineLine05.getAtomCount());
    assertEquals(new Dimension(165, 10), lineLine05.getSize());

    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);
    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> 0estpart> 2 ", getContent(lineLine05));
    assertEquals(22, lineLine05.getAtomCount());
    assertEquals(new Dimension(110, 10), lineLine05.getSize());

    lineLine05.removePreviousChar(appearance);

    assertEquals("testpart> estpart> 2 ", getContent(lineLine05));
    assertEquals(21, lineLine05.getAtomCount());
    assertEquals(new Dimension(105, 10), lineLine05.getSize());
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#getActivePosition()}.
   */
  @Test
  public void testGetActivePosition()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);

    lineLine05.setActiveAtom(10);
    assertEquals(new Point(50, 0), lineLine05.getActivePosition(appearance));

    lineLine05.setActiveAtom(30);
    assertEquals(new Point(150, 0), lineLine05.getActivePosition(appearance));

    lineLine05.setActiveAtom(0);
    assertEquals(new Point(0, 0), lineLine05.getActivePosition(appearance));

    lineLine05.setActiveAtom(60);
    assertEquals(new Point(300, 0), lineLine05.getActivePosition(appearance));
  }

  /**
   * Test method for
   * {@link org.fenggui.text.content.ContentLine#setSelection()}.
   */
  @Test
  public void testSetSelection()
  {
    ContentLine lineLine05 = fillWithAmount(0, 5);
    lineLine05.setActiveAtom(10);
    assertEquals(false, lineLine05.hasSelection());

    lineLine05.setSelection(0, 10, appearance);
    assertEquals(true, lineLine05.hasSelection());
    assertEquals(0, lineLine05.getSelectionStart());
    assertEquals(10, lineLine05.getSelectionEnd());

    lineLine05.clearSelection(appearance);
    assertEquals(false, lineLine05.hasSelection());
    lineLine05.setSelection(5, 10, appearance);
    assertEquals(true, lineLine05.hasSelection());
    assertEquals(5, lineLine05.getSelectionStart());
    assertEquals(10, lineLine05.getSelectionEnd());

    lineLine05.clearSelection(appearance);
    lineLine05.setSelection(10, 30, appearance);
    assertEquals(true, lineLine05.hasSelection());
    assertEquals(10, lineLine05.getSelectionStart());
    assertEquals(30, lineLine05.getSelectionEnd());

    lineLine05.clearSelection(appearance);
    lineLine05.setSelection(-10, 300, appearance);
    assertEquals(true, lineLine05.hasSelection());
    assertEquals(0, lineLine05.getSelectionStart());
    assertEquals(60, lineLine05.getSelectionEnd());
  }

}
