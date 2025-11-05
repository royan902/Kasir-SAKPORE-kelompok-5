package config;

import java.util.prefs.Preferences;

public class UserSession {

    private static final Preferences prefs = Preferences.userNodeForPackage(UserSession.class);

    // Kunci untuk menyimpan data di Preferences
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JABATAN = "jabatan";
    private static final String KEY_TELEPON = "telepon";
    private static final String KEY_ALAMAT = "alamat";

    /**
     * Menyimpan data user ke session setelah login berhasil.
     * @param id ID user dari database.
     * @param username Username user.
     * @param jabatan Jabatan user (admin/kasir).
     * @param telepon Nomor telepon user.
     * @param alamat Alamat user.
     */
    public static void createSession(String id, String username, String jabatan, String telepon, String alamat) {
        prefs.put(KEY_ID, id);
        prefs.put(KEY_USERNAME, username);
        prefs.put(KEY_JABATAN, jabatan);
        prefs.put(KEY_TELEPON, telepon);
        prefs.put(KEY_ALAMAT, alamat);
    }

    /**
     * Menghapus semua data session saat logout.
     */
    public static void clearSession() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Memeriksa apakah ada session yang aktif (user sudah pernah login).
     * @return true jika ada session, false jika tidak.
     */
    public static boolean isLoggedIn() {
        // Cek apakah data username tersimpan. Jika ya, berarti ada sesi.
        return prefs.get(KEY_USERNAME, null) != null;
    }

    // --- Getter untuk mengambil data dari session ---

    /**
     * Mengambil ID user yang sedang login.
     * @return ID user sebagai String.
     */
    public static String getId() {
        return prefs.get(KEY_ID, null);
    }

    public static String getUsername() {
        return prefs.get(KEY_USERNAME, null);
    }

    public static String getJabatan() {
        return prefs.get(KEY_JABATAN, null);
    }
    
    public static String getTelepon() {
        return prefs.get(KEY_TELEPON, null);
    }

    public static String getAlamat() {
        return prefs.get(KEY_ALAMAT, null);
    }
}