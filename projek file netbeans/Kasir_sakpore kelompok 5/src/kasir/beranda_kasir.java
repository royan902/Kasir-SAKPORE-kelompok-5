/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package kasir;

/**
 *
 * @author Acer Aspire Lite 15
 */
import config.Koneksi;
import config.UserSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JTextArea; // Import JTextArea

public class beranda_kasir extends javax.swing.JPanel {

    /**
     * Creates new form beranda_kasir
     */
    private DefaultTableModel transactionTableModel;

    public beranda_kasir() {
        initComponents();
        initPanel();
    }

    private void initPanel() {
        transactionTableModel = (DefaultTableModel) K_tabel_daftar_transaksi.getModel();

        // Atur filter ke bulan dan tahun saat ini secara default
        Calendar now = Calendar.getInstance();
        filter_MonthChooser.setMonth(now.get(Calendar.MONTH));
        filter_YearChooser.setYear(now.get(Calendar.YEAR));

        // Langsung muat data untuk bulan dan tahun saat ini
        applyFilter();
        setupKeyBindings();
    }

    private void loadTransactionData(int year, int month) {
        transactionTableModel.setRowCount(0);
        String kasirId = UserSession.getId();

        String sql = "SELECT id_transaksi, kasir, tanggal, total_akhir, diskon, bayar, kembalian, metode_pembayaran FROM transaksi WHERE id_kasir = ? AND EXTRACT(YEAR FROM tanggal) = ? AND EXTRACT(MONTH FROM tanggal) = ? ORDER BY tanggal DESC";

        try (Connection conn = Koneksi.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kasirId);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ResultSet rs = ps.executeQuery();

            int no = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            while (rs.next()) {
                transactionTableModel.addRow(new Object[] {
                        no++,
                        rs.getString("id_transaksi"),
                        rs.getString("kasir"),
                        dateFormat.format(rs.getTimestamp("tanggal")),
                        rs.getLong("total_akhir"),
                        rs.getInt("diskon"),
                        rs.getLong("bayar"),
                        rs.getLong("kembalian"),
                        rs.getString("metode_pembayaran")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyFilter() {
        int year = filter_YearChooser.getYear();
        int month = filter_MonthChooser.getMonth() + 1; // JMonthChooser 0-indexed (Jan=0)
        loadTransactionData(year, month);
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // -- Shortcut ENTER untuk Transaksi Baru --
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enterKeyStroke, "newTransactionAction");
        actionMap.put("newTransactionAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_transaksi_baru.doClick(); // Simulasikan klik tombol Transaksi Baru
            }
        });

        // -- Shortcut SHIFT + ENTER untuk Lihat Detail --
        KeyStroke shiftEnterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK);
        inputMap.put(shiftEnterKeyStroke, "viewDetailAction");
        actionMap.put("viewDetailAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_lihat_detail.doClick(); // Simulasikan klik tombol Lihat Detail
            }
        });
    }

    // Method untuk mencetak teks struk (SAMA SEPERTI DI KASIR.JAVA)
    private void printReceipt(String receiptText) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                String[] lines = receiptText.split("\n");
                int y = 15;
                int lineHeight = 12;
                for (String line : lines) {
                    g2d.drawString(line, 10, y);
                    y += lineHeight;
                }
                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak struk: " + e.getMessage(), "Error Cetak",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lAP_TRANSAKSI_HARIAN1 = new admin.LAP_TRANSAKSI_HARIAN();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        K_btn_transaksi_baru = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        K_tabel_daftar_transaksi = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        filter_YearChooser = new com.toedter.calendar.JYearChooser();
        filter_MonthChooser = new com.toedter.calendar.JMonthChooser();
        K_btn_refresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        K_btn_lihat_detail = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(1680, 940));
        setPreferredSize(new java.awt.Dimension(1680, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setBackground(new java.awt.Color(0, 0, 102));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("DAFTAR TRANSAKSI BULANAN :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 320, 40));

        K_btn_transaksi_baru.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_transaksi_baru.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        K_btn_transaksi_baru.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_transaksi_baru.setText("TRANSAKSI BARU [ENTER]");
        K_btn_transaksi_baru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_transaksi_baruActionPerformed(evt);
            }
        });
        jPanel1.add(K_btn_transaksi_baru, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 400, 70));

        K_tabel_daftar_transaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        K_tabel_daftar_transaksi.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "NO.", "ID TRANSAKSI", "NAMA KASIR", "TANGGAL", "TOTAL HARGA", "DISKON", "BAYAR", "KEMBALIAN",
                        "METODE PEMBAYARAN"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        K_tabel_daftar_transaksi.setRowHeight(35);
        jScrollPane1.setViewportView(K_tabel_daftar_transaksi);
        if (K_tabel_daftar_transaksi.getColumnModel().getColumnCount() > 0) {
            K_tabel_daftar_transaksi.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 1620, 660));

        jLabel9.setBackground(new java.awt.Color(0, 0, 102));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("BERANDA KASIR");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel9.setOpaque(true);
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1660, 60));

        filter_YearChooser.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.add(filter_YearChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 80, 130, 50));

        filter_MonthChooser.setBackground(new java.awt.Color(102, 102, 102));
        filter_MonthChooser.setForeground(new java.awt.Color(255, 255, 255));
        filter_MonthChooser.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel1.add(filter_MonthChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 80, 180, 50));

        K_btn_refresh.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_refresh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_refresh.setText("REFRESH");
        K_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_refreshActionPerformed(evt);
            }
        });
        jPanel1.add(K_btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 80, 180, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("FILTER :");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 80, 120, 50));

        K_btn_lihat_detail.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_lihat_detail.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_lihat_detail.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_lihat_detail.setText("LIHAT DETAIL [SHIFT + ENTER]");
        K_btn_lihat_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_lihat_detailActionPerformed(evt);
            }
        });
        jPanel1.add(K_btn_lihat_detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 400, 70));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1660, 910));
    }// </editor-fold>//GEN-END:initComponents

    private void K_btn_transaksi_baruActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_transaksi_baruActionPerformed
        // TODO add your handling code here:
        javax.swing.JFrame mainFrame = (javax.swing.JFrame) this.getTopLevelAncestor();

        // Periksa apakah frame tersebut adalah instance dari dashboard_kasir
        if (mainFrame instanceof dashboard_kasir) {
            // Panggil method publik showKasirPanel() dari dashboard_kasir
            ((dashboard_kasir) mainFrame).showKasirPanel();
        }

    }// GEN-LAST:event_K_btn_transaksi_baruActionPerformed

    private void K_btn_lihat_detailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_lihat_detailActionPerformed
        // TODO add your handling code here:
        int selectedRow = K_tabel_daftar_transaksi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih transaksi yang ingin dilihat detailnya!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String transactionId = transactionTableModel.getValueAt(selectedRow, 1).toString();
        StringBuilder receiptText = new StringBuilder(); // Untuk menampung teks struk

        try (Connection conn = Koneksi.getConnection()) {
            // 1. Ambil data utama dari tabel 'transaksi' (termasuk metode bayar)
            String sqlTransaksi = "SELECT * FROM transaksi WHERE id_transaksi = ?";
            String kasirName = "";
            String metodeBayar = "";
            String akunBayar = "";
            String subtotalStr = "0";
            String diskonStr = "0";
            String totalAkhirStr = "0";
            String bayarStr = "0";
            String kembalianStr = "0";
            String tanggalStr = "";

            try (PreparedStatement ps = conn.prepareStatement(sqlTransaksi)) {
                ps.setString(1, transactionId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    kasirName = rs.getString("kasir");
                    metodeBayar = rs.getString("metode_pembayaran");
                    akunBayar = rs.getString("akun_pembayaran") != null ? rs.getString("akun_pembayaran") : "-";
                    subtotalStr = String.valueOf(rs.getLong("subtotal")); // Ambil subtotal
                    diskonStr = String.valueOf(rs.getInt("diskon")); // Ambil diskon
                    totalAkhirStr = String.valueOf(rs.getLong("total_akhir"));
                    bayarStr = String.valueOf(rs.getLong("bayar"));
                    kembalianStr = String.valueOf(rs.getLong("kembalian"));
                    tanggalStr = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(rs.getTimestamp("tanggal"));
                } else {
                    JOptionPane.showMessageDialog(this, "Data transaksi tidak ditemukan!");
                    return;
                }
            }

            // --- Bangun Teks Struk ---
            receiptText.append("          *** DETAIL TRANSAKSI ***\n");
            receiptText.append("----------------------------------------------------\n");
            receiptText.append("No. Transaksi : ").append(transactionId).append("\n");
            receiptText.append("Tanggal       : ").append(tanggalStr).append("\n");
            receiptText.append("Kasir         : ").append(kasirName).append("\n");
            receiptText.append("----------------------------------------------------\n\n");
            receiptText.append(String.format("%-20s %5s %10s %12s\n", "Nama Barang", "Jml", "Harga", "Total"));
            receiptText.append("----------------------------------------------------\n");

            // 2. Ambil daftar barang dari tabel 'detail_transaksi'
            String sqlDetail = "SELECT * FROM detail_transaksi WHERE id_transaksi = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                ps.setString(1, transactionId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String nama = rs.getString("nama_barang");
                    int jumlah = rs.getInt("jumlah_beli");
                    long harga = rs.getLong("harga_jual");
                    long total = rs.getLong("total");
                    receiptText.append(String.format("%-20.20s %5d %10d %12d\n", nama, jumlah, harga, total));
                }
            }

            receiptText.append("----------------------------------------------------\n");
            receiptText.append(String.format("%-37s %12s\n", "Subtotal:", subtotalStr));
            // Hitung nilai diskon
            long subtotalVal = Long.parseLong(subtotalStr);
            long totalAkhirVal = Long.parseLong(totalAkhirStr);
            long diskonNilai = subtotalVal - totalAkhirVal;
            receiptText.append(String.format("%-37s %12s\n", "Diskon (" + diskonStr + "%):", diskonNilai));
            receiptText.append("----------------------------------------------------\n");
            receiptText.append(String.format("%-37s %12s\n", "TOTAL:", totalAkhirStr));
            receiptText.append(String.format("%-37s %12s\n", "Bayar:", bayarStr));
            receiptText.append(String.format("%-37s %12s\n", "Kembalian:", kembalianStr));
            receiptText.append("----------------------------------------------------\n");
            // Tambahkan detail pembayaran
            receiptText.append(String.format("%-37s %12s\n", "Metode Bayar:", metodeBayar));
            receiptText.append(String.format("%-37s %12s\n", "Akun Bayar:", akunBayar));
            receiptText.append("\n----------------------------------------------------\n");
            // --- Akhir Teks Struk ---

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil detail transaksi: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Buat komponen untuk ditampilkan di JOptionPane
        JTextArea textArea = new JTextArea(receiptText.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(480, 320));

        // Definisikan tombol custom
        Object[] options = { "CETAK", "OK" };

        // Tampilkan Option Dialog
        int choice = JOptionPane.showOptionDialog(
                this,
                scrollPane,
                "Detail Transaksi - " + transactionId,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[1]);

        // Cek tombol mana yang ditekan
        if (choice == JOptionPane.YES_OPTION) { // Jika "CETAK"
            printReceipt(receiptText.toString());
        }
    }// GEN-LAST:event_K_btn_lihat_detailActionPerformed

    private void K_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_refreshActionPerformed
        // TODO add your handling code here:
        applyFilter();
    }// GEN-LAST:event_K_btn_refreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton K_btn_lihat_detail;
    private javax.swing.JButton K_btn_refresh;
    private javax.swing.JButton K_btn_transaksi_baru;
    private javax.swing.JTable K_tabel_daftar_transaksi;
    private com.toedter.calendar.JMonthChooser filter_MonthChooser;
    private com.toedter.calendar.JYearChooser filter_YearChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private admin.LAP_TRANSAKSI_HARIAN lAP_TRANSAKSI_HARIAN1;
    // End of variables declaration//GEN-END:variables
}
