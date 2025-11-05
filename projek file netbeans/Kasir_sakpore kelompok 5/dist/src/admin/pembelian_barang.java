/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package admin;

/**
 *
 * @author Acer Aspire Lite 15
 */

import config.Koneksi;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class pembelian_barang extends javax.swing.JPanel {

    /**
     * Creates new form pembelian_barang
     */
    private DefaultTableModel productTableModel;
    private int selectedProductRow = -1;
    
    public pembelian_barang() {
        initComponents();
        initForm();
    }
    
     private void initForm() {
        // Mengatur field form agar tidak bisa diedit manual
        A_tf_kode_barang.setEditable(false);
        A_tf_nama_barang.setEditable(false);
        A_tf_kategori.setEditable(false);
        A_tf_satuan.setEditable(false);
        A_tf_harga_pokok.setEditable(false);
        A_tf_total_harga.setEditable(false);

        // Inisialisasi model tabel untuk daftar barang
        productTableModel = (DefaultTableModel) A_tbl_daftar_barang.getModel();
        loadProductTableData(null); // Memuat semua data produk saat panel dibuka
        
        // Listener untuk kalkulasi total harga otomatis saat jumlah beli diisi
        A_tf_jumlah_beli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotalPrice();
            }
        });

        // Listener untuk mengetahui baris mana yang diklik di tabel produk
        A_tbl_daftar_barang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedProductRow = A_tbl_daftar_barang.getSelectedRow();
            }
        });
    }
     
     private void loadProductTableData(String searchTerm) {
        productTableModel.setRowCount(0);
        String sql = "SELECT id_barang, nama_barang, kategori, satuan, harga_pokok, stok FROM daftar_barang";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql += " WHERE id_barang ILIKE ? OR nama_barang ILIKE ? OR kategori ILIKE ?";
        }
        
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String pattern = "%" + searchTerm + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }
            ResultSet rs = ps.executeQuery();
            int no = 1;
            while (rs.next()) {
                productTableModel.addRow(new Object[]{
                    no++,
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori"),
                    rs.getString("satuan"),
                    rs.getLong("harga_pokok"),
                    rs.getInt("stok")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
        }
    }

    private void calculateTotalPrice() {
        try {
            long hargaPokok = Long.parseLong(A_tf_harga_pokok.getText());
            int jumlahBeli = Integer.parseInt(A_tf_jumlah_beli.getText());
            long totalHarga = hargaPokok * jumlahBeli;
            A_tf_total_harga.setText(String.valueOf(totalHarga));
        } catch (NumberFormatException e) {
            A_tf_total_harga.setText("0");
        }
    }
    
    private void clearPurchaseForm() {
        A_dc_tanggal_pembelian.setDate(null);
        A_tf_kode_barang.setText("");
        A_tf_nama_barang.setText("");
        A_tf_kategori.setText("");
        A_tf_satuan.setText("");
        A_tf_harga_pokok.setText("");
        A_tf_jumlah_beli.setText("");
        A_tf_total_harga.setText("");
        A_tf_supplier.setText("");
        A_tf_perusahaan_supplier.setText("");
        selectedProductRow = -1;
        A_tbl_daftar_barang.clearSelection();
    }

    // Method ini akan dipanggil oleh kedua tombol PILIH
    private void performPilihAction() {
        if (selectedProductRow == -1) {
            JOptionPane.showMessageDialog(this, "Harap pilih data dari tabel barang terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mengambil data dari baris yang dipilih (ingat kolom NO. adalah kolom 0)
        A_tf_kode_barang.setText(productTableModel.getValueAt(selectedProductRow, 1).toString());
        A_tf_nama_barang.setText(productTableModel.getValueAt(selectedProductRow, 2).toString());
        A_tf_kategori.setText(productTableModel.getValueAt(selectedProductRow, 3).toString());
        A_tf_satuan.setText(productTableModel.getValueAt(selectedProductRow, 4).toString());
        A_tf_harga_pokok.setText(productTableModel.getValueAt(selectedProductRow, 5).toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        A_tf_kode_barang = new javax.swing.JTextField();
        A_btn_pilih1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        A_tf_nama_barang = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        A_tf_satuan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        A_tf_harga_pokok = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        A_tf_jumlah_beli = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        A_tf_total_harga = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        A_tf_supplier = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        A_tf_perusahaan_supplier = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        A_btn_lihat_laporan = new javax.swing.JButton();
        A_dc_tanggal_pembelian = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        A_tbl_daftar_barang = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        A_tf_pencarian = new javax.swing.JTextField();
        A_btn_pilih2 = new javax.swing.JButton();
        A_btn_refresh = new javax.swing.JButton();
        A_btn_cari = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        A_tf_kategori = new javax.swing.JTextField();
        A_btn_simpan = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(1650, 940));
        setPreferredSize(new java.awt.Dimension(700, 700));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setPreferredSize(new java.awt.Dimension(1700, 940));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(0, 0, 102));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("    TAMBAH PEMBELIAN BARANG");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1650, 60));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("TANGGAL :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 160, 40));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("KODE BARANG :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 160, 40));

        A_tf_kode_barang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_kode_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_kode_barangActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_kode_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 240, 40));

        A_btn_pilih1.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_pilih1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_pilih1.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_pilih1.setText("PILIH");
        A_btn_pilih1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_pilih1ActionPerformed(evt);
            }
        });
        jPanel1.add(A_btn_pilih1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 100, 40));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("NAMA BARANG :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 160, 40));

        A_tf_nama_barang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_nama_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_nama_barangActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_nama_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 340, 40));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("SATUAN :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 160, 40));

        A_tf_satuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_satuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_satuanActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_satuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 340, 40));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("HARGA SATUAN :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 160, 40));

        A_tf_harga_pokok.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_harga_pokok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_harga_pokokActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_harga_pokok, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 340, 40));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("JUMLAH BELI :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 160, 40));

        A_tf_jumlah_beli.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_jumlah_beli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_jumlah_beliActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_jumlah_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 340, 40));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("TOTAL HARGA :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 160, 40));

        A_tf_total_harga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_total_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_total_hargaActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_total_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 430, 340, 40));

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("NAMA SUPPLIER :");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 160, 40));

        A_tf_supplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_supplierActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_supplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, 340, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText(":");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 540, 20, -1));

        A_tf_perusahaan_supplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_perusahaan_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_perusahaan_supplierActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_perusahaan_supplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 530, 340, 40));

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("PERUSAHAAN /");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 160, -1));

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("PT PRODUKSI");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 160, -1));

        A_btn_lihat_laporan.setBackground(new java.awt.Color(51, 204, 0));
        A_btn_lihat_laporan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        A_btn_lihat_laporan.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_lihat_laporan.setText("LIHAT LAPORAN PEMBELIAN BARANG");
        A_btn_lihat_laporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_lihat_laporanActionPerformed(evt);
            }
        });
        jPanel1.add(A_btn_lihat_laporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 680, 500, 60));
        jPanel1.add(A_dc_tanggal_pembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 340, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        A_tbl_daftar_barang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tbl_daftar_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NO.", "ID BARANG", "NAMA BARANG", "KATEGORI", "SATUAN", "HARGA POKOK", "STOK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        A_tbl_daftar_barang.setRowHeight(30);
        jScrollPane1.setViewportView(A_tbl_daftar_barang);
        if (A_tbl_daftar_barang.getColumnModel().getColumnCount() > 0) {
            A_tbl_daftar_barang.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1040, 630));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("DAFTAR BARANG");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 40));

        A_tf_pencarian.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_pencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_pencarianActionPerformed(evt);
            }
        });
        jPanel2.add(A_tf_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 370, 40));

        A_btn_pilih2.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_pilih2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_pilih2.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_pilih2.setText("PILIH");
        A_btn_pilih2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_pilih2ActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_pilih2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 150, 40));

        A_btn_refresh.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_refresh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_refresh.setText("REFRESH");
        A_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_refreshActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, 120, 40));

        A_btn_cari.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_cari.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_cari.setText("CARI");
        A_btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_cariActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 120, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, 1060, 750));

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("KATEGORI :");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 160, 40));

        A_tf_kategori.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        A_tf_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_tf_kategoriActionPerformed(evt);
            }
        });
        jPanel1.add(A_tf_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 340, 40));

        A_btn_simpan.setBackground(new java.awt.Color(102, 102, 102));
        A_btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        A_btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_simpan.setText("SIMPAN");
        A_btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_simpanActionPerformed(evt);
            }
        });
        jPanel1.add(A_btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 500, 60));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1650, 880));
    }// </editor-fold>//GEN-END:initComponents

    private void A_tf_kode_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_kode_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_kode_barangActionPerformed

    private void A_btn_pilih1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_pilih1ActionPerformed
        // TODO add your handling code here:
        performPilihAction();
    }//GEN-LAST:event_A_btn_pilih1ActionPerformed

    private void A_tf_nama_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_nama_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_nama_barangActionPerformed

    private void A_tf_satuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_satuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_satuanActionPerformed

    private void A_tf_harga_pokokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_harga_pokokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_harga_pokokActionPerformed

    private void A_tf_jumlah_beliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_jumlah_beliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_jumlah_beliActionPerformed

    private void A_tf_total_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_total_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_total_hargaActionPerformed

    private void A_tf_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_supplierActionPerformed

    private void A_tf_perusahaan_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_perusahaan_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_perusahaan_supplierActionPerformed

    private void A_tf_pencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_pencarianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_pencarianActionPerformed

    private void A_btn_pilih2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_pilih2ActionPerformed
        // TODO add your handling code here:
        performPilihAction();
    }//GEN-LAST:event_A_btn_pilih2ActionPerformed

    private void A_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_refreshActionPerformed
        // TODO add your handling code here:
        A_tf_pencarian.setText("");
        loadProductTableData(null);
        selectedProductRow = -1;
    }//GEN-LAST:event_A_btn_refreshActionPerformed

    private void A_btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_cariActionPerformed
        // TODO add your handling code here:
        loadProductTableData(A_tf_pencarian.getText());
    }//GEN-LAST:event_A_btn_cariActionPerformed

    private void A_tf_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_tf_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_A_tf_kategoriActionPerformed

    private void A_btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_simpanActionPerformed
        // TODO add your handling code here:
        if (A_dc_tanggal_pembelian.getDate() == null || A_tf_kode_barang.getText().isEmpty() || A_tf_jumlah_beli.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal, Barang, dan Jumlah Beli tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false); // Memulai mode transaksi

            // Aksi 1: Menyimpan data ke tabel 'pembelian_barang'
            String sqlPembelian = "INSERT INTO pembelian_barang (id_pembelian, tanggal, id_barang, nama_barang, kategori, satuan, harga_satuan, jumlah_beli, total_harga, nama_supplier, perusahaan_supplier) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlPembelian)) {
                String idBeli = "PEM-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                
                ps.setString(1, idBeli);
                ps.setDate(2, new java.sql.Date(A_dc_tanggal_pembelian.getDate().getTime()));
                ps.setString(3, A_tf_kode_barang.getText());
                ps.setString(4, A_tf_nama_barang.getText());
                ps.setString(5, A_tf_kategori.getText());
                ps.setString(6, A_tf_satuan.getText());
                ps.setLong(7, Long.parseLong(A_tf_harga_pokok.getText()));
                ps.setInt(8, Integer.parseInt(A_tf_jumlah_beli.getText()));
                ps.setLong(9, Long.parseLong(A_tf_total_harga.getText()));
                ps.setString(10, A_tf_supplier.getText());
                ps.setString(11, A_tf_perusahaan_supplier.getText());
                ps.executeUpdate();
            }

            // Aksi 2: Memperbarui stok di tabel 'daftar_barang'
            String sqlUpdateStok = "UPDATE daftar_barang SET stok = CAST(stok AS INTEGER) + ? WHERE id_barang = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateStok)) {
                ps.setInt(1, Integer.parseInt(A_tf_jumlah_beli.getText()));
                ps.setString(2, A_tf_kode_barang.getText());
                ps.executeUpdate();
            }
            
            conn.commit(); // Menyimpan semua perubahan jika berhasil
            JOptionPane.showMessageDialog(this, "Data pembelian berhasil disimpan dan stok telah diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            clearPurchaseForm();
            loadProductTableData(null);

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {} // Batalkan jika gagal
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception e) {}
        }
    
    }//GEN-LAST:event_A_btn_simpanActionPerformed

    private void A_btn_lihat_laporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_btn_lihat_laporanActionPerformed
        // TODO add your handling code here:
        javax.swing.JFrame mainFrame = (javax.swing.JFrame) this.getTopLevelAncestor();
    
    // Periksa apakah frame tersebut adalah instance dari dashboard_admin
    if (mainFrame instanceof dashboard_admin) {
        // Panggil method publik showLaporanPembelianPanel() dari dashboard_admin
        ((dashboard_admin) mainFrame).showLaporanPembelianPanel();
    }
    }//GEN-LAST:event_A_btn_lihat_laporanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A_btn_cari;
    private javax.swing.JButton A_btn_lihat_laporan;
    private javax.swing.JButton A_btn_pilih1;
    private javax.swing.JButton A_btn_pilih2;
    private javax.swing.JButton A_btn_refresh;
    private javax.swing.JButton A_btn_simpan;
    private com.toedter.calendar.JDateChooser A_dc_tanggal_pembelian;
    private javax.swing.JTable A_tbl_daftar_barang;
    private javax.swing.JTextField A_tf_harga_pokok;
    private javax.swing.JTextField A_tf_jumlah_beli;
    private javax.swing.JTextField A_tf_kategori;
    private javax.swing.JTextField A_tf_kode_barang;
    private javax.swing.JTextField A_tf_nama_barang;
    private javax.swing.JTextField A_tf_pencarian;
    private javax.swing.JTextField A_tf_perusahaan_supplier;
    private javax.swing.JTextField A_tf_satuan;
    private javax.swing.JTextField A_tf_supplier;
    private javax.swing.JTextField A_tf_total_harga;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
