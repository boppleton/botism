package gui;

import data.Order;
import gui.order.OpenOrdersCell;
import gui.order.OpenOrdersTableModel;
import org.apache.commons.lang3.StringUtils;
import rest.BitmexRestMethods;
import utils.Broadcaster;
import utils.Formatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainWindow extends JFrame implements Broadcaster.BroadcastListener {

    private JScrollPane ordersScrollPane;
    private JTable ordersTable;
    private static ArrayList<Order> orders = new ArrayList<>();

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

        orders.add(new Order("update", "Buy", 10000, "New", "7200.5", "dff14hj-8ecbff2-8591039a-348a"));
        orders.add(new Order("update", "Buy", 10000, "New", "7100.5", "678dshj-dgssdjf-sdfg39a-sgf64h"));
        orders.add(new Order("update", "Buy", 20000, "New", "7000.5", "ppooyhj-8ecbfnj-859jgfjjj-oyyi"));


        tabs = new JTabbedPane();

        gbc.fill = GridBagConstraints.BOTH;
        gbc(0,0,1,1, GridBagConstraints.NORTHWEST);
        add(tabs, gbc);
        gbc.fill = GridBagConstraints.NONE;

        newInstrumentTab("XBT/USD", new JPanel(new GridBagLayout()), new JTextField(), new JTextField(), new JPanel(false),  XBTUSDforcemakerBool, XBTUSDlimitBool, XBTUSDmarketBool);
//
//        JPanel xbtu18Panel = new JPanel(new GridBagLayout());
//

        newInstrumentTab("XBT/U18", new JPanel(new GridBagLayout()), new JTextField(),  new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);

        newInstrumentTab("XBT/Z18", new JPanel(new GridBagLayout()), new JTextField(),  new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);

        newInstrumentTab("ETH/U18", new JPanel(new GridBagLayout()), new JTextField(),  new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);

        newInstrumentTab("BCH/U18", new JPanel(new GridBagLayout()), new JTextField(),  new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);

        newInstrumentTab("XRP/U18", new JPanel(new GridBagLayout()), new JTextField(),  new JTextField(), new JPanel(), XBTU18forcemakerBool, XBTU18limitBool, XBTU18marketBool);





    }

    private void newInstrumentTab(String instrument, JPanel panel, JTextField amtField, JTextField priceField, JPanel pricePanel, boolean forcemakerBool, boolean limitBool, boolean marketBool) {

        tabs.add(instrument, panel);

        setupTradeProfitStopPanels(instrument, panel, amtField, priceField, pricePanel, forcemakerBool, limitBool, marketBool);

        openOrdersPanel(panel);





    }


    private void setupTradeProfitStopPanels(String instrument, JPanel panel, JTextField amtField, JTextField priceField, JPanel pricePanel,  boolean forcemakerBool, boolean limitBool, boolean marketBool) {

        JPanel tradeprofitstopPanel = new JPanel(new GridBagLayout());

        JButton buy = new JButton("buy");

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println((forcemakerBool ? "forcemaker" : "") + (limitBool ? "limit" : "") + (marketBool ? "market" : "") + " buy " + amtField.getText());

                if (forcemakerBool) {

                    System.out.println(instrument + "forcemaker buy " + amtField.getText());

                } else if (limitBool) {
//                    try { BitmexRestMethods.limit((double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "limit buy " + amtField.getText() + " @ " + priceField.getText());

                } else if (marketBool) {
                    System.out.println("market");
//                    try { BitmexRestMethods.market((double) Formatter.getNumber(amtField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "market buy " + amtField.getText());

                }

            }
        });

        JButton sell = new JButton("sell");

        JRadioButton forcemakerRadio = new JRadioButton("force maker");
        JRadioButton limitCheckbox = new JRadioButton("limit");
        JRadioButton marketCheckbox = new JRadioButton("market");



        gbc(0, 0, 0, 0, GridBagConstraints.NORTHWEST);
        panel.add(tradeprofitstopPanel, gbc);

        tradePanel(instrument, tradeprofitstopPanel, amtField, priceField, pricePanel, buy, sell, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool);
        takeProfitPanel(tradeprofitstopPanel);
        stopsPanel(tradeprofitstopPanel);

        setupTradeStuff(forcemakerRadio, pricePanel);

//        setupBuySellButtons(instrument, buy, sell, priceField, pricePanel, forcemakerBool, limitBool, marketBool);


    }

//    private void setupBuySellButtons(String instrument, JButton buy, JButton sell, JTextField amtField, JTextField priceField, JPanel pricePanel, boolean forcemakerBool, boolean limitBool, boolean marketBool) {
//
//
//
//
//
//    }


    private void openOrdersPanel(JPanel panel) {

        ordersTable = new JTable(new OpenOrdersTableModel(orders));
        ordersTable.setDefaultRenderer(Order.class, new OpenOrdersCell());
        ordersTable.setTableHeader(null);


        ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.setBorder(BorderFactory.createTitledBorder("open orders"));

        ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        ordersScrollPane.addMouseWheelListener(e -> {
            if (ordersScrollPane.getVerticalScrollBar().getValue() == 0) {
                ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            } else {
                ordersScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            }
        });


        gbc(0,1,1,1,GridBagConstraints.NORTHWEST);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(ordersScrollPane, gbc);
        gbc.fill = GridBagConstraints.NONE;


    }


    private void tradePanel(String instrument, JPanel tradeprofitstopPanel, JTextField amtField,  JTextField priceField, JPanel pricePanel,  JButton buy, JButton sell, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool) {

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
            setupXBTUSDTradeRadiosAndButtons("XBT/USD", buy, sell, amtField, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool, priceField, pricePanel);
        } else if (instrument.contains("XBT/U18")) {
            setupXBTU18TradeRadiosAndButtons("XBT/U18", buy, sell, amtField, forcemakerRadio, limitCheckbox, marketCheckbox, forcemakerBool, limitBool, marketBool, priceField, pricePanel);
        }


        //add tradepanel to main panelgroup
        gbc(0, 0, 0, 0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(tradePanel, gbc);

    }
    private void takeProfitPanel(JPanel tradeprofitstopPanel) {

        JPanel takeprofitPanel = new JPanel(new GridBagLayout());
        takeprofitPanel.setBorder(BorderFactory.createTitledBorder("take profits"));

        gbc(0, 0, 0, 0, GridBagConstraints.WEST);
        JCheckBox takeprofit1 = new JCheckBox("take profit 1");

        takeprofitPanel.add(takeprofit1, gbc);

        //add to main panel
        gbc(1, 0, 0, 0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(takeprofitPanel, gbc);

    }
    private void stopsPanel(JPanel tradeprofitstopPanel) {

        JPanel stopsPanel = new JPanel(new GridBagLayout());
        stopsPanel.setBorder(BorderFactory.createTitledBorder("stops"));

        gbc(0, 0, 0, 0, GridBagConstraints.WEST);
        JCheckBox stop1 = new JCheckBox("stop 1");

        stopsPanel.add(stop1, gbc);

        //add to main panel
        gbc(2, 0, 0, 0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(stopsPanel, gbc);

    }



    private void setupXBTUSDTradeRadiosAndButtons(String instrument, JButton buy, JButton sell, JTextField amtField, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool, JTextField priceField, JPanel pricePanel) {

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(instrument +( XBTUSDforcemakerBool ? "forcemaker" : "") + (XBTUSDlimitBool ? "limit" : "") + (XBTUSDmarketBool ? "market" : "") + " buy " + amtField.getText());

//                if (forcemakerBool) {
//
//                    System.out.println(instrument + "forcemaker buy " + amtField.getText());
//
//                } else if (limitBool) {
////                    try { BitmexRestMethods.limit((double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
////                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//
//                    System.out.println(instrument + "limit buy " + amtField.getText() + " @ " + priceField.getText());
//
//                } else if (marketBool) {
//                    System.out.println("market");
////                    try { BitmexRestMethods.market((double) Formatter.getNumber(amtField.getText()), true);
////                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//
//                    System.out.println(instrument + "market buy " + amtField.getText());
//
//                }

            }
        });

        sell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (forcemakerBool) {

                    System.out.println(instrument + "forcemaker sell " + amtField.getText());

                } else if (limitBool) {
//                    try { BitmexRestMethods.limit((double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "limit sell " + amtField.getText() + " @ " + priceField.getText());

                } else if (marketBool) {
                    System.out.println("market");
//                    try { BitmexRestMethods.market((double) Formatter.getNumber(amtField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "market sell " + amtField.getText());

                }

//                if (forcemakerBool) {
//
//                } else if (limitBool) {
//                    try { BitmexRestMethods.limit((double) -Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//                } else if (marketBool) {
//                    System.out.println("market");
//                    try { BitmexRestMethods.market((double) -Formatter.getNumber(amtField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//                }
            }
        });


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

    private void setupXBTU18TradeRadiosAndButtons(String instrument, JButton buy, JButton sell, JTextField amtField, JRadioButton forcemakerRadio, JRadioButton limitCheckbox, JRadioButton marketCheckbox, boolean forcemakerBool, boolean limitBool, boolean marketBool, JTextField priceField, JPanel pricePanel) {

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(instrument + (XBTU18forcemakerBool ? "forcemaker" : "") + (XBTU18limitBool ? "limit" : "") + (XBTU18marketBool ? "market" : "") + " buy " + amtField.getText());

//                if (forcemakerBool) {
//
//                    System.out.println(instrument + "forcemaker buy " + amtField.getText());
//
//                } else if (limitBool) {
////                    try { BitmexRestMethods.limit((double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
////                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//
//                    System.out.println(instrument + "limit buy " + amtField.getText() + " @ " + priceField.getText());
//
//                } else if (marketBool) {
//                    System.out.println("market");
////                    try { BitmexRestMethods.market((double) Formatter.getNumber(amtField.getText()), true);
////                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//
//                    System.out.println(instrument + "market buy " + amtField.getText());
//
//                }

            }
        });

        sell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (forcemakerBool) {

                    System.out.println(instrument + "forcemaker sell " + amtField.getText());

                } else if (limitBool) {
//                    try { BitmexRestMethods.limit((double) Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "limit sell " + amtField.getText() + " @ " + priceField.getText());

                } else if (marketBool) {
                    System.out.println("market");
//                    try { BitmexRestMethods.market((double) Formatter.getNumber(amtField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }

                    System.out.println(instrument + "market sell " + amtField.getText());

                }

//                if (forcemakerBool) {
//
//                } else if (limitBool) {
//                    try { BitmexRestMethods.limit((double) -Formatter.getNumber(amtField.getText()), Double.parseDouble(priceField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//                } else if (marketBool) {
//                    System.out.println("market");
//                    try { BitmexRestMethods.market((double) -Formatter.getNumber(amtField.getText()), true);
//                    } catch (InterruptedException e1) { e1.printStackTrace(); }
//                }
            }
        });


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

        System.out.println("order msg received: " + message);

        if (message.contains("table\":\"order")) {

            String id = StringUtils.substringBetween(message, "\"orderID\":\"", "\",");
            String action = StringUtils.substringBetween(message, "\"action\":\"", "\",");
            int currentAmt;
            try {
                currentAmt = Integer.parseInt(StringUtils.substringBetween(message, "\"leavesQty\":", ","));
            } catch (Exception e) {
                currentAmt = -1;
            }
            String side = StringUtils.substringBetween(message, "\"side\":\"", "\",");
            String status = StringUtils.substringBetween(message, "\"ordStatus\":\"", "\",");

            String price = StringUtils.substringBetween(message, "\"price\":\"", "\",");

            boolean existingOrder = false;
            for (Order o : orders) {
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
                orders.add(new Order(action, side, currentAmt, status, price, id));
            }

            SwingUtilities.invokeLater(() -> {

                revalidate();
                repaint();

                getContentPane().revalidate();
                getContentPane().revalidate();

                ordersTable.revalidate();
                ordersTable.repaint();

            });

        }

    }
}
