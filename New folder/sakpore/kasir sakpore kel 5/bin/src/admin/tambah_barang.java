/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package admin;

/**
 *
 * @author Acer
 */

import config.Koneksi;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class tambah_barang extends javax.swing.JPanel {

    /**
     * Creates new form tambah_barang
     */
    private String currentItemId = null;

    // CONSTRUCTOR 1: Untuk mode TAMBAH BARU (dipanggil dari dashboard_admin)
    public tambah_barang() {
        initComponents();
        initForm();
        jLabel8.setText("TAMBAH BARANG"); // Set judul
        A_btn_simpan.setText("SIMPAN");
    }
    
    public tambah_barang(String itemId) {
        initComponents();
        this.currentItemId = itemId;
        initForm();
        jLabel8.setText("EDIT BARANG"); // Set judul
        A_btn_simpan.setText("UPDATE");
        loadItemData(); // Muat data barang yang akan diedit
    }

    private void initForm() {
        A_tf_kode_barang.setEditable(false);
        A_tf_harga_jual.setEditable(false);
        
        loadKategoriComboBox();
        
        // Hanya buat ID baru jika ini adalah mode TAMBAH
        if (currentItemId == null) {
            generateNewItemId();
        }
        
        KeyAdapter calculationListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateHargaJual();
            }
        };
        A_tf_harga_pokok.addKeyListener(calculationListener);
        A_tf_ppn.addKeyListener(calculationListener);
    }
    
    private void loadItemData() {
        String sql = "SELECT * FROM daftar_barang WHERE id_barang = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, this.currentItemId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                A_tf_kode_barang.setText(rs.getString("id_barang"));
                A_tf_nama_barang.setText(rs.getString("nama_barang"));
                A_cb_kategori.setSelectedItem(rs.getString("kategori"));
                A_tf_satuan_barang.setText(rs.getString("satuan"));
                A_tf_harga_pokok.setText(String.valueOf(rs.getLong("harga_pokok")));
                A_tf_ppn.setText(String.valueOf(rs.getLong("ppn")));
                A_tf_harga_jual.setText(String.valueOf(rs.getLong("harga_jual")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
        }
    }

    private void loadKategoriComboBox() {
        try (Connection conn = Koneksi.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT nama_kategori FROM kategori_barang ORDER BY nama_kategori ASC")) {

            A_cb_kategori.removeAllItems(); // Hapus item default
            A_cb_kategori.addItem("-- Pilih Kategori --"); // Tambahkan item placeholder

            while (rs.next()) {
                A_cb_kategori.addItem(rs.getString("nama_kategori"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data kategori: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void generateNewItemId() {
        String newId = "brg001";
        try (Connection conn = Koneksi.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MAX(id_barang) FROM daftar_barang")) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null) {
                    // Ambil bagian angka dari "brg005" -> "005"
                    String numericPart = lastId.substring(3);
                    int idNum = Integer.parseInt(numericPart);
                    idNum++;
                    newId = "brg" + String.format("%03d", idNum); // Format kembali menjadi "brg006"
                }
            }
            A_tf_kode_barang.setText(newId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuat ID Barang baru: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void calculateHargaJual() {
        try {
            // Ambil nilai dari text field, ganti teks kosong dengan "0"
            String hargaPokokStr = A_tf_harga_pokok.getText().isEmpty() ? "0" : A_tf_harga_pokok.getText();
            String ppnStr = A_tf_ppn.getText().isEmpty() ? "0" : A_tf_ppn.getText();

            // Konversi ke tipe data long untuk angka besar
            long hargaPokok = Long.parseLong(hargaPokokStr);
            long ppn = Long.parseLong(ppnStr);

            long hargaJual = hargaPokok + ppn;

            A_tf_harga_jual.setText(String.valueOf(hargaJual));

        } catch (NumberFormatException e) {
            // Jika input bukan angka, set harga jual ke 0
            A_tf_harga_jual.setText("0");
        }
    }

    private void clearForm() {
        A_tf_nama_barang.setText("");
        A_cb_kategori.setSelectedIndex(0); // Kembali ke item placeholder "-- Pilih Kategori --"
        A_tf_satuan_barang.setText("");
        A_tf_harga_pokok.setText("");
        A_tf_ppn.setText("");
        A_tf_harga_jual.setText("");

        // Buat ID baru untuk data selanjutnya
        generateNewItemId();
        A_tf_nama_barang.requestFocus(); // Fokuskan kursor ke field nama barang
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        A_btn_kembali = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        A_tf_ppn = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        A_btn_simpan = new javax.swing.JButton();
        A_tf_kode_barang = new javax.swing.JTextField();
        A_tf_nama_barang = new javax.swing.JTextField();
        A_tf_harga_pokok = new javax.swing.JTextField();
        A_cb_kategori = new javax.swing.JComboBox<>();
        A_tf_harga_jual = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        A_tf_satuan_barang = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setMaximumSize(new java.awt.Dimension(1650, 940));
        setMinimumSize(new java.awt.Dimension(1650, 940));
        setPreferredSize(new java.awt.Dimension(1650, 940));
        setRequestFocusEnabled(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TAMBAH DAFTAR BARANG");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 340, 40));

        A_btn_kembali.setBackground(new java.awt.Color(0, 0, 102));
        A_btn_kembali.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_kembali.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_kembali.setText("KEMBALI");
        A_btn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_kembaliActionPerformed(evt);
            }
        });
        add(A_btn_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 120, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("KODE BARANG:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 230, 70));

        A_tf_ppn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_ppn, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 520, 700, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("NAMA BARANG:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 230, 70));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("KATEGORI:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 230, 70));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("HARGA POKOK:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 230, 70));

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 1053, 512, 92));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel5.setText("KODE BARANG:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 1052, 489, 92));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("PPN/PAJAK/KEUNTUNGAN :");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, 290, 70));

        A_btn_simpan.setBackground(new java.awt.Color(0, 204, 0));
        A_btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 35)); // NOI18N
        A_btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_simpan.setText("SIMPAN");
        A_btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_simpanActionPerformed(evt);
            }
        });
        add(A_btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 1010, 90));

        A_tf_kode_barang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_kode_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 700, 70));

        A_tf_nama_barang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_nama_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 700, 70));

        A_tf_harga_pokok.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_harga_pokok, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 440, 700, 70));

        A_cb_kategori.setBackground(new java.awt.Color(102, 102, 102));
        A_cb_kategori.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_cb_kategori.setForeground(new java.awt.Color(204, 204, 204));
        A_cb_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(A_cb_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 700, 70));

        A_tf_harga_jual.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_harga_jual, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 600, 700, 70));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("HARGA JUAL:");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, 230, 70));

        jLabel8.setBackground(new java.awt.Color(0, 0, 102));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setOpaque(true);
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1650, 80));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("SATUAN BARANG:");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 230, 70));

        A_tf_satuan_barang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        add(A_tf_satuan_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 360, 700, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void A_btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_simpanActionPerformed
        // TODO add your handling code here:
        String idBarang = A_tf_kode_barang.getText();
        String namaBarang = A_tf_nama_barang.getText().trim();
        String kategori = A_cb_kategori.getSelectedItem().toString();
        String satuan = A_tf_satuan_barang.getText().trim();
        String hargaPokokStr = A_tf_harga_pokok.getText().trim();
        String ppnStr = A_tf_ppn.getText().trim();

        if (namaBarang.isEmpty() || kategori.equals("-- Pilih Kategori --") || satuan.isEmpty() || hargaPokokStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom kecuali PPN harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long hargaPokok = Long.parseLong(hargaPokokStr);
            long ppn = ppnStr.isEmpty() ? 0 : Long.parseLong(ppnStr);
            long hargaJual = hargaPokok + ppn;

            // Logika Update atau Insert
            if (currentItemId != null) { // Mode UPDATE
                String sql = "UPDATE daftar_barang SET nama_barang=?, kategori=?, satuan=?, harga_pokok=?, ppn=?, harga_jual=? WHERE id_barang=?";
                try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, namaBarang);
                    ps.setString(2, kategori);
                    ps.setString(3, satuan);
                    ps.setLong(4, hargaPokok);
                    ps.setLong(5, ppn);
                    ps.setLong(6, hargaJual);
                    ps.setString(7, currentItemId);
                    
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Kembali ke daftar barang
                    A_btn_kembaliActionPerformed(null);
                }
            } else { // Mode INSERT
                int stok = 0;
                String sql = "INSERT INTO daftar_barang (id_barang, nama_barang, kategori, satuan, harga_pokok, ppn, harga_jual, stok) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, idBarang);
                    ps.setString(2, namaBarang);
                    ps.setString(3, kategori);
                    ps.setString(4, satuan);
                    ps.setLong(5, hargaPokok);
                    ps.setLong(6, ppn);
                    ps.setLong(7, hargaJual);
                    ps.setInt(8, stok);
                    
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }// GEN-LAST:event_A_btn_simpanActionPerformed

    private void A_btn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_kembaliActionPerformed
        // TODO add your handling code here:
        javax.swing.JFrame mainFrame = (javax.swing.JFrame) this.getTopLevelAncestor();
        if (mainFrame instanceof dashboard_admin) {
            ((dashboard_admin) mainFrame).showDaftarBarangPanel();
        }
    }// GEN-LAST:event_A_btn_kembaliActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A_btn_kembali;
    private javax.swing.JButton A_btn_simpan;
    private javax.swing.JComboBox<String> A_cb_kategori;
    private javax.swing.JTextField A_tf_harga_jual;
    private javax.swing.JTextField A_tf_harga_pokok;
    private javax.swing.JTextField A_tf_kode_barang;
    private javax.swing.JTextField A_tf_nama_barang;
    private javax.swing.JTextField A_tf_ppn;
    private javax.swing.JTextField A_tf_satuan_barang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
