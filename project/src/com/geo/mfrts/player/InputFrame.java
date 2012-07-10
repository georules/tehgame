package com.geo.mfrts.player;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

class InputFrame extends ResizableFrame {
    private final StringBuilder sb;
    private final HTMLTextAreaModel textAreaModel;
    private final TextArea textArea;
    private final EditField editField;
    private final ScrollPane scrollPane;
    private int curColor;

    public InputFrame() {
        setTitle("Code");

        this.sb = new StringBuilder();
        this.textAreaModel = new HTMLTextAreaModel();
        this.textArea = new TextArea(textAreaModel);
        this.editField = new EditField();
        this.editField.setMultiLine(true);
        this.editField.setText("//Code here\npublic void render() {\n  super.render();\n}\n");

        scrollPane = new ScrollPane(editField);
        scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
      

        DialogLayout l = new DialogLayout();
        l.setTheme("content");
        
        l.setHorizontalGroup(l.createParallelGroup(scrollPane));
        l.setVerticalGroup(l.createSequentialGroup(scrollPane));

        add(l);
    }
    public String getText() {
    	return editField.getText();
    }

}
