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
 * $Id: FontExample.java 613 2009-03-25 22:02:20Z marcmenghin $
 */
package org.fenggui.example;

import java.awt.Paint;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.ProgressBar;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.IFont;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.Window;
import org.fenggui.layout.RowLayout;
import org.fenggui.layout.StaticLayout;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.AssemblyLine;
import org.fenggui.util.fonttoolkit.BinaryDilation;
import org.fenggui.util.fonttoolkit.Clear;
import org.fenggui.util.fonttoolkit.DrawCharacter;
import org.fenggui.util.fonttoolkit.FontFactory;
import org.fenggui.util.fonttoolkit.PixelReplacer;

/**
 * Displays some lines of texts in various fonts and colors.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2009-03-25 23:02:20 +0100 (Mi, 25 Mär 2009) $
 * @version $Revision: 613 $
 */
public class FontExample implements IExample
{

  private Display     display;
  private ProgressBar progressBar;

  private ImageFont loadKlingonFont()
  {
    try
    {
      ImageFont klingon = new ImageFont("data/fonts/ST-Klinzhai.png", "data/fonts/ST-Klinzhai.font");
      return klingon;
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  private ImageFont loadStarTrekFont()
  {
    try
    {
      ImageFont klingon = new ImageFont("data/fonts/STTNG.png", "data/fonts/STTNG.font");
      return klingon;
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  private ImageFont createCoolAWTHelveticalFont()
  {
    java.awt.Font awtFont = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24);

    FontFactory ff = new FontFactory(Alphabet.getDefaultAlphabet(), awtFont);
    AssemblyLine line = ff.getAssemblyLine();

    Paint redYellowPaint = new java.awt.GradientPaint(0, 0, java.awt.Color.RED, 15, 15, java.awt.Color.YELLOW, true);
    Paint greenWhitePaint = new java.awt.GradientPaint(0, 0, java.awt.Color.BLACK, 15, 0, java.awt.Color.GREEN, true);

    line.addStage(new Clear());
    line.addStage(new DrawCharacter(java.awt.Color.WHITE, false));

    line.addStage(new BinaryDilation(java.awt.Color.WHITE, 3));

    line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
    line.addStage(new DrawCharacter(java.awt.Color.CYAN, false));

    line.addStage(new PixelReplacer(greenWhitePaint, java.awt.Color.CYAN));

    ImageFont font = ff.createFont();

    return font;
  }

  private ImageFont createCoolAWTSerifFont()
  {
    java.awt.Font awtFont = new java.awt.Font("Serif", java.awt.Font.BOLD, 24);

    FontFactory ff = new FontFactory(Alphabet.getDefaultAlphabet(), awtFont);
    AssemblyLine line = ff.getAssemblyLine();

    Paint redYellowPaint = new java.awt.GradientPaint(0, 0, java.awt.Color.RED, 15, 15, java.awt.Color.YELLOW, true);

    line.addStage(new Clear());
    line.addStage(new DrawCharacter(java.awt.Color.WHITE, false));

    line.addStage(new BinaryDilation(java.awt.Color.BLACK, 3));
    line.addStage(new DrawCharacter(java.awt.Color.WHITE, false));
    line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));

    ImageFont font = ff.createFont();
    return font;
  }

  private ImageFont createAntiAliasedFont()
  {
    return FontFactory.renderStandardFont(new java.awt.Font("Sans", java.awt.Font.BOLD, 24), true, Alphabet
        .getDefaultAlphabet());
  }

  class GUIBuildThread extends Thread
  {
    public void run()
    {
      try
      {
        sleep(1);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }

      final float max = 8; /// number of created fonts
      float value = 0;

      progressBar.setValue(value++ / max);

      final ImageFont defaultFont = ImageFont.getDefaultFont();

      progressBar.setValue(value++ / max);

      final ImageFont coolHelveticaFont = createCoolAWTHelveticalFont();

      progressBar.setValue(value++ / max);

      final ImageFont coolSerifFont = createCoolAWTSerifFont();

      progressBar.setValue(value++ / max);

      final ImageFont font4 = FontFactory.renderStandardFont(new java.awt.Font("Courier", java.awt.Font.PLAIN, 12));

      progressBar.setValue(value++ / max);

      final ImageFont germanFont = FontFactory.renderStandardFont(new java.awt.Font("Sans", java.awt.Font.PLAIN, 16),
        true, Alphabet.GERMAN);

      progressBar.setValue(value++ / max);

      Window dialog = FengGUI.createWindow(true, false, false, true);
      dialog.setTitle("Some Example Fonts");
      Label label1 = FengGUI.createWidget(Label.class);
      Label label2 = FengGUI.createWidget(Label.class);
      Label label4 = FengGUI.createWidget(Label.class);
      Label label3 = FengGUI.createWidget(Label.class);
      Label label6 = FengGUI.createWidget(Label.class);
      Label label5 = FengGUI.createWidget(Label.class);
      Label label7 = FengGUI.createWidget(Label.class);
      Label label8 = FengGUI.createWidget(Label.class);
      Label label9 = FengGUI.createWidget(Label.class);
      Label label10 = FengGUI.createWidget(Label.class);
      Label label11 = FengGUI.createWidget(Label.class);
      dialog.getContentContainer().addWidget(label1, label2, label4, label3, label6, label5, label7, label8, label9, label10, label11);
      dialog.getContentContainer().setLayoutManager(new RowLayout(false));

      setFontToDefaultStyle(label1.getAppearance(), defaultFont, Color.BLACK);
      label1.setText("This is a black colored line of text in the default font");

      final char umlaut_ae = 0x00E4; /* <-- this hassle is only to bypass encoding problems with the SVN!
      				        Of course in your own project, you can use all special characters defined in org.fenggui.util.Alphabet as usual in normal strings! */

      setFontToDefaultStyle(label2.getAppearance(), germanFont, Color.CYAN);
      label2.setText("German saying: \"Wer anderen eine Bratwurst br" + umlaut_ae + "t, braucht ein Bratwurstbratger"
          + umlaut_ae + "t\"");

      setFontToDefaultStyle(label3.getAppearance(), coolHelveticaFont, Color.WHITE);
      label3.setText("pre-rendered Helvetica Font... Yeah!");

      setFontToDefaultStyle(label4.getAppearance(), coolHelveticaFont, Color.WHITE);
      label4.setText("This is the fancy, with the Font Toolkit");

      setFontToDefaultStyle(label6.getAppearance(), coolSerifFont, Color.WHITE);
      label6.setText("Pre-rendered Fonts can be saved to a file");

      setFontToDefaultStyle(label5.getAppearance(), coolSerifFont, Color.WHITE);
      label5.setText("to increase loading speed!");

      setFontToDefaultStyle(label7.getAppearance(), font4, Color.BLUE);
      label7.setText("Line of blue colored text in Courier.....");

      setFontToDefaultStyle(label8.getAppearance(), createAntiAliasedFont(), Color.BLACK);
      label8.setText("This is a line of anti-aliased text!");

      setFontToDefaultStyle(label9.getAppearance(), createAntiAliasedFont(), Color.WHITE);
      label9.setText("This is a WHITE line of anti-aliased text!");

      progressBar.setValue(value++ / max);

      setFontToDefaultStyle(label10.getAppearance(), loadKlingonFont(), Color.BLACK);
      label10.setText("Whatever that means!");

      progressBar.setValue(value++ / max);

      setFontToDefaultStyle(label11.getAppearance(), loadStarTrekFont(), Color.BLACK);
      label11.setText("from disk loaded Star Trek font...");

      progressBar.setValue(value++ / max);

      display.removeWidget(progressBar);

      dialog.setSizeToMinSize();
      dialog.layout();
      display.addWidget(dialog);
    }
  }

  private void setFontToDefaultStyle(TextAppearance appearance, IFont font, Color color)
  {
    TextStyle def = new TextStyle();
    def.getTextStyleEntry(TextStyle.DEFAULTSTYLEKEY).setColor(color);
    appearance.addStyle(TextStyle.DEFAULTSTYLEKEY, def);

    ITextRenderer renderer = appearance.getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY).copy();
    renderer.setFont(font);
    appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);
  }

  public void buildGUI(Display f)
  {
    display = f;

    progressBar = FengGUI.createWidget(ProgressBar.class);
    progressBar.setText("Loading Fonts...");
    progressBar.setSize(200, 25);
    progressBar.setValue(0);
    
    display.addWidget(progressBar);
    StaticLayout.center(progressBar, display);

    GUIBuildThread t = new GUIBuildThread();
    t.start();
  }

  public String getExampleName()
  {
    return "Fonts Example";
  }

  public String getExampleDescription()
  {
    return "Shows some text in various styles";
  }

}
