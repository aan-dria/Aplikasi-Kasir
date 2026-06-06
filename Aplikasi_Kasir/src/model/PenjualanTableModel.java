package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class PenjualanTableModel extends AbstractTableModel {
    private List<Penjualan> list = new ArrayList<>();
    
    public void tentukanList(List<Penjualan> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Penjualan ambil(int row) {
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return list.get(rowIndex).getNo();
            case 2:
                return list.get(rowIndex).getIdpelanggan();
            case 3:
                return list.get(rowIndex).getNama();
            case 4:
                return list.get(rowIndex).getNotelepon();
            case 5:
                return list.get(rowIndex).getTanggal();
            case 6:
                return list.get(rowIndex).getTotalharga();
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
                return "Nomor Penjualan";
            case 2:
                return "ID Pelanggan";
            case 3:
                return "Nama Pelanggan";
            case 4:
                return "No. Telepon";
            case 5:
                return "Tanggal";
            case 6:
                return "Total Harga";
            default:
                return null;
        }
    }
    
}
