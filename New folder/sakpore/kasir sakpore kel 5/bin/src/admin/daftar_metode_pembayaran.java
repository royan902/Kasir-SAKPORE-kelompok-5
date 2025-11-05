/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package admin;

import config.Koneksi;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Acer Aspire Lite 15
 */
public class daftar_metode_pembayaran extends javax.swing.JPanel {

    /**
     * Creates new form daftar_metode_pembayaran
     */
    private DefaultTableModel tableModel;
    private String selectedAccountId = null;
    
    public daftar_metode_pembayaran() {
        initComponents();
        
        initForm();
    }
    
    private void initForm() {
        tableModel = (DefaultTableModel) A_tabel_daftar_akunPembayaran.getModel();
        A_tf_id_akunPembayaran.setEditable(false); // ID tidak bisa diubah manual

        loadMetodeComboBox(); // Muat pilihan untuk ComboBox
        loadAccountTableData(null); // Muat data untuk tabel
        generateNewId(); // Siapkan ID baru untuk form tambah

        // Listener untuk mendeteksi klik pada tabel
        A_tabel_daftar_akunPembayaran.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = A_tabel_daftar_akunPembayaran.getSelectedRow();
                if (selectedRow != -1) {
                    // Simpan ID dari baris yang diklik (kolom ke-1)
                    selectedAccountId = tableModel.getValueAt(selectedRow, 1).toString();
                }
            }
        });
    }
    
    private void loadMetodeComboBox() {
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nama_metode FROM metode_pembayaran ORDER BY nama_metode ASC")) {
            
            A_cb_metode_pembayaran.removeAllItems();
            A_cb_metode_pembayaran.addItem("-- Pilih Metode --");
            while (rs.next()) {
                A_cb_metode_pembayaran.addItem(rs.getString("nama_metode"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat metode pembayaran: " + e.getMessage());
        }
    }
    
    private void loadAccountTableData(String searchTerm) {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM akun_pembayaran";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql += " WHERE id_akunPembayaran ILIKE ? OR nama_pembayaran ILIKE ? OR metode_pembayaran ILIKE ?";
        }
        sql += " ORDER BY id_akunPembayaran ASC";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String pattern = "%" + searchTerm + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }

            ResultSet rs = ps.executeQuery();
            int no = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    no++,
                    rs.getString("id_akunPembayaran"),
                    rs.getString("nama_pembayaran"),
                    rs.getString("metode_pembayaran"),
                    rs.getString("keterangan")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data akun: " + e.getMessage());
        }
    }
    
    private void generateNewId() {
        String newId = "001";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(id_akunPembayaran) FROM akun_pembayaran")) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null && !lastId.isEmpty()) {
                    int idNum = Integer.parseInt(lastId);
                    idNum++;
                    newId = String.format("%03d", idNum);
                }
            }
            A_tf_id_akunPembayaran.setText(newId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuat ID baru: " + e.getMessage());
        }
    }
    
    // 7. METHOD UNTUK MEMBERSIHKAN FORM DAN SELEKSI
    private void clearForm() {
        A_tf_nama_pembayaran.setText("");
        A_cb_metode_pembayaran.setSelectedIndex(0);
        A_tf_keterangan.setText("");
        A_tabel_daftar_akunPembayaran.clearSelection();
        selectedAccountId = null;
        A_btn_simpan.setText("SIMPAN");
        generateNewId(); // Siapkan ID baru
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        A_tf_id_akunPembayaran = new javax.swing.JTextField();
        A_btn_simpan = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        A_tf_keterangan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        A_tf_nama_pembayaran = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        A_cb_metode_pembayaran = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        A_tf_pencarian = new javax.swing.JTextField();
        A_btn_delete = new javax.swing.JButton();
        A_btn_cari = new javax.swing.JButton();
        A_btn_refresh = new javax.swing.JButton();
        A_btn_edit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        A_tabel_daftar_akunPembayaran = new javax.swing.JTable();

        jButton1.setText("jButton1");

        setMinimumSize(new java.awt.Dimension(1650, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(0, 0, 102));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TAMBAH METODE PEMBAYARAN");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel1.setOpaque(true);
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 50));

        A_tf_id_akunPembayaran.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel3.add(A_tf_id_akunPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 450, 40));

        A_btn_simpan.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_simpan.setText("SIMPAN");
        A_btn_simpan.setActionCommand("");
        A_btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_simpanActionPerformed(evt);
            }
        });
        jPanel3.add(A_btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 450, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("ID AKUN PEMBAYARAN");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 450, 40));

        A_tf_keterangan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        A_tf_keterangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_keteranganActionPerformed(evt);
            }
        });
        jPanel3.add(A_tf_keterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 450, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("KETERANGAN");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 450, 40));

        A_tf_nama_pembayaran.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel3.add(A_tf_nama_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 450, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("NAMA AKUN PEMBAYARAN");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 450, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("METODE PEMBAYARAN");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 450, 40));

        A_cb_metode_pembayaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        A_cb_metode_pembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_cb_metode_pembayaranActionPerformed(evt);
            }
        });
        jPanel3.add(A_cb_metode_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 450, 40));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 500, 920));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(0, 0, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("DAFTAR METODE PEMBAYARAN");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel2.setOpaque(true);
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 50));

        A_tf_pencarian.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel4.add(A_tf_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 300, 40));

        A_btn_delete.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_delete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_delete.setText("DELETE");
        A_btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_deleteActionPerformed(evt);
            }
        });
        jPanel4.add(A_btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 70, 150, 40));

        A_btn_cari.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_cari.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_cari.setText("CARI");
        A_btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_cariActionPerformed(evt);
            }
        });
        jPanel4.add(A_btn_cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 150, 40));

        A_btn_refresh.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_refresh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_refresh.setText("REFRESH");
        A_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_refreshActionPerformed(evt);
            }
        });
        jPanel4.add(A_btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 70, 150, 40));

        A_btn_edit.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_edit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_edit.setText("EDIT");
        A_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_editActionPerformed(evt);
            }
        });
        jPanel4.add(A_btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 70, 150, 40));

        A_tabel_daftar_akunPembayaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tabel_daftar_akunPembayaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NO.", "ID AKUN PEMBAYARAN", "NAMA PEMBAYARAN", "METODE PEMBAYARAN", "KETERANGAN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        A_tabel_daftar_akunPembayaran.setRowHeight(30);
        jScrollPane1.setViewportView(A_tabel_daftar_akunPembayaran);
        if (A_tabel_daftar_akunPembayaran.getColumnModel().getColumnCount() > 0) {
            A_tabel_daftar_akunPembayaran.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 1060, 750));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 1110, 920));
    }// </editor-fold>//GEN-END:initComponents

    private void A_btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_simpanActionPerformed
        // TODO add your handling code here:
        String id = A_tf_id_akunPembayaran.getText();
        String nama = A_tf_nama_pembayaran.getText().trim();
        String metode = A_cb_metode_pembayaran.getSelectedItem().toString();
        String keterangan = A_tf_keterangan.getText().trim();
        
        if (nama.isEmpty() || metode.equals("-- Pilih Metode --")) {
            JOptionPane.showMessageDialog(this, "Nama dan Metode Pembayaran harus diisi!");
            return;
        }

        try (Connection conn = Koneksi.getConnection()) {
            PreparedStatement ps;
            // Jika mode UPDATE
            if (A_btn_simpan.getText().equals("UPDATE")) {
                String sql = "UPDATE akun_pembayaran SET nama_pembayaran=?, metode_pembayaran=?, keterangan=? WHERE id_akunPembayaran=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, metode);
                ps.setString(3, keterangan);
                ps.setString(4, selectedAccountId);
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            } else { // Jika mode SIMPAN BARU
                String sql = "INSERT INTO akun_pembayaran (id_akunPembayaran, nama_pembayaran, metode_pembayaran, keterangan) VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                ps.setString(2, nama);
                ps.setString(3, metode);
                ps.setString(4, keterangan);
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            }
            ps.executeUpdate();
            ps.close();
            
            clearForm();
            loadAccountTableData(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }//GEN-LAST:event_A_btn_simpanActionPerformed

    private void A_tf_keteranganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_keteranganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_keteranganActionPerformed

    private void A_cb_metode_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_cb_metode_pembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_cb_metode_pembayaranActionPerformed

    private void A_btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_deleteActionPerformed
        // TODO add your handling code here:
        if (selectedAccountId == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dihapus dari tabel.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM akun_pembayaran WHERE id_akunPembayaran = ?";
                try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, selectedAccountId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    
                    clearForm();
                    loadAccountTableData(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_A_btn_deleteActionPerformed

    private void A_btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_cariActionPerformed
        // TODO add your handling code here:
        loadAccountTableData(A_tf_pencarian.getText());
    }//GEN-LAST:event_A_btn_cariActionPerformed

    private void A_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_refreshActionPerformed
        // TODO add your handling code here:
        A_tf_pencarian.setText("");
        clearForm();
        loadAccountTableData(null);
    }//GEN-LAST:event_A_btn_refreshActionPerformed

    private void A_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_editActionPerformed
        // TODO add your handling code here:
        if (selectedAccountId == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin di-edit dari tabel.");
            return;
        }

        int selectedRow = A_tabel_daftar_akunPembayaran.getSelectedRow();
        A_tf_id_akunPembayaran.setText(tableModel.getValueAt(selectedRow, 1).toString());
        A_tf_nama_pembayaran.setText(tableModel.getValueAt(selectedRow, 2).toString());
        A_cb_metode_pembayaran.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
        A_tf_keterangan.setText(tableModel.getValueAt(selectedRow, 4).toString());
        
        A_btn_simpan.setText("UPDATE");
    }//GEN-LAST:event_A_btn_editActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A_btn_cari;
    private javax.swing.JButton A_btn_delete;
    private javax.swing.JButton A_btn_edit;
    private javax.swing.JButton A_btn_refresh;
    private javax.swing.JButton A_btn_simpan;
    private javax.swing.JComboBox<String> A_cb_metode_pembayaran;
    private javax.swing.JTable A_tabel_daftar_akunPembayaran;
    private javax.swing.JTextField A_tf_id_akunPembayaran;
    private javax.swing.JTextField A_tf_keterangan;
    private javax.swing.JTextField A_tf_nama_pembayaran;
    private javax.swing.JTextField A_tf_pencarian;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
