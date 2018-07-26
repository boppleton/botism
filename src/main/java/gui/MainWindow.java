package gui;

import data.Order;
import data.Position;
import data.Quote;
import gui.order.OpenOrdersCell;
import gui.order.OpenOrdersTableModel;
import gui.position.PositionsCell;
import gui.position.PositionsTableModel;
import macro.ForceMaker;
import org.apache.commons.lang3.StringUtils;
import rest.BitmexRestMethods;
import utils.Broadcaster;
import utils.Formatter;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainWindow extends JFrame implements Broadcaster.BroadcastListener {

    private JScrollPane ordersScrollPane;
    private JTable ordersTable;

    private JScrollPane positionsScrollPane;
    private JTable positionsTable;

    public static ArrayList<Quote> getQuotes() {
        return quotes;
    }

    private static ArrayList<Quote> quotes = new ArrayList<>();

    public static ArrayList<ForceMaker> getForcemakers() {
        return forcemakers;
    }

    private static ArrayList<ForceMaker> forcemakers = new ArrayList<>();

    /// doin this
    private static ArrayList<Order> XBTUSDorders = new ArrayList<>();
    private static ArrayList<Order> XBTU18orders = new ArrayList<>();

    private static ArrayList<Position> XBTUSDpositions = new ArrayList<>();
    private static ArrayList<Position> XBTU18positions = new ArrayList<>();

    private static GridBagConstraints gbc = new GridBagConstraints();


//    private static JTextField amtField;
//    private static JTextField priceField;
//    private static JPanel pricePanel;


    private static boolean XBTUSDforcemakerBool = true;
    private static boolean XBTUSDlimitBool = false;
    private static boolean XBTUSDmarketBool = false;

    private static boolean XBTU18forcemakerBool = true;
    private static boolean XBTU18limitBool = false;
    private static boolean XBTU18marketBool = false;


    private static JTabbedPane tabs;

    public MainWindow(String title) {
        super(title);
        setLayout(new GridBagLayout());
        Broadcaster.register(this);


        timerRefresh();

//        XBTUSDorders.add(new Order("update", "Buy", 10000, "New", "7200.5", "dff14hj-8ecbff2-8591039a-348a"));
//        XBTUSDorders.add(new Order("update", "Buy", 10000, "New", "7100.5", "678dshj-dgssdjf-sdfg39a-sgf64h"));
//        XBTUSDorders.add(new Order("update", "Buy", 20000, "New", "7000.5", "ppooyhj-8ecbfnj-859jgfjjj-oyyi"));
//
//        XBTU18orders.add(new Order("update", "Buy", 500, "New", "7000.5", "ppooyhj-8ecbfnj-859jgfjjj-oyyi"));

//        XBTUSDpositions.add(new Position("XBT/USD","insert", 10000, "New", "7200.5"));

        tabs = new JTabbedPane();

        gbc.fill = GridBagConstraints.BOTH;
        gbc(0, 0, 1, 1, GridBagConstraints.NORTHWEST);
        add(tabs, gbc);
        gbc.fill = GridBagConstraints.NONE;

        newInstrumentTab("XBT/USD", new Quote(), new JPanel(new GridBagLayout()), new JTextField(), new JTextField(), new JPanel(false), XBTUSDforcemakerBool, XBTUSDlimitBool, XBTUSDmarketBool);

        newInstrumentTab("XBT/U18", new Quote(), new JPanel(new GridBagLayout()), new JTextField(), new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);


//        timerRefresh();
    }

    private void newInstrumentTab(String instrument, Quote quote, JPanel panel, JTextField amtField, JTextField priceField, JPanel pricePanel, boolean forcemakerBool, boolean limitBool, boolean marketBool) {

        quote.setInstrument(instrument);
        quotes.add(quote);

        tabs.add(instrument, panel);

        setupTradeProfitStopPanels(instrument, panel, amtField, priceField, pricePanel, forcemakerBool, limitBool, marketBool);

        openOrdersPanel(instrument, panel);

        positionsPanel(instrument, panel);


    }




    private void setupTradeProfitStopPanels(String instrument, JPanel panel, JTextField amtField, JTextField priceField, JPanel pricePanel, boolean forcemakerBool, boolean limitBool, boolean marketBool) {

        JPanel tradeprofitstopPanel = new JPanel(new GridBagLayout());

        JButton buy = new JButton("buy");

        JButton sell = new JButton("sell");

        JRadioButton forcemakerRadio = new JRadioButton("force maker");
        JRadioButton limitCheckbox = new JRadioButton("limit");
        JRadioButton marketCheckbox = new JRadioButton("market");


        gbc(0, 0, 1, 0, GridBagConstraints.NORTHWEST);
        panel.add(tradeprofitstopPanel, gbc);

        tradePanel(instrument, tradeprofitstopPanel, amtField, priceField, pricePanel, buy, sell, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool);
        takeProfitPanel(instrument, tradeprofitstopPanel);
//        stopsPanel(tradeprofitstopPanel);

        setupTradeStuff(forcemakerRadio, pricePanel);

//        setupBuySellButtons(instrument, buy, sell, priceField, pricePanel, forcemakerBool, limitBool, marketBool);


    }

    private void tradePanel(String instrument, JPanel tradeprofitstopPanel, JTextField amtField, JTextField priceField, JPanel pricePanel, JButton buy, JButton sell, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool) {

        //make main trade panel
        JPanel tradePanel = new JPanel(new GridBagLayout());
        tradePanel.setBorder(BorderFactory.createTitledBorder("trade"));

        //amount: [ ]
        JPanel amountPanel = new JPanel();

        JLabel amountLabel = new JLabel("amount");
        amountPanel.add(amountLabel);

        amtField = new JTextField(7);
        amountPanel.add(amtField);

        //price: [ ]
        pricePanel = new JPanel();
        pricePanel.setVisible(false);

        JLabel priceLabel = new JLabel("price");
        pricePanel.add(priceLabel);

        priceField = new JTextField(7);
        pricePanel.add(priceField);

        //panel for amount and price
        JPanel amountPricePanel = new JPanel(new GridBagLayout());
        gbc(0, 0, 0, 0, GridBagConstraints.EAST);
        amountPricePanel.add(amountPanel, gbc);
        gbc(0, 1, 0, 0, GridBagConstraints.EAST);
        amountPricePanel.add(pricePanel, gbc);

        gbc(0, 0, 0, 0, GridBagConstraints.CENTER);
        tradePanel.add(amountPricePanel, gbc);

        // [Buy] [Sell]
        JPanel buysellButtonsPanel = new JPanel();

        buy = new JButton("Buy");
        buysellButtonsPanel.add(buy);

        sell = new JButton("Sell");
        buysellButtonsPanel.add(sell);

        gbc(0, 1, 1, 1, GridBagConstraints.CENTER);
        tradePanel.add(buysellButtonsPanel, gbc);

        // * ordertype radios


        ButtonGroup radios1 = new ButtonGroup();
        radios1.add(forcemakerRadio);
        radios1.add(limitCheckbox);
        radios1.add(marketCheckbox);

        gbc(0, 2, 1, 1, GridBagConstraints.WEST);
        tradePanel.add(forcemakerRadio, gbc);
        gbc(0, 3, 1, 1, GridBagConstraints.WEST);
        tradePanel.add(limitCheckbox, gbc);
        gbc(0, 4, 1, 1, GridBagConstraints.WEST);
        tradePanel.add(marketCheckbox, gbc);

        if (instrument.contains("XBT/USD")) {
            XBTUSDbuttons(instrument, buy, sell, amtField, priceField);
            setupXBTUSDTradeRadios("XBT/USD", buy, sell, amtField, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool, priceField, pricePanel);
        } else if (instrument.contains("XBT/U18")) {
            XBTU18buttons(instrument, buy, sell, amtField, priceField);
            setupXBTU18TradeRadios("XBT/U18", buy, sell, amtField, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool, priceField, pricePanel);
        }


        //add tradepanel to main panelgroup
        gbc(0, 0, .2, 0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(tradePanel, gbc);

    }

    private void XBTU18buttons(String instrument, JButton buy, JButton sell, JTextField amtField, JTextField priceField) {

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(instrument +( XBTUSDforcemakerBool ? "forcemaker" : "") + (XBTUSDlimitBool ? "limit" : "") + (XBTUSDmarketBool ? "market" : "") + " buy " + amtField.getText());

                if (XBTU18forcemakerBool) {

                    System.out.println("XBTU18" + "forcemaker buy " + amtField.getText());

                } else if (XBTU18limitBool) {
                    try {
                        BitmexRestMethods.limit("XBTU18", (double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println(instrument + "limit buy " + amtField.getText() + " @ " + priceField.getText());

                } else if (XBTU18marketBool) {
                    System.out.println("market");
                    try {
                        BitmexRestMethods.market("XBTU18", (double) Formatter.getNumber(amtField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println(instrument + "market buy " + amtField.getText());

                }

            }
        });

        sell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (XBTU18forcemakerBool) {

                    System.out.println("XBTU18" + "forcemaker sell " + amtField.getText());

                } else if (XBTU18limitBool) {
                    try {
                        BitmexRestMethods.limit("XBTU18", (double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTU18" + "limit sell " + amtField.getText() + " @ " + priceField.getText());

                } else if (XBTU18marketBool) {
//                    System.out.println("market");
                    try {
                        BitmexRestMethods.market("XBTU18", (double) Formatter.getNumber(amtField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTU18" + "market sell " + amtField.getText());

                }

            }
        });

    }

    private void XBTUSDbuttons(String instrument, JButton buy, JButton sell, JTextField amtField, JTextField priceField) {

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(instrument +( XBTUSDforcemakerBool ? "forcemaker" : "") + (XBTUSDlimitBool ? "limit" : "") + (XBTUSDmarketBool ? "market" : "") + " buy " + amtField.getText());

                if (XBTUSDforcemakerBool) {

                    System.out.println("XBTUSD" + "forcemaker buy " + Formatter.getNumber(amtField.getText()));

                    try {
                        ForceMaker forcemaker = new ForceMaker("XBT/USD", Formatter.getNumber(amtField.getText()));
                        forcemakers.add(forcemaker);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }


                } else if (XBTUSDlimitBool) {
                    try {
                        BitmexRestMethods.limit("XBTUSD", (double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTUSD" + "limit buy " + amtField.getText() + " @ " + priceField.getText());

                } else if (XBTUSDmarketBool) {
                    System.out.println("market");
                    try {
                        BitmexRestMethods.market("XBTUSD", (double) Formatter.getNumber(amtField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTUSD" + "market buy " + amtField.getText());

                }

            }
        });

        sell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (XBTUSDforcemakerBool) {

                    System.out.println("XBTUSD" + "forcemaker sell " + amtField.getText());

                } else if (XBTUSDlimitBool) {
                    try {
                        BitmexRestMethods.limit("XBTUSD", (double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTUSD" + "limit sell " + amtField.getText() + " @ " + priceField.getText());

                } else if (XBTUSDmarketBool) {
//                    System.out.println("market");
                    try {
                        BitmexRestMethods.market("XBTUSD", (double) Formatter.getNumber(amtField.getText()), true);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("XBTUSD" + "market sell " + amtField.getText());

                }

            }
        });

    }

    private void takeProfitPanel(String instrument, JPanel tradeprofitstopPanel) {

        JPanel takeprofitPanel = new JPanel(new GridBagLayout());
        takeprofitPanel.setBorder(BorderFactory.createTitledBorder("take profits"));


        gbc.fill = GridBagConstraints.BOTH;


        JLabel profLabel = new JLabel("close ");
        gbc(0, 0, .1, .1, GridBagConstraints.WEST);
        takeprofitPanel.add(profLabel, gbc);

        gbc(1, 0, .1, .1, GridBagConstraints.EAST);
//        JCheckBox takeprofit1 = new JCheckBox("take profit 1");
        JTextField profitAmountPercent = new JTextField();

        profitAmountPercent.setText("10");


        takeprofitPanel.add(profitAmountPercent, gbc);

        JLabel percentAtLabel = new JLabel("% at");
        gbc(2, 0, .1, .1, GridBagConstraints.WEST);
        takeprofitPanel.add(percentAtLabel, gbc);

        JTextField profitPercent = new JTextField(".1");
        gbc(3, 0, .1, .1, GridBagConstraints.WEST);
        takeprofitPanel.add(profitPercent, gbc);

        //add to main panel
        gbc(1, 0, 1, 0, GridBagConstraints.NORTHEAST);
        tradeprofitstopPanel.add(takeprofitPanel, gbc);


    }

    private void stopsPanel(JPanel tradeprofitstopPanel) {

        JPanel stopsPanel = new JPanel(new GridBagLayout());
        stopsPanel.setBorder(BorderFactory.createTitledBorder("stops"));

        gbc(0, 0, 0, 0, GridBagConstraints.WEST);
        JCheckBox stop1 = new JCheckBox("stop 1");

        stopsPanel.add(stop1, gbc);

        //add to main panel
        gbc(2, 0, .1, 0, GridBagConstraints.NORTHEAST);
        tradeprofitstopPanel.add(stopsPanel, gbc);

    }

    private void setupXBTUSDTradeRadios(String instrument, JButton buy, JButton sell, JTextField amtField, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool, JTextField priceField, JPanel pricePanel) {

        forcemakerRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTUSDforcemakerBool = true;
                XBTUSDlimitBool = false;
                XBTUSDmarketBool = false;


                pricePanel.setVisible(false);
            }
        });

        limitCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTUSDforcemakerBool = false;
                XBTUSDlimitBool = true;
                XBTUSDmarketBool = false;

                pricePanel.setVisible(true);
            }
        });

        marketCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTUSDforcemakerBool = false;
                XBTUSDlimitBool = false;
                XBTUSDmarketBool = true;

                pricePanel.setVisible(false);
            }
        });
    }

    private void setupXBTU18TradeRadios(String instrument, JButton buy, JButton sell, JTextField amtField, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool, JTextField priceField, JPanel pricePanel) {


        forcemakerRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTU18forcemakerBool = true;
                XBTU18limitBool = false;
                XBTU18marketBool = false;


                pricePanel.setVisible(false);
            }
        });

        limitCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTU18forcemakerBool = false;
                XBTU18limitBool = true;
                XBTU18marketBool = false;

                pricePanel.setVisible(true);
            }
        });

        marketCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                XBTU18forcemakerBool = false;
                XBTU18limitBool = false;
                XBTU18marketBool = true;

                pricePanel.setVisible(false);
            }
        });
    }


    private void openOrdersPanel(String instrument, JPanel panel) {

        if (instrument.contains("XBT/USD")) {
            ordersTable = new JTable(new OpenOrdersTableModel(XBTUSDorders));
        } else if (instrument.contains("XBT/U18")) {
            ordersTable = new JTable(new OpenOrdersTableModel(XBTU18orders));
        }


        ordersTable.setDefaultRenderer(Order.class, new OpenOrdersCell());
        ordersTable.setTableHeader(null);


        ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.setBorder(BorderFactory.createTitledBorder("orders"));

        ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        ordersScrollPane.addMouseWheelListener(e -> {
            if (ordersScrollPane.getVerticalScrollBar().getValue() == 0) {
                ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            } else {
                ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            }
        });


        gbc(0, 1, .9, .9, GridBagConstraints.NORTHWEST);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(ordersScrollPane, gbc);
        gbc.fill = GridBagConstraints.NONE;


    }


    private void positionsPanel(String instrument, JPanel panel) {


        if (instrument.contains("XBT/USD")) {
            positionsTable = new JTable(new PositionsTableModel(XBTUSDpositions));
        } else if (instrument.contains("XBT/U18")) {
            positionsTable = new JTable(new PositionsTableModel(XBTU18positions));
        }


        positionsTable.setDefaultRenderer(Position.class, new PositionsCell());
        positionsTable.setTableHeader(null);


        positionsScrollPane = new JScrollPane(positionsTable);
        positionsScrollPane.setBorder(BorderFactory.createTitledBorder("position"));

        positionsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        positionsScrollPane.addMouseWheelListener(e -> {
            if (positionsScrollPane.getVerticalScrollBar().getValue() == 0) {
                positionsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            } else {
                positionsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            }
        });


        gbc(0, 2, .1, .1, GridBagConstraints.SOUTHWEST);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(positionsScrollPane, gbc);
        gbc.fill = GridBagConstraints.NONE;



    }


    private void setupTradeStuff(JRadioButton forcemakerRadio, JPanel pricePanel) {

        //price field hidden on start
        pricePanel.setVisible(false);

        //start with forcemaker radio on
        forcemakerRadio.setSelected(true);

    }


    private void gbc(int gridx, int gridy, double weightx, double weighty, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
    }


    @Override
    public void receiveBroadcast(String message) throws InterruptedException, IOException {

        System.out.println("msg received: " + message);

        if (message.contains("table\":\"order")) {


            String instrument = StringUtils.substringBetween(message, "\"symbol\":\"", "\",");
            String id = StringUtils.substringBetween(message, "\"orderID\":\"", "\",");
            String action = StringUtils.substringBetween(message, "\"action\":\"", "\",");
            int currentAmt;
            try {
                currentAmt = Integer.parseInt(StringUtils.substringBetween(message, "\"leavesQty\":", ","));
            } catch (Exception e) {
                currentAmt = -1;
            }

            final int ammt = currentAmt;

            String side = StringUtils.substringBetween(message, "\"side\":\"", "\",");
            String status = StringUtils.substringBetween(message, "\"ordStatus\":\"", "\",");

            String price = StringUtils.substringBetween(message, "\"price\":", ",\"");

            boolean existingOrder = false;
            for (Order o : XBTUSDorders) {
                if (o.getId().contains(id)) {
                    System.out.println("order already in list, updating");
                    if (status != null) {
                        o.setStatus(status);
                    }
                    existingOrder = true;
                }
            }


            if (!existingOrder && !id.contains("guid")) {

                System.out.println("order not in list, adding");
                XBTUSDorders.add(new Order(action, side, ammt, status, price, id));
            }

            if (status != null && status.contains("Filled")) {
                for (ForceMaker fm : forcemakers) {
                    if (fm.getId().contains(id)) {
                        System.out.println("forcemaker filled, removing. amt-" + fm.getAmount() + " id-" + fm.getId());
                        forcemakers.remove(fm);
                    }
                }
            }


        } else if (message.contains("table\":\"position")) {

            String instrument = StringUtils.substringBetween(message, "\"symbol\":\"", "\",");
            String action = StringUtils.substringBetween(message, "\"action\":\"", "\",");
            int currentAmt;
            try {
                currentAmt = Integer.parseInt(StringUtils.substringBetween(message, "\"currentQty\":", ","));
            } catch (Exception e) {
                currentAmt = -1;
            }

            final int ammt = currentAmt;

            String status = StringUtils.substringBetween(message, "\"ordStatus\":\"", "\",");

            String price = StringUtils.substringBetween(message, "\"avgEntryPrice\":", ",\"");

            boolean existingOrder = false;
            for (Position p : XBTUSDpositions) {
                if (p.getInstrument().contains(instrument)) {
                    System.out.println("position already active, updating");
                    if (ammt != -1) {
                        p.setAmount(ammt);
                    }
                    if (price != null) {
                        p.setPrice(price);
                    }
                    existingOrder = true;
                }
            }

            if (ammt == 0) {
                XBTUSDpositions.clear();
                existingOrder = true;
            }


            if (!existingOrder) {

                System.out.println("position not in list, adding " + instrument + " action: " + action + " ammt:" + ammt + " status " + status + " price " + price);
                XBTUSDpositions.add(new Position(instrument, action, ammt, status, price));



            }
        } else if (message.contains("table\":\"quote")) {

            String instrument = StringUtils.substringBetween(message, "\"symbol\":\"", "\",");
            if (instrument.contains("XBT")) {
                instrument = new StringBuilder(instrument).insert(3, "/").toString();
            }

            double bid = Double.parseDouble(StringUtils.substringBetween(message, "\"bidPrice\":", ",\""));
            double ask = Double.parseDouble(StringUtils.substringBetween(message, "\"askPrice\":", ",\""));

            for (Quote q : quotes) {
                if (q.getInstrument().contains(instrument)) {
                    if (bid > 0 && q.getBid() != bid) {
                        q.setBid(bid);
                        System.out.println("new " + instrument + " bid: " + bid);
                        updateForcemakers(q.getBid(), q.getAsk());
                    }
                    if (ask > 0 && q.getAsk() != ask) {
                        q.setAsk(ask);
                        System.out.println("new " + instrument + " ask: " + ask);
                        updateForcemakers(q.getBid(), q.getAsk());
                    }

//                    System.out.println("current " + instrument + "bid: " + bid + " ask: " + ask + " ** quotebid: " + q.getBid() + " quoteask: " + q.getAsk());
                }
            }



        }
    }

    private void updateForcemakers(double bid, double ask) throws InterruptedException {

        for (ForceMaker fm : forcemakers) {

            fm.updateForcelimit((fm.getAmount() > 0), bid, ask);
        }

    }

    private void timerRefresh() {
        System.out.println("timer start");


        // using javax.swing.Timer
        Timer timer = new Timer(0, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {

//                        System.out.println("timer now validating");

                        resize(getSize().width == 600 ? 601:600, getSize().height);


                    }
                });


            }
        });
        timer.setRepeats(true);
        timer.setInitialDelay(1000);
        timer.setDelay(1000);
        timer.start();
    }

    public static void ordersStatusBarSet(String s) {



    }
}
