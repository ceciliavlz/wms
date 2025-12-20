/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.gui;

import view.gui.frames.PrincipalJFrame;

/**
 *
 * @author Ce___
 */
public class Dashboard {

    public static void iniciarModoGUI() {
        java.awt.EventQueue.invokeLater(() -> 
            new PrincipalJFrame().setVisible(true)
        );
    }
}
