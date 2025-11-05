package login;

import admin.dashboard_admin;
import config.UserSession;
import kasir.dashboard_kasir;
// Import UIManager
import javax.swing.UIManager; 

public class Main {
    public static void main(String[] args) {

        // === TAMBAHKAN KODE LOOK AND FEEL DI SINI ===
        try {
            // Cari Look and Feel Nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break; // Keluar dari loop setelah Nimbus ditemukan dan diatur
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            // Jika Nimbus tidak tersedia, log error (opsional)
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            // Aplikasi akan berjalan dengan Look and Feel default jika Nimbus gagal
        }
        // ===========================================

        // Kode pengecekan session Anda (tetap sama)
        if (UserSession.isLoggedIn()) {
            String jabatan = UserSession.getJabatan();

            if ("admin".equalsIgnoreCase(jabatan)) {
                new dashboard_admin().setVisible(true);
            } else if ("kasir".equalsIgnoreCase(jabatan)) {
                new dashboard_kasir().setVisible(true);
            } else {
                // Tambahkan penanganan jika jabatan tidak dikenali (opsional)
                 new halaman_login().setVisible(true); // Kembali ke login jika jabatan aneh
            }
        } else {
            new halaman_login().setVisible(true);
        }
    }
}