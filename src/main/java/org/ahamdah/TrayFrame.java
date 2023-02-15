package org.ahamdah;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class TrayFrame {
    private TrayIcon trayIcon;
    private PopupMenu popupMenu;
    private List<Distribution> menuItems;
    private MenuItem exit;

    public TrayFrame( ) throws IOException, InterruptedException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
            popupMenu = new PopupMenu();

            menuItems=Service.listAll();
            for (Distribution item:
            menuItems) {
                System.out.println(item);
                CheckboxMenuItem checkboxMenuItem=new CheckboxMenuItem();
                checkboxMenuItem.setLabel(item.getName());
                System.out.println(item.getState());
                if(item.getState().toString().equals("Running"))checkboxMenuItem.setState(true);
                else checkboxMenuItem.setState(false);
                popupMenu.add(checkboxMenuItem);

            }
            exit=new MenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            popupMenu.add(exit);
            
            
            
            // create the tray icon
            trayIcon = new TrayIcon(image, "System Tray", popupMenu);
            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("Could not add tray icon");
            }
        } else {
            System.err.println("System tray is not supported");
        }
    }

}