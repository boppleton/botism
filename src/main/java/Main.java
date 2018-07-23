import gui.MainWindow;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;

import javax.swing.*;

public class Main {

    private static MainWindow launchWindow = null;

    public static void main(String[] args) {

        startLaunchWindow();

    }

    private static void startLaunchWindow() { //launch the starting JFrame with l & f set

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteAquaLookAndFeel());
            } catch (Exception e) { System.out.println("Substance look&feel load error!"); e.printStackTrace(); }

            try {
                launchWindow = new MainWindow("botism");
            } catch (Exception e) { System.out.println("window object create error"); e.printStackTrace(); }

            if (launchWindow != null) {
                launchWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                launchWindow.setSize(600, 400);
                launchWindow.setLocationRelativeTo(null);
                launchWindow.setVisible(true);
            }
        });
    }

}
