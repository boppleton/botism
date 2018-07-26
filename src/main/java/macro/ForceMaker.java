package macro;

import data.Quote;
import gui.MainWindow;
import org.knowm.xchange.bitmex.dto.marketdata.BitmexPrivateOrder;
import rest.BitmexRestMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ForceMaker {

    private long timestamp;
    private String instrument;
    private int amount;

    private String id;

    private double bid;
    private double ask;

    private String orderStatus;

    private Timer timer;


    public ForceMaker(String instrument, int amount) throws InterruptedException {

        timestamp = System.currentTimeMillis();

        this.instrument = instrument.replace("/", "");
        this.amount = amount;

        id = startLimit(this.instrument, amount);

        startForceListener();

    }

    private void startForceListener() {




    }

    public void updateForcelimit(boolean longing, double bid, double ask) throws InterruptedException {

        System.out.println("updating forcelimits bid--" + bid + " ask--" + ask);

        if (longing && bid != this.bid) {
            System.out.println("bid:" + bid + "this.bid:" + this.bid);
            replaceLimit(bid);
        }

        if (!longing && ask != this.ask) {
            replaceLimit(ask);
        }

        this.bid = bid;
        this.ask = ask;

    }

    private String replaceLimit(double price) throws InterruptedException {

        System.out.println("replaceing " + instrument.replace("/", "") + amount + price + id);

        BitmexPrivateOrder replaceOrder = null;

        replaceOrder = BitmexRestMethods.replaceOrder(instrument.replace("/", ""), amount, price, id);

        System.out.println("replaced order ok, id " + replaceOrder.getId());

        return replaceOrder.getId();

    }

    private String startLimit(String instrument, int amount) throws InterruptedException {

        BitmexPrivateOrder order = null;

        ArrayList<Quote> quotes = MainWindow.getQuotes();

        for (Quote q : quotes) {
            if (q.getInstrument().replace("/","").contains(instrument)) {

                System.out.println("doing limit for " + instrument + " amt: " + amount + (amount>0?"bid-"+q.getBid():"ask-"+q.getAsk()));

                bid = q.getBid();
                ask = q.getAsk();

                order = BitmexRestMethods.limit(instrument.replace("/", ""), amount, amount>0?q.getBid():q.getAsk(), false);
            }
        }



        if (order != null) {
            return order.getId();
        } else {
            return "null";
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
