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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class kasir extends javax.swing.JPanel {

    /**
     * Creates new form kasir
     */
    private DefaultTableModel productTableModel;
    private DefaultTableModel cartTableModel;
    private int currentStock = 0;

    public kasir() {
        initComponents();
        initForm();
    }

    private void initForm() {
        productTableModel = (DefaultTableModel) K_tabel_daftar_barang.getModel();
        cartTableModel = (DefaultTableModel) K_tabel_daftar_beli.getModel();
        cartTableModel.setRowCount(0);

        // Mengatur field yang nilainya diisi otomatis agar tidak bisa diedit
        K_tf_harga_jual.setEditable(false);
        K_tf_total_harga_beli.setEditable(false);
        K_tf_subtotal.setEditable(false);
        K_tf_total_akhir.setEditable(false);
        K_tf_kembalian.setEditable(false);

        // Menampilkan info kasir dan tanggal
        K_label_username.setText(UserSession.getUsername().toUpperCase());
        K_label_tanggal.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        // Memuat data awal untuk komponen
        loadProductTableData(null);
        loadMetodePembayaran();

        // Listener Tabel Produk
        K_tabel_daftar_barang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateProductSelectionForm();
            }
        });

        // Listener Jumlah Beli (Hanya Validasi & Hitung)
        K_tf_jumlah_beli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateStockAndCalculate();
            }
        });

        // Listener Diskon (Hanya Hitung Total)
        K_tf_diskon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateFinalTotal();
            }
        });

        // Listener Metode Pembayaran
        K_cb_metode_pembayaran.addActionListener(e -> {
            if (K_cb_metode_pembayaran.getSelectedIndex() <= 0 || K_cb_metode_pembayaran.getSelectedItem() == null) {
                K_cb_akun_pembayaran.removeAllItems();
                K_cb_akun_pembayaran.addItem("-- Pilih Akun --");
                K_tf_id_akun_pembayaran.setText("");
                K_tf_id_akun_pembayaran.setEnabled(false);
                return;
            }
            String selectedMethod = K_cb_metode_pembayaran.getSelectedItem().toString();
            loadAkunPembayaranByMetode(selectedMethod);
            if ("TUNAI".equalsIgnoreCase(selectedMethod)) {
                K_tf_id_akun_pembayaran.setText("");
                K_tf_id_akun_pembayaran.setEnabled(false);
                K_cb_akun_pembayaran.setSelectedItem("UANG TUNAI");
            } else {
                K_tf_id_akun_pembayaran.setEnabled(true);
            }
        });

        // Listener Jumlah Bayar (Hanya Hitung Kembalian)
        K_tf_jumlah_bayar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateKembalian();
            }
        });

        // === ACTION LISTENER UNTUK ENTER KEY FLOW ===
        K_tf_pencarian.addActionListener(e -> K_btn_cari.doClick());
        K_tf_id_barang.addActionListener(e -> findItemByIdAndFocusNext());
        K_tf_jumlah_beli.addActionListener(e -> K_btn_simpan.doClick());
        K_tf_diskon.addActionListener(e -> {
            calculateFinalTotal();
            K_tf_jumlah_bayar.requestFocusInWindow();
            K_tf_jumlah_bayar.selectAll();
        });
        K_tf_jumlah_bayar.addActionListener(e -> {
            calculateKembalian();
            K_btn_save_transaksi.doClick();
        });

        // Panggil setupKeyBindings
        setupKeyBindings();

        // Atur nilai default "TUNAI" di akhir
        K_cb_metode_pembayaran.setSelectedItem("TUNAI");
    }

    private void loadMetodePembayaran() {
        try (Connection conn = Koneksi.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT nama_metode FROM metode_pembayaran ORDER BY nama_metode ASC")) {

            K_cb_metode_pembayaran.removeAllItems();
            K_cb_metode_pembayaran.addItem("-- Pilih Metode --");
            while (rs.next()) {
                K_cb_metode_pembayaran.addItem(rs.getString("nama_metode"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat metode pembayaran: " + e.getMessage());
        }
    }

    private void loadAkunPembayaranByMetode(String metode) {
        String sql = "SELECT nama_pembayaran FROM akun_pembayaran WHERE metode_pembayaran = ? ORDER BY nama_pembayaran ASC";
        try (Connection conn = Koneksi.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, metode);
            ResultSet rs = ps.executeQuery();

            K_cb_akun_pembayaran.removeAllItems();
            K_cb_akun_pembayaran.addItem("-- Pilih Akun --");
            while (rs.next()) {
                K_cb_akun_pembayaran.addItem(rs.getString("nama_pembayaran"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat akun pembayaran: " + e.getMessage());
        }
    }

    private void calculateKembalian() {
        try {
            long totalAkhir = Long.parseLong(K_tf_total_akhir.getText());
            long jumlahBayar = Long.parseLong(K_tf_jumlah_bayar.getText());
            long kembalian = jumlahBayar - totalAkhir;
            K_tf_kembalian.setText(String.valueOf(kembalian));
        } catch (NumberFormatException e) {
            K_tf_kembalian.setText("0");
        }
    }

    private void validateStockAndCalculate() {
        try {
            int jumlah = Integer.parseInt(K_tf_jumlah_beli.getText());
            if (jumlah > currentStock) {
                JOptionPane.showMessageDialog(this, "Jumlah beli melebihi stok yang tersedia (" + currentStock + ")!",
                        "Peringatan Stok", JOptionPane.WARNING_MESSAGE);
                K_tf_jumlah_beli.setText("");
                K_tf_total_harga_beli.setText("0");
                return;
            }
            calculateItemTotal();
        } catch (NumberFormatException e) {
            // Biarkan kosong
        }
    }

    private void loadProductTableData(String searchTerm) {
        // Versi ini akan selalu membuat koneksi baru dan menutupnya,
        // cocok untuk pemanggilan normal seperti dari tombol Refresh.
        try (Connection conn = Koneksi.getConnection()) {
            loadProductTableData(searchTerm, conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // BUAT METHOD BARU INI (OVERLOAD)
    private void loadProductTableData(String searchTerm, Connection conn) throws Exception {
        productTableModel.setRowCount(0);
        String sql = "SELECT id_barang, nama_barang, harga_jual, stok FROM daftar_barang WHERE CAST(stok AS INTEGER) > 0";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql += " AND (id_barang ILIKE ? OR nama_barang ILIKE ?)";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String pattern = "%" + searchTerm + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            }
            ResultSet rs = ps.executeQuery();
            int no = 1;
            while (rs.next()) {
                productTableModel.addRow(new Object[] {
                        no++,
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getLong("harga_jual"),
                        rs.getInt("stok")
                });
            }
        }
    }

    private void calculateItemTotal() {
        try {
            long harga = Long.parseLong(K_tf_harga_jual.getText());
            int jumlah = Integer.parseInt(K_tf_jumlah_beli.getText());
            K_tf_total_harga_beli.setText(String.valueOf(harga * jumlah));
        } catch (NumberFormatException e) {
            K_tf_total_harga_beli.setText("0");
        }
    }

    private void calculateSubtotal() {
        long subtotal = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            subtotal += (long) cartTableModel.getValueAt(i, 5);
        }
        K_tf_subtotal.setText(String.valueOf(subtotal));
        calculateFinalTotal();
    }

    private void calculateFinalTotal() {
        try {
            long subtotal = Long.parseLong(K_tf_subtotal.getText());
            String diskonStr = K_tf_diskon.getText().isEmpty() ? "0" : K_tf_diskon.getText();
            double diskonPersen = Double.parseDouble(diskonStr) / 100.0;
            long nilaiDiskon = (long) (subtotal * diskonPersen);
            long totalAkhir = subtotal - nilaiDiskon;
            K_tf_total_akhir.setText(String.valueOf(totalAkhir));

            calculateKembalian();
        } catch (NumberFormatException e) {
            if (!K_tf_subtotal.getText().isEmpty()) {
                K_tf_total_akhir.setText(K_tf_subtotal.getText());
            } else {
                K_tf_total_akhir.setText("0");
            }
        }
    }

    private void clearTopForm() {
        K_tf_id_barang.setText("");
        K_tf_nama_barang.setText("");
        K_tf_harga_jual.setText("");
        K_tf_jumlah_beli.setText("");
        K_tf_total_harga_beli.setText("");
        currentStock = 0;
    }

    private void clearAll() {
        clearTopForm();
        cartTableModel.setRowCount(0);
        K_tf_subtotal.setText("");
        K_tf_diskon.setText("");
        K_tf_total_akhir.setText("");
        K_cb_metode_pembayaran.setSelectedIndex(0);
        K_cb_akun_pembayaran.removeAllItems();
        K_cb_akun_pembayaran.addItem("-- Pilih Akun --");
        K_tf_id_akun_pembayaran.setText("");
        K_tf_jumlah_bayar.setText("");
        K_tf_kembalian.setText("");
    }

    private void showReceipt(String transactionId) {
        StringBuilder receiptText = new StringBuilder(); // Gunakan StringBuilder untuk teks struk

        // Ambil detail pembayaran dari komponen UI
        String metodeBayar = K_cb_metode_pembayaran.getSelectedItem().toString();
        String akunBayar = (K_cb_akun_pembayaran.getSelectedIndex() > 0)
                ? K_cb_akun_pembayaran.getSelectedItem().toString()
                : "-";

        // --- Bangun Teks Struk ---
        receiptText.append("          *** STRUK PEMBAYARAN ***\n");
        receiptText.append("----------------------------------------------------\n");
        receiptText.append("No. Transaksi : ").append(transactionId).append("\n");
        receiptText.append("Tanggal       : ").append(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                .append("\n");
        receiptText.append("Kasir         : ").append(UserSession.getUsername()).append("\n");
        receiptText.append("----------------------------------------------------\n\n");
        receiptText.append(String.format("%-20s %5s %10s %12s\n", "Nama Barang", "Jml", "Harga", "Total"));
        receiptText.append("----------------------------------------------------\n");

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String nama = cartTableModel.getValueAt(i, 2).toString();
            int jumlah = (int) cartTableModel.getValueAt(i, 4);
            long harga = (long) cartTableModel.getValueAt(i, 3);
            long total = (long) cartTableModel.getValueAt(i, 5);
            receiptText.append(String.format("%-20.20s %5d %10d %12d\n", nama, jumlah, harga, total));
        }

        receiptText.append("----------------------------------------------------\n");
        receiptText.append(String.format("%-37s %12s\n", "Subtotal:", K_tf_subtotal.getText()));
        String diskonTampil = K_tf_diskon.getText().isEmpty() ? "0" : K_tf_diskon.getText();
        receiptText.append(String.format("%-37s %12s\n", "Diskon (" + diskonTampil + "%):",
                (Long.parseLong(K_tf_subtotal.getText()) - Long.parseLong(K_tf_total_akhir.getText()))));
        receiptText.append("----------------------------------------------------\n");
        receiptText.append(String.format("%-37s %12s\n", "TOTAL:", K_tf_total_akhir.getText()));
        receiptText.append(String.format("%-37s %12s\n", "Bayar:", K_tf_jumlah_bayar.getText()));
        receiptText.append(String.format("%-37s %12s\n", "Kembalian:", K_tf_kembalian.getText()));
        receiptText.append("----------------------------------------------------\n");
        // Tambahkan detail pembayaran
        receiptText.append(String.format("%-37s %12s\n", "Metode Bayar:", metodeBayar));
        receiptText.append(String.format("%-37s %12s\n", "Akun Bayar:", akunBayar));
        receiptText.append("\n----------------------------------------------------\n");
        receiptText.append("           TERIMA KASIH TELAH BERBELANJA!\n");
        // --- Akhir Teks Struk ---

        // Buat komponen untuk ditampilkan di JOptionPane
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(receiptText.toString());
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        textArea.setEditable(false);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(480, 320));

        // Definisikan tombol custom
        Object[] options = { "CETAK", "OK" };

        // Tampilkan Option Dialog
        int choice = JOptionPane.showOptionDialog(
                this, // Parent component
                scrollPane, // Pesan (panel struk)
                "Transaksi Berhasil - " + transactionId, // Judul
                JOptionPane.YES_NO_OPTION, // Tipe Opsi (YES = tombol pertama, NO = tombol kedua)
                JOptionPane.INFORMATION_MESSAGE, // Tipe Pesan
                null, // Ikon
                options, // Tombol custom
                options[1] // Tombol default (OK)
        );

        // Cek tombol mana yang ditekan
        if (choice == JOptionPane.YES_OPTION) { // Jika tombol "CETAK" (tombol pertama) ditekan
            printReceipt(receiptText.toString()); // Panggil method print
        }
        // Jika tombol "OK" atau menutup dialog, tidak melakukan apa-apa lagi
    }

    private void printReceipt(String receiptText) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                // Hanya cetak satu halaman
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                // Pindahkan titik awal cetak ke area yang bisa dicetak
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // Set font (gunakan Monospaced agar alignment rapi)
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));

                // Pecah teks struk menjadi baris-baris
                String[] lines = receiptText.split("\n");
                int y = 15; // Posisi Y awal
                int lineHeight = 12; // Jarak antar baris

                // Gambar setiap baris teks
                for (String line : lines) {
                    g2d.drawString(line, 10, y);
                    y += lineHeight;
                }

                return PAGE_EXISTS; // Beritahu bahwa halaman ini ada
            }
        });

        // Tampilkan dialog printer (opsional, tapi disarankan)
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                // Lakukan pencetakan
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak struk: " + e.getMessage(), "Error Cetak",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // -- F1 untuk Edit Item Keranjang --
        KeyStroke f1KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        inputMap.put(f1KeyStroke, "editAction");
        actionMap.put("editAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_edit.doClick();
            }
        });

        // -- F2 untuk Refresh Daftar Produk --
        KeyStroke f2KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        inputMap.put(f2KeyStroke, "refreshAction");
        actionMap.put("refreshAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_refresh.doClick();
            }
        });

        // -- F3 untuk Fokus ke Pencarian --
        KeyStroke f3KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
        inputMap.put(f3KeyStroke, "searchFocusAction");
        actionMap.put("searchFocusAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tf_pencarian.requestFocusInWindow();
            }
        });

        // -- F4 untuk Clear Keranjang --
        KeyStroke f4KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        inputMap.put(f4KeyStroke, "clearCartAction");
        actionMap.put("clearCartAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_clear_keranjang.doClick();
            }
        });

        // -- F5 untuk Fokus ke ID Barang --
        KeyStroke f5KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        inputMap.put(f5KeyStroke, "idFocusAction");
        actionMap.put("idFocusAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tf_id_barang.requestFocusInWindow();
                K_tf_id_barang.selectAll();
            }
        });

        // -- Shortcut F6 untuk Fokus ke Diskon --
        KeyStroke f6KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
        inputMap.put(f6KeyStroke, "diskonFocusAction");
        actionMap.put("diskonFocusAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tf_diskon.requestFocusInWindow();
                K_tf_diskon.selectAll();
            }
        });

        // -- Shortcut F7 untuk Fokus ke Jumlah Bayar --
        KeyStroke f7KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
        inputMap.put(f7KeyStroke, "bayarFocusAction");
        actionMap.put("bayarFocusAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tf_jumlah_bayar.requestFocusInWindow();
                K_tf_jumlah_bayar.selectAll();
            }
        });

        // -- F11 untuk Fokus ke Tabel Keranjang & Pilih Baris Pertama --
        KeyStroke f11KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        inputMap.put(f11KeyStroke, "focusCartAction");
        actionMap.put("focusCartAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tabel_daftar_beli.requestFocusInWindow();
                if (K_tabel_daftar_beli.getRowCount() > 0) {
                    K_tabel_daftar_beli.setRowSelectionInterval(0, 0);
                }
            }
        });

        // -- F12 untuk Fokus ke Tabel Produk & Pilih Baris Pertama --
        KeyStroke f12KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0);
        inputMap.put(f12KeyStroke, "focusProductAction");
        actionMap.put("focusProductAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_tabel_daftar_barang.requestFocusInWindow();
                if (K_tabel_daftar_barang.getRowCount() > 0) {
                    K_tabel_daftar_barang.setRowSelectionInterval(0, 0);
                }
            }
        });

        // -- DEL untuk Delete Item Keranjang --
        KeyStroke deleteKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        inputMap.put(deleteKeyStroke, "deleteAction");
        actionMap.put("deleteAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_delete.doClick();
            }
        });

        // -- Shortcut Shift + Enter untuk Simpan Transaksi --
        KeyStroke shiftEnterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK);
        inputMap.put(shiftEnterKeyStroke, "saveTransactionAction");
        actionMap.put("saveTransactionAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_save_transaksi.doClick();
            }
        });

        KeyStroke altEnterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK);
        inputMap.put(altEnterKeyStroke, "saveItemAction");
        actionMap.put("saveItemAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                K_btn_simpan.doClick(); // Simulasikan klik tombol simpan item
            }
        });
    }

    private void findItemByIdAndFocusNext() {
        String idBarangInput = K_tf_id_barang.getText().trim();
        if (idBarangInput.isEmpty()) {
            return; // Jangan lakukan apa-apa jika field kosong
        }

        String sql = "SELECT nama_barang, harga_jual, stok FROM daftar_barang WHERE id_barang = ? AND CAST(stok AS INTEGER) > 0";

        try (Connection conn = Koneksi.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idBarangInput);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Jika barang ditemukan dan stok > 0
                K_tf_nama_barang.setText(rs.getString("nama_barang"));
                K_tf_harga_jual.setText(String.valueOf(rs.getLong("harga_jual")));
                currentStock = rs.getInt("stok"); // Simpan stok
                K_tf_jumlah_beli.setText("1"); // Default jumlah
                calculateItemTotal(); // Hitung total

                // Pindahkan fokus ke field Jumlah Beli
                K_tf_jumlah_beli.requestFocusInWindow();
                K_tf_jumlah_beli.selectAll(); // Pilih semua teks agar mudah diganti

            } else {
                // Jika barang tidak ditemukan atau stok habis
                JOptionPane.showMessageDialog(this,
                        "Barang dengan ID '" + idBarangInput + "' tidak ditemukan atau stok habis!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                clearTopForm(); // Bersihkan form atas
                K_tf_id_barang.requestFocusInWindow(); // Kembalikan fokus ke ID Barang
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error mencari barang: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateProductSelectionForm() {
        int selectedRow = K_tabel_daftar_barang.getSelectedRow();
        // Pastikan ada baris yang valid terpilih
        if (selectedRow != -1 && selectedRow < productTableModel.getRowCount()) {
            K_tf_id_barang.setText(productTableModel.getValueAt(selectedRow, 1).toString());
            K_tf_nama_barang.setText(productTableModel.getValueAt(selectedRow, 2).toString());
            K_tf_harga_jual.setText(productTableModel.getValueAt(selectedRow, 3).toString());
            currentStock = (int) productTableModel.getValueAt(selectedRow, 4);

            K_tf_jumlah_beli.setText("1"); // Reset jumlah ke 1 setiap ganti barang
            calculateItemTotal();
        } else {
            // Jika tidak ada baris valid yang dipilih (misal setelah refresh), kosongkan
            // form
            clearTopForm();
        }
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btn_petunjuk_shortcut = new javax.swing.JButton();
        K_label_username = new javax.swing.JLabel();
        K_tf_id_barang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        K_tf_nama_barang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        K_tf_harga_jual = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        K_btn_simpan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        K_tf_jumlah_beli = new javax.swing.JTextField();
        K_tf_total_harga_beli = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        K_tf_pencarian = new javax.swing.JTextField();
        K_btn_refresh = new javax.swing.JButton();
        K_btn_cari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        K_tabel_daftar_barang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        K_btn_delete = new javax.swing.JButton();
        K_btn_edit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        K_tabel_daftar_beli = new javax.swing.JTable();
        K_btn_save_transaksi = new javax.swing.JButton();
        K_label_tanggal = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        K_tf_total_akhir = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        K_tf_diskon = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        K_tf_subtotal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        K_tf_id_akun_pembayaran = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        K_tf_jumlah_bayar = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        K_tf_kembalian = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        K_btn_clear_keranjang = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        K_cb_metode_pembayaran = new javax.swing.JComboBox<>();
        K_cb_akun_pembayaran = new javax.swing.JComboBox<>();

        jTextField2.setText("jTextField2");

        setMinimumSize(new java.awt.Dimension(1680, 940));
        setPreferredSize(new java.awt.Dimension(1680, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_petunjuk_shortcut.setBackground(new java.awt.Color(0, 51, 153));
        btn_petunjuk_shortcut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_petunjuk_shortcut.setForeground(new java.awt.Color(255, 255, 255));
        btn_petunjuk_shortcut.setText("PETUNJUK SHORTCUT KASIR");
        btn_petunjuk_shortcut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_petunjuk_shortcutActionPerformed(evt);
            }
        });
        jPanel1.add(btn_petunjuk_shortcut, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 240, 30));

        K_label_username.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_label_username.setForeground(new java.awt.Color(255, 255, 255));
        K_label_username.setText("KASIR");
        jPanel1.add(K_label_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 200, 30));

        K_tf_id_barang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel1.add(K_tf_id_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 240, 40));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("ID BRG :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 40));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("KASIR :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, 30));

        K_tf_nama_barang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_nama_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_nama_barangActionPerformed(evt);
            }
        });
        jPanel1.add(K_tf_nama_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 240, 40));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("NAMA BRG :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, 40));

        K_tf_harga_jual.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_harga_jual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_harga_jualActionPerformed(evt);
            }
        });
        jPanel1.add(K_tf_harga_jual, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 240, 40));

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("HARGA :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 40));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("JUMLAH BELI :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 140, 40));

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("TOTAL HARGA :");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 140, 40));

        K_btn_simpan.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        K_btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_simpan.setText("SIMPAN [ENTER]");
        K_btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_simpanActionPerformed(evt);
            }
        });
        jPanel1.add(K_btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 60, 280, 90));

        jLabel7.setBackground(new java.awt.Color(0, 0, 102));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("TRANSAKSI");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel7.setOpaque(true);
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 50));

        K_tf_jumlah_beli.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel1.add(K_tf_jumlah_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, 240, 40));

        K_tf_total_harga_beli.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_total_harga_beli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_total_harga_beliActionPerformed(evt);
            }
        });
        jPanel1.add(K_tf_total_harga_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 240, 40));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1100, 210));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        K_tf_pencarian.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        K_tf_pencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_pencarianActionPerformed(evt);
            }
        });
        jPanel2.add(K_tf_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 250, 40));

        K_btn_refresh.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_refresh.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_refresh.setText("REFRESH[F2]");
        K_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_refreshActionPerformed(evt);
            }
        });
        jPanel2.add(K_btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 140, 40));

        K_btn_cari.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_cari.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_cari.setText("CARI");
        K_btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_cariActionPerformed(evt);
            }
        });
        jPanel2.add(K_btn_cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 110, 40));

        K_tabel_daftar_barang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        K_tabel_daftar_barang.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "NO.", "ID", "NAMA BRG", "HARGA JUAL", "STOK"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        K_tabel_daftar_barang.setRowHeight(35);
        jScrollPane1.setViewportView(K_tabel_daftar_barang);
        if (K_tabel_daftar_barang.getColumnModel().getColumnCount() > 0) {
            K_tabel_daftar_barang.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 530, 730));

        jLabel2.setBackground(new java.awt.Color(0, 0, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("DAFTAR BARANG TERSEDIA");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jLabel2.setOpaque(true);
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TABEL DAFTAR BARANG");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 530, 30));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 10, 550, 920));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        K_btn_delete.setBackground(new java.awt.Color(153, 0, 51));
        K_btn_delete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_delete.setText("DELETE [DEL]");
        K_btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_deleteActionPerformed(evt);
            }
        });
        jPanel3.add(K_btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 200, 40));

        K_btn_edit.setBackground(new java.awt.Color(102, 102, 102));
        K_btn_edit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_edit.setText("EDIT [F1]");
        K_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_editActionPerformed(evt);
            }
        });
        jPanel3.add(K_btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 200, 40));

        K_tabel_daftar_beli.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        K_tabel_daftar_beli.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "NO.", "ID BARANG", "NAMA BARANG", "HARGA", "JUMLAH BELI", "TOTAL HARGA"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        K_tabel_daftar_beli.setRowHeight(30);
        jScrollPane2.setViewportView(K_tabel_daftar_beli);
        if (K_tabel_daftar_beli.getColumnModel().getColumnCount() > 0) {
            K_tabel_daftar_beli.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1080, 380));

        K_btn_save_transaksi.setBackground(new java.awt.Color(51, 204, 0));
        K_btn_save_transaksi.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        K_btn_save_transaksi.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_save_transaksi.setText("SAVE TRANSAKSI [SHIFT + ENTER]");
        K_btn_save_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_save_transaksiActionPerformed(evt);
            }
        });
        jPanel3.add(K_btn_save_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 610, 1020, 60));

        K_label_tanggal.setBackground(new java.awt.Color(0, 0, 0));
        K_label_tanggal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_label_tanggal.setForeground(new java.awt.Color(0, 0, 0));
        K_label_tanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        K_label_tanggal.setText("10/10/2025");
        K_label_tanggal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(K_label_tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 240, 40));

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("TANGGAL :");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, 40));

        K_tf_total_akhir.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_total_akhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_total_akhirActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_total_akhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 560, 200, 40));

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("TOTAL :");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 120, 40));

        K_tf_diskon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_diskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_diskonActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_diskon, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 510, 200, 40));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("DISKON [F6] :");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 120, 40));

        K_tf_subtotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_subtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_subtotalActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, 200, 40));

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("SUB TOTAL :");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 120, 40));

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("METODE PEMBAYARAN :");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 460, 180, 40));

        K_tf_id_akun_pembayaran.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_id_akun_pembayaran.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                K_tf_id_akun_pembayaranFocusLost(evt);
            }
        });
        K_tf_id_akun_pembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_id_akun_pembayaranActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_id_akun_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 560, 220, 40));

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("ID AKUN PEMBAYARAN :");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 560, 180, 40));

        K_tf_jumlah_bayar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_jumlah_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_jumlah_bayarActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_jumlah_bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 460, 200, 40));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("BAYAR [F7] :");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 460, 120, 40));

        K_tf_kembalian.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_tf_kembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_tf_kembalianActionPerformed(evt);
            }
        });
        jPanel3.add(K_tf_kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 510, 200, 40));

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("KEMBALIAN :");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 510, 120, 40));

        K_btn_clear_keranjang.setBackground(new java.awt.Color(153, 0, 51));
        K_btn_clear_keranjang.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        K_btn_clear_keranjang.setForeground(new java.awt.Color(255, 255, 255));
        K_btn_clear_keranjang.setText("CLEAR KERANJANG [F4]");
        K_btn_clear_keranjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_btn_clear_keranjangActionPerformed(evt);
            }
        });
        jPanel3.add(K_btn_clear_keranjang, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, 240, 40));

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("PEMBAYARAN MELALUI :");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 510, 180, 40));

        K_cb_metode_pembayaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        K_cb_metode_pembayaran.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        K_cb_metode_pembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_cb_metode_pembayaranActionPerformed(evt);
            }
        });
        jPanel3.add(K_cb_metode_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 460, 220, 40));

        K_cb_akun_pembayaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        K_cb_akun_pembayaran.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        K_cb_akun_pembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                K_cb_akun_pembayaranActionPerformed(evt);
            }
        });
        jPanel3.add(K_cb_akun_pembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 510, 220, 40));

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 1100, 700));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_petunjuk_shortcutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_petunjuk_shortcutActionPerformed
        // TODO add your handling code here:
        String helpText = "PETUNJUK SHORTCUT KASIR\n\n" +
                "- F1 = Edit item terpilih di keranjang.\n" +
                "- F2 = Refresh daftar produk.\n" +
                "- F3 = Fokus ke field Pencarian Produk (lalu Enter untuk mencari).\n" +
                "- F4 = Kosongkan Keranjang (Clear Cart).\n" +
                "- F5 = Fokus ke field ID Barang (untuk input manual).\n" +
                "- F6 = Fokus ke field Diskon.\n" +
                "- F7 = Fokus ke field Jumlah Bayar.\n" +
                "- F11 = Fokus ke tabel Keranjang (gunakan panah/Enter untuk memilih).\n" +
                "- F12 = Fokus ke tabel Daftar Produk (gunakan panah/Enter untuk memilih).\n" +
                "- DEL = Hapus item terpilih dari keranjang.\n" +
                "- ENTER (di ID Barang) = Cari barang & pindah ke Jumlah Beli.\n" +
                "- ENTER (di Jumlah Beli) = Simpan item ke keranjang.\n" +
                "- ENTER (di Diskon) = Pindah ke Jumlah Bayar.\n" +
                "- ENTER (di Jumlah Bayar) = Simpan Transaksi.\n" +
                "- ALT + ENTER = Simpan item ke keranjang.\n" +
                "- SHIFT + ENTER = Simpan Transaksi.";

        // Gunakan JTextArea agar teks bisa di-scroll jika panjang
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setColumns(50); // Sesuaikan lebar
        textArea.setRows(18); // Sesuaikan tinggi
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);

        // Tampilkan pop-up
        JOptionPane.showMessageDialog(this, scrollPane, "Petunjuk Shortcut", JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_btn_petunjuk_shortcutActionPerformed

    private void K_tf_pencarianActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_pencarianActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_pencarianActionPerformed

    private void K_cb_metode_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_cb_metode_pembayaranActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_cb_metode_pembayaranActionPerformed

    private void K_cb_akun_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_cb_akun_pembayaranActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_cb_akun_pembayaranActionPerformed

    private void K_tf_id_akun_pembayaranFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_K_tf_id_akun_pembayaranFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_id_akun_pembayaranFocusLost

    private void K_btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_deleteActionPerformed
        // TODO add your handling code here:
        int selectedRow = K_tabel_daftar_beli.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang di keranjang yang ingin dihapus!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus barang ini dari keranjang?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartTableModel.removeRow(selectedRow);
            calculateSubtotal(); // Hitung ulang subtotal
        }
    }// GEN-LAST:event_K_btn_deleteActionPerformed

    private void K_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_refreshActionPerformed
        // TODO add your handling code here:
        K_tf_pencarian.setText("");
        loadProductTableData(null);
    }// GEN-LAST:event_K_btn_refreshActionPerformed

    private void K_tf_nama_barangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_nama_barangActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_nama_barangActionPerformed

    private void K_tf_harga_jualActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_harga_jualActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_harga_jualActionPerformed

    private void K_btn_scanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_scanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_btn_scanActionPerformed

    private void K_btn_editActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_editActionPerformed
        // TODO add your handling code here:
        int selectedRow = K_tabel_daftar_beli.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang di keranjang yang ingin diedit!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newQtyStr = JOptionPane.showInputDialog(this, "Masukkan jumlah baru:",
                cartTableModel.getValueAt(selectedRow, 4));

        if (newQtyStr != null) { // User tidak menekan tombol cancel
            try {
                int newQty = Integer.parseInt(newQtyStr);
                if (newQty <= 0) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Cari stok asli dari barang yang diedit di tabel produk
                String idBarang = cartTableModel.getValueAt(selectedRow, 1).toString();
                int originalStock = 0;
                for (int i = 0; i < productTableModel.getRowCount(); i++) {
                    if (idBarang.equals(productTableModel.getValueAt(i, 1).toString())) {
                        originalStock = (int) productTableModel.getValueAt(i, 4);
                        break;
                    }
                }

                // Validasi stok saat edit
                if (newQty > originalStock) {
                    JOptionPane.showMessageDialog(this,
                            "Jumlah baru melebihi stok yang tersedia (" + originalStock + ")!", "Peringatan Stok",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Jika valid, update jumlah dan total harga di keranjang
                long harga = (long) cartTableModel.getValueAt(selectedRow, 3);
                cartTableModel.setValueAt(newQty, selectedRow, 4);
                cartTableModel.setValueAt(harga * newQty, selectedRow, 5);

                calculateSubtotal(); // Hitung ulang subtotal

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Input jumlah tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }// GEN-LAST:event_K_btn_editActionPerformed

    private void K_tf_total_harga_beliActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_total_harga_beliActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_total_harga_beliActionPerformed

    private void K_btn_save_transaksiActionPerformed(java.awt.event.ActionEvent evt) {
        // === 1. Validasi Awal Sebelum Memulai Transaksi ===
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (K_tf_jumlah_bayar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah bayar harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            long kembalian = Long.parseLong(K_tf_kembalian.getText());
            if (kembalian < 0) {
                JOptionPane.showMessageDialog(this, "Jumlah bayar kurang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah bayar tidak valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Deklarasikan semua resource di luar try-catch agar bisa diakses di 'finally'
        Connection conn = null;
        PreparedStatement psTransaksi = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateStok = null;

        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false); // Memulai mode transaksi database

            // === 2. Menyimpan Data Utama ke Tabel 'transaksi' ===
            String sqlTransaksi = "INSERT INTO transaksi (id_transaksi, tanggal, kasir, id_kasir, jumlah_beli, subtotal, diskon, total_akhir, bayar, kembalian, metode_pembayaran, akun_pembayaran) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            psTransaksi = conn.prepareStatement(sqlTransaksi);

            String idTransaksi = "TR-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            int totalItem = 0;
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                totalItem += (int) cartTableModel.getValueAt(i, 4);
            }

            psTransaksi.setString(1, idTransaksi);
            psTransaksi.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            psTransaksi.setString(3, UserSession.getUsername());
            psTransaksi.setString(4, UserSession.getId());
            psTransaksi.setInt(5, totalItem);
            psTransaksi.setLong(6, Long.parseLong(K_tf_subtotal.getText()));
            String diskonStr = K_tf_diskon.getText().isEmpty() ? "0" : K_tf_diskon.getText();
            psTransaksi.setInt(7, Integer.parseInt(diskonStr));
            psTransaksi.setLong(8, Long.parseLong(K_tf_total_akhir.getText()));
            psTransaksi.setLong(9, Long.parseLong(K_tf_jumlah_bayar.getText()));
            psTransaksi.setLong(10, Long.parseLong(K_tf_kembalian.getText()));
            psTransaksi.setString(11, K_cb_metode_pembayaran.getSelectedItem().toString());
            String akun = K_cb_akun_pembayaran.getSelectedIndex() > 0
                    ? K_cb_akun_pembayaran.getSelectedItem().toString()
                    : "-";
            psTransaksi.setString(12, akun);
            psTransaksi.executeUpdate();

            // === 3. Menyimpan Setiap Item di Keranjang ke Tabel 'detail_transaksi' &
            // Mengurangi Stok ===
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_barang, nama_barang, harga_jual, jumlah_beli, total) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlUpdateStok = "UPDATE daftar_barang SET stok = CAST(stok AS INTEGER) - ? WHERE id_barang = ?";

            psDetail = conn.prepareStatement(sqlDetail);
            psUpdateStok = conn.prepareStatement(sqlUpdateStok);

            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String idBarang = cartTableModel.getValueAt(i, 1).toString();
                String namaBarang = cartTableModel.getValueAt(i, 2).toString();
                long hargaJual = (long) cartTableModel.getValueAt(i, 3);
                int jumlahBeli = (int) cartTableModel.getValueAt(i, 4);
                long totalHarga = (long) cartTableModel.getValueAt(i, 5);

                // Menyiapkan data untuk tabel detail_transaksi
                psDetail.setString(1, idTransaksi);
                psDetail.setString(2, idBarang);
                psDetail.setString(3, namaBarang);
                psDetail.setLong(4, hargaJual);
                psDetail.setInt(5, jumlahBeli);
                psDetail.setLong(6, totalHarga);
                psDetail.addBatch(); // Tambahkan ke 'antrian' eksekusi

                // Menyiapkan data untuk mengurangi stok
                psUpdateStok.setInt(1, jumlahBeli);
                psUpdateStok.setString(2, idBarang);
                psUpdateStok.addBatch(); // Tambahkan ke 'antrian' eksekusi
            }
            psDetail.executeBatch(); // Eksekusi semua insert ke detail_transaksi sekaligus
            psUpdateStok.executeBatch(); // Eksekusi semua update stok sekaligus

            conn.commit(); // Jika semua proses di atas berhasil, simpan perubahan secara permanen

            // === 4. Tampilkan Struk dan Bersihkan Form untuk Transaksi Berikutnya ===
            showReceipt(idTransaksi);
            clearAll();
            loadProductTableData(null, conn);

        } catch (Exception e) {
            try {
                if (conn != null)
                    conn.rollback(); // Jika terjadi error di mana pun, batalkan semua perubahan
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage(), "Error Transaksi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // === 5. Tutup Semua Resource Database Secara Manual untuk Mencegah Error ===
            try {
                if (psTransaksi != null)
                    psTransaksi.close();
            } catch (Exception e) {
            }
            try {
                if (psDetail != null)
                    psDetail.close();
            } catch (Exception e) {
            }
            try {
                if (psUpdateStok != null)
                    psUpdateStok.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Kembalikan koneksi ke mode normal
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }// GEN-LAST:event_K_btn_save_transaksiActionPerformed

    private void K_tf_total_akhirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_total_akhirActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_total_akhirActionPerformed

    private void K_tf_diskonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_diskonActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_diskonActionPerformed

    private void K_tf_subtotalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_subtotalActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_subtotalActionPerformed

    private void K_tf_metode_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_metode_pembayaranActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_metode_pembayaranActionPerformed

    private void K_tf_id_akun_pembayaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_id_akun_pembayaranActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_id_akun_pembayaranActionPerformed

    private void K_tf_jumlah_bayarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_jumlah_bayarActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_jumlah_bayarActionPerformed

    private void K_tf_kembalianActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_kembalianActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_kembalianActionPerformed

    private void K_btn_clear_keranjangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_clear_keranjangActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin mengosongkan keranjang?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartTableModel.setRowCount(0);
            calculateSubtotal(); // Ini akan mengupdate subtotal, diskon, dan total akhir menjadi 0
        }
    }// GEN-LAST:event_K_btn_clear_keranjangActionPerformed

    private void K_tf_akun_pembayaran1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_tf_akun_pembayaran1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_K_tf_akun_pembayaran1ActionPerformed

    private void K_btn_cariActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_cariActionPerformed
        // TODO add your handling code here:
        loadProductTableData(K_tf_pencarian.getText());
    }// GEN-LAST:event_K_btn_cariActionPerformed

    private void K_btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_K_btn_simpanActionPerformed
        // TODO add your handling code here:
        String idBarang = K_tf_id_barang.getText();

        // Validasi: Pastikan ada barang yang dipilih
        if (idBarang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih barang terlebih dahulu!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int jumlah = Integer.parseInt(K_tf_jumlah_beli.getText());

            // Validasi: Pastikan jumlah beli valid
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah beli harus lebih dari 0!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi stok sebelum menambahkan ke keranjang
            if (jumlah > currentStock) {
                JOptionPane.showMessageDialog(this, "Jumlah beli melebihi stok yang tersedia (" + currentStock + ")!",
                        "Peringatan Stok", JOptionPane.WARNING_MESSAGE);
                return;
            }

            long harga = Long.parseLong(K_tf_harga_jual.getText());
            long total = Long.parseLong(K_tf_total_harga_beli.getText());
            String namaBarang = K_tf_nama_barang.getText();

            boolean itemExists = false;
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                if (idBarang.equals(cartTableModel.getValueAt(i, 1))) {
                    int currentQty = (int) cartTableModel.getValueAt(i, 4);

                    // Cek stok lagi untuk jumlah gabungan
                    if (currentQty + jumlah > currentStock) {
                        JOptionPane.showMessageDialog(this,
                                "Jumlah total di keranjang akan melebihi stok (" + currentStock + ")!",
                                "Peringatan Stok", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Jika stok cukup, update jumlah dan total harga
                    long currentTotal = (long) cartTableModel.getValueAt(i, 5);
                    cartTableModel.setValueAt(currentQty + jumlah, i, 4);
                    cartTableModel.setValueAt(currentTotal + total, i, 5);

                    itemExists = true;
                    break;
                }
            }

            // Jika barang belum ada di keranjang, tambahkan baris baru
            if (!itemExists) {
                cartTableModel.addRow(new Object[] {
                        cartTableModel.getRowCount() + 1,
                        idBarang,
                        namaBarang,
                        harga,
                        jumlah,
                        total
                });
            }

            calculateSubtotal(); // Hitung ulang subtotal setelah ada perubahan
            clearTopForm(); // Bersihkan form atas untuk input selanjutnya

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah beli tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_K_btn_simpanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton K_btn_cari;
    private javax.swing.JButton K_btn_clear_keranjang;
    private javax.swing.JButton K_btn_delete;
    private javax.swing.JButton K_btn_edit;
    private javax.swing.JButton K_btn_refresh;
    private javax.swing.JButton K_btn_save_transaksi;
    private javax.swing.JButton K_btn_simpan;
    private javax.swing.JComboBox<String> K_cb_akun_pembayaran;
    private javax.swing.JComboBox<String> K_cb_metode_pembayaran;
    private javax.swing.JLabel K_label_tanggal;
    private javax.swing.JLabel K_label_username;
    private javax.swing.JTable K_tabel_daftar_barang;
    private javax.swing.JTable K_tabel_daftar_beli;
    private javax.swing.JTextField K_tf_diskon;
    private javax.swing.JTextField K_tf_harga_jual;
    private javax.swing.JTextField K_tf_id_akun_pembayaran;
    private javax.swing.JTextField K_tf_id_barang;
    private javax.swing.JTextField K_tf_jumlah_bayar;
    private javax.swing.JTextField K_tf_jumlah_beli;
    private javax.swing.JTextField K_tf_kembalian;
    private javax.swing.JTextField K_tf_nama_barang;
    private javax.swing.JTextField K_tf_pencarian;
    private javax.swing.JTextField K_tf_subtotal;
    private javax.swing.JTextField K_tf_total_akhir;
    private javax.swing.JTextField K_tf_total_harga_beli;
    private javax.swing.JButton btn_petunjuk_shortcut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
