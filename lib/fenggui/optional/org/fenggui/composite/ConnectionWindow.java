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
 * $Id: ConnectionWindow.java 606 2009-03-13 14:56:05Z marcmenghin $
 */
package org.fenggui.composite;

import org.fenggui.*;
import org.fenggui.decorator.border.TitledBorder;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.layout.GridLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Spacing;

/**
 * Window that contains TextFields to enter an address, port, user name
 * and password. It also has a label to notify the user about something.
 * 
 * @author Johannes Schaback, last edtir by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 *
 */
public class ConnectionWindow extends Window
{

  private TextEditor addressTextField, portTextField, loginNameTextField, passwordTextField;

  private Label      statusLabel;

  private Container  addressContainer;
  private Container  loginContainer;
  private Button     connectButton, cancelButton;

  /**
   * Creates a new <code>ConnectionWindow</code>.
   * @param closeBtn if this Window has a close button
   */
  public ConnectionWindow(boolean closeBtn)
  {
    super(closeBtn, false, false, true);

    // FIXME: change this soon
    this.setupTheme(ConnectionWindow.class);

    setSize(250, 250);
    setTitle("Connection Window");
    ((Container) getContentContainer()).setLayoutManager(new FormLayout());

    addressContainer = FengGUI.createContainer(getContentContainer());

    addressContainer.getAppearance().add(new TitledBorder("Address"));
    FormData fd = new FormData();
    fd.left = new FormAttachment(0, 0);
    fd.right = new FormAttachment(100, 0);
    fd.top = new FormAttachment(100, 0);
    addressContainer.setLayoutData(fd);
    addressContainer.setLayoutManager(new GridLayout(2, 2));

    Label l1 = FengGUI.createLabel(addressContainer, "Address:");

    addressTextField = FengGUI.createTextField(addressContainer);
    addressTextField.updateMinSize();

    FengGUI.createLabel(addressContainer, "Port:");
    portTextField = FengGUI.createTextField(addressContainer);
    portTextField.setRestrict(TextEditor.RESTRICT_NUMBERSONLY);
    portTextField.setMaxCharacters(8);
    portTextField.updateMinSize();
    portTextField.setText("80");

    loginContainer = FengGUI.createContainer(getContentContainer());
    loginContainer.getAppearance().add(new TitledBorder("Login"));
    fd = new FormData();
    fd.left = new FormAttachment(0, 0);
    fd.right = new FormAttachment(100, 0);
    fd.top = new FormAttachment(addressContainer, 0);
    loginContainer.setLayoutData(fd);

    loginContainer.setLayoutManager(new GridLayout(2, 2));
    Label l3 = FengGUI.createLabel(loginContainer, "Name:");

    loginNameTextField = FengGUI.createTextField(loginContainer);
    loginNameTextField.updateMinSize();

    FengGUI.createLabel(loginContainer, "Password:");

    passwordTextField = FengGUI.createTextField(loginContainer);
    passwordTextField.setPasswordField(true);
    passwordTextField.updateMinSize();

    connectButton = FengGUI.createButton(getContentContainer(), "Connect");
    connectButton.getAppearance().setMargin(new Spacing(2, 2));

    cancelButton = FengGUI.createButton(getContentContainer(), "Cancel");
    cancelButton.getAppearance().setMargin(new Spacing(2, 2));

    statusLabel = FengGUI.createLabel(getContentContainer(), "Say something...");
    statusLabel.getAppearance().setAlignment(Alignment.MIDDLE);

    fd = new FormData();
    fd.left = new FormAttachment(0, 0);
    fd.right = new FormAttachment(100, 0);
    fd.bottom = new FormAttachment(connectButton, 0);
    fd.top = new FormAttachment(loginContainer, 0);
    statusLabel.setLayoutData(fd);

    fd = new FormData();
    fd.left = new FormAttachment(0, 0);
    fd.right = new FormAttachment(50, 0);
    fd.bottom = new FormAttachment(0, 0);
    cancelButton.setLayoutData(fd);

    fd = new FormData();
    fd.left = new FormAttachment(50, 0);
    fd.right = new FormAttachment(100, 0);
    fd.bottom = new FormAttachment(0, 0);
    connectButton.setLayoutData(fd);

    cancelButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        close();
      }

    });

    layout();
  }

  public Container getAddressContainer()
  {
    return addressContainer;
  }

  public TextEditor getAddressTextField()
  {
    return addressTextField;
  }

  public Button getCancelButton()
  {
    return cancelButton;
  }

  public Button getConnectButton()
  {
    return connectButton;
  }

  public Container getLoginContainer()
  {
    return loginContainer;
  }

  public TextEditor getLoginNameTextField()
  {
    return loginNameTextField;
  }

  public TextEditor getPasswordTextField()
  {
    return passwordTextField;
  }

  public TextEditor getPortTextField()
  {
    return portTextField;
  }

  public Label getStatusLabel()
  {
    return statusLabel;
  }

}
