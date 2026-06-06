package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class CetakTableModel extends AbstractTableModel {
    private List<Cetak> list = new ArrayList<>();
    
    public void tentukanList(List<Cetak> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Cetak ambil(int row) {
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
                return list.get(rowIndex).getNama();
            case 1:
                return list.get(rowIndex).getHarga();
            case 2:
                return list.get(rowIndex).getJumlah();
            case 3:
                return list.get(rowIndex).getSubtotal();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Nama";
            case 1:
                return "Harga";
            case 2:
                return "Jumlah";
            case 3:
                return "Subtotal";
            default:
                return null;
        }
    }
    
}
