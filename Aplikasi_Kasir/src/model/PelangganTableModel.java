package model;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author AiroDRIA
 */
public class PelangganTableModel extends AbstractTableModel {
    private List<Pelanggan> list = new ArrayList<>();
    
    public void tentukanList(List<Pelanggan> list) {
        this.list = list;
        fireTableDataChanged();
    }
    
    public Pelanggan ambil(int row) {
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
                return list.get(rowIndex).getId();
            case 2:
                return list.get(rowIndex).getNama();
            case 3:
                return list.get(rowIndex).getAlamat();
            case 4:
                return list.get(rowIndex).getNotelepon();
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
                return "ID Pelanggan";
            case 2:
                return "Nama";
            case 3:
                return "Alamat";
            case 4:
                return "Nomor Telepon";
            default:
                return null;
        }
    }
    
}
