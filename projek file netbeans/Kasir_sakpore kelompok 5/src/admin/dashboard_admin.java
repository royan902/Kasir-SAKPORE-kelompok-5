/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import login.halaman_login;

import config.UserSession;

/**
 *
 * @author user
 */
public class dashboard_admin extends javax.swing.JFrame {

    /**
     * Creates new form admin
     */
    public dashboard_admin() {
        initComponents();
        String username = UserSession.getUsername();
        String jabatan = UserSession.getJabatan();
        A_label_username.setText(username != null ? username.toUpperCase() : "N/A");
        A_label_jabatan.setText("Posisi : " + (jabatan != null ? jabatan.toUpperCase() : "N/A"));

        beranda_admin beranda = new beranda_admin();
        showPanel(beranda);

        setupDashboardKeyBindings();
    }

    private void showPanel(javax.swing.JPanel panel) {
        A_panel_utama.removeAll(); // hapus isi panel utama
        A_panel_utama.setLayout(new java.awt.BorderLayout());
        A_panel_utama.add(panel, java.awt.BorderLayout.CENTER);
        A_panel_utama.revalidate(); // refresh tampilan
        A_panel_utama.repaint();
    }

    public void showDaftarBarangPanel() {
        daftar_barang panel = new daftar_barang();
        showPanel(panel);
    }

    // Method untuk menampilkan panel tambah barang (bisa dipanggil dari panel lain)
    public void showTambahBarangPanel() {
        tambah_barang panel = new tambah_barang();
        showPanel(panel);
    }

    public void showEditBarangPanel(String itemId) {
        // Membuat instance dari tambah_barang menggunakan constructor baru yang
        // menerima ID
        tambah_barang panel = new tambah_barang(itemId);
        showPanel(panel);
    }

    public void showPembelianBarangPanel() {
        pembelian_barang panel = new pembelian_barang();
        showPanel(panel);
    }

    // Method untuk menampilkan panel laporan pembelian
    public void showLaporanTransaksiPanel() {
        laporan_transaksi panel = new laporan_transaksi();
        showPanel(panel);
    }

    public void showLaporanPembelianPanel() {
        laporan_pembelian panel = new laporan_pembelian();
        showPanel(panel);
    }

    public void showMetodePembayaranPanel() {
        daftar_metode_pembayaran panel = new daftar_metode_pembayaran();
        showPanel(panel);
    }

    public void showLaporanKeuanganPanel() {
        laporan_keuangan panel = new laporan_keuangan();
        showPanel(panel);
    }

    private void setupDashboardKeyBindings() {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();

        // Map tombol ALT + 1 s/d ALT + 9 ke tombol navigasi
        KeyStroke alt1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt8 = KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.ALT_DOWN_MASK);
        KeyStroke alt9 = KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.ALT_DOWN_MASK);

        inputMap.put(alt1, "navBeranda");
        inputMap.put(alt2, "navDaftarBarang");
        inputMap.put(alt3, "navKategori");
        inputMap.put(alt4, "navLapKeuangan");
        inputMap.put(alt5, "navLapTransaksi");
        inputMap.put(alt6, "navLapPembelian");
        inputMap.put(alt7, "navPembelian");
        inputMap.put(alt8, "navDaftarUser");
        inputMap.put(alt9, "navMetodeBayar");

        actionMap.put("navBeranda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_beranda.doClick();
            }
        });
        actionMap.put("navDaftarBarang", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_daftar_brg.doClick();
            }
        });
        actionMap.put("navKategori", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_kategori.doClick();
            }
        });
        actionMap.put("navLapKeuangan", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_lap_keuangan.doClick();
            }
        });
        actionMap.put("navLapTransaksi", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_lap_transaksi.doClick();
            }
        });
        actionMap.put("navLapPembelian", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_lap_pembelian.doClick();
            }
        });
        actionMap.put("navPembelian", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_pembelian.doClick();
            }
        });
        actionMap.put("navDaftarUser", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_daftar_user.doClick();
            }
        });
        actionMap.put("navMetodeBayar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_metode_pembayaran.doClick();
            }
        });

        // Opsional: Tambahkan shortcut logout jika belum ada
        KeyStroke altShiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                KeyEvent.ALT_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        inputMap.put(altShiftEnter, "logoutAction");
        actionMap.put("logoutAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                A_btn_logout.doClick();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        A_txt_1 = new javax.swing.JLabel();
        A_btn_logout = new javax.swing.JButton();
        A_label_jabatan = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        A_label_username = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        A_btn_beranda = new javax.swing.JButton();
        A_btn_lap_keuangan = new javax.swing.JButton();
        A_btn_lap_transaksi = new javax.swing.JButton();
        A_btn_lap_pembelian = new javax.swing.JButton();
        A_btn_daftar_brg = new javax.swing.JButton();
        A_btn_kategori = new javax.swing.JButton();
        A_btn_pembelian = new javax.swing.JButton();
        A_btn_daftar_user = new javax.swing.JButton();
        A_btn_metode_pembayaran = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        A_panel_utama = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));
        jPanel1.setMinimumSize(new java.awt.Dimension(1870, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        A_txt_1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        A_txt_1.setForeground(new java.awt.Color(255, 255, 255));
        A_txt_1.setText("ADMINE SAKPORE");
        jPanel1.add(A_txt_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 720, 100));

        A_btn_logout.setBackground(new java.awt.Color(0, 51, 51));
        A_btn_logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        A_btn_logout.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_logout.setText("LOGOUT");
        A_btn_logout.setMaximumSize(new java.awt.Dimension(70, 70));
        A_btn_logout.setMinimumSize(new java.awt.Dimension(70, 70));
        A_btn_logout.setPreferredSize(new java.awt.Dimension(70, 70));
        A_btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_logoutActionPerformed(evt);
            }
        });
        jPanel1.add(A_btn_logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1750, 40, 130, 40));

        A_label_jabatan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_label_jabatan.setForeground(new java.awt.Color(255, 255, 255));
        A_label_jabatan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        A_label_jabatan.setText("Posisi : ADMIN");
        jPanel1.add(A_label_jabatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1530, 60, 220, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("=");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, 80));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 100)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("=");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 80, 80));

        A_label_username.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        A_label_username.setForeground(new java.awt.Color(255, 255, 255));
        A_label_username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        A_label_username.setText("ALMADANI");
        jPanel1.add(A_label_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1530, 30, 220, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 120));

        jPanel2.setBackground(java.awt.Color.gray);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        A_btn_beranda.setBackground(java.awt.Color.darkGray);
        A_btn_beranda.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        A_btn_beranda.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_beranda.setText("BERANDA");
        A_btn_beranda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_beranda.setOpaque(true);
        A_btn_beranda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_berandaActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_beranda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 230, 60));

        A_btn_lap_keuangan.setBackground(java.awt.Color.darkGray);
        A_btn_lap_keuangan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_lap_keuangan.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_lap_keuangan.setText("LAPORAN KEUANGAN");
        A_btn_lap_keuangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_lap_keuangan.setOpaque(true);
        A_btn_lap_keuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_lap_keuanganActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_lap_keuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 230, 60));

        A_btn_lap_transaksi.setBackground(java.awt.Color.darkGray);
        A_btn_lap_transaksi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_lap_transaksi.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_lap_transaksi.setText("LAPORAN TRANSAKSI");
        A_btn_lap_transaksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_lap_transaksi.setOpaque(true);
        A_btn_lap_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_lap_transaksiActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_lap_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 230, 60));

        A_btn_lap_pembelian.setBackground(java.awt.Color.darkGray);
        A_btn_lap_pembelian.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_lap_pembelian.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_lap_pembelian.setText("LAPORAN PEMBELIAN");
        A_btn_lap_pembelian.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_lap_pembelian.setOpaque(true);
        A_btn_lap_pembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_lap_pembelianActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_lap_pembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 230, 60));

        A_btn_daftar_brg.setBackground(java.awt.Color.darkGray);
        A_btn_daftar_brg.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        A_btn_daftar_brg.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_daftar_brg.setText("DAFTAR BARANG");
        A_btn_daftar_brg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_daftar_brg.setOpaque(true);
        A_btn_daftar_brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_daftar_brgActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_daftar_brg, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 230, 60));

        A_btn_kategori.setBackground(java.awt.Color.darkGray);
        A_btn_kategori.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        A_btn_kategori.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_kategori.setText("KATEGORI BARANG");
        A_btn_kategori.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_kategori.setOpaque(true);
        A_btn_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_kategoriActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 230, 60));

        A_btn_pembelian.setBackground(java.awt.Color.darkGray);
        A_btn_pembelian.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        A_btn_pembelian.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_pembelian.setText("PEMBELIAN BRG");
        A_btn_pembelian.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_pembelian.setOpaque(true);
        A_btn_pembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_pembelianActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_pembelian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 230, 60));

        A_btn_daftar_user.setBackground(java.awt.Color.darkGray);
        A_btn_daftar_user.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        A_btn_daftar_user.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_daftar_user.setText("DAFTAR USER");
        A_btn_daftar_user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_daftar_user.setOpaque(true);
        A_btn_daftar_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_daftar_userActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_daftar_user, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 230, 60));

        A_btn_metode_pembayaran.setBackground(java.awt.Color.darkGray);
        A_btn_metode_pembayaran.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        A_btn_metode_pembayaran.setForeground(new java.awt.Color(255, 255, 255));
        A_btn_metode_pembayaran.setText("METODE PEMBAYARAN");
        A_btn_metode_pembayaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        A_btn_metode_pembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_btn_metode_pembayaranActionPerformed(evt);
            }
        });
        jPanel2.add(A_btn_metode_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 230, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PINDAH TAP HALAMAN");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 200, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("[ ALT + 1,2,3.... }");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 200, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 250, 960));

        A_panel_utama.setBackground(new java.awt.Color(255, 255, 255));
        A_panel_utama.setMinimumSize(new java.awt.Dimension(1650, 940));

        javax.swing.GroupLayout A_panel_utamaLayout = new javax.swing.GroupLayout(A_panel_utama);
        A_panel_utama.setLayout(A_panel_utamaLayout);
        A_panel_utamaLayout.setHorizontalGroup(
                A_panel_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1650, Short.MAX_VALUE));
        A_panel_utamaLayout.setVerticalGroup(
                A_panel_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 940, Short.MAX_VALUE));

        getContentPane().add(A_panel_utama, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 1650, 940));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void A_btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_logoutActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Hapus sesi yang tersimpan
            UserSession.clearSession();

            // Tutup dashboard saat ini
            this.dispose();

            // Buka kembali halaman login
            new login.halaman_login().setVisible(true);
        }
    }// GEN-LAST:event_A_btn_logoutActionPerformed

    private void A_btn_metode_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_metode_pembayaranActionPerformed
        // TODO add your handling code here:
        showMetodePembayaranPanel();
    }// GEN-LAST:event_A_btn_metode_pembayaranActionPerformed

    private void A_btn_berandaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_berandaActionPerformed
        // TODO add your handling code here:
        beranda_admin beranda = new beranda_admin(); // buat instance panel
        showPanel(beranda);
    }// GEN-LAST:event_A_btn_berandaActionPerformed

    private void A_btn_daftar_userActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_daftar_userActionPerformed
        // TODO add your handling code here:
        daftar_user user = new daftar_user(); // buat instance panel
        showPanel(user);
    }// GEN-LAST:event_A_btn_daftar_userActionPerformed

    private void A_btn_lap_keuanganActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_lap_keuanganActionPerformed
        // TODO add your handling code here:
        laporan_keuangan lap_keuangan = new laporan_keuangan(); // buat instance panel
        showPanel(lap_keuangan);
    }// GEN-LAST:event_A_btn_lap_keuanganActionPerformed

    private void A_btn_lap_transaksiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_lap_transaksiActionPerformed
        // TODO add your handling code here:
        laporan_transaksi lap_transaksi = new laporan_transaksi(); // buat instance panel
        showPanel(lap_transaksi);
    }// GEN-LAST:event_A_btn_lap_transaksiActionPerformed

    private void A_btn_lap_pembelianActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_lap_pembelianActionPerformed
        // TODO add your handling code here:
        laporan_pembelian lap_pembelian = new laporan_pembelian(); // buat instance panel
        showPanel(lap_pembelian);
        showLaporanPembelianPanel();
    }// GEN-LAST:event_A_btn_lap_pembelianActionPerformed

    private void A_btn_daftar_brgActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_daftar_brgActionPerformed
        // TODO add your handling code here:
        daftar_barang d_barang = new daftar_barang(); // buat instance panel
        showPanel(d_barang);
        showDaftarBarangPanel();
    }// GEN-LAST:event_A_btn_daftar_brgActionPerformed

    private void A_btn_kategoriActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_kategoriActionPerformed
        // TODO add your handling code here:
        kategori_barang kategori = new kategori_barang(); // buat instance panel
        showPanel(kategori);
    }// GEN-LAST:event_A_btn_kategoriActionPerformed

    private void A_btn_pembelianActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_A_btn_pembelianActionPerformed
        // TODO add your handling code here:
        pembelian_barang pembelian_brg = new pembelian_barang(); // buat instance panel
        showPanel(pembelian_brg);
        showPembelianBarangPanel();
    }// GEN-LAST:event_A_btn_pembelianActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard_admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A_btn_beranda;
    private javax.swing.JButton A_btn_daftar_brg;
    private javax.swing.JButton A_btn_daftar_user;
    private javax.swing.JButton A_btn_kategori;
    private javax.swing.JButton A_btn_lap_keuangan;
    private javax.swing.JButton A_btn_lap_pembelian;
    private javax.swing.JButton A_btn_lap_transaksi;
    private javax.swing.JButton A_btn_logout;
    private javax.swing.JButton A_btn_metode_pembayaran;
    private javax.swing.JButton A_btn_pembelian;
    private javax.swing.JLabel A_label_jabatan;
    private javax.swing.JLabel A_label_username;
    private javax.swing.JPanel A_panel_utama;
    private javax.swing.JLabel A_txt_1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
