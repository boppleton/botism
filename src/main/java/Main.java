import gui.MainWindow;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
import rest.BitmexRestMethods;
import rest.BitmexSetup;
import utils.HMAC;
import websocket.bitmex.BitmexClient;

import javax.swing.*;
import java.net.URISyntaxException;

public class Main {

    private static MainWindow launchWindow = null;

    private static BitmexClient bitmexClient;


    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        startLaunchWindow();

//        coonectBitmexWebsocket();
//        connectBitmexREST();

    }

    private static void connectBitmexREST() {

        BitmexRestMethods rest = new BitmexRestMethods();

    }

    private static void coonectBitmexWebsocket() throws URISyntaxException, InterruptedException {

        bitmexClient = new BitmexClient();
        bitmexClient.connectBlocking();

        bitmexClient.authSubscribe(BitmexSetup.getKey(), HMAC.hmacDigest("GET/realtime" + 1630475936, BitmexSetup.getSec(), "HmacSHA256"));

//        bitmexClient.send("{\"op\": \"subscribe\", \"args\": [\"position\"]}");
//        bitmexClient.subscribe(true, "quote", "XBTUSD");
        bitmexClient.subscribe(true, "order", "XBTUSD");
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
                launchWindow.setSize(800, 600);
                launchWindow.setLocationRelativeTo(null);
                launchWindow.setVisible(true);
            }
        });
    }

}
