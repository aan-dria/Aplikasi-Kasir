package utilitas;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author AiroDRIA
 */
public class Koneksi {
    private static Connection koneksi;
    
    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/db_kasir";
                String user = "root";
                String pass = "";

                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, pass);
                
                System.out.println("Koneksi Database berhasil!");
            } catch (Exception e) {
                System.out.println("Error! Keterangan : " + e);
            }
        }
        return koneksi;
    }
}
