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
 * Created on Oct 21, 2008
 * $Id$
 */
package org.fenggui.example.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Johannes, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ChatManager
{
  public class ChatMessage
  {
    public String userName = "<Noname>";
    public String message  = "<Empty>";
    public Date   time     = new Date();
  }

  public interface IUpdateMessage
  {
    public void update(ChatMessage newMessage);
  }

  private List<ChatMessage>    messages   = new ArrayList<ChatMessage>();
  private boolean              showTime   = false;
  private List<IUpdateMessage> updateHook = new ArrayList<IUpdateMessage>(0);

  public void addUpdateListener(IUpdateMessage listener)
  {
    updateHook.add(listener);
  }

  public void removeUpdateListener(IUpdateMessage listener)
  {
    this.updateHook.remove(listener);
  }

  protected void fireUpdateListener(ChatMessage newMessage)
  {
    for (IUpdateMessage listener : updateHook)
    {
      listener.update(newMessage);
    }
  }

  public boolean showTime()
  {
    return showTime;
  }

  public void setShowTime(boolean showTime)
  {
    this.showTime = showTime;
  }

  public void addMessage(ChatMessage message)
  {
    messages.add(message);
    fireUpdateListener(message);
  }

  public List<ChatMessage> getChatMessages()
  {
    return messages;
  }

  public void handleInput(String input, String username)
  {
    ChatMessage message = new ChatMessage();

    if (input.startsWith("/"))
    {
      message.userName = "";
      message.message = "'" + input + "' is not a valid command.";

      //command
      if (input.equalsIgnoreCase("/clear"))
      {
        message.message = "Chat cleared.";
        messages.clear();
      }

      addMessage(message);
    }
    else
    {
      //chat input
      message.userName = username;
      message.message = input;
    }

    addMessage(message);
  }
}
