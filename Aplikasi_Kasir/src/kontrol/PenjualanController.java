package kontrol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.Pelanggan;
import model.PelangganTableModel;
import model.Penjualan;
import model.PenjualanTableModel;
import tampilan.FormCetak;
import tampilan.FrameUtama;
import tampilan.FormKeranjang;
import tampilan.FormPenjualan;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class PenjualanController {
    private final PenjualanTableModel pjtm = new PenjualanTableModel();
    private final PelangganTableModel pltm = new PelangganTableModel();
    
    private String status;
    private String sql_query;
    private String no_penjualan;
    private String id_pelanggan;
    private String nama_pelanggan;
    private String tanggal_penjualan;
    private String total_harga_penjualan;
    private String jumlah_keranjang;
    private String keranjang_aktif;
    private String keranjang_terekam;
    
    private final String tanggal_insert = "2024-01-01", total_insert = "0.00";
    
    public void tentukanTampilan(FormPenjualan fpj) {
        try {
            fpj.setMaximum(true);
            fpj.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fpj.getUI();
            bifUI.setNorthPane(null);
            fpj.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-tentukanTampilan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tentukanTampilan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormPenjualan fpj) {
        try {
            fpj.dispose();
            FrameUtama.PanelTampilan.remove(fpj);
            FrameUtama.PanelMenu.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-tutupAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tutupAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void keranjangAction(FormPenjualan fpj) {
        try {
            if(tanggal_penjualan.equals(tanggal_insert)) {
                FormKeranjang fkj = new FormKeranjang();
                FormKeranjang.FieldPenjualan.setText(no_penjualan);
                FrameUtama.PanelTampilan.add(fkj);
                fkj.setVisible(true);
                fpj.dispose();
            } else {
                FormCetak fct = new FormCetak();
                FormCetak.LabelNoReferensi.setText(no_penjualan);
                FormCetak.LabelNama.setText(nama_pelanggan);
                FormCetak.LabelTanggal.setText(tanggal_penjualan);
                FormCetak.LabelTotalHarga.setText("Rp" + total_harga_penjualan);
                FrameUtama.PanelUtama.setVisible(false);
                FrameUtama.MenuBar.setVisible(false);
                FrameUtama.PanelDasar.add(fct);
                fct.setVisible(true);
                fpj.dispose();
            }
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-keranjangAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-keranjangAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTablePenjualan(FormPenjualan fpj) {
        try {
            FormPenjualan.TablePenjualan.setModel(pjtm);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-tentukanTablePenjualan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tentukanTablePenjualan, keterangan : " + error.toString());
        }
    }
    
    public void tentukanTablePelanggan(FormPenjualan fpj) {
        try {
            FormPenjualan.TablePelanggan.setModel(pltm);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-tentukanTablePelanggan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tentukanTablePelanggan, keterangan : " + error.toString());
        }
    }
    
    public void ambilPelanggan(FormPenjualan fpj) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT * FROM tab_pelanggan ORDER by id_pelanggan ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            List<Pelanggan> list = new ArrayList<>();
            
            while(rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setId(rs.getString("id_pelanggan"));
                p.setNama(rs.getString("nama"));
                p.setAlamat(rs.getString("alamat"));
                p.setNotelepon(rs.getString("no_telepon"));
                
                list.add(p);
            }
            
            pltm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-ambilPelanggan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-ambilPelanggan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilPenjualan(FormPenjualan fpj) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT tab_penjualan.*, tab_pelanggan.nama, tab_pelanggan.no_telepon FROM tab_penjualan INNER JOIN tab_pelanggan ON "
                    + "tab_penjualan.id_pelanggan = tab_pelanggan.id_pelanggan ORDER by nama ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            List<Penjualan> list = new ArrayList<>();
            
            while(rs.next()) {
                Penjualan p = new Penjualan();
                p.setNo(rs.getString("no_penjualan"));
                p.setTanggal(rs.getString("tanggal"));
                p.setTotalharga(rs.getString("total_harga"));
                p.setIdpelanggan(rs.getString("id_pelanggan"));
                p.setNama(rs.getString("nama"));
                p.setNotelepon(rs.getString("no_telepon"));
                
                list.add(p);
            }
            
            sql_query = "SELECT a.aktif, b.terekam FROM (SELECT COUNT(no_penjualan) AS aktif FROM tab_penjualan WHERE tanggal = ?) a, "
                    + "(SELECT COUNT(no_penjualan) as terekam FROM tab_penjualan WHERE tanggal != ?) b;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, tanggal_insert);
            ps.setString(2, tanggal_insert);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                keranjang_aktif = rs.getString("aktif");
                keranjang_terekam = rs.getString("terekam");
                FrameUtama.LabelKeranjangAktif.setText(keranjang_aktif);
                FrameUtama.LabelKeranjangTerekam.setText(keranjang_terekam);
            }
            
            pjtm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-ambilData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-ambilData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void comboCariAction(FormPenjualan fpj) {
        try {
            String parameter = "tanggal";
            String keyword_query = "2024-01-01";
            
            PreparedStatement ps;
            ResultSet rs;
            
            if(FormPenjualan.ComboCari.getSelectedIndex() == 0) {
                sql_query = "SELECT tab_penjualan.*, tab_pelanggan.nama, tab_pelanggan.no_telepon FROM tab_penjualan INNER JOIN tab_pelanggan ON "
                    + "tab_penjualan.id_pelanggan = tab_pelanggan.id_pelanggan ORDER BY nama ASC;";
            } else if(FormPenjualan.ComboCari.getSelectedIndex() == 1) {
                sql_query = "SELECT tab_penjualan.*, tab_pelanggan.nama, tab_pelanggan.no_telepon FROM tab_penjualan INNER JOIN tab_pelanggan ON "
                    + "tab_penjualan.id_pelanggan = tab_pelanggan.id_pelanggan WHERE " + parameter + " LIKE '" + keyword_query + "' ORDER BY nama ASC;";
            } else if(FormPenjualan.ComboCari.getSelectedIndex() == 2) {
                sql_query = "SELECT tab_penjualan.*, tab_pelanggan.nama, tab_pelanggan.no_telepon FROM tab_penjualan INNER JOIN tab_pelanggan ON "
                    + "tab_penjualan.id_pelanggan = tab_pelanggan.id_pelanggan WHERE " + parameter + " NOT LIKE '" + keyword_query + "' ORDER BY nama ASC;";
            }

            ps = Koneksi.getKoneksi().prepareStatement(sql_query);

            rs = ps.executeQuery();

            List<Penjualan> list = new ArrayList<>();

            while(rs.next()) {
                Penjualan p = new Penjualan();
                p.setNo(rs.getString("no_penjualan"));
                p.setTanggal(rs.getString("tanggal"));
                p.setTotalharga(rs.getString("total_harga"));
                p.setIdpelanggan(rs.getString("id_pelanggan"));
                p.setNama(rs.getString("nama"));
                p.setNotelepon(rs.getString("no_telepon"));

                list.add(p);
            }
            FormPenjualan.FieldKeranjang.setText("");

            pjtm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-cariData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-cariData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void segarkan(FormPenjualan fpj) {
        try {
            status = "";
            no_penjualan = "";
            id_pelanggan = "";
            tanggal_penjualan = "";
            jumlah_keranjang = "";
            
            FormPenjualan.ComboCari.setSelectedIndex(0);
            FormPenjualan.TablePelanggan.clearSelection();
            FormPenjualan.TablePenjualan.clearSelection();
            
            FormPenjualan.ButtonSimpan.setEnabled(false);
            FormPenjualan.ButtonBatal.setEnabled(false);
            
            FormPenjualan.ComboCari.setEnabled(true);
            FormPenjualan.TablePelanggan.setEnabled(false);
            FormPenjualan.TablePenjualan.setEnabled(true);
            FormPenjualan.ButtonTambah.setEnabled(true);
            FormPenjualan.ButtonKeranjang.setEnabled(false);
            FormPenjualan.ButtonHapus.setEnabled(true);
            FormPenjualan.ButtonSegarkan.setEnabled(true);
            FormPenjualan.ButtonTutup.setEnabled(true);
            
            ambilPelanggan(fpj);
            ambilPenjualan(fpj);
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-segarkan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-segarkan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void tablePelangganAction(final FormPenjualan fpj) {
        FormPenjualan.TablePelanggan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int baris = FormPenjualan.TablePelanggan.getSelectedRow();
                    if(baris > -1) {
                        Pelanggan p = pltm.ambil(baris);

                        id_pelanggan = p.getId();
                    }
                } catch(Exception error) {
                    System.err.println("Error pada PenjualanController-tablePenjualanAction, keterangan : " + error.toString());
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tablePenjualanAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    public void tablePenjualanAction(final FormPenjualan fpj) {
        FormPenjualan.TablePenjualan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int baris = FormPenjualan.TablePenjualan.getSelectedRow();
                    if(baris > -1) {
                        Penjualan p = pjtm.ambil(baris);

                        no_penjualan = p.getNo();
                        tanggal_penjualan = p.getTanggal();
                        nama_pelanggan = p.getNama();
                        total_harga_penjualan = p.getTotalharga();
                        
                        FormPenjualan.ButtonKeranjang.setEnabled(true);

                        try {
                            PreparedStatement ps;
                            ResultSet rs;

                            sql_query = "SELECT COUNT(no_detail) AS pesanan FROM tab_detail_penjualan WHERE no_penjualan = ?;";
                            
                            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                            ps.setString(1, no_penjualan);
                            
                            rs = ps.executeQuery();

                            if(rs.next()) {
                                jumlah_keranjang = rs.getString("pesanan");
                                FormPenjualan.FieldKeranjang.setText(jumlah_keranjang);
                            }
                        } catch(Exception error) {
                            System.err.println("Error pada PenjualanController-tablePenjualanAction.jumlah_penjualan, keterangan : " + error.toString());
                            error.printStackTrace();
                            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tablePenjualanAction.jumlah_penjualan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch(Exception error) {
                    System.err.println("Error pada PenjualanController-tablePenjualanAction, keterangan : " + error.toString());
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tablePenjualanAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private String noOtomatis(FormPenjualan fpj) {
        String pk = "";
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT no_penjualan from tab_penjualan ORDER BY no_penjualan DESC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                String pkOnDB = rs.getString("no_penjualan");
                
                pk = Integer.parseInt(pkOnDB) + 1 + "";
            } else {
                pk = "20240001";
            }
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-noOtomatis, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-noOtomatis, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return pk;
    }
    
    public void tambahAction(FormPenjualan fpj) {
        try {
            status = "INSERT";
            no_penjualan = noOtomatis(fpj);
            
            FormPenjualan.ComboCari.setSelectedIndex(0);
            FormPenjualan.TablePelanggan.clearSelection();
            FormPenjualan.TablePenjualan.clearSelection();
            
            FormPenjualan.ButtonSimpan.setEnabled(true);
            FormPenjualan.ButtonBatal.setEnabled(true);
            
            FormPenjualan.ComboCari.setEnabled(false);
            FormPenjualan.TablePelanggan.setEnabled(true);
            FormPenjualan.TablePenjualan.setEnabled(false);
            FormPenjualan.ButtonTambah.setEnabled(false);
            FormPenjualan.ButtonKeranjang.setEnabled(false);
            FormPenjualan.ButtonHapus.setEnabled(false);
            FormPenjualan.ButtonSegarkan.setEnabled(false);
            FormPenjualan.ButtonTutup.setEnabled(false);
            
            FormPenjualan.TablePelanggan.requestFocus();
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-tambahAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-tambahAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusAction(FormPenjualan fpj) {
        try {
            int baris = FormPenjualan.TablePenjualan.getSelectedRow();
            
            if(baris > -1) {
                String pk = pjtm.ambil(baris).getNo();
                
                int konfirmasi = JOptionPane.showConfirmDialog(fpj, "Apakah anda yakin ingin menghapus data dengan Nomor Penjualan " + pk + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    PreparedStatement ps;
                    
                    sql_query = "DELETE FROM tab_penjualan WHERE no_penjualan = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    
                    ps.setString(1, pk);
                    
                    int isSuccess = ps.executeUpdate();
                    
                    if(isSuccess != 1) {
                        JOptionPane.showMessageDialog(fpj, "Data gagal dihapus", "Pesan", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpj, "Data berhasil dihapus", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                segarkan(fpj);
            } else {
                JOptionPane.showMessageDialog(fpj, "Silahkan tekan data Penjualan yang ingin dihapus", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-hapusAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-hapusAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private boolean validasiData(FormPenjualan fpj) {
        boolean b = false;
        try {
            int baris = FormPenjualan.TablePelanggan.getSelectedRow();
            
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT no_penjualan from tab_penjualan WHERE id_pelanggan = ? AND tanggal = ?;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, id_pelanggan);
            ps.setString(2, tanggal_insert);
            
            rs = ps.executeQuery();
            
            if(baris <= -1) {
                JOptionPane.showMessageDialog(fpj, "Harap pilih pelanggan!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(rs.next()) {
                JOptionPane.showMessageDialog(fpj, "Pelanggan masih memiliki keranjang aktif!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else {
                b = true;
            }
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-validasiData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-validaasiData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public void simpanAction(FormPenjualan fpj) {
        try {
            boolean valid = validasiData(fpj);
            if(valid == true) {   
                Penjualan p = new Penjualan();
                p.setNo(no_penjualan);
                p.setTanggal(tanggal_insert);
                p.setTotalharga(total_insert);
                p.setIdpelanggan(id_pelanggan);
                
                PreparedStatement ps;
                
                if(status.equals("INSERT")) {
                    sql_query = "INSERT INTO tab_penjualan VALUES (?, ?, ?, ?);";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getNo());
                    ps.setString(2, p.getTanggal());
                    ps.setString(3, p.getTotalharga());
                    ps.setString(4, p.getIdpelanggan());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpj, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpj, "Data gagal disimpan", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpj);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada PenjualanController-simpanAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpj, "Error pada PenjualanController-simpanAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
