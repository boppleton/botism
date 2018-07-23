package gui.order;

import data.Order;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class OpenOrdersTableModel extends AbstractTableModel {
    private List openOrders;

    public OpenOrdersTableModel(List openOrders) {
        this.openOrders = openOrders;
    }

    public Class getColumnClass(int columnIndex) { return Order.class; }
    public int getColumnCount() { return 1; }
    public String getColumnName(int columnIndex) { return ""; }
    public int getRowCount() { return (openOrders == null) ? 0 : openOrders.size(); }
    public Object getValueAt(int rowIndex, int columnIndex) { return (openOrders == null) ? null : openOrders.get(rowIndex); }
    public boolean isCellEditable(int columnIndex, int rowIndex) { return false; }
}

