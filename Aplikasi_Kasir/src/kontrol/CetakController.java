package kontrol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.Cetak;
import model.CetakTableModel;
import tampilan.FormCetak;
import tampilan.FormPenjualan;
import tampilan.FrameUtama;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class CetakController {
    private final CetakTableModel ctm = new CetakTableModel();
    
    public void tentukanTampilan(FormCetak fct) {
        try {
            fct.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fct.getUI();
            bifUI.setNorthPane(null);
            fct.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada CetakController-tentukanTampilan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fct, "Error pada CetakController-tentukanTampilan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormCetak fct) {
        try {
            FormPenjualan fpj = new FormPenjualan();
            fct.dispose();
            FrameUtama.PanelDasar.remove(fct);
            FrameUtama.PanelUtama.setVisible(true);
            FrameUtama.MenuBar.setVisible(true);
            FrameUtama.PanelTampilan.add(fpj);
            fpj.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada CetakController-tutupAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fct, "Error pada CetakController-tutupAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTableKeranjang(FormCetak fct) {
        try {
            FormCetak.TableKeranjang.setModel(ctm);
        } catch(Exception error) {
            System.err.println("Error pada CetakController-tentukanTableKeranjang, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fct, "Error pada CetakController-tentukanTableKeranjang, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilKeranjang(FormCetak fct) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            String sql_query = "SELECT tab_detail_penjualan.*, tab_produk.nama, tab_produk.harga FROM tab_detail_penjualan INNER JOIN tab_produk ON "
                    + "tab_detail_penjualan.no_produk = tab_produk.no_produk WHERE tab_detail_penjualan.no_penjualan = ? ORDER by no_detail ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, FormCetak.LabelNoReferensi.getText());
            
            rs = ps.executeQuery();
            
            List<Cetak> list = new ArrayList<>();
            
            while(rs.next()) {
                Cetak c = new Cetak();
                c.setNama(rs.getString("nama"));
                c.setHarga(rs.getString("harga"));
                c.setJumlah(rs.getString("jumlah_produk"));
                c.setSubtotal(rs.getString("sub_total"));
                
                list.add(c);
            }
            
            ctm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada CetakController-ambilKeranjang, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fct, "Error pada CetakController-ambilKeranjang, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cetakAction(FormCetak fct) {
        try {
            MessageFormat header = new MessageFormat("Struk Pembelanjaan | Total Harga : " + FormCetak.LabelTotalHarga.getText());
            MessageFormat footer = new MessageFormat("Pembeli : " + FormCetak.LabelNama.getText() + " | "
                    + "Tanggal : " + FormCetak.LabelTanggal.getText() + " | " + "No. Referensi : " + FormCetak.LabelNoReferensi.getText());
            
            try {
                PrintRequestAttributeSet document = new HashPrintRequestAttributeSet();
                document.add(OrientationRequested.PORTRAIT);
                FormCetak.TableKeranjang.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, document, true);
                
                int konfirmasi = JOptionPane.showConfirmDialog(fct, "Struk berhasil dicetak! Kembali ke laman Penjualan?", "Pesan", JOptionPane.YES_NO_OPTION);
                
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    tutupAction(fct);
                }
            } catch(java.awt.print.PrinterException error) {
                System.err.println("Error pada CetakController-cetakAction.print, keterangan : ");
                error.printStackTrace();
                JOptionPane.showMessageDialog(fct, "Error pada CetakController-cetakAction.print, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada CetakController-cetakAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fct, "Error pada CetakController-cetakAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
