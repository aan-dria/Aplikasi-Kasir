package kontrol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.Keranjang;
import model.KeranjangTableModel;
import model.Produk;
import model.ProdukTableModel;
import tampilan.FrameUtama;
import tampilan.FormPenjualan;
import tampilan.FormKeranjang;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class KeranjangController {
    private final KeranjangTableModel ktm = new KeranjangTableModel();
    private final ProdukTableModel ptm = new ProdukTableModel();
    
    private String status;
    private String sql_query;
    private String stok_akhir;
    private String total_harga;
    private int stok_awal;
    private int jumlah_awal;
    private int jumlah_akhir;
    
    public void tentukanTampilan(FormKeranjang fkj) {
        try {
            fkj.setMaximum(true);
            fkj.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fkj.getUI();
            bifUI.setNorthPane(null);
            fkj.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-tentukanTampilan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fkj, "Error pada KeranjangController-tentukanTampilan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormKeranjang kf) {
        try {
            FormPenjualan fpj = new FormPenjualan();
            kf.dispose();
            FrameUtama.PanelTampilan.remove(kf);
            FrameUtama.PanelTampilan.add(fpj);
            fpj.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-tutupAction, detail : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-tutupAction, detail : " + error.toString());
        }
    }
    
    public void tentukanTableKeranjang(FormKeranjang kf) {
        try {
            FormKeranjang.TableKeranjang.setModel(ktm);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-tentukanTableKeranjang, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-tentukanTableKeranjang, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTableProduk(FormKeranjang kf) {
        try {
            FormKeranjang.TableProduk.setModel(ptm);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-tentukanTableProduk, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-tentukanTableProduk, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilKeranjang(FormKeranjang kf) {
        try {
            String no_penjualan = FormKeranjang.FieldPenjualan.getText();
            double total_harga = 0.00;
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT tab_detail_penjualan.*, tab_produk.nama, tab_produk.harga, tab_produk.stok FROM tab_detail_penjualan INNER JOIN tab_produk ON "
                    + "tab_detail_penjualan.no_produk = tab_produk.no_produk WHERE tab_detail_penjualan.no_penjualan = ? ORDER by no_detail ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, no_penjualan);
            
            rs = ps.executeQuery();
            
            List<Keranjang> list = new ArrayList<>();
            
            while(rs.next()) {
                Keranjang k = new Keranjang();
                k.setNodetail(rs.getString("no_detail"));
                k.setNopenjualan(rs.getString("no_penjualan"));
                k.setNoproduk(rs.getString("no_produk"));
                k.setJumlah(rs.getString("jumlah_produk"));
                k.setSubtotal(rs.getString("sub_total"));
                k.setNama(rs.getString("nama"));
                k.setHarga(rs.getString("harga"));
                k.setStok(rs.getString("stok"));
                total_harga += Double.parseDouble(rs.getString("sub_total"));
                
                list.add(k);
            }
            
            ktm.tentukanList(list);
            
            FormKeranjang.FieldTotalHarga.setText("Rp" + total_harga);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-ambilKeranjang, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-ambilKeranjang, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilProduk(FormKeranjang kf) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT * FROM tab_produk ORDER BY no_produk ASC;";
            
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
            
            ptm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-ambilProduk, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-ambilProduk, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cariData(FormKeranjang kf) {
        try {
            String parameter = "";
            String keyword = FormKeranjang.FieldCari.getText();
            String keyword_slashed = keyword.replace("'", "\\'");
            String keyword_query = "%" + keyword_slashed + "%";
            
            PreparedStatement ps;
            ResultSet rs;
            
            if(FormKeranjang.ComboCari.getSelectedIndex() > 0) {
                if(FormKeranjang.ComboCari.getSelectedIndex() == 1) {
                    parameter = "no_detail";
                } else if(FormKeranjang.ComboCari.getSelectedIndex() == 2) {
                    parameter = "nama";
                } else if(FormKeranjang.ComboCari.getSelectedIndex() == 3) {
                    parameter = "harga";
                } else if(FormKeranjang.ComboCari.getSelectedIndex() == 4) {
                    parameter = "jumlah_produk";
                } else if(FormKeranjang.ComboCari.getSelectedIndex() == 5) {
                    parameter = "sub_total";
                }
                
                sql_query = "SELECT tab_detail_penjualan.*, tab_produk.nama, tab_produk.harga, tab_produk.stok FROM tab_detail_penjualan INNER JOIN tab_produk ON "
                        + "tab_detail_penjualan.no_produk = tab_produk.no_produk WHERE " + parameter + " LIKE ? AND no_penjualan = ? ORDER BY no_detail ASC;";
                
                ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                ps.setString(1, keyword_query);
                ps.setString(2, FormKeranjang.FieldPenjualan.getText());
                
                rs = ps.executeQuery();
                
                List<Keranjang> list = new ArrayList<>();
                while(rs.next()) {
                    Keranjang k = new Keranjang();
                    k.setNodetail(rs.getString("no_detail"));
                    k.setNopenjualan(rs.getString("no_penjualan"));
                    k.setNoproduk(rs.getString("no_produk"));
                    k.setJumlah(rs.getString("jumlah_produk"));
                    k.setSubtotal(rs.getString("sub_total"));
                    k.setNama(rs.getString("nama"));
                    k.setHarga(rs.getString("harga"));
                    k.setStok(rs.getString("stok"));
                    
                    list.add(k);
                }
                
                ktm.tentukanList(list);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-cariData, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-cariData, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void segarkan(FormKeranjang kf) {
        try {
            status = "";
            
            sql_query = "";
            
            stok_awal = 0;
            stok_akhir = "";
            jumlah_awal = 0;
            jumlah_akhir = 0;
            
            FormKeranjang.FieldDetail.setText("");
            FormKeranjang.FTFieldSubtotal.setText("");
            FormKeranjang.FTFieldJumlah.setText("");
            FormKeranjang.ComboProduk.setSelectedIndex(0);
            FormKeranjang.ProdukNama.setText("");
            FormKeranjang.ProdukHarga.setText("");
            FormKeranjang.ProdukStok.setText("");
            FormKeranjang.ComboCari.setSelectedIndex(0);
            FormKeranjang.FieldCari.setText("");
            FormKeranjang.TableProduk.clearSelection();
            FormKeranjang.TableKeranjang.clearSelection();
            
            FormKeranjang.ComboProduk.setEnabled(true);
            FormKeranjang.FTFieldJumlah.setEditable(false);
            
            FormKeranjang.ButtonSimpan.setEnabled(false);
            FormKeranjang.ButtonBatal.setEnabled(false);
            
            FormKeranjang.ComboCari.setEnabled(true);
            FormKeranjang.FieldCari.setEnabled(true);
            FormKeranjang.TableKeranjang.setEnabled(true);
            FormKeranjang.TableProduk.setEnabled(true);
            FormKeranjang.ButtonTambah.setEnabled(false);
            FormKeranjang.ButtonUbah.setEnabled(false);
            FormKeranjang.ButtonHapus.setEnabled(true);
            FormKeranjang.ButtonSegarkan.setEnabled(true);
            FormKeranjang.ButtonFinalisasi.setEnabled(true);
            FormKeranjang.ButtonTutup.setEnabled(true);
            
            ambilKeranjang(kf);
            ambilProduk(kf);
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-segarkan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-segarkan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void tableKeranjangAction(final FormKeranjang kf) {
        FormKeranjang.TableKeranjang.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int row = FormKeranjang.TableKeranjang.getSelectedRow();

                    if(row > -1) {
                        Keranjang k = ktm.ambil(row);
                        FormKeranjang.FieldDetail.setText(k.getNodetail());
                        FormKeranjang.FTFieldSubtotal.setText(k.getSubtotal());
                        FormKeranjang.FTFieldJumlah.setText(k.getJumlah());
                        FormKeranjang.ComboProduk.setSelectedItem(k.getNoproduk());
                        FormKeranjang.ButtonUbah.setEnabled(true);
                    }
                } catch(Exception error) {
                    System.err.println("Error pada KeranjangController-tableKeranjangAction, keterangan : ");
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-tableKeranjangAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    public void tableProdukAction(final FormKeranjang kf) {
        FormKeranjang.TableProduk.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = FormKeranjang.TableProduk.getSelectedRow();
                if(row > -1) {
                    Produk p = ptm.ambil(row);
                    
                    FormKeranjang.ComboProduk.setSelectedItem(p.getNo());
                    FormKeranjang.ProdukNama.setText(p.getNama());
                    FormKeranjang.ProdukHarga.setText(p.getHarga());
                    FormKeranjang.ProdukStok.setText(p.getStok());
                    FormKeranjang.ButtonTambah.setEnabled(true);
                }
            }
        });
    }
    
    private String noOtomatis(FormKeranjang kf) {
        String pk = "";
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT no_detail from tab_detail_penjualan ORDER BY no_detail DESC;";
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                String pkOnDB = rs.getString("no_detail");
                
                pk = Integer.toString(Integer.parseInt(pkOnDB) + 1);
            } else {
                pk = "1";
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-noOtomatis, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-noOtomatis, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return pk;
    }
    
    public void tambahAction(FormKeranjang kf) {
        try {
            if(Integer.parseInt(FormKeranjang.ProdukStok.getText()) > 0) {
                status = "INSERT";
                
                stok_awal = Integer.parseInt(FormKeranjang.ProdukStok.getText());
                jumlah_awal = 0;
                
                FormKeranjang.FieldDetail.setText(noOtomatis(kf));
                FormKeranjang.FTFieldSubtotal.setText("");
                FormKeranjang.FTFieldJumlah.setText("");
                FormKeranjang.ComboCari.setSelectedIndex(0);
                FormKeranjang.FieldCari.setText("");
                FormKeranjang.TableProduk.clearSelection();
                FormKeranjang.TableKeranjang.clearSelection();
                
                FormKeranjang.ComboProduk.setEnabled(false);
                FormKeranjang.FTFieldJumlah.setEditable(true);

                FormKeranjang.ButtonSimpan.setEnabled(true);
                FormKeranjang.ButtonBatal.setEnabled(true);

                FormKeranjang.ComboCari.setEnabled(false);
                FormKeranjang.FieldCari.setEnabled(false);
                FormKeranjang.TableKeranjang.setEnabled(false);
                FormKeranjang.TableProduk.setEnabled(false);
                FormKeranjang.ButtonTambah.setEnabled(false);
                FormKeranjang.ButtonUbah.setEnabled(false);
                FormKeranjang.ButtonHapus.setEnabled(false);
                FormKeranjang.ButtonSegarkan.setEnabled(false);
                FormKeranjang.ButtonTutup.setEnabled(false);

                FormKeranjang.FTFieldJumlah.requestFocus();
            } else {
                JOptionPane.showMessageDialog(kf, "Stok produk yang dipilih sudah habis!", "Pesan", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-tambahAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-tambahAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ubahAction(FormKeranjang kf) {
        try {
            int baris = FormKeranjang.TableKeranjang.getSelectedRow();
            
            if (baris > -1) {
                status = "UPDATE";
                
                stok_awal = Integer.parseInt(FormKeranjang.ProdukStok.getText());
                jumlah_awal = Integer.parseInt(FormKeranjang.FTFieldJumlah.getText());
                
                FormKeranjang.ComboProduk.setEnabled(false);
                FormKeranjang.FTFieldJumlah.setEditable(true);
                
                FormKeranjang.ButtonSimpan.setEnabled(true);
                FormKeranjang.ButtonBatal.setEnabled(true);
                
                FormKeranjang.ComboCari.setEnabled(false);
                FormKeranjang.FieldCari.setEnabled(false);
                FormKeranjang.TableProduk.setEnabled(false);
                FormKeranjang.TableKeranjang.setEnabled(false);
                FormKeranjang.ButtonTambah.setEnabled(false);
                FormKeranjang.ButtonUbah.setEnabled(false);
                FormKeranjang.ButtonHapus.setEnabled(false);
                FormKeranjang.ButtonSegarkan.setEnabled(false);
                FormKeranjang.ButtonTutup.setEnabled(false);
                
                FormKeranjang.FTFieldJumlah.requestFocus();
            } else {
                JOptionPane.showMessageDialog(kf, "Silahkan tekan data Keranjang yang ingin diubah!", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-ubahAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-ubahAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusAction(FormKeranjang kf) {
        try {
            int baris = FormKeranjang.TableKeranjang.getSelectedRow();
            
            if(baris > -1) {
                Keranjang k = new Keranjang();
                k.setNodetail(ktm.ambil(baris).getNodetail());
                k.setNoproduk(FormKeranjang.ComboProduk.getSelectedItem().toString());
                k.setNopenjualan(FormKeranjang.FieldPenjualan.getText());
                k.setStok(FormKeranjang.ProdukStok.getText());
                k.setJumlah(FormKeranjang.FTFieldJumlah.getText());
                
                PreparedStatement ps;
                
                int konfirmasi = JOptionPane.showConfirmDialog(kf, "Apakah anda yakin ingin menghapus data dengan Nomor Detail "
                        + "" + k.getNodetail() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    sql_query = "DELETE FROM tab_detail_penjualan WHERE no_detail = ?";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, k.getNodetail());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(kf, "Data berhasil dihapus", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                        
                        sql_query = "UPDATE tab_produk SET stok = ? + ? WHERE no_produk = ?;";
                        
                        ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                        ps.setString(1, k.getStok());
                        ps.setString(2, k.getJumlah());
                        ps.setString(3, k.getNoproduk());
                        
                        int perbarui = ps.executeUpdate();
                        
                        if(perbarui == 1) {
                            sql_query = "UPDATE tab_penjualan SET total_harga = (SELECT COALESCE(SUM(sub_total), 0) FROM tab_detail_penjualan "
                                    + "WHERE tab_detail_penjualan.no_penjualan = ?) WHERE tab_penjualan.no_penjualan = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getNopenjualan());
                            ps.setString(2, k.getNopenjualan());
                            
                            ps.executeUpdate();
                        }
                    } else {
                        JOptionPane.showMessageDialog(kf, "Data gagal dihapus", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                }
                segarkan(kf);
            } else {
                JOptionPane.showMessageDialog(kf, "Silahkan tekan data Keranjang yang ingin dihapus", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-hapusAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-hapusAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private boolean validasiData(FormKeranjang kf) {
        boolean b = false;
        try {
            jumlah_akhir = Integer.parseInt(FormKeranjang.FTFieldJumlah.getText());
            
            if(FormKeranjang.ComboProduk.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(kf, "Pilih salah satu Produk!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(FormKeranjang.FTFieldJumlah.getText().isEmpty() || jumlah_akhir <= 0) {
                JOptionPane.showMessageDialog(kf, "Harap isi jumlah melebihi angka 0!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(stok_awal < jumlah_akhir && status.equals("INSERT")) {
                JOptionPane.showMessageDialog(kf, "Jumlah melebihi stok!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(((stok_awal + jumlah_awal) - jumlah_akhir) < 0 && status.equals("UPDATE")) {
                JOptionPane.showMessageDialog(kf, "Jumlah melebihi stok!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else {
                b = true;
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-validasiData, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "Error pada KeranjangController-validasiData, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public void simpanAction(FormKeranjang kf) {
        try {
            boolean valid = validasiData(kf);
            if (valid == true) {
                Keranjang k = new Keranjang();
                k.setNodetail(FormKeranjang.FieldDetail.getText());
                k.setNopenjualan(FormKeranjang.FieldPenjualan.getText());
                k.setNoproduk(FormKeranjang.ComboProduk.getSelectedItem().toString());
                k.setStok(FormKeranjang.ProdukStok.getText());
                k.setJumlah(FormKeranjang.FTFieldJumlah.getText());
                k.setSubtotal(FormKeranjang.FTFieldSubtotal.getText());
                
                stok_akhir = String.valueOf(jumlah_akhir);
                
                PreparedStatement ps;
                
                if(status.equals("INSERT")) {
                    sql_query = "INSERT INTO tab_detail_penjualan VALUES (?, ?, ?, ?, ?);";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, k.getNodetail());
                    ps.setString(2, k.getNopenjualan());
                    ps.setString(3, k.getNoproduk());
                    ps.setString(4, k.getJumlah());
                    ps.setString(5, k.getSubtotal());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(kf, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                        
                        sql_query = "UPDATE tab_penjualan SET total_harga = (SELECT SUM(sub_total) FROM tab_detail_penjualan "
                                + "WHERE tab_detail_penjualan.no_penjualan = ?) WHERE tab_penjualan.no_penjualan = ?;";
                        
                        ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                        ps.setString(1, k.getNopenjualan());
                        ps.setString(2, k.getNopenjualan());
                        
                        int perbarui = ps.executeUpdate();
                        
                        if(perbarui == 1) {
                            sql_query = "UPDATE tab_produk SET stok = ? - ? WHERE no_produk = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getStok());
                            ps.setString(2, stok_akhir);
                            ps.setString(3, k.getNoproduk());
                            
                            ps.executeUpdate();
                        }
                    } else {
                        JOptionPane.showMessageDialog(kf, "Data gagal disimpan", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(kf);
                } else if(status.equals("UPDATE")) {
                    sql_query = "UPDATE tab_detail_penjualan SET jumlah_produk = ?, sub_total = ? WHERE no_detail = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, k.getJumlah());
                    ps.setString(2, k.getSubtotal());
                    ps.setString(3, k.getNodetail());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(kf, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                        
                        if(jumlah_awal > jumlah_akhir) {
                            stok_akhir = String.valueOf(jumlah_awal - jumlah_akhir);
                            
                            sql_query = "UPDATE tab_produk SET stok = ? + ? WHERE no_produk = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getStok());
                            ps.setString(2, stok_akhir);
                            ps.setString(3, k.getNoproduk());
                        } else if(jumlah_awal < jumlah_akhir){
                            stok_akhir = String.valueOf(jumlah_akhir - jumlah_awal);
                            
                            sql_query = "UPDATE tab_produk SET stok = ? - ? WHERE no_produk = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getStok());
                            ps.setString(2, stok_akhir);
                            ps.setString(3, k.getNoproduk());
                        } else {
                            sql_query = "UPDATE tab_produk SET stok = ? WHERE no_produk = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getStok());
                            ps.setString(2, k.getNoproduk());
                        }
                        
                        int perbarui = ps.executeUpdate();
                        
                        if(perbarui == 1) {
                            sql_query = "UPDATE tab_penjualan SET total_harga = (SELECT COALESCE(SUM(sub_total), 0) FROM tab_detail_penjualan "
                                    + "WHERE tab_detail_penjualan.no_penjualan = ?) WHERE tab_penjualan.no_penjualan = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, k.getNopenjualan());
                            ps.setString(2, k.getNopenjualan());
                            
                            ps.executeUpdate();
                        }
                    } else {
                        JOptionPane.showMessageDialog(kf, "Data gagal diubah", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(kf);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-simpanAction, detail : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "error at KeranjangController-simpanAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void isiComboProduk(FormKeranjang kf) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT no_produk FROM tab_produk ORDER BY no_produk ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            while(rs.next()) {
                FormKeranjang.ComboProduk.addItem(rs.getString("no_produk"));
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-isiComboProduk, detail : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "error at KeranjangController-isiComboProduk, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void comboProdukAction(FormKeranjang kf) {
        try {
            if(FormKeranjang.ComboProduk.getSelectedIndex() > 0) {
                PreparedStatement ps;
                ResultSet rs;

                sql_query = "SELECT nama, harga, stok FROM tab_produk WHERE no_produk = ?;";
                
                ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                ps.setString(1, FormKeranjang.ComboProduk.getSelectedItem().toString());
                
                rs = ps.executeQuery();

                if(rs.next()) {
                    FormKeranjang.ProdukNama.setText(rs.getString("nama"));
                    FormKeranjang.ProdukHarga.setText(rs.getString("harga"));
                    FormKeranjang.ProdukStok.setText(rs.getString("stok"));
                    FormKeranjang.ButtonTambah.setEnabled(true);
                }
            } else {
                FormKeranjang.ProdukNama.setText("");
                FormKeranjang.ProdukHarga.setText("");
                FormKeranjang.ProdukStok.setText("");
                FormKeranjang.ButtonTambah.setEnabled(false);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-comboProdukAction, detail : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(kf, "error at KeranjangController-comboProdukAction, detail : " + error.toString());
        }
    }
    
    public void hitungSubtotal(FormKeranjang fkj) {
        try {
            if(FormKeranjang.FTFieldJumlah.getText().isEmpty()) {
                FormKeranjang.FTFieldSubtotal.setText("");
            } else {
                try {
                    double harga = Double.parseDouble(FormKeranjang.ProdukHarga.getText());
                    int jumlah = Integer.parseInt(FormKeranjang.FTFieldJumlah.getText());
                    String subtotal = Double.toString(harga * jumlah);
                    FormKeranjang.FTFieldSubtotal.setText(subtotal);
                } catch(NumberFormatException e) {
                    FormKeranjang.FTFieldJumlah.setText("");
                    FormKeranjang.FTFieldSubtotal.setText("");
                    JOptionPane.showMessageDialog(fkj, "Harap masukkan angka pada kolom Jumlah!", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-hitungSubtotal, detail : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fkj, "error at KeranjangController-hitungSubtotal, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void finalisasiAction(FormKeranjang fkj) {
        try {
            String pk = FormKeranjang.FieldPenjualan.getText();
            
            int baris = ktm.getRowCount();
            
            if(baris > 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(fkj, "Apakah anda yakin ingin finalisasi data dengan Nomor Penjualan " + pk + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    PreparedStatement ps;

                    String sqlUpdate = "UPDATE tab_penjualan SET tanggal = CURRENT_DATE WHERE no_penjualan = ?;";

                    ps = Koneksi.getKoneksi().prepareStatement(sqlUpdate);
                    ps.setString(1, pk);

                    int sukses = ps.executeUpdate();

                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fkj, "Data berhasil dikonfirmasi", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fkj, "Data gagal dikonfirmasi", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    FormPenjualan fpj = new FormPenjualan();
                    fkj.dispose();
                    FrameUtama.PanelTampilan.remove(fkj);
                    FrameUtama.PanelTampilan.add(fpj);
                    fpj.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(fkj, "Keranjang masih kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada KeranjangController-konfirmasiAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fkj, "Error pada KeranjangController-konfirmasiAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
