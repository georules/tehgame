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

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.DirectTextRenderer;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.Window;
import org.fenggui.event.IWindowClosedListener;
import org.fenggui.event.WindowClosedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.example.IExample;
import org.fenggui.example.chat.ChatManager.ChatMessage;
import org.fenggui.example.chat.ChatManager.IUpdateMessage;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.util.Alignment;
import org.fenggui.util.Alphabet;
import org.fenggui.util.fonttoolkit.FontFactory;

/**
 * 
 * @author Marc Menghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public class ChatExample implements IExample
{
  private static ChatManager        chatManager = new ChatManager();
  private static ChatContentFactory factory     = new ChatContentFactory();
  private static int                usernumber  = 0;
  private static ITextRenderer      normalFont  = null;
  private static ITextRenderer      boldFont    = null;

  private void staticInit()
  {
    if (normalFont != null)
      return;

    java.awt.Font awtNormal = new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 12);
    java.awt.Font awtBold = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12);

    FontFactory ffNormal = new FontFactory(Alphabet.getDefaultAlphabet(), awtNormal);
    FontFactory ffBold = new FontFactory(Alphabet.getDefaultAlphabet(), awtBold);

    ImageFont imgFontNormal = ffNormal.createFont();
    ImageFont imgFontBold = ffBold.createFont();
    normalFont = new DirectTextRenderer(imgFontNormal);
    boldFont = new DirectTextRenderer(imgFontBold);
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#buildGUI(org.fenggui.Display)
   */
  public void buildGUI(Display display)
  {
    staticInit();

    //create uniquie username (simple but works for an example ;)
    int number = usernumber;
    usernumber++;
    final String username = "Guest#" + number;

    //add chat enter message
    ChatMessage message = chatManager.new ChatMessage();
    message.userName = "";
    message.message = username + " entered chat.";
    chatManager.addMessage(message);

    //Create window
    Window w = FengGUI.createWindow(display, true, false, false, true);
    w.setTitle(getExampleName());
    w.getContentContainer().setLayoutManager(new RowExLayout(false));

    //Readonly Chat with scrolling
    ScrollContainer chatSC = FengGUI.createScrollContainer(w.getContentContainer());
    chatSC.setLayoutData(new RowExLayoutData(true, true));
    final TextEditor chat = FengGUI.createTextEditor();
    final IUpdateMessage updateListener = new IUpdateMessage()
    {
      public void update(ChatMessage newMessage)
      {
        chat.addContentAtEnd(msgToString(newMessage));

        //this needs to be here because of a bug (needs fixing)
        chat.updateMinSize();
      }

      private String msgToString(ChatMessage newMessage)
      {
        return (newMessage.userName.length() <= 0 ? "" : "[" + newMessage.userName + "] ") + newMessage.message;
      }
    };
    chat.setWordWarping(true);
    chat.setMultiline(true);
    chat.getAppearance().setAlignment(Alignment.TOP_LEFT);
    chat.setReadonly(true);
    chat.getTextRendererData().getManager().setFactory(factory, chat.getAppearance());

    chatSC.setInnerWidget(chat);

    //input field with scrolling (no scrollbars visible)
    ScrollContainer inputSC = FengGUI.createScrollContainer(w.getContentContainer());
    inputSC.setLayoutData(new RowExLayoutData(true, false));
    inputSC.setMinSize(10, 25); //set a height to the input line
    inputSC.setShowScrollbars(false);
    final TextEditor input = FengGUI.createTextEditor();
    input.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyReleasedEvent keyReleasedEvent)
      {
        if (keyReleasedEvent.getKeyClass() == Key.ENTER)
        {
          //enter pressed so add text to chat manager
          chatManager.handleInput(input.getText(), username);
          input.setText("");
        }
      }
    });
    inputSC.setInnerWidget(input);

    w.setSize(300, 300);
    w.addWindowClosedListener(new IWindowClosedListener()
    {
      public void windowClosed(WindowClosedEvent windowClosedEvent)
      {
        ChatMessage message = chatManager.new ChatMessage();
        message.userName = "";
        message.message = username + " left the chat.";
        chatManager.addMessage(message);
        chatManager.removeUpdateListener(updateListener);
      }
    });
    chatManager.addUpdateListener(updateListener);

    display.setFocusedWidget(input);
    chat.setText("Welcome to this simple chat example. Open an other one to have multiple users in this chat.");
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleDescription()
   */
  public String getExampleDescription()
  {
    return "Chat example showing text framework";
  }

  /* (non-Javadoc)
   * @see org.fenggui.example.IExample#getExampleName()
   */
  public String getExampleName()
  {
    return "Chat Example";
  }
}
