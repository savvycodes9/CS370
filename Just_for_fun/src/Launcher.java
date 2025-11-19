import javax.swing.*;

public class Launcher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LogInFrame loginJFrame =  new LogInFrame();
            }
        });
    }
}

//Swing components are not thread-safe. Any updates to GUI should be done on EDT
//invokeLater will ensure Swing related code is run in the same thread - Event Dispatch Thread

//Composition approach