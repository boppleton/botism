package rest;

import gui.MainWindow;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitmex.dto.marketdata.BitmexPrivateOrder;
import org.knowm.xchange.bitmex.dto.trade.BitmexPosition;
import org.knowm.xchange.bitmex.dto.trade.BitmexSide;
import org.knowm.xchange.bitmex.service.BitmexMarketDataServiceRaw;
import org.knowm.xchange.bitmex.service.BitmexTradeServiceRaw;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import utils.Formatter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class BitmexRestMethods {

    private static Exchange exchange;
    private static MarketDataService service;
    private static TradeService tradeService;
    private static BitmexMarketDataServiceRaw serviceRaw;
    private static BitmexTradeServiceRaw tradeRaw;

    public BitmexRestMethods() {

        exchange = BitmexSetup.createExchange();

        startExchangeServices();


    }

    public static BitmexPrivateOrder updateClose(int positionsize, double entry, double profitPercent, boolean force) throws InterruptedException {

        double closePrice = positionsize > 0 ? (entry * (1+profitPercent*.01)) : (entry * (1-profitPercent*.01));

        System.out.println("setting close for position:" + positionsize + " entry:" + entry + "closeprice:" + closePrice);

        BitmexPrivateOrder order = limit(-positionsize, positionsize > 0 ? Math.ceil(closePrice) : Math.floor(closePrice), force);


        return order;

    }

    public static Order getOrder(String uuid) throws IOException, InterruptedException {

        Order order = null;

        Collection<Order> orders = null;

        try {

            orders = tradeService.getOrder(uuid);

            for (Order o : orders) {
                order = o;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error getting order, retrying");
            Thread.sleep(1000);
            getOrder(uuid);
        }
        return order;


    }

//    public static BitmexPrivateOrder replaceOrder(double amt, double price, String uuid) throws InterruptedException {
//
//        System.out.println("trying to replace " + amt + " " + price + " " + uuid + "active: " + MainWindow.active);
//
//        BitmexPrivateOrder repacedOrder = tradeRaw.replaceLimitOrder("XBTUSD", new BigDecimal(amt), new BigDecimal(price), uuid, null, null);
//
//        System.out.println(repacedOrder);
//
//        if (repacedOrder.getOrderStatus().toString().contains("Canceled")) {
//            System.out.println("canceled, re-doing with amt " + amt + " price " + (amt > 0 ? MainWindow.currentBid: MainWindow.currentAsk));
//            replaceOrder(amt, amt > 0 ? MainWindow.currentBid: MainWindow.currentAsk, uuid);
//        }
//
//        return repacedOrder;
//
//    }

    public static void cancelOrder(String uuid) throws InterruptedException, IOException {

        System.out.println("cancelling " + uuid);

        System.out.println("get order");

        Order theOrder = getOrder(uuid);

        System.out.println(theOrder.getStatus());

        if (theOrder.getStatus().toString().contains("CANCELED") || theOrder.getStatus().toString().contains("FILLED") || theOrder.getStatus().toString().contains("REJECTED")) {

            System.out.println("dont need to cancel, status: " + theOrder.getStatus());

        } else {


            BitmexPrivateOrder tryingtocancel = tradeRaw.cancelBitmexOrder(uuid).get(0);

            if (tryingtocancel.getOrderStatus().equals(Order.OrderStatus.CANCELED)) {
                System.out.println("cancelled successfully");
            } else {
                System.out.println("order maybe not cancelled, pausing then trying again");
                Thread.sleep(1000);
                cancelOrder(uuid);
            }
        }

    }

    public static BitmexPrivateOrder limit(double amt, double price, boolean force) throws InterruptedException {


        BitmexPrivateOrder limitOrder = null;

        try {

            limitOrder = tradeRaw.placeLimitOrder("XBTUSD", new BigDecimal(amt), new BigDecimal(Formatter.getRoundedPrice(price)), amt > 0 ? BitmexSide.BUY : BitmexSide.SELL, null, "ParticipateDoNotInitiate");

        } catch (Exception e) {

            e.printStackTrace();

            if (e.getCause() != null) {

                System.out.println(e.getCause().getMessage());
                System.out.println("stacktrace obj: " + e.getCause().getMessage());
                if (force && e.getCause().getMessage().contains("503")) {
                    System.out.println("overload error (limit), retrying in 1000ms");
                    Thread.sleep(1000);
                    limit(amt, price, true);
                } else if (e.getCause().getMessage().contains("503")) {
                    System.out.println("overload error (limit), not forcing");
                } else {
                    e.printStackTrace();
                }
            }
        }

        return limitOrder;
    }

    public static BitmexPrivateOrder market(double amt, boolean force) throws InterruptedException {

        BitmexPrivateOrder marketOrder = null;

        try {

//            marketOrder = tradeRaw.placeMarketOrder("XBTUSD", new BigDecimal(amt), null);

        } catch (Exception e) {
//            e.printStackTrace();
            if (force && e.getMessage().contains("503")) {
                System.out.println("overload error (market), retrying in 1000ms");
                Thread.sleep(1000);
                market(amt, true);
            }else if (e.getMessage().contains("503")) {
                System.out.println("overload error (market), not forcing");
            } else {
                e.printStackTrace();
            }
        }

        return marketOrder;

    }


    public static void MLscalp(double amt, double profitPercent) throws InterruptedException {

        BitmexPrivateOrder market = market(amt, true);

        System.out.println("market order: " + market.toString());

        if (market.getId() != null) {

            limit(-amt,market.getPrice().doubleValue() * (amt > 0 ? (1 + profitPercent*.01) : (1 - profitPercent*.01) ), true );

        }



    }

    public static void limitLine(double start, double howMany, double gap, double amt) throws InterruptedException {

        System.out.println("limitline start: " + start + " amt " + amt + " #:" + howMany + " gap:" + gap);

        double price = start;


        for (int i = 0; i < howMany; i++) {

            System.out.println("placing limit: " + amt + " price: " + price);

            limit(amt, price, true);

            if (amt > 0) {
                price -= gap;
            } else {
                price += gap;
            }

            Thread.sleep(3000);

        }




    }


    public static double getPosition() throws IOException {

        double posSize = -1;

        try {

            List<BitmexPosition> positions = tradeRaw.getBitmexPositions();

            System.out.println("getting position");

            Thread.sleep(500);

            for (BitmexPosition pos : positions) {

                System.out.println(pos);

                posSize = pos.getCurrentQty().doubleValue();

                System.out.println(posSize);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }



        return posSize;


    }






    public static void startExchangeServices() {

        exchange = BitmexSetup.createExchange();
        service = exchange.getMarketDataService();
        tradeService = exchange.getTradeService();
//
        serviceRaw = (BitmexMarketDataServiceRaw) service;
        tradeRaw = (BitmexTradeServiceRaw) tradeService;

    }



}
