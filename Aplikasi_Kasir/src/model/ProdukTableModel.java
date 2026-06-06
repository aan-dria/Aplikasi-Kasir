package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class ProdukTableModel extends AbstractTableModel {
    private List<Produk> list = new ArrayList<>();
    
    public void tentukanList(List<Produk> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Produk ambil(int row) {
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return list.get(rowIndex).getNo();
            case 2:
                return list.get(rowIndex).getNama();
            case 3:
                return list.get(rowIndex).getHarga();
            case 4:
                return list.get(rowIndex).getStok();
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
                return "Nomor Produk";
            case 2:
                return "Nama";
            case 3:
                return "Harga";
            case 4:
                return "Stok";
            default:
                return null;
        }
    }
    
}
