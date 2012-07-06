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
 * Created on Jul 15, 2005
 * $Id: ListExample.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.example;

import java.io.IOException;

import org.fenggui.*;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.composite.Window;

/**
 * Demonstrates the usage of lists.
 * 
 * @author Johannes Schaback 
 */
public class ListExample implements IExample
{

  /**
  * Not FengGUI related
  */
  private static final long serialVersionUID = 1L;

  private Display           desk;
  private Pixmap            pixmap           = null;

  private void buildFrame1()
  {

    Window listFrame = FengGUI.createDialog(desk);
    listFrame.setSize(150, 200);

    listFrame.setTitle("Fun with Primes");

    ScrollContainer sc = FengGUI.createScrollContainer(listFrame.getContentContainer());

    List list = FengGUI.createList(sc);

    int[] primes = getPrimeNumbers(30);
    String[] itemText = new String[primes.length];

    for (int i = 0; i < primes.length; i++)
      itemText[i] = (i + 1) + ". prime: " + primes[i];

    list.addItem(itemText);
    sc.scrollVertical(1.0d);
    listFrame.layout();
  }

  private void buildFrame2()
  {

    Window listFrame = FengGUI.createDialog(desk);
    listFrame.setSize(150, 200);

    listFrame.setTitle("List with icons");

    ScrollContainer sc = FengGUI.createScrollContainer(listFrame.getContentContainer());

    List list = FengGUI.createList(sc);

    Item[] items = new Item[15];

    for (int i = 0; i < 15; i++)
    {
      items[i] = new Item((i + 1) + ". Item", pixmap, list.getAppearance());
    }

    list.addItem(items);
    sc.scrollVertical(1.0d);
    listFrame.layout();
  }

  private int[] getPrimeNumbers(int amount)
  {

    int[] primes = new int[amount];

    int i = 0;
    int number = 2;

    while (i < amount)
    {

      boolean isPrime = true;
      for (int j = 0; j < i; j++)
      {
        if (number % primes[j] == 0)
        {
          isPrime = false;
          break;
        }
      }
      if (isPrime)
      {
        primes[i++] = number;
      }

      number++;
    }

    return primes;
  }

  public void buildGUI(Display display)
  {

    this.desk = display;

    try
    {
      ITexture texture = Binding.getInstance().getTexture("data/redCross.gif");
      pixmap = new Pixmap(texture, 1, 0, 23, 23);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    buildFrame1();
    buildFrame2();
  }

  public String getExampleName()
  {
    return "List Example";
  }

  public String getExampleDescription()
  {
    return "Demonstrates the usage of List Containers";
  }

}
