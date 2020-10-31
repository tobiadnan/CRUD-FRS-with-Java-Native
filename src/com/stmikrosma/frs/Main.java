package com.stmikrosma.frs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;
import static java.lang.String.format;
import static java.lang.System.*;

public class Main {

    // paramter JDBC untuk koneksi ke datbase
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/myfrs";
    static final String USER = "root";
    static final String PASS = "";

    //  objek untuk mengelola database
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    static BufferedReader input = new BufferedReader(inputStreamReader);


    public static void main(String[] args) throws IOException, SQLException {
        Scanner terminalInput = new Scanner(in);
        String pilihanUser,namaDepan,namaBelakang;

        out.println("\n.......................................");
        out.print("Masukan Nama Depan kamu    : ");
        namaDepan = terminalInput.nextLine();
        out.print("Masukan Nama Belakang kamu : ");
        namaBelakang = terminalInput.nextLine();
        out.println(".......................................");
        

        var isLanjutkan = true;
        while(isLanjutkan) {
            out.println(".......................................");
            out.println("...           DAFTAR  MENU          ...");
            out.println(".......................................");
            out.println("...         [1] Tambah data         ...");
            out.println("...         [2] Update data         ...");
            out.println("...         [3] Hapus data          ...");
            out.println("...         [4] Lihat data          ...");
            out.println("...         [5] Cari data           ...");
            out.println("...         [0] KELUAR              ...");
            out.println(".......................................\n");
            out.println("Hai "+namaDepan+", Selamat datang di Aplikasi MYFRS :)");
            out.println("...............................................");
            out.print("Silahkan masukan pilihan Kamu : ");
            pilihanUser = terminalInput.next();
            out.println("................................");

            if ("1".equals(pilihanUser)) {
                out.println("..................................");
                out.println("Selamat datang di Menu Tambah Data");
                out.println("..................................\n");
                tampilkanDataFrs(false);
                tambahDataFrs();
            } else if ("2".equals(pilihanUser)) {
                out.println("..................................");
                out.println("Selamat datang di Menu Update Data");
                out.println("..................................\n");
                updateDataFrs();
            } else if ("3".equals(pilihanUser)) {
                out.println(".................................");
                out.println("Selamat datang di Menu Hapus Data");
                out.println(".................................\n");
                deleteDataFrs();
            } else if ("4".equals(pilihanUser)) {
                out.println("...................................................................................");
                out.println("Selamat datang di Menu Tampilkan Data "+namaDepan+", ini adalah list seluruh data");
                out.println("...................................................................................\n");
                tampilkanDataFrs(true);
            } else if ("5".equals(pilihanUser)) {
                out.println("................................");
                out.println("Selamat datang di Menu Cari Data");
                out.println("................................\n");

                cariDataFrs();
            }else if ("0".equals(pilihanUser)) {
                out.println("\nTerimakasih "+namaDepan+" "+namaBelakang+", Sampai Jumpa lagi :)");

                exit(0);
            } else {
                err.println("INPUT TIDAK SESUAI\nSILAHKAN MASUKAN [Y] UNTUK MELANJUTKAN" +
                        "\nKEMUDIAN MASUKAN ANGKA SESUAI PILIHANMU\n");
            }
            isLanjutkan = getYesOrNo("Apakah Kamu mau Melanjutkan ");
        }
    }
    private static void tambahDataFrs() {
        out.println("Harap Isi setiap data dengan Benar dan teliti !");
        try {
            // ambil input dari user
            out.print("\n\nMasukan NIM, (Cth, 201901251039) : ");
            String nim = input.readLine().trim();
            out.print("Masukan Nama \t\t\t\t\t : ");
            String nama = input.readLine().trim();
            out.print("Masukan Program Study \t\t\t : ");
            String prodi = input.readLine().trim();
            out.print("Kelas Reguler/Karyawan \t\t\t : ");
            String kelas = input.readLine().trim();
            out.print("Masukan No. HP (0859xxxxxxxx)\t : ");
            String noHp = input.readLine().trim();
            out.println("Note : Maksimal 5 Matakuliah");
            out.println("       Awali dengan bintang (*) Sebelum memilih satu Matakuliah");
            out.print("Masukan Mata Kuliah \t\t\t : ");
            String matkul = input.readLine().trim();

                out.println("\nBerikut data yang akan Kamu tambahkan");
                out.println("===================================");
                out.println("\tNIM\t\t\t\t: " + nim);
                out.println("\tNama\t\t\t: " + nama);
                out.println("\tProgram Study   : " + prodi);
                out.println("\tKelas\t\t\t: " + kelas);
                out.println("\tNo. HP\t\t\t: " + noHp);
                out.println("\tMata Kuliah\t\t: " + matkul);
                out.println("===================================");

                var tambah = getYesOrNo("Apakah Kamu yakin Ingin Menambahkan data ?");
                //menulis ke database
                if (tambah) {
                    String sql = "INSERT into tbdata (`nim`, `nama`, `prodi`, `kelas`, `noHp`, `matkul`) VALUES('%s', '%s', '%s', '%s', '%s', '%s')";
                    sql = format(sql,nim,nama,prodi,kelas,noHp,matkul);
                    stmt.execute(sql);
                    out.println("\nData Berhasil Ditambahkan\n");

                    var lihatData = getYesOrNo("Apakah Kamu Ingin Melihat Data ? ");
                    if (lihatData) {
                        tampilkanDataFrs(false);
                    }
                }else {
                    err.println("data tidak ditambahkan");
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void updateDataFrs() throws IOException, SQLException {
        tampilkanDataFrs(false);
        out.println();
        // ambil input dari user
        System.out.print("Masukan Nomor data yang akan anda update : ");
        int id_mhs = Integer.parseInt(input.readLine());
        out.println();
        border();
        border();
        header();
        border();
        // Melakukan koneksi ke database
        try {
            String sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
            sql = format(sql, id_mhs);
            // eksekusi query dan simpan hasilnya di obj ResultSet
            rs = stmt.executeQuery(sql);

            // tampilkan hasil query
            while (rs.next()) {
                out.printf("  %-3d", rs.getInt("id_mhs"));
                out.printf("| %-2s ", rs.getString("nim"));
                out.printf("| %-17s ", rs.getString("nama"));
                out.printf("| %-23s ", rs.getString("prodi"));
                out.printf("| %-13s ", rs.getString("kelas"));
                out.printf("| %-15s ", rs.getString("noHp"));
                out.printf("| %-17s ", rs.getString("matkul"));
                out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        border();
        border();
        out.println();
            out.println("\nData mana yang akan kamu update ?");
            out.println(".................................");
            out.println("\t(1)\tNIM");
            out.println("\t(2)\tNama");
            out.println("\t(3)\tProgram Study");
            out.println("\t(4)\tKelas");
            out.println("\t(5)\tNo. HP");
            out.println("\t(6)\tMata Kuliah");
            out.println("\t(0)\tBatalkan");
            out.println(".................................");
            out.print("Silahkan Masukan angka di atas ! >");
            Scanner terminalInput = new Scanner(System.in);
            String inputUser = terminalInput.next();
            if ("1".equals(inputUser)){
                out.print("\n\nMasukan NIM Baru, (Cth, 201901251039) : ");
                String nim = input.readLine().trim();
                String sql = "UPDATE tbdata SET nim='%s' WHERE id_mhs=%d";
                sql = String.format(sql, nim,  id_mhs);
                stmt.execute(sql);
                out.println("\nNIM anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("2".equals(inputUser)){
                out.print("Masukan Nama Baru\t\t\t\t\t : ");
                String nama = input.readLine().trim();
                String sql = "UPDATE tbdata SET nama='%s' WHERE id_mhs=%d";
                sql = String.format(sql, nama,  id_mhs);
                stmt.execute(sql);
                out.println("\nNama anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("3".equals(inputUser)){
                out.print("Masukan Program Study Baru\t\t\t\t\t : ");
                String prodi = input.readLine().trim();
                String sql = "UPDATE tbdata SET prodi='%s' WHERE id_mhs=%d";
                sql = String.format(sql, prodi,  id_mhs);
                stmt.execute(sql);
                out.println("\nProgram Study anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("4".equals(inputUser)){
                out.print("Masukan Kelas Baru\t\t\t\t\t : ");
                String kelas = input.readLine().trim();
                String sql = "UPDATE tbdata SET kelas='%s' WHERE id_mhs=%d";
                sql = String.format(sql, kelas,  id_mhs);
                stmt.execute(sql);
                out.println("\nKelas anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("5".equals(inputUser)){
                out.print("Masukan No. HP Baru\t\t\t\t\t : ");
                String noHp = input.readLine().trim();
                String sql = "UPDATE tbdata SET noHp='%s' WHERE id_mhs=%d";
                sql = String.format(sql, noHp,  id_mhs);
                stmt.execute(sql);
                out.println("\nNo HP anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("6".equals(inputUser)){
                out.print("Masukan Mata Kuliah Baru\t\t\t\t\t : ");
                String matkul = input.readLine().trim();
                String sql = "UPDATE tbdata SET matkul='%s' WHERE id_mhs=%d";
                sql = String.format(sql, matkul,  id_mhs);
                stmt.execute(sql);
                out.println("\nMata Kuliah anda Berhasil di update");
                try {
                    sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                    sql = format(sql, id_mhs);
                    // eksekusi query dan simpan hasilnya di obj ResultSet
                    rs = stmt.executeQuery(sql);
                    border();
                    header();
                    border();
                    // tampilkan hasil query
                    while (rs.next()) {
                        out.printf("  %-3d", rs.getInt("id_mhs"));
                        out.printf("| %-2s ", rs.getString("nim"));
                        out.printf("| %-17s ", rs.getString("nama"));
                        out.printf("| %-23s ", rs.getString("prodi"));
                        out.printf("| %-13s ", rs.getString("kelas"));
                        out.printf("| %-15s ", rs.getString("noHp"));
                        out.printf("| %-17s ", rs.getString("matkul"));
                        out.println();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                border();
                reUpdate();
            }else if ("0".equals(inputUser)) {
                err.println("Anda membatalkan Update");
            }else {
                err.println("Input Yang anda masukan salah !!!");
            }
    }
    private static void deleteDataFrs() {
        try {
            tampilkanDataFrs(false);
            out.println();
            // ambil input dari user
            System.out.print("Masukan Nomor data yang akan anda hapus : ");
            int id_mhs = Integer.parseInt(input.readLine());
            out.println();
            border();
            border();
            header();
            border();
            // Melakukan koneksi ke database
            try {
                String sql = "SELECT * FROM tbdata WHERE id_mhs=('%d')";
                sql = format(sql, id_mhs);
                // eksekusi query dan simpan hasilnya di obj ResultSet
                rs = stmt.executeQuery(sql);

                // tampilkan hasil query
                while (rs.next()) {
                    out.printf("  %-3d", rs.getInt("id_mhs"));
                    out.printf("| %-2s ", rs.getString("nim"));
                    out.printf("| %-17s ", rs.getString("nama"));
                    out.printf("| %-23s ", rs.getString("prodi"));
                    out.printf("| %-13s ", rs.getString("kelas"));
                    out.printf("| %-15s ", rs.getString("noHp"));
                    out.printf("| %-17s ", rs.getString("matkul"));
                    out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            border();
            border();
            boolean isDelet = getYesOrNo("Apakah anda yakin akan menghapus data ");
            if (isDelet) {
                // buat query hapus
                String sql = String.format("DELETE FROM tbdata WHERE id_mhs=%d", id_mhs);
                // hapus data
                stmt.execute(sql);

                err.println("Data telah terhapus...\n");

            }else {
                out.println("\nData tidak terhapus");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void tampilkanDataFrs(boolean isDisplay) {
        border();
        border();
        header();
        border();
        // Melakukan koneksi ke database
        try {
            // register driver yang akan dipakai
            Class.forName(JDBC_DRIVER);
            // buat koneksi ke database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // buat objek statement
            stmt = conn.createStatement();
//            // buat query ke database
            String sql = "SELECT * FROM tbdata";
            // eksekusi query dan simpan hasilnya di obj ResultSet
            rs = stmt.executeQuery(sql);

            // tampilkan hasil query
            while (rs.next()) {
                out.printf("  %-3d", rs.getInt("id_mhs"));
                out.printf("| %-2s ", rs.getString("nim"));
                out.printf("| %-17s ", rs.getString("nama"));
                out.printf("| %-23s ", rs.getString("prodi"));
                out.printf("| %-13s ", rs.getString("kelas"));
                out.printf("| %-15s ", rs.getString("noHp"));
                out.printf("| %-17s ", rs.getString("matkul"));
                out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        border();
        border();
        if (isDisplay) {
            var tambah = getYesOrNo("Apakah Kamu Ingin Menambahkan data ?");
            //menulis ke database
            if (tambah) {
                tambahDataFrs();
            }
        }
    }
    private static void cariDataFrs() {
        tampilkanDataFrs(false);
        try {
            out.print("\nSilahkan masukan Nama yang akan anda cari : ");
            String nama = input.readLine();
            String sql = "SELECT * FROM tbdata WHERE nama LIKE('%s')";
            sql = format(sql, nama);
            // eksekusi query dan simpan hasilnya di obj ResultSet
            rs = stmt.executeQuery(sql);
            border();
            header();
            border();
            while (rs.next()) {
                out.printf("  %-3d", rs.getInt("id_mhs"));
                out.printf("| %-2s ", rs.getString("nim"));
                out.printf("| %-17s ", rs.getString("nama"));
                out.printf("| %-23s ", rs.getString("prodi"));
                out.printf("| %-13s ", rs.getString("kelas"));
                out.printf("| %-15s ", rs.getString("noHp"));
                out.printf("| %-17s ", rs.getString("matkul"));
                out.println();
            }
            border();
        } catch (SQLException ignored) {
        } catch (IOException e) {
            err.println("Imput yang anda masukan tidak ditemukan, harap perhatikan kembali inputan anda !");
        }
    }
    private static boolean getYesOrNo(String message){

        Scanner terminalInput = new Scanner(in);
        out.print("\n" + message + " [ y ] Yes / [ n ] No ? ");
        var pilihanUser = terminalInput.next();
        while (!pilihanUser.equalsIgnoreCase("y")&& !pilihanUser.equalsIgnoreCase("n")){
            err.println("Maaf harap masukan [y] Iya, atau [n] untuk Tidak !\n");
            out.print("\n" + message + " [ y ] Yes / [ n ] No ? ");
            pilihanUser = terminalInput.next();
        }
        return pilihanUser.equalsIgnoreCase("y");
    }
    private static void border(){
        out.println("........................................." +
                "............................................." +
                "............................................." +
                "......................................");
    }
    private static void header(){
        out.println("  No |\t\tNIM\t\t" +
                "|\t\tNAMA\t\t" +
                "|\t  PROGRAM STUDI\t\t  " +
                "|  \tKELAS\t  " +
                "|\t\tNo. HP\t\t" +
                "| \t\t\t\t\t\t\t  MATA KULIAH");
    }
    private static void reUpdate() throws IOException, SQLException {
        var reUpdate = getYesOrNo("Apakah kamu mau mengupdate yang lain ");
        if (reUpdate){
            updateDataFrs();
        }
    }
}