package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow(String title) {
        super(title);

        setLayout(new BorderLayout());

        addLabel("hi", BorderLayout.WEST);

    }

    private void addLabel(String text, String o) {

        add(new JLabel(text), o);
    }

}
