package gui.position;

import data.Order;
import data.Position;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PositionsCell extends AbstractCellEditor implements TableCellRenderer {

    private JPanel panel;
    private JLabel side;
    private JLabel size;
    private JLabel status;
    private JLabel price;
    private JLabel id;


    public PositionsCell() {

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        side = new JLabel();
        panel.add(side);

        size = new JLabel();
        panel.add(size);

        price = new JLabel();
        panel.add(price);





    }


    private void updateData(Position position) {


        size.setText("[" + (position.getAmount()>0?"long ":"short ") + position.getAmount() + "]");

        price.setText(" entry: " + position.getPrice());


//        size.setText(Formatter.kFormat((double) Math.abs(order.getAmt()), 0) + (order.getSide() ? " short liq'd " : " long liq'd "));
//        size.setIcon(getIcon(order));
//        setInstrument(order);
//
//        panel.setBackground(getColor(order.getSide(), order.getAmt()));
//
//        int orderAmt = Math.abs(order.getAmt());
//

    }


//    private void setSlip(LiqOrder order) {
//
//        int slipInt = order.getSlip();
//
//        if (Math.abs(slipInt) >= 1) {
//
//            slip.setText(String.valueOf(Math.abs(slipInt)));
//:sh
//            if (slipInt > 0) {
//                slip.setIcon(new ImageIcon(getClass().getResource("/uparrow.png")));
//            } else {
//                slip.setIcon(new ImageIcon(getClass().getResource("/downarrow.png")));
//            }
//
//        } else {
//            slip.setText("");
//            slip.setIcon(null);
//        }
//    }
//
//    private void setInstrument(LiqOrder order) {
//
//        String in = order.getInstrument();
//
//        instrument.setText(in);
//    }
//
//    private Icon getIcon(LiqOrder order) {
//
//        ImageIcon icon = null;
//
//        switch (order.getExchange()) {
//            case "bitmex":
//                if (order.isActive()) {
//                    icon = new ImageIcon(getClass().getResource("/alert.png"));
//                } else {
//                    icon = new ImageIcon(getClass().getResource("/bitmex22.png"));
//                }
//
//                break;
//            case "bitfinex":
//                icon = new ImageIcon(getClass().getResource("/bitfinex22.png"));
//                break;
//            case "okex":
//                icon = new ImageIcon(getClass().getResource("/okex22.png"));
//                break;
//            case "binance":
//                icon = new ImageIcon(getClass().getResource("/binance.png"));
//                break;
//            case "gdax":
//                icon = new ImageIcon(getClass().getResource("/gdax25.png"));
//                break;
//        }
//
//        return icon;
//    }
//
//    private static Color getColor(boolean side, int amt) {
//
//        int intensity = Math.abs(amt) / 4200;
//
//        if (intensity > 165) {
//            intensity = 165;
//        } else if (intensity < 1) {
//            intensity = 1;
//        }
//
//        if (side) {
//            return new Color(170 - intensity, 255 - intensity / 2, 170 - intensity);
//        } else {
//            return new Color(255 - intensity / 2, 170 - intensity, 170 - intensity);
//        }
//
//    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Position position = (Position) value;
        updateData(position);
        return panel;
    }


}
