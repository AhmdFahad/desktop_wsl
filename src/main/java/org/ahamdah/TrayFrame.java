package org.ahamdah;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class TrayFrame {
    private final  SystemTray tray ;
    private final TrayIcon trayIcon;
    private final MenuItem exit;
    private  Image image;
    private PopupMenu popupMenu;
    private List<Distribution> menuItems;
    public TrayFrame( )  {

        this.tray=SystemTray.getSystemTray();
        this.popupMenu = new PopupMenu();
        this.image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
        this.exit=new MenuItem("Exit");
        this.trayIcon = new TrayIcon(image, "Desktop-wsl", popupMenu);
        this.trayIcon.setImageAutoSize(true);


        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Could not add tray icon");
        }


        List<Distribution>menuItems= null;
        try {
            menuItems = Service.listAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Distribution> finalMenuItems = menuItems;
        load(tray,menuItems);
        addAction();

    }
    public void load(SystemTray tray,List<Distribution> menuItems){
        popupMenu.removeAll();
        if (SystemTray.isSupported() != true) {
            System.out.println("Not Subported");
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
            CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem();
            checkboxMenuItem.setLabel(item.getName());
            if(item.getState().toString().equals("Running"))checkboxMenuItem.setState(true);
                else checkboxMenuItem.setState(false);
            checkboxMenuItem.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(checkboxMenuItem.getState() != true){
                            try {
                                Service.turnOff(checkboxMenuItem.getLabel());
                                JOptionPane.showMessageDialog(null, "Shutdown :"+checkboxMenuItem.getLabel());
                                checkboxMenuItem.setState(false);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                        }else {
                            try {
                                Service.turnOn(checkboxMenuItem.getLabel());
                                JOptionPane.showMessageDialog(null, "Run  :"+checkboxMenuItem.getLabel());
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

        popupMenu.addSeparator();
        popupMenu.add(exit);
        }
    public void addAction(){
        
            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    List<Distribution> finalMenuItems = menuItems;
                    load(tray, finalMenuItems);
                }

                @Override
                public void mousePressed(MouseEvent e) {
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
        
        exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }

}

