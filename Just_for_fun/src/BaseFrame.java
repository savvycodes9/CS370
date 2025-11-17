import javax.swing.*;
import java.awt.*;

public class BaseFrame {
    protected JFrame frame;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;

    //constructor
    public BaseFrame(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void add(Component comp, Object constraints){
        frame.add(comp, constraints);
    }

    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setCloseOperation(int closeOperation) {
        frame.setDefaultCloseOperation(closeOperation);
    }

    //getter
    public JFrame getFrame() {
        return frame;
    }
}