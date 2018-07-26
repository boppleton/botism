package websocket.bitmex;

import gui.MainWindow;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.handshake.ServerHandshake;
import org.knowm.xchange.bitmex.dto.marketdata.BitmexPrivateOrder;
import rest.BitmexRestMethods;
import utils.Broadcaster;
import websocket.Client;
import websocket.bitmex.trade.BitmexTrade;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class BitmexClient extends Client {

    private static int currentPosition = 0;

    private static boolean freshStart = true;

    private static double staticEntry = 0;


    private static double currentBid = 0;
    private static double currentAsk = 0;

    private static ArrayList<BitmexPrivateOrder> openOrders = new ArrayList<>();

    public BitmexClient() throws URISyntaxException {
        super(new URI("wss://www.bitmex.com/realtime/"));
    }


    public void subscribe(boolean connect, String topic, String pair) {
        send("{\"op\": \""   +(connect ? "" : "un")+   "subscribe\", \"args\": [\"" +topic+ ":" +pair+ "\"]}");
    }

    public void authSubscribe(String apikey, String sig) {
        send("{\"op\": \"authKeyExpires\", \"args\": [\"" + apikey + "\"," + 1630475936 + ", \"" + sig + "\"]}");
    }

//    private void onMessagePosition(String message) throws InterruptedException, IOException {
//
//        String action = StringUtils.substringBetween(message, "\"action\":\"", "\",");
//        String currency = StringUtils.substringBetween(message, "\"currency\":\"", "\",");
//        int currentAmt; try { currentAmt = Integer.parseInt(StringUtils.substringBetween(message, "\"currentQty\":", ",")); }catch (Exception e) { currentAmt = -1; }
//
//        double entryPrice; try { entryPrice = Double.parseDouble(StringUtils.substringBetween(message, "\"avgEntryPrice\":", ",")); }catch (Exception e) { entryPrice = -1; }
//
//        if (entryPrice > 0) {
//            staticEntry = entryPrice;
//        }
//
//
//        System.out.println("pos change: " + action + " " + currency + " - " + (currentAmt == -1? null : currentAmt));
//
//
//        if (currentAmt != -1 && currentAmt != 0 && (currentAmt>= 0?(currentAmt > currentPosition):(currentAmt < currentPosition)) ) {
//            System.out.println("update close (position changed)");
//
//            currentPosition = currentAmt;
//
//            MainWindow.active = false;
//
//            if (!freshStart) {
//
//                BitmexPrivateOrder closeOrder = BitmexRestMethods.updateClose(currentPosition, entryPrice > 0 ? entryPrice : staticEntry, .07, true);
//
//                System.out.println("closeorder: " + closeOrder.getId());
//
//                if (openOrders.size() > 0) {
//                    for (int i = 0; i < openOrders.size(); i++) {
//                        BitmexRestMethods.cancelOrder(openOrders.get(i).getId());
//                    }
//                    openOrders.clear();
//                }
//
//                openOrders.add(closeOrder);
//
//                currentPosition = currentAmt;
//
//            } else {
//                freshStart = false;
//                System.out.println("starting position: " + currentPosition);
//            }
//
//
//        } else if (currentAmt == 0) {
//            System.out.println("position 0, reset limits");
//
//
//
//        }
//
//
//    }

    @Override
    public void onMessage(String message) {

//        System.out.println(message);

        if (!message.contains("keys")) {

            if (message.contains("{\"table\":\"order\"")) {
                onMessageOrder(message);
            }
            if (message.contains("{\"table\":\"position\"")) {
                onMessageOrder(message);
            }
            if (message.contains("{\"table\":\"quote\"")) {
                onMessageOrder(message);
            }

//        if (message.contains("{\"table\":\"position\"")) {
//            try {
//                onMessagePosition(message);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        if (message.contains())

//        if (message.contains("\"table\":\"quote\"")) {
//            onMessageQuote(message);
//        }
//        } else if (message.contains("orderBookL2")) {
//            try {
//                onMessageOrderBook(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (message.contains("liquidation")) {
//            try {
//                onMessageLiq(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////        } else {
////            onMessageOther(message);
//        }
        }
    }

    private void onMessageOrder(String message) {



//        String action = StringUtils.substringBetween(message, "\"action\":\"", "\",");
//        int currentAmt; try { currentAmt = Integer.parseInt(StringUtils.substringBetween(message, "\"leavesQty\":", ",")); }catch (Exception e) { currentAmt = -1; }
//        String side = StringUtils.substringBetween(message, "\"side\":\"", "\",");
//        String status = StringUtils.substringBetween(message, "\"ordStatus\":\"", "\",");
//
//        String id = StringUtils.substringBetween(message, "\"orderID\":\"", "\",");
//
//
//        String price = StringUtils.substringBetween(message, "\"price\":\"", "\",");


//        Broadcaster.broadcast("%" + "bitmexOrder" + "%<" + action + ">!" + side + "!#" + currentAmt + "#@" + status + "@*" + price + "*~" + id + "~=");

        Broadcaster.broadcast(message);

    }

//    private void onMessageQuote(String message) {
//
//
//        String bid = StringUtils.substringBetween(message, "\"bidPrice\":", ",\"");
//        String ask = StringUtils.substringBetween(message, "\"askPrice\":", ",\"");
//
//        double bidd = 0;
//        double askk = 0;
//
//        if (!bid.contains("float") && !ask.contains("float"))
//            try {
//
//                bidd = Double.parseDouble(bid);
//                askk = Double.parseDouble(ask);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        if (currentBid != bidd) {
//            currentBid = bidd;
//            Broadcaster.broadcast("bid:" + currentBid + "*");
//        }
//
//        if (currentAsk != askk) {
//            currentAsk = askk;
//            Broadcaster.broadcast("ask:" + currentAsk + "*");
//        }
//
//
//
//
//
//
//    }

//    private void onMessageTrade(String message) {
//
//        BitmexTrades trades;
//
//        try {
//            trades = mapper.readValue(message, BitmexTrades.class);
//            List<BitmexTrade> tradeData = trades.getData();
//
////            int price
//
//
//            //ez
//            int total = 0;
//            for (int i = 0; i < tradeData.size(); i++) {
//                total += tradeData.get(i).getSize();
//            }
//
//
////            double firstPrice = tradeData.get(0).getPrice();
////            double lasttPrice = tradeData.get(tradeData.size() - 1).getPrice();
////
////            if (total >= 100) {
////                Broadcaster.broadcast("%" + "bitmex" + "%<" + getInstrument(tradeData.get(0)) + ">!" + (tradeData.get(0).getSide().equals("Buy")) + "!#" + (tradeData.get(0).getSide().equals("Sell") ? "-" : "") + total + "#@" + tradeData.get(0).getPrice() + "@*" + tradeData.get(0).getTimestamp() + "*~" + firstPrice + "~=" + lasttPrice + "=+");
////            }
//
//        } catch (Exception ee) {
//            System.out.println(ee.getLocalizedMessage());
//            System.out.println(message);
//        }
//
//    }



//    private void onMessageOther(String message) {
//        System.out.println(message);
//    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("bitmex onOpen");
        super.onOpen(handshakedata);
    }

//    private String getInstrument(BitmexTrade trade) {
//        String instrument;
//
//        switch (trade.getSymbol()) {
//            case "XBTUSD":
//                instrument = "bitmexPerp";
//                break;
//            case "XBTM18":
//                instrument = "bitmexJune";
//                break;
//            case "XBTU18":
//                instrument = "bitmexSept";
//                break;
//            default:
//                instrument = "invalid instrument";
//                break;
//        }
//
//        return instrument;
//
//    }
}
