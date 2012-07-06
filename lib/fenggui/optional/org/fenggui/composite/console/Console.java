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
 * Created on Mar 9, 2007
 * $Id: Console.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite.console;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import org.fenggui.ObservableWidget;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.event.key.IKeyPressedListener;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.util.Dimension;

/**
 * A Quake-like console widget. Add own commands through <code>add</code>. 
 * <br/>
 * <br/>
 * Btw, you can forward <code>System.out</code> to the console. Oh man, I _love_ Java!
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public class Console extends ObservableWidget
{
  private ConsoleAppearance   appearance   = null;                      ;
  private ArrayList<ICommand> commands     = new ArrayList<ICommand>();
  private PrintStream         out          = null;
  public static final String  PROMPT       = ">>";
  private File                currentDir   = new File("");
  private int                 carretIndex  = -1;
  private ArrayList<String>   history      = new ArrayList<String>();
  private int                 historyIndex = 0;

  public Console()
  {
    System.err.println("Does not work anymore");
    //TODO: reenable
    //		appearance = new ConsoleAppearance(this);
    //		out = new PrintStream(new ConsoleOutputStream(appearance.getTextRenderer()));
    //		buildKeyBehavior();
    //		
    //		getAppearance().getTextRenderer().setText("Welcome to the FengGUI console, warrior!\n");
    //		getAppearance().getPromtRenderer().setText(PROMPT);
    setCarretIndex(PROMPT.length());
    setTraversable(true);

    add(new ListCommand());
    add(new PwdCommand());
    add(new ChangeDirectoryCommand());
  }

  private void buildKeyBehavior()
  {
    addKeyPressedListener(new IKeyPressedListener()
    {
      public void keyPressed(KeyPressedEvent kpe)
      {
        handleKeyPressed(kpe);
      }
    });
  }

  public String getPrompt()
  {
    //		return getAppearance().getPromtRenderer().getText();
    return "";
  }

  public void setPrompt(String s)
  {
    //		getAppearance().getPromtRenderer().setText(s);
  }

  private void handleKeyPressed(KeyPressedEvent kpe)
  {
    switch (kpe.getKeyClass())
    {
    case ENTER:
      run(getPrompt().substring(PROMPT.length(), getPrompt().length()));
      history.add(getPrompt());
      historyIndex++;
      setPrompt(PROMPT);
      carretIndex = PROMPT.length();
      historyIndex = history.size() - 1;
      break;
    case BACKSPACE:
      String s = getPrompt();
      if (carretIndex > PROMPT.length())
      {
        s = s.substring(0, s.length() - 1);
        setPrompt(s);
        carretIndex--;
      }
      break;
    case LEFT:
      s = getPrompt();
      if (carretIndex > PROMPT.length())
      {
        carretIndex--;
      }
      break;
    case RIGHT:
      s = getPrompt();
      if (carretIndex <= s.length())
      {
        carretIndex++;
      }
      break;
    case UP:
      historyIndex--;
      if (historyIndex < 0)
        historyIndex = 0;
      setPrompt(history.get(historyIndex));
      carretIndex = getPrompt().length();
      break;
    case DOWN:
      historyIndex++;
      if (historyIndex >= history.size())
      {
        setPrompt(PROMPT);
        historyIndex--;
      }
      else
        setPrompt(history.get(historyIndex));
      carretIndex = getPrompt().length();
      break;
    case DIGIT:
    case LETTER:
      s = getPrompt();
      setPrompt(s.substring(0, carretIndex) + kpe.getKey() + s.substring(carretIndex, s.length()));
      carretIndex++;
      break;
    }
    getAppearance().getCarretTimer().reset();

  }

  public void add(ICommand command)
  {
    commands.add(command);
  }

  public void remove(ICommand command)
  {
    commands.remove(command);
  }

  public void removeAll()
  {
    commands.clear();
  }

  @Override
  public ConsoleAppearance getAppearance()
  {
    return appearance;
  }

  public void run(String commandLine)
  {
    getOut().println(PROMPT + commandLine);
    String[] split = commandLine.split(" ");

    ICommand command = getCommand(split[0]);

    if (command == null)
    {
      getOut().println("Command \"" + split[0] + "\" not recognized!");
    }
    else
    {
      command.execute(getOut(), this, split);
    }
  }

  public ICommand getCommand(String command)
  {
    for (ICommand c : commands)
    {
      if (c.getCommand().equals(command))
        return c;
    }

    return null;
  }

  public PrintStream getOut()
  {
    return out;
  }

  public File getCurrentDir()
  {
    return currentDir;
  }

  public void setCurrentDir(File currentDir)
  {
    this.currentDir = currentDir;
  }

  public int getCarretIndex()
  {
    return carretIndex;
  }

  public void setCarretIndex(int carretIndex)
  {
    this.carretIndex = carretIndex;
  }

  @Override
  public Dimension getMinContentSize()
  {
    return null;
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    //		g.setColor(textColor);
    //		promtRenderer.render(0, 0, g, gl);
    //		if(carretTimer.getState() == 0 && widget.hasFocus())
    //			promtRenderer.renderCarret(0, 0, widget.getCarretIndex()-1, carretRenderer, g, gl);
    //		textRenderer.render(0, promtRenderer.getHeight(), g, gl);
  }

}
