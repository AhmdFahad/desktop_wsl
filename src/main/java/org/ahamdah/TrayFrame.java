package org.ahamdah;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.EventListener;
import java.util.List;

public class TrayFrame {
    private  SystemTray tray ;
    private TrayIcon trayIcon;
    private PopupMenu popupMenu;
    private List<Distribution> menuItems;
    private MenuItem exit;
    private Image image;

    public TrayFrame( )  {

        this.tray=SystemTray.getSystemTray();
        this.popupMenu = new PopupMenu();
        this.image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
        this.exit=new MenuItem("Exit");
        this.trayIcon = new TrayIcon(image, "Desktop-wsl", popupMenu);
        this.trayIcon.setImageAutoSize(true);

        List<Distribution>menuItems= null;
        try {
            menuItems = Service.listAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Could not add tray icon");
        }
        List<Distribution> finalMenuItems = menuItems;
        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                load(tray, finalMenuItems);
            }

            @Override
            public void mousePressed(MouseEvent e) {

                System.out.println("yty");

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open");
            }
        });

        load(tray,menuItems);




    }

    public void load(SystemTray tray,List<Distribution> menuItems){
        popupMenu.removeAll();
        if (SystemTray.isSupported() != true) {
            System.err.println("System tray is not supported");
        }

        try {
            menuItems=Service.listAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        popupMenu.addSeparator();

        for (Distribution item:
                    menuItems) {
            System.out.println(item);
            CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem();
            checkboxMenuItem.setLabel(item.getName());
            System.out.println(item.getState());
            if(item.getState().toString().equals("Running"))checkboxMenuItem.setState(true);
                else checkboxMenuItem.setState(false);
            checkboxMenuItem.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(checkboxMenuItem.getState() != true){
                            try {
                                System.out.println("Shutdowm :  "+checkboxMenuItem.getLabel());
                                JOptionPane.showMessageDialog(null, "Shutdown :"+checkboxMenuItem.getLabel());
                                Service.turnOff(checkboxMenuItem.getLabel());
                                checkboxMenuItem.setState(false);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                        }else {
                            try {
                                System.out.println("Run : "+checkboxMenuItem.getLabel());
                                JOptionPane.showMessageDialog(null, "Run  :"+checkboxMenuItem.getLabel());
                                Service.turnOn(checkboxMenuItem.getLabel());
                                checkboxMenuItem.setState(true);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                });

            popupMenu.add(checkboxMenuItem);

        }

        exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            popupMenu.addSeparator();
            popupMenu.add(exit);



        }

}

