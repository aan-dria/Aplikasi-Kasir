package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class KeranjangTableModel extends AbstractTableModel {
    private List<Keranjang> list = new ArrayList<>();
    
    public void tentukanList(List<Keranjang> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Keranjang ambil(int row) {
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return list.get(rowIndex).getNodetail();
            case 2:
                return list.get(rowIndex).getNama();
            case 3:
                return list.get(rowIndex).getHarga();
            case 4:
                return list.get(rowIndex).getJumlah();
            case 5:
                return list.get(rowIndex).getSubtotal();
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
                return "Nomor Detail";
            case 2:
                return "Nama Produk";
            case 3:
                return "Harga";
            case 4:
                return "Jumlah";
            case 5:
                return "Subtotal";
            default:
                return null;
        }
    }
    
}
