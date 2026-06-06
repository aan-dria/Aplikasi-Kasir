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
import tampilan.FrameUtama;
import tampilan.FormPelanggan;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class PelangganController {
    private final PelangganTableModel ptm = new PelangganTableModel();
    
    private String status;
    private String sql_query;
    private String jumlah_pelanggan;
    
    public void tentukanTampilan(FormPelanggan fpl) {
        try {
            fpl.setMaximum(true);
            fpl.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fpl.getUI();
            bifUI.setNorthPane(null);
            fpl.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-tentukanTampilan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-tentukanTampilan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormPelanggan fpl) {
        try {
            fpl.dispose();
            FrameUtama.PanelTampilan.remove(fpl);
            FrameUtama.PanelMenu.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-tutupAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-tutupAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTablePelanggan(FormPelanggan fpl) {
        try {
            FormPelanggan.TablePelanggan.setModel(ptm);
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-tentukanTablePelanggan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-tentukanTablePelanggan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilPelanggan(FormPelanggan fpl) {
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
            
            sql_query = "SELECT COUNT(id_pelanggan) AS pelanggan FROM tab_pelanggan;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                jumlah_pelanggan = rs.getString("pelanggan");
                FrameUtama.LabelPelanggan.setText(jumlah_pelanggan);
            }
            
            ptm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-ambilData, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-ambilData, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cariData(FormPelanggan fpl) {
        try {
            String parameter = "";
            String keyword = FormPelanggan.FieldCari.getText();
            String keyword_slashed = keyword.replace("'", "\\'");
            String keyword_query = "%" + keyword_slashed + "%";
            
            PreparedStatement ps;
            ResultSet rs;
            
            if(FormPelanggan.ComboCari.getSelectedIndex() > 0) {
                if(FormPelanggan.ComboCari.getSelectedIndex() == 1) {
                    parameter = "id_pelanggan";
                } else if(FormPelanggan.ComboCari.getSelectedIndex() == 2) {
                    parameter = "nama";
                } else if(FormPelanggan.ComboCari.getSelectedIndex() == 3) {
                    parameter = "alamat";
                } else if(FormPelanggan.ComboCari.getSelectedIndex() == 4) {
                    parameter = "no_telepon";
                }
                
                sql_query = "SELECT * FROM tab_pelanggan WHERE " + parameter + " LIKE ? ORDER BY id_pelanggan ASC;";
                
                ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                ps.setString(1, keyword_query);
                
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
                
                ptm.tentukanList(list);
            }
            
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-cariData, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-cariData, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void segarkan(FormPelanggan fpl) {
        try {
            status = "";
            
            FormPelanggan.FieldID.setText("");
            FormPelanggan.FieldNama.setText("");
            FormPelanggan.AreaAlamat.setText("");
            FormPelanggan.FieldNoTelepon.setText("");
            FormPelanggan.ComboCari.setSelectedIndex(0);
            FormPelanggan.FieldCari.setText("");
            FormPelanggan.TablePelanggan.clearSelection();
            
            FormPelanggan.FieldNama.setEditable(false);
            FormPelanggan.AreaAlamat.setEditable(false);
            FormPelanggan.FieldNoTelepon.setEditable(false);
            
            FormPelanggan.ButtonSimpan.setEnabled(false);
            FormPelanggan.ButtonBatal.setEnabled(false);
            
            FormPelanggan.ComboCari.setEnabled(true);
            FormPelanggan.FieldCari.setEditable(true);
            FormPelanggan.TablePelanggan.setEnabled(true);
            FormPelanggan.ButtonTambah.setEnabled(true);
            FormPelanggan.ButtonUbah.setEnabled(true);
            FormPelanggan.ButtonHapus.setEnabled(true);
            FormPelanggan.ButtonSegarkan.setEnabled(true);
            FormPelanggan.ButtonTutup.setEnabled(true);
            
            ambilPelanggan(fpl);
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-segarkan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-segarkan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void tablePelangganAction(final FormPelanggan fpl) {
        FormPelanggan.TablePelanggan.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int row = FormPelanggan.TablePelanggan.getSelectedRow();

                    if(row > -1) {
                        Pelanggan p = ptm.ambil(row);
                        FormPelanggan.FieldID.setText(p.getId());
                        FormPelanggan.FieldNama.setText(p.getNama());
                        FormPelanggan.AreaAlamat.setText(p.getAlamat());
                        FormPelanggan.FieldNoTelepon.setText(p.getNotelepon());
                    }
                } catch(Exception error) {
                    System.err.println("Error pada PelangganController-tablePelangganAction, keterangan : ");
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-tablePelangganAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private String idOtomatis(FormPelanggan fpl) {
        String pk = "";
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT id_pelanggan from tab_pelanggan ORDER BY id_pelanggan DESC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                String pkOnDB = rs.getString("id_pelanggan");
                
                pk = Integer.parseInt(pkOnDB) + 1 + "";
            } else {
                pk = "1";
            }
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-idOtomatis, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-idOtomatis, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return pk;
    }
    
    public void tambahAction(FormPelanggan fpl) {
        try {
            status = "INSERT";
            
            FormPelanggan.FieldID.setText(idOtomatis(fpl));
            FormPelanggan.FieldNama.setText("");
            FormPelanggan.AreaAlamat.setText("");
            FormPelanggan.FieldNoTelepon.setText("");
            FormPelanggan.ComboCari.setSelectedIndex(0);
            FormPelanggan.FieldCari.setText("");
            FormPelanggan.TablePelanggan.clearSelection();
            
            FormPelanggan.FieldNama.setEditable(true);
            FormPelanggan.AreaAlamat.setEditable(true);
            FormPelanggan.FieldNoTelepon.setEditable(true);
            
            FormPelanggan.ButtonSimpan.setEnabled(true);
            FormPelanggan.ButtonBatal.setEnabled(true);
            
            FormPelanggan.ComboCari.setEnabled(false);
            FormPelanggan.FieldCari.setEditable(false);
            FormPelanggan.TablePelanggan.setEnabled(false);
            FormPelanggan.ButtonTambah.setEnabled(false);
            FormPelanggan.ButtonUbah.setEnabled(false);
            FormPelanggan.ButtonHapus.setEnabled(false);
            FormPelanggan.ButtonSegarkan.setEnabled(false);
            FormPelanggan.ButtonTutup.setEnabled(false);
            
            FormPelanggan.FieldNama.requestFocus();
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-tambahAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-tambahAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ubahAction(FormPelanggan fpl) {
        try {
            int row = FormPelanggan.TablePelanggan.getSelectedRow();
            
            if(row > -1) {
                status = "UPDATE";
                
                FormPelanggan.FieldNama.setEditable(true);
                FormPelanggan.AreaAlamat.setEditable(true);
                FormPelanggan.FieldNoTelepon.setEditable(true);
                
                FormPelanggan.ButtonSimpan.setEnabled(true);
                FormPelanggan.ButtonBatal.setEnabled(true);
                
                FormPelanggan.ComboCari.setEnabled(false);
                FormPelanggan.FieldCari.setEditable(false);
                FormPelanggan.TablePelanggan.setEnabled(false);
                FormPelanggan.ButtonTambah.setEnabled(false);
                FormPelanggan.ButtonUbah.setEnabled(false);
                FormPelanggan.ButtonHapus.setEnabled(false);
                FormPelanggan.ButtonSegarkan.setEnabled(false);
                FormPelanggan.ButtonTutup.setEnabled(false);
                
                FormPelanggan.FieldNama.requestFocus();
            } else {
                JOptionPane.showMessageDialog(fpl, "Silahkan tekan data Pelanggan yang ingin diubah!", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-ubahAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-ubahAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusAction(FormPelanggan fpl) {
        try {
            int row = FormPelanggan.TablePelanggan.getSelectedRow();
            
            if(row > -1) {
                String pk = ptm.ambil(row).getId();
                
                int konfirmasi = JOptionPane.showConfirmDialog(fpl, "Apakah anda yakin ingin menghapus data dengan ID Pelanggan " + pk + "?",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    PreparedStatement ps;
                    
                    sql_query = "DELETE FROM tab_pelanggan WHERE id_pelanggan = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, pk);
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpl, "Data berhasil dihapus", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpl, "Data gagal dihapus", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                }
                segarkan(fpl);
            } else {
                JOptionPane.showMessageDialog(fpl, "Silahkan tekan data Pelanggan yang ingin dihapus", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-hapusAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-hapusAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private boolean validasiData(FormPelanggan fpl) {
        boolean b = false;
        try {
            String nama_pelanggan = FormPelanggan.FieldNama.getText();
            
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT nama from tab_pelanggan WHERE nama = ?;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, nama_pelanggan);
            
            rs = ps.executeQuery();
            
            if(FormPelanggan.FieldNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpl, "Nama tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(rs.next()) {
                JOptionPane.showMessageDialog(fpl, "Nama sudah terdaftar!" , "Pesan", JOptionPane.WARNING_MESSAGE);
                FormPelanggan.FieldNama.setText("");
            } else if(FormPelanggan.AreaAlamat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpl, "Alamat tidak boleh kosong!" , "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(FormPelanggan.FieldNoTelepon.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpl, "No. Telepon tidak boleh kosong!" , "Pesan", JOptionPane.WARNING_MESSAGE);
            }  else {
                b = true;
            }
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-validasiData, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-validasiData, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public void simpanAction(FormPelanggan fpl) {
        try {
            boolean valid = validasiData(fpl);
            if(valid == true) {   
                Pelanggan p = new Pelanggan();
                p.setId(FormPelanggan.FieldID.getText());
                p.setNama(FormPelanggan.FieldNama.getText());
                p.setAlamat(FormPelanggan.AreaAlamat.getText());
                p.setNotelepon(FormPelanggan.FieldNoTelepon.getText());
                
                PreparedStatement ps;
                
                if(status.equals("INSERT")) {
                    sql_query = "INSERT INTO tab_pelanggan VALUES (?, ?, ?, ?);";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getId());
                    ps.setString(2, p.getNama());
                    ps.setString(3, p.getAlamat());
                    ps.setString(4, p.getNotelepon());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpl, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpl, "Data gagal disimpan", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpl);
                } else if(status.equals("UPDATE")) {
                    sql_query = "UPDATE tab_pelanggan SET nama = ?, alamat = ?, "
                            + "no_telepon = ? WHERE id_pelanggan = ?;";
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getNama());
                    ps.setString(2, p.getAlamat());
                    ps.setString(3, p.getNotelepon());
                    ps.setString(4, p.getId());
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpl, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpl, "Data gagal diubah", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpl);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada PelangganController-simpanAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpl, "Error pada PelangganController-simpanAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
