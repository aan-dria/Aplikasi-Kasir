package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class PetugasTableModel extends AbstractTableModel {
    private List<Petugas> list = new ArrayList<>();

    public void tentukanList(List<Petugas> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Petugas ambil(int row) {
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return list.get(rowIndex).getId();
            case 2:
                return list.get(rowIndex).getNama();
            case 3:
                return list.get(rowIndex).getSandi();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "No.";
            case 1:
                return "ID Petugas";
            case 2:
                return "Nama";
            case 3:
                return "Sandi";
            default:
                return null;
        }
    }
    
}
