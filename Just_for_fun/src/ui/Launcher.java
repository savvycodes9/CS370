package ui;

import ui.LoginFrame;

import javax.swing.*;


public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}


//Swing components are not thread-safe. Any updates to GUI should be done on EDT
//invokeLater will ensure Swing related code is run in the same thread - Event Dispatch Thread

//Composition approach