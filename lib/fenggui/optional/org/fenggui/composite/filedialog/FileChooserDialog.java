/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (C) 2005-2009 FengGUI Project
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
 * Created on Mar 22, 2009
 * $Id$
 */
package org.fenggui.composite.filedialog;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.fenggui.Button;
import org.fenggui.ComboBox;
import org.fenggui.Container;
import org.fenggui.FengGUI;
import org.fenggui.Item;
import org.fenggui.Label;
import org.fenggui.List;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.appearance.LabelAppearance;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.IOpenGL;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.mouse.MouseAdapter;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.RowExLayout;
import org.fenggui.layout.RowExLayoutData;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Dimension;

/**
 * This class displays the standard components of a file choosing dialog.
 * To retrieve the chosen file from the dialog, attach a FileChooserDialog.FileDialogListener
 * to the FileChooserDialog.
 * 
 * @author Sam Bayless
 *
 */
public class FileChooserDialog extends Container
{

  /**Maximum amount of time to wait while another thread is displaying folder contents before giving up.*/
  private static final int                     DIRECTORY_TIMEOUT        = 500;
  /**Maximum amount of time to wait before giving up on calling the GL thread.*/
  private static final long                    TIME_OUT                 = 1000;
  /**Number of types of icons to hold in cache. Items are removed from the cache in the order they were entered.*/
  private final static int                     LRU_CACHE_SIZE           = 100;
  /**this determines the number of pixels that are examined from each icon to form a hash number; increase this number if icons are incorrectly identified.*/
  private final static int                     ICON_FINGERPRINT_SIZE    = 16;                                   //
  /**The size of an icon.*/
  private final int                            ICON_DIMENSIONS;
  /**The displayed directory.*/
  private File                                 currentDirectory;
  /**Thread safe list of previous viewed directories*/
  private Stack<File>                          previousDirectories      = new Stack<File>();
  /**Set of files to display in the top drop down box*/
  private Set<File>                            rootFiles                = new HashSet<File>();
  /**File selected by the user*/
  private File                                 selectedFile             = null;

  private ExecutorService                      threadPool               = null;

  private ArrayList<FileDialogListener>        fileDialogListeners      = new ArrayList<FileDialogListener>();

  /**
   * Whether or not to use icon caching
   */
  private final boolean                        useCache;

  /**
   * Whether or not to use multithreaded routines.
   */
  private final boolean                        multithreaded;
  /**
   * The identity of the openGl thread. Required to prevent deadlocks.
   */
  private Thread                               glThread                 = null;

  private AtomicBoolean                        dropDownLock             = new AtomicBoolean();

  private Semaphore                            directoryUpdateSemaphore = new Semaphore(1);

  /**
   * Contains a set of random coordinates used for generating hashes from icons.
   */
  private final int[]                          fingerprintX;

  /**
   * Contains a set of random coordinates used for generating hashes from icons.
   */
  private final int[]                          fingerprintY;

  /**
   * This is an LRU cache for recently used icon textures.
   * See http://blogs.sun.com/swinger/date/20041012#collections_trick_i_lru_cache
   */
  private final LinkedHashMap<Integer, Pixmap> iconMap                  = new LinkedHashMap<Integer, Pixmap>(10, 0.75f,
                                                                            true)
                                                                        {
                                                                          private static final long serialVersionUID = 1;

                                                                          @Override
                                                                          protected boolean removeEldestEntry(
                                                                              java.util.Map.Entry<Integer, Pixmap> eldest)
                                                                          {
                                                                            return size() > LRU_CACHE_SIZE;
                                                                          }

                                                                        };

  /**
   * Holds a simple, thread safe task queue that is executed when the dialog is painted (ie, in the openGL thread).
   */
  private ConcurrentLinkedQueue<Runnable>      glQueue                  = new ConcurrentLinkedQueue<Runnable>();

  private Map<FileFilter, String>              fileFilters              = new HashMap<FileFilter, String>();
  private FileFilter                           currentFilter            = null;

  private List                                 fileList;
  private ComboBox                             dropDown;
  private TextEditor                           fileName;
  private ComboBox                             filterList;

  private Button                               upButton;
  private Button                               backButton;

  private ScrollContainer                      scrollContainer;

  private Container                            internalContainer;

  /**
   * Creates a simple file choosing dialog. 
   * @param threadPool This parameter is ignored if multithreading is not in use. Otherwise, if null, a new 
   * ThreadPoolExecutor will be created for this dialog; if you provide a ThreadPool, then the dialog will use
   * it for loading file icons.
   * @param glThread This parameter is ignored if multithreading is not in use. Certain methods must be run from
   * the OpenGL thread (for example, creating a texture). Providing the identity of the glThread, while not necessary,
   * is recommended; if the GLThread is not provided, it will attempt to find it out; until it does so, 
   * multithreading will be disabled.
   * @param multithreaded Specify whether to make this dialog multithreaded or not. (It is strongly recommended to use multithreading).
   */
  public FileChooserDialog(ExecutorService threadPool, Thread glThread, boolean multithreaded)
  {
    super();
    this.multithreaded = multithreaded;
    this.glThread = glThread;
    this.useCache = true;//use icon caching

    //find out the size of a test icon, and assume all other icons will have the same dimensions
    File file = FileSystemView.getFileSystemView().getDefaultDirectory();
    if (file != null)
      ICON_DIMENSIONS = FileSystemView.getFileSystemView().getSystemIcon(file).getIconWidth();
    else
      ICON_DIMENSIONS = 16;

    /*
     * Because loading and displaying the files is relatively slow, it is strongly recommended
     * to use multithreading with this dialog. 
     */
    if (!multithreaded)
      threadPool = null;
    else
    {
      if (threadPool == null)
        this.threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
      else
        this.threadPool = threadPool;
    }

    /*
     * Creating textures for icons is expensive, so this dialog will cache recently created 
     * icon textures. However, there is only one reliable way to determine whether a particular file
     * has a particular icon: to load the icons and compare them. 
     * It would be expensive to compare, pixel for pixel, each icon, so instead a hash is created 
     * from 16 preselected random pixels (fingerprintX and Y). 
     * 
     * 
     */

    Random random = new Random();

    fingerprintX = new int[ICON_FINGERPRINT_SIZE];
    for (int i = 0; i < fingerprintX.length; i++)
    {

      fingerprintX[i] = random.nextInt(ICON_DIMENSIONS - 4) + 2;
      //restrict the random pixels to not include the outermost ones, 
      //which are more likely to be transparent
    }

    fingerprintY = new int[ICON_FINGERPRINT_SIZE];
    for (int i = 0; i < fingerprintY.length; i++)
    {

      fingerprintY[i] = random.nextInt(ICON_DIMENSIONS - 4) + 2;
      //restrict the random pixels to not include the outermost ones, 
      //which are more likely to be transparent
    }

    buildComponents();
    setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
  }

  /**
   * Create a file dialog that is multithreaded.
   */
  public FileChooserDialog()
  {
    this(null, null, true);
  }

  /**
   * Set the current directory to display
   * @param file
   */
  public void setCurrentDirectory(File file)
  {
    setCurrentDirectory(file, false);
  }

  /**
   * Go back to the last seen directory
   */
  public void back()
  {
    synchronized (previousDirectories)
    {
      File file;
      try
      {
        if ((file = previousDirectories.pop()) != null)
        {
          setCurrentDirectory(file, true);
        }
      }
      catch (EmptyStackException e)
      {
        //do nothing
      }
    }
  }

  /**
   * Display the files in the current folder.
   */
  public void refreshFiles()
  {
    if (!multithreaded || (glThread == null))
    {
      try
      {
        if (directoryUpdateSemaphore.tryAcquire(DIRECTORY_TIMEOUT, TimeUnit.MILLISECONDS))
        {

          try
          {

            final FileFilter filter = this.getCurrentFilter();
            final File file = currentDirectory;
            if (file == null || !file.isDirectory())
              return;
            fileList.clear();
            populateDropDown(dropDown, file);
            //this can be slow - if possible, put it into a separate thread.

            ArrayList<FileItem> items = new ArrayList<FileItem>();
            for (File f : file.listFiles(filter))
            {
              if (!f.isHidden())
              {
                FileItem item = new FileItem(f);
                items.add(item);
              }
            }

            Collections.sort(items);

            for (FileItem item : items)
            {
              Item listItem = new Item(item.toString(), getIcon(item.getFile()), fileList.getAppearance());
              listItem.setData(item);
              fileList.addItem(listItem);
            }
            scrollContainer.scrollVertical(1);
          }
          finally
          {//release the semaphore no matter what happens
            directoryUpdateSemaphore.release();
          }
        }
      }
      catch (InterruptedException e)
      {
        return;
      }

    }
    else
    {
      threadPool.execute(new Runnable()
      {

        public void run()
        {
          try
          {

            if (directoryUpdateSemaphore.tryAcquire(DIRECTORY_TIMEOUT, TimeUnit.MILLISECONDS))
            {

              try
              {

                final FileFilter filter = getCurrentFilter();
                final File file = currentDirectory;
                if (file == null || !file.isDirectory())
                  return;
                fileList.clear();
                populateDropDown(dropDown, file);
                ArrayList<FileItem> items = new ArrayList<FileItem>();
                if (filter != null)
                {
                  File[] files = file.listFiles(filter);
                  if (files != null)
                  {

                    for (File f : files)
                    {
                      if (!f.isHidden())
                      {
                        FileItem item = new FileItem(f);
                        items.add(item);
                      }
                    }

                  }
                  else
                  {//For unknown reasons, my computer returns a null list of files when given a filter, so try again if that happens without a filter.
                    files = file.listFiles();
                    if (files != null)
                    {
                      for (File f : files)
                      {
                        if (!f.isHidden())
                        {
                          FileItem item = new FileItem(f);
                          items.add(item);
                        }
                      }
                    }
                  }
                }
                else
                {
                  File[] files = file.listFiles();
                  if (files != null)
                  {
                    for (File f : files)
                    {
                      if (!f.isHidden())
                      {
                        FileItem item = new FileItem(f);
                        items.add(item);
                      }
                    }
                  }
                }
                if (items.isEmpty())
                  return;
                Collections.sort(items);
                scrollContainer.scrollVertical(1);
                final FileItem lastItem = items.get(items.size() - 1);
                for (final FileItem item : items)
                {
                  final Pixmap icon = getIcon(item.getFile());

                  try
                  {
                    executeInGL(new Callable<Object>()
                    {//This has to be executed from the GL thread, otherwise the list will jump between scroll positions in an ugly way while it is being filled
                      public Object call() throws Exception
                      {
                        try
                        {
                          if (icon != null)
                          {
                            Item listItem = new Item(item.toString(), icon, fileList.getAppearance());
                            listItem.setData(item);
                            fileList.addItem(listItem);
                          }
                          else
                          {
                            Item listItem = new Item(item.toString(), fileList.getAppearance());
                            listItem.setData(item);
                            fileList.addItem(listItem);
                          }
                          scrollContainer.scrollVertical(1);
                        }
                        finally
                        {
                          if (item == lastItem)
                            directoryUpdateSemaphore.release();
                          //allow the directory to be refreshed after the file is displayed
                        }
                        return null;
                      }

                    });
                  }
                  catch (Exception e)
                  {

                  }

                }
                scrollContainer.scrollVertical(1);
              }
              finally
              {

              }
            }
          }
          catch (InterruptedException e)
          {

          }
        }

      });
    }

  }

  public void setCurrentDirectory(final File file, boolean dontAddToHistory)
  {
    if (file == null || !file.isDirectory())
      return;
    File currentDirectory = this.getCurrentDirectory();
    if (currentDirectory != null && currentDirectory.equals(file))
      return;

    if (!dontAddToHistory && currentDirectory != null)
    {
      synchronized (previousDirectories)
      {
        previousDirectories.add(currentDirectory);
      }
    }
    this.currentDirectory = file;
    setSelectedFile(null);
    refreshFiles();
  }

  /**
     * Execute the callable from the GL thread, sometime in the future.
     * @param toCall
     */
  @SuppressWarnings("unchecked")
  private void executeInGL(Callable<?> toCall) throws Exception
  {
    if (glThread == null)
      return;
    if (!multithreaded || Thread.currentThread() == glThread)
    {
      toCall.call();
    }
    else
    {
      FutureTask<?> task = new FutureTask(toCall);
      glQueue.add(task);

    }
  }

  /**
   * Execute the callable from the GL thread, and wait until it returns;
   * @param <E>
   * @param toCall
   * @return
   */
  private <E> E callFromGL(Callable<E> toCall) throws Exception
  {
    if (glThread == null)
      throw new Exception("GL Thread not known");
    if (!multithreaded || Thread.currentThread() == glThread)
    {
      return toCall.call();
    }
    else
    {
      FutureTask<E> task = new FutureTask<E>(toCall);
      glQueue.add(task);
      return task.get(TIME_OUT, TimeUnit.MILLISECONDS);
    }
  }

  private BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

  private int computeHash(BufferedImage image)
  {
    long hash = 31;//prime value
    for (int i = 0; i < fingerprintX.length; i++)
    {
      hash *= image.getRGB(fingerprintX[i], fingerprintY[i]) + 1;
      hash %= Integer.MAX_VALUE;

    }

    return (int) hash;
  }

  /**
   * Efficiently get the icon for this file, if it exists.
   * Retrieved icons are cached for later use.
   * Returns null if no icon could be found.
   * @param file
   * @return
   */
  private Pixmap getIcon(final File file)
  {

    FileSystemView view = FileSystemView.getFileSystemView();

    //final String fileType = view.getSystemTypeDescription(file);
    //Unfortunately, on some platforms (Windows), files and folders that have different icons will have the same description.
    //For example, My Documents may have a unique icon, but will have the same description as other folders.
    //If a cache is used, this can lead to incorrect icons.

    Icon icon = view.getSystemIcon(file);

    //Because only one Image context is being used, it is important that only one thread access it at a time.
    synchronized (img)
    {
      Graphics2D graphics = img.createGraphics();
      Composite previousComposite = graphics.getComposite();

      //Erase any previous texture in this icon
      graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
      graphics.setColor(java.awt.Color.WHITE);
      graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
      graphics.setComposite(previousComposite);

      icon.paintIcon(null, graphics, 0, 0);

      int hash = computeHash(img);

      if (useCache)
      {
        if (iconMap.containsKey(hash))
        {
          return iconMap.get(hash);
        }
      }

      Pixmap pixmap;

      if (!multithreaded || glThread == null || Thread.currentThread() == glThread)
      {
        ITexture texture = Binding.getInstance().getTexture(img);
        pixmap = new Pixmap(texture);
      }
      else
      {
        Callable<Pixmap> pixCall = new Callable<Pixmap>()
        {
          public Pixmap call()
          {
            ITexture texture = Binding.getInstance().getTexture(img);
            Pixmap pixmap = new Pixmap(texture);

            return pixmap;
          }
        };

        try
        {
          pixmap = callFromGL(pixCall);
        }
        catch (Exception e)
        {
          pixmap = null;
        }
      }
      if (useCache && pixmap != null)
        iconMap.put(hash, pixmap);
      return pixmap;

    }

  }

  /**
   * Set common root files to appear in the top drop down box.
   */
  private void setRootFiles()
  {
    FileSystemView view = FileSystemView.getFileSystemView();

    for (File f : view.getRoots())
    {
      if (!f.isHidden())
      {

        rootFiles.add(f);
      }
    }
    rootFiles.add(view.getHomeDirectory());
    File home = new File(System.getProperty("user.home"));
    if (!home.equals(view.getHomeDirectory()))
    {//on some systems (including Vista) these are not the same
      rootFiles.add(home);
    }
  }

  /**
   * Add a folder to appear in the top drop down box of the dialog.
   * Call refreshFiles to display the change.
   * @param file
   */
  public void addToRoots(File file)
  {
    rootFiles.add(file);
  }

  /**
   * Remove a folder from the top drop down box of the dialog.
   * Call refreshFiles to display the change.
   * @param file
   */
  public void removeFromRoots(File file)
  {
    rootFiles.remove(file);
  }

  /**
   * Get the list of files in the top drop down box.
   * Changes to this list will be reflected in changes to the displayed folders (once refreshFiles is called).
   * @return
   */
  public Set<File> getRootFiles()
  {
    return rootFiles;
  }

  /**
   * Populate the drop down with the root folders, and the current directory, and ensure the current directory is selected.
   * @param dropDown
   * @param currentDirectory
   */
  private void populateDropDown(ComboBox dropDown, File currentDirectory)
  {
    try
    {
      dropDownLock.set(true);
      dropDown.getList().clear();
      LabelAppearance appearance = dropDown.getList().getAppearance();
      for (File file : rootFiles)
      {
        FileItem item = new FileItem(file);
        Item listItem = new Item(item.toString(), getIcon(item.getFile()), appearance);
        listItem.setData(item);
        dropDown.addItem(listItem);

        if (file.equals(currentDirectory))
          dropDown.setSelected(listItem);
      }

      if (!rootFiles.contains(currentDirectory))
      {
        FileItem item = new FileItem(currentDirectory);
        Item current = new Item(item.toString(), getIcon(item.getFile()), appearance);
        current.setData(item);
        dropDown.addItem(current);
        dropDown.setSelected(current);
      }
    }
    finally
    {
      dropDownLock.set(false);
      //dropDownSemaphore.release();
    }

  }

  /**
   * Signal that a file has been selected to the listeners.
   */
  private synchronized void fileSelected()
  {

    File file = selectedFile;
    for (FileDialogListener listener : fileDialogListeners.toArray(new FileDialogListener[fileDialogListeners.size()]))
    {
      listener.fileSelected(file);
    }
  }

  /**
   * Signal that no file was selected and the dialog was cancelled.
   */
  public synchronized void cancel()
  {
    for (FileDialogListener listener : fileDialogListeners.toArray(new FileDialogListener[fileDialogListeners.size()]))
    {
      listener.cancel();
    }
  }

  /**
   * Construct all the inner components of the dialog. They will be loaded with the current theme.
   */
  private void buildComponents()
  {

    internalContainer = this;

    internalContainer.setLayoutManager(new BorderLayout());

    Container topRow = new Container();
    topRow.setLayoutData(BorderLayoutData.NORTH);
    topRow.setLayoutManager(new RowExLayout());
    internalContainer.addWidget(topRow);

    dropDown = new ComboBox();
    dropDown.setLayoutData(new RowExLayoutData(true, true));

    topRow.addWidget(dropDown);
    FengGUI.getTheme().setUp(dropDown);

    dropDown.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (dropDownLock.get())
          return;

        File file = ((FileItem) ((Item) dropDown.getSelectedItem()).getData()).getFile();
        setCurrentDirectory(file);

      }

    });

    //This container defines the bottom section of the dialog
    Container south = FengGUI.createContainer(internalContainer);
    south.setLayoutData(BorderLayoutData.SOUTH);
    south.setLayoutManager(new RowExLayout(false));

    Container fileNameContainer = FengGUI.createContainer(south);
    fileNameContainer.setLayoutData(new RowExLayoutData(true, true));
    fileNameContainer.setLayoutManager(new RowExLayout());

    Label nameLabel = FengGUI.createLabel(fileNameContainer);
    nameLabel.setText("Files Name: ");
    nameLabel.setLayoutData(new RowExLayoutData(false, false));

    fileName = FengGUI.createTextEditor(fileNameContainer);
    fileName.setLayoutData(new RowExLayoutData(true, true));
    fileName.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyPressedEvent keyPressedEvent)
      {
        if (keyPressedEvent.getKeyClass() == Key.ENTER)
        {
          attemptSelectFile(fileName.getText());
          keyPressedEvent.setUsed();
          fileSelected();
        }
        else if (keyPressedEvent.getKeyClass() == Key.ESCAPE)
        {
          keyPressedEvent.setUsed();
          cancel();
        }
      }
    });

    Container filterContainer = FengGUI.createContainer(south);
    filterContainer.setLayoutData(new RowExLayoutData(true, true));
    filterContainer.setLayoutManager(new RowExLayout());

    Label filterLabel = FengGUI.createLabel(filterContainer);
    filterLabel.setText("Files of Type: ");
    filterLabel.setLayoutData(new RowExLayoutData(false, false));

    filterList = FengGUI.createComboBox(filterContainer);
    FileFilter filter = generateFileFilter("All Files", "*");
    addFileFilter(filter, "All Files");

    updateFilterList();
    setCurrentFilter(filter);

    filterList.setLayoutData(new RowExLayoutData(true, true));

    filterList.addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {
        if (filterList.getSelectedItem() != null)
        {
          FileFilter filter = ((FileFilter) ((Item) filterList.getSelectedItem()).getData());
          if (filter != null)
          {
            setCurrentFilter(filter);
          }

        }

      }

    });

    Container buttonContainer = FengGUI.createContainer(south);
    buttonContainer.setLayoutData(new RowExLayoutData(true, true));
    buttonContainer.setLayoutManager(new BorderLayout());

    Container buttons = FengGUI.createContainer(buttonContainer);
    buttons.setLayoutManager(new RowLayout());
    buttons.setLayoutData(BorderLayoutData.EAST);

    Button acceptButton = FengGUI.createButton(buttons, "OK");
    acceptButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        attemptSelectFile(fileName.getText());
        e.setUsed();
        fileSelected();
      }

    });

    Button cancelButton = FengGUI.createButton(buttons, "Cancel");

    cancelButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        e.setUsed();
        cancel();
      }

    });

    backButton = new Button();
    backButton.setLayoutData(new RowExLayoutData(true, false));
    topRow.addWidget(backButton);
    FengGUI.getTheme().setUp(backButton);

    backButton.setText("Back");

    upButton = new Button();
    upButton.setLayoutData(new RowExLayoutData(true, false));

    topRow.addWidget(upButton);
    FengGUI.getTheme().setUp(upButton);

    upButton.setText("Up");

    backButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        back();
      }

    });

    upButton.addButtonPressedListener(new IButtonPressedListener()
    {

      public void buttonPressed(ButtonPressedEvent e)
      {
        File file;
        if ((file = getCurrentDirectory()) != null)
        {

          File parent = file.getParentFile();
          if (parent != null)
            setCurrentDirectory(parent);
        }
      }

    });

    setRootFiles();

    scrollContainer = FengGUI.createScrollContainer(internalContainer);

    scrollContainer.setLayoutData(BorderLayoutData.CENTER);
    //  internalContainer.addWidget(scrollContainer);

    fileList = FengGUI.createWidget(List.class);//FengGUI.<FileItem>createList(scrollContainer) ;//new List<FileItem>() ;
    //scrollContainer.addWidget(fileList);
    scrollContainer.setInnerWidget(fileList);

    fileList.setTraversable(true);
    fileList.setToggle(false);

    fileList.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyPressedEvent keyPressedEvent)
      {
        if (keyPressedEvent.getKeyClass() == Key.ENTER)
        {
          keyPressedEvent.setUsed();
          Item Item = fileList.getSelectedItem();
          if (Item != null)
          {
            if (((FileItem) Item.getData()).getFile().isDirectory())
            {
              //move to that folder
              setCurrentDirectory(((FileItem) Item.getData()).getFile());

            }
            else
            {
              setSelectedFile(((FileItem) Item.getData()).getFile(), true);

              fileSelected();
            }
          }

        }
        else if (keyPressedEvent.getKeyClass() == Key.ESCAPE)
        {
          keyPressedEvent.setUsed();
          cancel();
        }
      }
    });

    fileList.getToggableWidgetGroup().addSelectionChangedListener(new ISelectionChangedListener()
    {

      public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
      {

        Item Item = fileList.getSelectedItem();
        selectionChangedEvent.setUsed();
        if (Item != null)
        {
          if (((FileItem) Item.getData()).getFile().isFile())
            setSelectedFile(((FileItem) Item.getData()).getFile(), true);
        }

      }

    });

    fileList.addMouseListener(new MouseAdapter()
    {

      @Override
      public void mouseDoubleClicked(MouseDoubleClickedEvent mouseDoubleClickedEvent)
      {
        mouseDoubleClickedEvent.setUsed();
        Item Item = fileList.getSelectedItem();
        if (Item != null)
        {
          if (((FileItem) Item.getData()).getFile().isDirectory())
          {
            //move to that folder
            setCurrentDirectory(((FileItem) Item.getData()).getFile());

          }
          else
          {
            setSelectedFile(((FileItem) Item.getData()).getFile(), true);

            fileSelected();
          }
        }
      }

    });

    internalContainer.setMinSize(this.getMinContentSize());
    //internalContainer.setSizeToMinSize();

    Dimension minContent = buttonContainer.getMinContentSize();
    buttonContainer.setMinSize(minContent);
    internalContainer.layout();
    south.layout();
    buttonContainer.layout();
    buttons.layout();
    filterContainer.layout();

    topRow.layout();

  }

  /**
   * Attempt to select the given file, if it exists
   * @param fileName
   */
  private void attemptSelectFile(String fileName)
  {

    File file = new File(getCurrentDirectory().getPath() + "/" + fileName);
    if (file != null && file.exists())
    {
      setSelectedFile(file);
    }

  }

  /**
   * Set the currently selected file.
   * @param file
   */
  public void setSelectedFile(File file)
  {
    setSelectedFile(file, false);
  }

  /**
   * Set the currently selected file list, but do not update the file list's display.
   * @param file
   * @param ignoreFileList
   */
  private void setSelectedFile(File file, boolean ignoreFileList)
  {
    selectedFile = file;
    if (file == null)
    {
      Item selectedItem = fileList.getSelectedItem();
      if (!ignoreFileList && selectedItem != null)
        selectedItem.setSelected(false);
      fileName.setText("");
    }
    else
    {
      if (!ignoreFileList)
      {
        for (int i = 0; i < fileList.size(); i++)
        {
          Item item = fileList.getItem(i);
          if (((FileItem) item.getData()).getFile().equals(file))
          {
            fileList.setSelectedIndex(i);
            break;
          }
        }
      }
      fileName.setText(file.getName());
    }
  }

  /**
   * Get the currently displayed directory.
   * @return
   */
  public File getCurrentDirectory()
  {
    return currentDirectory;
  }

  /**
   * Get the list of file filters displayed in the bottom of the dialog. 
   * Changes to this list will be reflected in changes to that drop down 
   * box, once updateFilterList() is called.
   * @return
   */
  public Set<FileFilter> getFileFilters()
  {
    return fileFilters.keySet();
  }

  /**
   * Add a file filter with the given pattern (ie, *, *.*, or *.txt) to the list of filters.
   * @param description
   * @param pattern
   */
  public void addFileFilter(String description, String pattern)
  {
    addFileFilter(FileChooserDialog.generateFileFilter(description, pattern), description);

  }

  /**
   * Add a file filter to the list of file filters displayed at the bottom of the screen..
   * @param filter
   * @param description
   */
  public void addFileFilter(FileFilter filter, String description)
  {
    this.fileFilters.put(filter, description);
    updateFilterList();
  }

  /**
   * Remove a file filter from the list of displayed filters.
   * @param filter
   */
  public void removeFileFilter(FileFilter filter)
  {
    this.fileFilters.remove(filter);
    updateFilterList();
  }

  /**
   * Clear the list of displayed file filters.
   */
  public void clearFileFilters()
  {
    this.fileFilters.clear();
    updateFilterList();
  }

  /**
   * Updates the file filters displayed in the comboBox. 
   * Called automatically by addFileFilter and removeFileFilter.
   */
  public void updateFilterList()
  {

    filterList.getList().clear();
    LabelAppearance appearance = filterList.getList().getAppearance();

    for (FileFilter filter : fileFilters.keySet())
    {
      Item item = new Item(fileFilters.get(filter), appearance);
      item.setData(filter);
      filterList.addItem(item);
    }
  }

  /**
   * Get the list of currently attached file listeners.
   * @return
   */
  public ArrayList<FileDialogListener> getFileDialogListeners()
  {
    return fileDialogListeners;
  }

  /**
   * Attach a listener to receive events when a file is selected, or the dialog is cancelled.
   * @return
   */
  public void addListener(FileDialogListener listener)
  {
    this.fileDialogListeners.add(listener);
  }

  /**
   * Remove a File Dialog Listener.
   * @param listener
   */
  public void removeListener(FileDialogListener listener)
  {
    this.fileDialogListeners.remove(listener);
  }

  @Override
  public void paintContent(Graphics g, IOpenGL gl)
  {
    //Inject code into the GL thread here
    if (glThread == null)
      glThread = Thread.currentThread();

    Runnable task = glQueue.poll();
    do
    {
      if (task == null)
        break;
      task.run();

    } while (((task = glQueue.poll()) != null));

    super.paintContent(g, gl);
  }

  /**
   * Get the currently used file filter.
   * @return
   */
  public FileFilter getCurrentFilter()
  {
    return currentFilter;
  }

  /**
   * Set the currently selected file filter.
   * @param currentFilter
   */
  public void setCurrentFilter(FileFilter currentFilter)
  {
    if (this.currentFilter != currentFilter)
    {
      this.currentFilter = currentFilter;
      refreshFiles();
    }
  }

  /**
   * Generates a FileFilter for the given pattern.
   * @param description
   * @param pattern
   * @return
   */
  public static FileFilter generateFileFilter(String description, String pattern)
  {
    //Wrap the javax.swing.filechooser.FileNameExtensionFilter in java.io.FileFilter interface

    //final FileNameExtensionFilter extFilter =  new FileNameExtensionFilter(description, pattern );
    //have to alter the pattern to turn it into a regex

    pattern = pattern.replace(".", "\\.");
    pattern = pattern.replace("*", ".*");
    Pattern filter = Pattern.compile(pattern);
    return new FilePatternFilter(filter);
  }

}

class FilePatternFilter implements FileFilter
{
  private Pattern pattern;

  public FilePatternFilter(Pattern pattern)
  {
    super();
    this.pattern = pattern;
  }

  public boolean accept(File pathname)
  {
    if (pathname != null)
    {
      if (pathname.isDirectory())
      {
        return true;
      }

      if (pattern.matcher(pathname.getName()).matches())
      {
        return true;
      }
    }
    return false;
  }

}

class FileItem implements Comparable<FileItem>
{
  private String name;

  public int compareTo(FileItem o)
  {
    //directories are always before files
    if (o.getFile().isDirectory() && !getFile().isDirectory())
      return 1;
    else if (!o.getFile().isDirectory() && getFile().isDirectory())
      return -1;

    return getFile().getName().compareTo(o.getFile().getName());
  }

  File file;

  public FileItem(File file)
  {
    super();
    name = FileSystemView.getFileSystemView().getSystemDisplayName(file);
    if (name.length() <= 0)
      name = file.toString();
    this.file = file;
  }

  public File getFile()
  {
    return file;
  }

  @Override
  public String toString()
  {

    return name;
  }

}