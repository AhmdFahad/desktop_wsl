package org.ahamdah;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TrayFrame {
    private final SystemTray tray ;
    private final TrayIcon trayIcon;
    private final MenuItem exit;
    private final PopupMenu popupMenu;
    private List<Distribution> menuItems;
    public TrayFrame(List<Distribution> menuItems)  {
        this.tray=SystemTray.getSystemTray();
        this.popupMenu = new PopupMenu();
        this.exit=new MenuItem("Exit");
        this.trayIcon = new TrayIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png"))).getImage(), "Desktop-wsl", popupMenu);
        this.trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Could not add tray icon");
        }
        load();
        addAction();
    }
    public void load(){
        try {
            menuItems=Service.listAll();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        popupMenu.removeAll();
        popupMenu.addSeparator();

        for (Distribution item:
                    menuItems) {
            CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem();
            checkboxMenuItem.setLabel(item.getName());
            checkboxMenuItem.setState(item.getState().equals("Running"));
            checkboxMenuItem.addItemListener(e -> {
                if(!checkboxMenuItem.getState()){
                    try {
                        Service.turnOff(checkboxMenuItem.getLabel());
                        JOptionPane.showMessageDialog(null, "Shutdown :"+checkboxMenuItem.getLabel());
                        checkboxMenuItem.setState(false);
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                }else {
                    try {
                        Service.turnOn(checkboxMenuItem.getLabel());
                        JOptionPane.showMessageDialog(null, "Run  :"+checkboxMenuItem.getLabel());
                        checkboxMenuItem.setState(true);
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
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
                    load();
                    System.out.println(Arrays.toString(finalMenuItems.toArray()));
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
        exit.addActionListener(e -> System.exit(0));
        }

}

