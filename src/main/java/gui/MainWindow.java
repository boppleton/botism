package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    private static GridBagConstraints gbc = new GridBagConstraints();
    private static JPanel tradeprofitstopPanel = new JPanel(new GridBagLayout());

    private static JTextField amtField;
    private static JTextField priceField;

    private static JButton buy;
    private static JButton sell;

    private static JRadioButton forcemakerRadio;
    private static boolean forcemakerBool = true;

    private static JRadioButton limitCheckbox;
    private static boolean limitBool = false;

    private static JRadioButton marketCheckbox;
    private static boolean marketBool = false;

    public MainWindow(String title) {
        super(title);
        setLayout(new GridBagLayout());

        setupTradeProfitStopPanels();
        openOrdersPanel();

        setupBuySellButtons();

        setupTradeStuff();

        setupTradeRadios();

    }

    private void setupTradeRadios() {

        forcemakerRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                forcemakerBool = true;
                limitBool = false;
                marketBool = false;
                System.out.println(" " + forcemakerBool + limitBool + marketBool);
            }
        });

        limitCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                forcemakerBool = false;
                limitBool = true;
                marketBool = false;
                System.out.println(" " + forcemakerBool + limitBool + marketBool);
            }
        });

        marketCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                forcemakerBool = false;
                limitBool = false;
                marketBool = true;
                System.out.println(" " + forcemakerBool + limitBool + marketBool);
            }
        });
    }

    private void setupTradeStuff() {

        //start with forcemaker radio on
        forcemakerRadio.setSelected(true);

        //start with pricefield disabled
        priceField.setEnabled(false);

    }

    private void setupBuySellButtons() {

        buy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("buy " + amtField.getText());
            }
        });

        sell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("sell " + amtField.getText());
            }
        });


    }

    private void setupTradeProfitStopPanels() {

        gbc(0,0,0,0,GridBagConstraints.NORTHWEST);
        add(tradeprofitstopPanel, gbc);

        tradePanel();
        takeProfitPanel();
        stopsPanel();
    }

    private void tradePanel() {

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
        JPanel pricePanel = new JPanel();

        JLabel priceLabel = new JLabel("price");
        pricePanel.add(priceLabel);

        priceField = new JTextField(7);
        pricePanel.add(priceField);

        //panel for amount and price
        JPanel amountPricePanel = new JPanel(new GridBagLayout());
        gbc(0,0,0,0,GridBagConstraints.EAST);
        amountPricePanel.add(amountPanel, gbc);
        gbc(0,1,0,0,GridBagConstraints.EAST);
        amountPricePanel.add(pricePanel, gbc);

        gbc(0,0,0,0, GridBagConstraints.CENTER);
        tradePanel.add(amountPricePanel, gbc);

        // [Buy] [Sell]
        JPanel buysellButtonsPanel = new JPanel();

        buy = new JButton("Buy");
        buysellButtonsPanel.add(buy);

        sell = new JButton("Sell");
        buysellButtonsPanel.add(sell);

        gbc(0,1,1,1, GridBagConstraints.CENTER);
        tradePanel.add(buysellButtonsPanel, gbc);

        // * ordertype radios
        forcemakerRadio = new JRadioButton("force maker");
        limitCheckbox = new JRadioButton("limit");
        marketCheckbox = new JRadioButton("market");

        ButtonGroup radios1 = new ButtonGroup();
        radios1.add(forcemakerRadio);
        radios1.add(limitCheckbox);
        radios1.add(marketCheckbox);

        gbc(0,2,1,1, GridBagConstraints.WEST);
        tradePanel.add(forcemakerRadio, gbc);
        gbc(0,3,1,1, GridBagConstraints.WEST);
        tradePanel.add(limitCheckbox, gbc);
        gbc(0,4,1,1, GridBagConstraints.WEST);
        tradePanel.add(marketCheckbox, gbc);

        //add tradepanel to main panelgroup
        gbc(0,0,0,0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(tradePanel, gbc);

    }

    private void takeProfitPanel() {

        JPanel takeprofitPanel = new JPanel(new GridBagLayout());
        takeprofitPanel.setBorder(BorderFactory.createTitledBorder("take profits"));

        gbc(0,0,0,0,GridBagConstraints.WEST);
        JCheckBox takeprofit1 = new JCheckBox("take profit 1");

        takeprofitPanel.add(takeprofit1, gbc);

        //add to main panel
        gbc(1,0,0,0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(takeprofitPanel, gbc);

    }

    private void stopsPanel() {

        JPanel stopsPanel = new JPanel(new GridBagLayout());
        stopsPanel.setBorder(BorderFactory.createTitledBorder("stops"));

        gbc(0,0,0,0,GridBagConstraints.WEST);
        JCheckBox stop1 = new JCheckBox("stop 1");

        stopsPanel.add(stop1, gbc);

        //add to main panel
        gbc(2,0,0,0, GridBagConstraints.NORTHWEST);
        tradeprofitstopPanel.add(stopsPanel, gbc);

    }

    private void openOrdersPanel() {

        //panel
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBorder(BorderFactory.createTitledBorder("open orders"));

        //order1
        JLabel order1 = new JLabel("order 1: buy 5,000 (force maker) status: unfilled");
        ordersPanel.add(order1);

        //order2
        JLabel order2 = new JLabel("order 2: sell 10,000 at 6503.5 status: unfilled ");
        ordersPanel.add(order2);

        gbc(0,1,1,1, GridBagConstraints.NORTHWEST);
        add(ordersPanel, gbc);

    }

    private void gbc(int gridx, int gridy, double weightx, double weighty, int anchor) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.anchor = anchor;
    }


}
