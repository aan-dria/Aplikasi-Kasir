package kontrol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import model.Petugas;
import model.PetugasTableModel;
import tampilan.FrameUtama;
import tampilan.FormPetugas;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class PetugasController {
    private final PetugasTableModel pgtm = new PetugasTableModel();
    
    private String status;
    private String sql_query;
    private final String akses_petugas = "1";
    
    public void tentukanTampilan(FormPetugas fpg) {
        try {
            fpg.setMaximum(true);
            fpg.putClientProperty("JInternalFrame.IsPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fpg.getUI();
            bifUI.setNorthPane(null);
            fpg.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-tentukanTampilan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-tentukanTampilan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupAction(FormPetugas fpg) {
        try {
            fpg.dispose();
            FrameUtama.PanelTampilan.remove(fpg);
            FrameUtama.PanelMenu.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-tutupAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-tutupAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentukanTablePetugas(FormPetugas fpg) {
        try {
            FormPetugas.TablePetugas.setModel(pgtm);
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-setTablePetugas, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-setTablePetugas, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilPetugas(FormPetugas fpg) {
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT * FROM tab_user WHERE hak_akses = ? ORDER by id ASC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, akses_petugas);
            
            rs = ps.executeQuery();
            
            List<Petugas> list = new ArrayList<>();
            
            while(rs.next()) {
                Petugas p = new Petugas();
                p.setId(rs.getString("id"));
                p.setNama(rs.getString("nama"));
                p.setSandi(rs.getString("sandi"));
                
                list.add(p);
            }
            
            pgtm.tentukanList(list);
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-ambilData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-ambilData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cariData(FormPetugas fpg) {
        try {
            String parameter = "";
            String keyword = FormPetugas.FieldCari.getText();
            String keyword_slashed = keyword.replace("'", "\\'");
            String keyword_query = "%" + keyword_slashed + "%";
            
            PreparedStatement ps;
            ResultSet rs;
            
            if(FormPetugas.ComboCari.getSelectedIndex() > 0) {
                if(FormPetugas.ComboCari.getSelectedIndex() == 1) {
                    parameter = "id";
                } else if(FormPetugas.ComboCari.getSelectedIndex() == 2) {
                    parameter = "nama";
                }
                
                sql_query = "SELECT * FROM tab_user WHERE " + parameter + " LIKE ? AND hak_akses = ? ORDER BY id ASC;";
                
                ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                ps.setString(1, keyword_query);
                ps.setString(2, akses_petugas);
                
                rs = ps.executeQuery();
                
                List<Petugas> list = new ArrayList<>();
                
                while (rs.next()) {
                    Petugas p = new Petugas();
                    p.setId(rs.getString("id"));
                    p.setNama(rs.getString("nama"));
                    p.setSandi(rs.getString("sandi"));
                    
                    list.add(p);
                }
                
                pgtm.tentukanList(list);
            }
            
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-cariData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-cariData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void segarkan(FormPetugas fpg) {
        try {
            status = "";
            
            FormPetugas.FieldID.setText("");
            FormPetugas.FieldNama.setText("");
            FormPetugas.PFieldSandi.setText("");
            FormPetugas.PFieldKonfirmasi.setText("");
            FormPetugas.ComboCari.setSelectedIndex(0);
            FormPetugas.FieldCari.setText("");
            FormPetugas.TablePetugas.clearSelection();
            
            FormPetugas.FieldNama.setEditable(false);
            FormPetugas.PFieldSandi.setEditable(false);
            FormPetugas.PFieldKonfirmasi.setEditable(false);
            
            FormPetugas.ButtonSimpan.setEnabled(false);
            FormPetugas.ButtonBatal.setEnabled(false);
            
            FormPetugas.ComboCari.setEnabled(true);
            FormPetugas.FieldCari.setEditable(true);
            FormPetugas.TablePetugas.setEnabled(true);
            FormPetugas.ButtonTambah.setEnabled(true);
            FormPetugas.ButtonUbah.setEnabled(true);
            FormPetugas.ButtonHapus.setEnabled(true);
            FormPetugas.ButtonSegarkan.setEnabled(true);
            FormPetugas.ButtonTutup.setEnabled(true);
            
            ambilPetugas(fpg);
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-segarkan, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-segarkan, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void tablePetugasAction(final FormPetugas fpg) {
        FormPetugas.TablePetugas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int row = FormPetugas.TablePetugas.getSelectedRow();

                    if(row > -1) {
                        Petugas p = pgtm.ambil(row);
                        FormPetugas.FieldID.setText(p.getId());
                        FormPetugas.FieldNama.setText(p.getNama());
                        FormPetugas.PFieldSandi.setText(p.getSandi());
                    }
                } catch(Exception error) {
                    System.err.println("Error pada PetugasController-tablePetugasAction, keterangan : " + error.toString());
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-tablePetugasAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private String idOtomatis(FormPetugas fpg) {
        String pk = "";
        try {
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT id from tab_user ORDER BY id DESC;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                String pkOnDB = rs.getString("id");
                
                pk = Integer.parseInt(pkOnDB) + 1 + "";
            } else {
                pk = "1";
            }
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-idOtomatis, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-idOtomatis, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return pk;
    }
    
    public void tambahAction(FormPetugas fpg) {
        try {
            status = "INSERT";
            
            FormPetugas.FieldID.setText(idOtomatis(fpg));
            FormPetugas.FieldNama.setText("");
            FormPetugas.PFieldSandi.setText("");
            FormPetugas.PFieldKonfirmasi.setText("");
            FormPetugas.ComboCari.setSelectedIndex(0);
            FormPetugas.FieldCari.setText("");
            FormPetugas.TablePetugas.clearSelection();
            
            FormPetugas.FieldNama.setEditable(true);
            FormPetugas.PFieldSandi.setEditable(true);
            FormPetugas.PFieldKonfirmasi.setEditable(true);
            
            FormPetugas.ButtonSimpan.setEnabled(true);
            FormPetugas.ButtonBatal.setEnabled(true);
            
            FormPetugas.ComboCari.setEnabled(false);
            FormPetugas.FieldCari.setEditable(false);
            FormPetugas.TablePetugas.setEnabled(false);
            FormPetugas.ButtonTambah.setEnabled(false);
            FormPetugas.ButtonUbah.setEnabled(false);
            FormPetugas.ButtonHapus.setEnabled(false);
            FormPetugas.ButtonSegarkan.setEnabled(false);
            FormPetugas.ButtonTutup.setEnabled(false);
            
            FormPetugas.FieldNama.requestFocus();
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-tambahAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-tambahAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ubahAction(FormPetugas fpg) {
        try {
            int row = FormPetugas.TablePetugas.getSelectedRow();
            
            if(row > -1) {
                status = "UPDATE";
                
                FormPetugas.FieldNama.setEditable(true);
                FormPetugas.PFieldSandi.setEditable(true);
                FormPetugas.PFieldKonfirmasi.setEditable(true);
                
                FormPetugas.ButtonSimpan.setEnabled(true);
                FormPetugas.ButtonBatal.setEnabled(true);
                
                FormPetugas.ComboCari.setEnabled(false);
                FormPetugas.FieldCari.setEditable(false);
                FormPetugas.TablePetugas.setEnabled(false);
                FormPetugas.ButtonTambah.setEnabled(false);
                FormPetugas.ButtonUbah.setEnabled(false);
                FormPetugas.ButtonHapus.setEnabled(false);
                FormPetugas.ButtonSegarkan.setEnabled(false);
                FormPetugas.ButtonTutup.setEnabled(false);
                
                FormPetugas.FieldNama.requestFocus();
            } else {
                JOptionPane.showMessageDialog(fpg, "Silahkan tekan data Petugas yang ingin diubah!", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-ubahAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-ubahAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusAction(FormPetugas fpg) {
        try {
            int row = FormPetugas.TablePetugas.getSelectedRow();
            
            if(row > -1) {
                String pk = pgtm.ambil(row).getId();
                
                int konfirmasi = JOptionPane.showConfirmDialog(fpg, "Apakah anda yakin ingin menghapus data dengan ID Petugas " + pk + "?",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    PreparedStatement ps;
                    
                    sql_query = "DELETE FROM tab_user WHERE id = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, pk);
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpg, "Data berhasil dihapus", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpg, "Data gagal dihapus", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                }
                segarkan(fpg);
            } else {
                JOptionPane.showMessageDialog(fpg, "Silahkan tekan data Petugas yang ingin dihapus", "Pesan", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-hapusAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-hapusAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private boolean validasiData(FormPetugas fpg) {
        boolean b = false;
        try {
            String nama_pelanggan = FormPetugas.FieldNama.getText();
            
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT nama from tab_user WHERE nama = ?;";
            
            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, nama_pelanggan);
            
            rs = ps.executeQuery();
            
            if(FormPetugas.FieldNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpg, "Nama tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(rs.next() && status.equals("INSERT")) {
                JOptionPane.showMessageDialog(fpg, "Nama sudah terdaftar!", "Pesan", JOptionPane.WARNING_MESSAGE);
                FormPetugas.FieldNama.setText("");
            } else if(FormPetugas.PFieldSandi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpg, "Sandi tidak boleh kosong!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(FormPetugas.PFieldKonfirmasi.getText().isEmpty()) {
                JOptionPane.showMessageDialog(fpg, "Harap Konfirmasi Sandi!", "Pesan", JOptionPane.WARNING_MESSAGE);
            } else if(!FormPetugas.PFieldSandi.getText().equals(FormPetugas.PFieldKonfirmasi.getText())) {
                JOptionPane.showMessageDialog(fpg, "Konfirmasi Sandi tidak cocok!", "Pesan", JOptionPane.WARNING_MESSAGE);
                FormPetugas.PFieldKonfirmasi.setText("");
            } else {
                b = true;
            }
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-validasiData, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-validasiData, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public void simpanAction(FormPetugas fpg) {
        try {
            boolean valid = validasiData(fpg);
            if(valid == true) {   
                Petugas p = new Petugas();
                p.setId(FormPetugas.FieldID.getText());
                p.setNama(FormPetugas.FieldNama.getText());
                p.setSandi(FormPetugas.PFieldSandi.getText());
                
                PreparedStatement ps;
                
                if(status.equals("INSERT")) {
                    sql_query = "INSERT INTO tab_user VALUES (?, ?, ?, ?);";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getId());
                    ps.setString(2, p.getNama());
                    ps.setString(3, p.getSandi());
                    ps.setString(4, akses_petugas);
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpg, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpg, "Data gagal disimpan", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpg);
                } else if(status.equals("UPDATE")) {
                    sql_query = "UPDATE tab_user SET nama = ?, sandi = ? WHERE id = ? AND hak_akses = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, p.getNama());
                    ps.setString(2, p.getSandi());
                    ps.setString(3, p.getId());
                    ps.setString(4, akses_petugas);
                    
                    int sukses = ps.executeUpdate();
                    
                    if(sukses == 1) {
                        JOptionPane.showMessageDialog(fpg, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(fpg, "Data gagal diubah", "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                    segarkan(fpg);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada PetugasController-simpanAction, keterangan : " + error.toString());
            error.printStackTrace();
            JOptionPane.showMessageDialog(fpg, "Error pada PetugasController-simpanAction, keterangan : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }

}
