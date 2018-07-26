package gui.position;

import data.Order;
import data.Position;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PositionsTableModel extends AbstractTableModel {
    private List openPositions;

    public PositionsTableModel(List openPositions) {
        this.openPositions = openPositions;
    }

    public Class getColumnClass(int columnIndex) { return Position.class; }
    public int getColumnCount() { return 1; }
    public String getColumnName(int columnIndex) { return ""; }
    public int getRowCount() { return (openPositions == null) ? 0 : openPositions.size(); }
    public Object getValueAt(int rowIndex, int columnIndex) { return (openPositions == null) ? null : openPositions.get(rowIndex); }
    public boolean isCellEditable(int columnIndex, int rowIndex) { return false; }
}

