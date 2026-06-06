package kontrol;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import tampilan.FormCetak;
import tampilan.FormKeranjang;
import tampilan.FrameUtama;
import tampilan.FormLogin;
import tampilan.FormPelanggan;
import tampilan.FormPenjualan;
import tampilan.FormPetugas;
import tampilan.FormProduk;
import utilitas.Koneksi;

/**
 *
 * @author AiroDRIA
 */
public class AplikasiController {
    private final FormProduk fpr = new FormProduk();
    private final FormPelanggan fpl = new FormPelanggan();
    private final FormPenjualan fpj = new FormPenjualan();
    private final FormKeranjang fkj = new FormKeranjang();
    private final FormPetugas fpg = new FormPetugas();
    private final FormCetak fct = new FormCetak();
    
    private String sql_query;
    private String jumlah_produk;
    private String jumlah_pelanggan;
    private String jumlah_keranjang_aktif;
    private String jumlah_keranjang_terekam;
    
    public void tentukanTampilan(FrameUtama fu) {
        try {
            fu.setExtendedState(MAXIMIZED_BOTH);
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tentukanTampilan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tentukanTampilan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void keluarAction(FrameUtama fu) {
        try {
            int konfirmasi = JOptionPane.showConfirmDialog(fu, "Apakah anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if(konfirmasi == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(fu, "Terima kasih!");
                System.exit(0);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-keluarAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-keluarAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void ambilJumlah(FrameUtama fu) {
        try {
            String tanggal_penjualan = "2024-01-01";
            PreparedStatement ps;
            ResultSet rs;
            
            sql_query = "SELECT a.produk, b.pelanggan, c.aktif, d.terekam FROM "
                    + "(SELECT COUNT(no_produk) AS produk FROM tab_produk) a, "
                    + "(SELECT COUNT(id_pelanggan) AS pelanggan FROM tab_pelanggan) b, "
                    + "(SELECT COUNT(no_penjualan) AS aktif FROM tab_penjualan WHERE tanggal = ?) c, "
                    + "(SELECT COUNT(no_penjualan) AS terekam FROM tab_penjualan WHERE tanggal != ?) d;";

            ps = Koneksi.getKoneksi().prepareStatement(sql_query);
            ps.setString(1, tanggal_penjualan);
            ps.setString(2, tanggal_penjualan);

            rs = ps.executeQuery();

            if(rs.next()) {
                jumlah_produk = rs.getString("produk");
                jumlah_pelanggan = rs.getString("pelanggan");
                jumlah_keranjang_aktif = rs.getString("aktif");
                jumlah_keranjang_terekam = rs.getString("terekam");

                FrameUtama.LabelProduk.setText(jumlah_produk);
                FrameUtama.LabelPelanggan.setText(jumlah_pelanggan);
                FrameUtama.LabelKeranjangAktif.setText(jumlah_keranjang_aktif);
                FrameUtama.LabelKeranjangTerekam.setText(jumlah_keranjang_terekam);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-ambilJumlah, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-ambilJumlah, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void logoutAction(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                FormLogin lf = new FormLogin();
            
                int konfirmasi = JOptionPane.showConfirmDialog(fu, "Apakah anda yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                
                if(konfirmasi == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(fu, "Berhasil logout dari akun!", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    FrameUtama.MenuLihat.setVisible(false);
                    FrameUtama.MenuDaftar.setVisible(false);
                    FrameUtama.ItemTutupLaman.setVisible(false);
                    FrameUtama.PanelDasar.add(lf);
                    FrameUtama.PanelUtama.setVisible(false);
                    lf.setLocation((fu.getWidth() / 2) - (lf.getWidth() / 2), (fu.getHeight() / 2) - (lf.getHeight() / 2));
                    lf.setVisible(true);
                    
                    FrameUtama.StatusLogin.setText("FALSE");
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-logoutAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-logoutAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tampilLogin(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("FALSE")) {
                FormLogin lf = new FormLogin();
            
                FrameUtama.MenuLihat.setVisible(false);
                FrameUtama.MenuDaftar.setVisible(false);
                FrameUtama.ItemTutupLaman.setVisible(false);
                FrameUtama.PanelDasar.add(lf);
                FrameUtama.PanelUtama.setVisible(false);
                lf.setLocation((fu.getWidth() / 2) - (lf.getWidth() / 2), (fu.getHeight() / 2) - (lf.getHeight() / 2) - FrameUtama.MenuBar.getHeight());
                lf.setVisible(true);
                
                FormLogin.userField.requestFocus();
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilLogin, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilLogin, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tampilProduk(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                FrameUtama.PanelTampilan.add(fpr);
                FrameUtama.ComboLaman.setSelectedIndex(1);
                FrameUtama.PanelMenu.setVisible(false);
                fpr.setVisible(true);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilProduk, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilProduk, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tampilPelanggan(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                FrameUtama.PanelTampilan.add(fpl);
                FrameUtama.ComboLaman.setSelectedIndex(2);
                FrameUtama.PanelMenu.setVisible(false);
                fpl.setVisible(true);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilPelanggan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilPelanggan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tampilPenjualan(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                FrameUtama.PanelTampilan.add(fpj);
                FrameUtama.ComboLaman.setSelectedIndex(3);
                FrameUtama.PanelMenu.setVisible(false);
                fpj.setVisible(true);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilPenjualan, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilPenjualan, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tampilPetugas(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                FrameUtama.PanelTampilan.add(fpg);
                FrameUtama.PanelMenu.setVisible(false);
                fpg.setVisible(true);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilPetugas, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilPetugas, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tutupLamanAction(FrameUtama fu) {
        try {
            fpr.dispose();
            fpl.dispose();
            fpj.dispose();
            fkj.dispose();
            fpg.dispose();
            fct.dispose();
            FrameUtama.PanelTampilan.remove(fpr);
            FrameUtama.PanelTampilan.remove(fpl);
            FrameUtama.PanelTampilan.remove(fpj);
            FrameUtama.PanelTampilan.remove(fkj);
            FrameUtama.PanelTampilan.remove(fpg);
            FrameUtama.PanelTampilan.remove(fct);
            FrameUtama.PanelMenu.setVisible(true);
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tutupLamanAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tutupLamanAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void comboLamanAction(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                if(FrameUtama.ComboLaman.getSelectedIndex() > 0) {
                    if(FrameUtama.ComboLaman.getSelectedIndex() == 1) {
                        fpr.dispose();
                        fpl.dispose();
                        fpj.dispose();
                        fkj.dispose();
                        fpg.dispose();
                        fct.dispose();
                        FrameUtama.PanelTampilan.remove(fpr);
                        FrameUtama.PanelTampilan.remove(fpl);
                        FrameUtama.PanelTampilan.remove(fpj);
                        FrameUtama.PanelTampilan.remove(fkj);
                        FrameUtama.PanelTampilan.remove(fpg);
                        FrameUtama.PanelTampilan.remove(fct);
                        FrameUtama.PanelMenu.setVisible(false);
                        tampilProduk(fu);
                    } else if(FrameUtama.ComboLaman.getSelectedIndex() == 2) {
                        fpr.dispose();
                        fpl.dispose();
                        fpj.dispose();
                        fkj.dispose();
                        fpg.dispose();
                        fct.dispose();
                        FrameUtama.PanelTampilan.remove(fpr);
                        FrameUtama.PanelTampilan.remove(fpl);
                        FrameUtama.PanelTampilan.remove(fpj);
                        FrameUtama.PanelTampilan.remove(fkj);
                        FrameUtama.PanelTampilan.remove(fpg);
                        FrameUtama.PanelTampilan.remove(fct);
                        FrameUtama.PanelMenu.setVisible(false);
                        tampilPelanggan(fu);
                    } else if(FrameUtama.ComboLaman.getSelectedIndex() == 3) {
                        fpr.dispose();
                        fpl.dispose();
                        fpj.dispose();
                        fkj.dispose();
                        fpg.dispose();
                        fct.dispose();
                        FrameUtama.PanelTampilan.remove(fpr);
                        FrameUtama.PanelTampilan.remove(fpl);
                        FrameUtama.PanelTampilan.remove(fpj);
                        FrameUtama.PanelTampilan.remove(fkj);
                        FrameUtama.PanelTampilan.remove(fpg);
                        FrameUtama.PanelTampilan.remove(fct);
                        FrameUtama.PanelMenu.setVisible(false);
                        tampilPenjualan(fu);
                    }
                } else {
                    fpr.dispose();
                    fpl.dispose();
                    fpj.dispose();
                    fkj.dispose();
                    fpg.dispose();
                    fct.dispose();
                    FrameUtama.PanelTampilan.remove(fpr);
                    FrameUtama.PanelTampilan.remove(fpl);
                    FrameUtama.PanelTampilan.remove(fpj);
                    FrameUtama.PanelTampilan.remove(fkj);
                    FrameUtama.PanelTampilan.remove(fpg);
                    FrameUtama.PanelTampilan.remove(fct);
                    FrameUtama.PanelMenu.setVisible(true);
                }
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-comboLamanAction, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-comboLamanAction, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void tentangAction(FrameUtama fu) {
        try {
            if(FrameUtama.StatusLogin.getText().equals("TRUE")) {
                JOptionPane.showMessageDialog(fpr, "Aplikasi dibuat oleh AiroDRIA\n\nSebagai bentuk penyelesaian tugas\nPBO dengan bahasa Java", "Tentang", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch(Exception error) {
            System.err.println("Error pada AplikasiController-tampilPetugas, keterangan : ");
            error.printStackTrace();
            JOptionPane.showMessageDialog(fu, "Error pada AplikasiController-tampilPetugas, detail : " + error.toString(), "Pesan", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
