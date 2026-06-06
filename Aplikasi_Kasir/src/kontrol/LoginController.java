package kontrol;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import tampilan.FrameUtama;
import tampilan.FormLogin;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class LoginController {
    private int percobaan = 3;
    
    private String sql_query;
    private String hak_akses;
    private final String akses_admin = "7";
    
    public void tentukanTampilan(FormLogin fl) {
        try {
            fl.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
            BasicInternalFrameUI bifUI = (BasicInternalFrameUI) fl.getUI();
            bifUI.setNorthPane(null);
            fl.setBorder(null);
        } catch(Exception error) {
            System.err.println("Error pada LoginController-tentukanTampilan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fl, "Error pada LoginController-tentukanTampilan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loginAction(FormLogin fl) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("FALSE")) {
                if(FormLogin.userField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(fl, "Harap isi kolom nama!", "Pesan", JOptionPane.WARNING_MESSAGE);
                } else if(FormLogin.passField.getText().isEmpty()) { 
                    JOptionPane.showMessageDialog(fl, "Harap isi kolom sandi!", "Pesan", JOptionPane.WARNING_MESSAGE);
                } else {
                    PreparedStatement ps;
                    ResultSet rs;

                    sql_query = "SELECT * FROM tab_user WHERE nama = ? AND sandi = ?;";
                    
                    ps = Koneksi.getKoneksi().prepareStatement(sql_query);
                    ps.setString(1, FormLogin.userField.getText());
                    ps.setString(2, FormLogin.passField.getText());
                    
                    rs = ps.executeQuery();

                    if(rs.next()) {
                        JOptionPane.showMessageDialog(fl, "Sukses Login! Selamat Datang, " + FormLogin.userField.getText());
                        hak_akses = rs.getString("hak_akses");
                        
                        fl.dispose();
                        FrameUtama.PanelDasar.remove(fl);
                        FrameUtama.MenuLihat.setVisible(true);
                        FrameUtama.ItemTutupLaman.setVisible(true);
                        FrameUtama.PanelUtama.setVisible(true);
                        FrameUtama.LabelPengguna.setText("Halo, " + FormLogin.userField.getText());
                        
                        FrameUtama.StatusLogin.setText("TRUE");
                        
                        if(hak_akses.equals(akses_admin)) {
                            FrameUtama.LabelJabatan.setText("Administrator");
                            FrameUtama.LabelProfileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tekstur/icon_admin.png")));
                            FrameUtama.ButtonDaftar.setVisible(true);
                            FrameUtama.MenuDaftar.setVisible(true);
                        } else {
                            FrameUtama.LabelJabatan.setText("Petugas");
                            FrameUtama.LabelProfileIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tekstur/icon_petugas.png")));
                            FrameUtama.ButtonDaftar.setVisible(false);
                        }
                        
                        FrameUtama.PanelUtama.requestFocus();
                    } else {
                        if(percobaan > 0) {
                            JOptionPane.showMessageDialog(fl, "Gagal Login! Pastikan Nama atau Sandi benar!", "Pesan", JOptionPane.WARNING_MESSAGE);
                            percobaan--;
                            FormLogin.passField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(fl, "Terlalu banyak percobaan! Harap mulai ulang aplikasi.", "Pesan", JOptionPane.ERROR_MESSAGE);
                            System.exit(0);
                        }
                    }
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada LoginController-loginAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fl, "Error pada LoginController-loginAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void keluarAction(FormLogin fl) {
        try {
            int konfirmasi = JOptionPane.showConfirmDialog(fl, "Apakah anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if(konfirmasi == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(fl, "Terima kasih!");
                System.exit(0);
            }
        } catch(Exception error) {
            System.err.println("Error pada LoginController-keluarAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fl, "Error pada LoginController-keluarAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
