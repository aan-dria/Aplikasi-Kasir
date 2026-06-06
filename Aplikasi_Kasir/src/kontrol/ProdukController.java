package kontrol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.Produk;
import model.ProdukTableModel;
import tampilan.FrameUtama;
import tampilan.FormProduk;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class ProdukController {
    private final ProdukTableModel ptm = new ProdukTableModel();
    
    private String status;
    private String sql_query;
    private String jumlah_produk;
    
    public void tentukanTampilan(FormProduk fpr) {
        try {
            fpr.setMaximum(true);
            fpr.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fpr.getUI();
            bifUI.setNorthPane(null);
            fpr.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-tentukanTampilan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-tentukanTampilan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormProduk fpr) {
        try {
            fpr.dispose();
            FrameUtama.PanelTampilan.remove(fpr);
            FrameUtama.PanelMenu.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-tutupAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-tutupAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTableProduk(FormProduk fpr) {
        try {
            FormProduk.TableProduk.setModel(ptm);
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-tentukanTableProduk, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-tentukanTableProduk, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilProduk(FormProduk fpr) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT * FROM tab_produk ORDER by no_produk ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            List<Produk> list = new ArrayList<>();
            
            while(rs.next()) {
                Produk p = new Produk();
                p.setNo(rs.getString("no_produk"));
                p.setNama(rs.getString("nama"));
                p.setHarga(rs.getString("harga"));
                p.setStok(rs.getString("stok"));
                
                list.add(p);
            }
            
            sql_query = "SELECT COUNT(no_produk) AS produk FROM tab_produk;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                jumlah_produk = rs.getString("produk");
                FrameUtama.LabelProduk.setText(jumlah_produk);
            }
            
            ptm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-ambilProduk, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-ambilProduk, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cariData(FormProduk fpr) {
        try {
            String parameter = "";
            String keyword = FormProduk.FieldCari.getText();
            String keyword_slashed = keyword.replace("'", "\\'");
            String keyword_query = "%" + keyword_slashed + "%";
            
            PreparedStatement ps;
            ResultSet rs;
            
            if(FormProduk.ComboCari.getSelectedIndex() > 0) {
                if(FormProduk.ComboCari.getSelectedIndex() == 1) {
                    parameter = "no_produk";
                } else if(FormProduk.ComboCari.getSelectedIndex() == 2) {
                    parameter = "nama";
                } else if(FormProduk.ComboCari.getSelectedIndex() == 3) {
                    parameter = "harga";
                } else if(FormProduk.ComboCari.getSelectedIndex() == 4) {
                    parameter = "stok";
                }
                
                sql_query = "SELECT * FROM tab_produk WHERE " + parameter + " LIKE ? ORDER BY no_produk ASC;";
                
                ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                ps.setString(1, keyword_query);
                
                rs = ps.executeQuery();
                
                List<Produk> list = new ArrayList<>();
                
                while(rs.next()) {
                    Produk p = new Produk();
                    p.setNo(rs.getString("no_produk"));
                    p.setNama(rs.getString("nama"));
                    p.setHarga(rs.getString("harga"));
                    p.setStok(rs.getString("stok"));
                    list.add(p);
                }
                
                ptm.tentukanList(list);
            }
            
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-cariData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-cariData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void segarkan(FormProduk fpr) {
        try {
            status = "";
            
            FormProduk.FieldNo.setText("");
            FormProduk.FieldNama.setText("");
            FormProduk.FTFieldHarga.setText("");
            FormProduk.FTFieldStok.setText("");
            FormProduk.ComboCari.setSelectedIndex(0);
            FormProduk.FieldCari.setText("");
            FormProduk.TableProduk.clearSelection();
            
            FormProduk.FieldNama.setEditable(false);
            FormProduk.FTFieldHarga.setEditable(false);
            FormProduk.FTFieldStok.setEditable(false);
            
            FormProduk.ButtonSimpan.setEnabled(false);
            FormProduk.ButtonBatal.setEnabled(false);
            
            FormProduk.ComboCari.setEnabled(true);
            FormProduk.FieldCari.setEditable(true);
            FormProduk.TableProduk.setEnabled(true);
            FormProduk.ButtonTambah.setEnabled(true);
            FormProduk.ButtonUbah.setEnabled(true);
            FormProduk.ButtonHapus.setEnabled(true);
            FormProduk.ButtonSegarkan.setEnabled(true);
            FormProduk.ButtonTutup.setEnabled(true);
            
            ambilProduk(fpr);
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-segarkan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-segarkan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void tableProdukAction(final FormProduk fpr) {
        FormProduk.TableProduk.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int row = FormProduk.TableProduk.getSelectedRow();
                    
                    if(row != -1) {
                        Produk p = ptm.ambil(row);
                        FormProduk.FieldNo.setText(p.getNo());
                        FormProduk.FieldNama.setText(p.getNama());
                        FormProduk.FTFieldHarga.setText(p.getHarga());
                        FormProduk.FTFieldStok.setText(p.getStok());
                    }
                } catch(Exception error) {
                    System.err.println("Error pada ProdukController-tableProdukAction, keterangan : " + error.toString());
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-tableProdukAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private String noOtomatis(FormProduk fpr) {
        String pk = "";
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT no_produk from tab_produk ORDER BY no_produk DESC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                String pkOnDB = rs.getString("no_produk");
                
                pk = Integer.parseInt(pkOnDB) + 1 + "";
            } else {
                pk = "1";
            }
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-noOtomatis, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-noOtomatis, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return pk;
    }
    
    public void tambahAction(FormProduk fpr) {
        try {
            status = "INSERT";
            
            FormProduk.FieldNo.setText(noOtomatis(fpr));
            FormProduk.FieldNama.setText("");
            FormProduk.FTFieldHarga.setText("");
            FormProduk.FTFieldStok.setText("");
            FormProduk.ComboCari.setSelectedIndex(0);
            FormProduk.FieldCari.setText("");
            FormProduk.TableProduk.clearSelection();
            
            FormProduk.FieldNama.setEditable(true);
            FormProduk.FTFieldHarga.setEditable(true);
            FormProduk.FTFieldStok.setEditable(true);
            
            FormProduk.ButtonSimpan.setEnabled(true);
            FormProduk.ButtonBatal.setEnabled(true);
            
            FormProduk.ComboCari.setEnabled(false);
            FormProduk.FieldCari.setEditable(false);
            FormProduk.TableProduk.setEnabled(false);
            FormProduk.ButtonTambah.setEnabled(false);
            FormProduk.ButtonUbah.setEnabled(false);
            FormProduk.ButtonHapus.setEnabled(false);
            FormProduk.ButtonSegarkan.setEnabled(false);
            FormProduk.ButtonTutup.setEnabled(false);
            
            FormProduk.FieldNama.requestFocus();
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-tambahAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-tambahAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ubahAction(FormProduk fpr) {
        try {
            int row = FormProduk.TableProduk.getSelectedRow();
            
            if(row > -1) {
                status = "UPDATE";
                
                FormProduk.FieldNama.setEditable(true);
                FormProduk.FTFieldHarga.setEditable(true);
                FormProduk.FTFieldStok.setEditable(true);
                
                FormProduk.ButtonSimpan.setEnabled(true);
                FormProduk.ButtonBatal.setEnabled(true);
                
                FormProduk.ComboCari.setEnabled(false);
                FormProduk.FieldCari.setEditable(false);
                FormProduk.TableProduk.setEnabled(false);
                FormProduk.ButtonTambah.setEnabled(false);
                FormProduk.ButtonUbah.setEnabled(false);
                FormProduk.ButtonHapus.setEnabled(false);
                FormProduk.ButtonSegarkan.setEnabled(false);
                FormProduk.ButtonTutup.setEnabled(false);
                
                FormProduk.FieldNama.requestFocus();
            } else {
                JOptionPane.showMessageDialog(fpr, "Silahkan tekan data Produk yang ingin diubah!", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-ubahAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-ubahAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusAction(FormProduk fpr) {
        try {
            int row = FormProduk.TableProduk.getSelectedRow();
            
            if(row > -1) {
                String pk = ptm.ambil(row).getNo();
                
                int konfirmasi = JOptionPane.showConfirmDialog(fpr, "Apakah anda yakin ingin menghapus data dengan Nomor Produk " + pk + "?",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    PreparedStatement ps;
                    
                    sql_query = "DELETE FROM tab_produk WHERE no_produk = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    
                    ps.setString(1, pk);
                    
                    int isSuccess = ps.executeUpdate();
                    
                    if(isSuccess == 0) {
                        JOptionPane.showMessageDialog(fpr, "Data gagal dihapus", "Pesan", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpr, "Data berhasil dihapus", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                segarkan(fpr);
            } else {
                JOptionPane.showMessageDialog(fpr, "Silahkan tekan data Produk yang ingin dihapus", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-hapusAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-hapusAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private boolean validasiData(FormProduk fpr) {
        boolean b = false;
        try {
            Double harga = Double.parseDouble(FormProduk.FTFieldHarga.getText());
            int stok = Integer.parseInt(FormProduk.FTFieldStok.getText());
            
            if(FormProduk.FieldNama.getText().equals("")) {
                JOptionPane.showMessageDialog(fpr, "Nama tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(FormProduk.FTFieldHarga.getText().equals("")) {
                JOptionPane.showMessageDialog(fpr, "Harga tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(FormProduk.FTFieldStok.getText().equals("")) {
                JOptionPane.showMessageDialog(fpr, "Stok tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(harga < 0) {
                JOptionPane.showMessageDialog(fpr, "Harga tidak boleh kurang dari 0.00!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(stok < 0) {
                JOptionPane.showMessageDialog(fpr, "Stok tidak boleh kurang dari 0!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else {
                b = true;
            }
        } catch(Exception error) {
            System.err.println("Error pada ProdukController-validasiData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpr, "Error pada ProdukController-validasiData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public void simpanAction(FormProduk fpr) {
        try {
            boolean valid = validasiData(fpr);
            if(valid == true) {   
                Produk p = new Produk();
                p.setNo(FormProduk.FieldNo.getText());
                p.setNama(FormProduk.FieldNama.getText());
                p.setHarga(FormProduk.FTFieldHarga.getText());
                p.setStok(FormProduk.FTFieldStok.getText());
                
                PreparedStatement ps;
                
                if(status.equals("INSERT")) {
                    sql_query = "INSERT INTO tab_produk VALUES (?, ?, ?, ?);";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getNo());
                    ps.setString(2, p.getNama());
                    ps.setString(3, p.getHarga());
                    ps.setString(4, p.getStok());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpr, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpr, "Data gagal disimpan", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpr);
                } else if(status.equals("UPDATE")) {
                    sql_query = "UPDATE tab_produk SET nama = ?, harga = ?, stok = ? WHERE no_produk = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getNama());
                    ps.setString(2, p.getHarga());
                    ps.setString(3, p.getStok());
                    ps.setString(4, p.getNo());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpr, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpr, "Data gagal diubah", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpr);
                }
            }
        } catch(Exception error) {
            System.err.println("Error at ProdukController-simpanAction, detail : " + error.toString());
            JOptionPane.showMessageDialog(fpr, "error at ProdukController-simpanAction, detail : " + error.toString());
        }
    }
    
}
