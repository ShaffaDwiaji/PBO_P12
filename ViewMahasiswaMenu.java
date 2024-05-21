import java.sql.*;
import java.util.Scanner;

public class ViewMahasiswaMenu {
    public static void main(String[] args) {
        String jdbUrl = "jdbc:mysql://localhost:3306/dbmahasiswa";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbUrl, username, password)) {
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Menu:");
                System.out.println("1. View Data");
                System.out.println("2. Tambah Data");
                System.out.println("3. Ubah Data");
                System.out.println("4. Hapus Data");
                System.out.println("5. Keluar");
                System.out.print("Pilih opsi: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewData(statement);
                        break;
                    case 2:
                        tambahData(statement, scanner);
                        break;
                    case 3:
                        ubahData(statement, scanner);
                        break;
                    case 4:
                        hapusData(statement, scanner);
                        break;
                    case 5:
                        System.out.println("Keluar dari aplikasi...");
                        break;
                    default:
                        System.out.println("Opsi tidak valid. Silakan coba lagi.");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewData(Statement statement) throws SQLException {
        String selectQuery = "SELECT * FROM mahasiswa";
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {
            int npm = resultSet.getInt("npm");
            String nama = resultSet.getString("nama");
            String alamat = resultSet.getString("alamat");

            System.out.println("NPM: " + npm);
            System.out.println("Nama: " + nama);
            System.out.println("Alamat: " + alamat);
            System.out.println("------------------------");
        }

        resultSet.close();
    }

    private static void tambahData(Statement statement, Scanner scanner) throws SQLException {
        String nama = getInput(scanner, "Masukkan nama mahasiswa: ");
        String npm = getInput(scanner, "Masukkan NPM: ");
        String alamat = getInput(scanner, "Masukkan alamat: ");

        String insertQuery = String.format("INSERT INTO mahasiswa (npm, nama, alamat) VALUES ('%s', '%s', '%s')", npm, nama, alamat);
        int rowsAffected = statement.executeUpdate(insertQuery);

        if (rowsAffected > 0) {
            System.out.println("Data mahasiswa berhasil ditambahkan!");
        } else {
            System.out.println("Gagal menambahkan data mahasiswa");
        }
    }

    private static void ubahData(Statement statement, Scanner scanner) throws SQLException {
        String npm = getInput(scanner, "Masukkan NPM mahasiswa yang akan diubah: ");

        // Mengecek apakah NPM ada dalam database
        String checkQuery = String.format("SELECT * FROM mahasiswa WHERE npm = '%s'", npm);
        ResultSet rs = statement.executeQuery(checkQuery);
        if (!rs.next()) {
            System.out.println("NPM mahasiswa tidak ditemukan.");
            rs.close();
            return;
        }
        rs.close();

        // Mengubah data Mahasiswa
        String namaBaru = getInput(scanner, "Masukkan nama baru: ");
        String alamatBaru = getInput(scanner, "Masukkan alamat baru: ");

        String updateQuery = String.format("UPDATE mahasiswa SET nama = '%s', alamat = '%s' WHERE npm = '%s'",
                namaBaru, alamatBaru, npm);
        int rowsAffected = statement.executeUpdate(updateQuery);

        if (rowsAffected > 0) {
            System.out.println("Data mahasiswa berhasil diperbarui!");
        } else {
            System.out.println("Gagal memperbarui data mahasiswa");
        }
    }

    private static void hapusData(Statement statement, Scanner scanner) throws SQLException {
        String npm = getInput(scanner, "Masukkan NPM mahasiswa yang akan dihapus: ");

        // Mengecek apakah NPM ada dalam database
        String checkQuery = String.format("SELECT * FROM mahasiswa WHERE npm = '%s'", npm);
        ResultSet rs = statement.executeQuery(checkQuery);
        if (!rs.next()) {
            System.out.println("NPM mahasiswa tidak ditemukan.");
            rs.close();
            return;
        }
        rs.close();

        // Menghapus data Mahasiswa
        String deleteQuery = String.format("DELETE FROM mahasiswa WHERE npm = '%s'", npm);
        int rowsAffected = statement.executeUpdate(deleteQuery);

        if (rowsAffected > 0) {
            System.out.println("Data mahasiswa berhasil dihapus!");
        } else {
            System.out.println("Gagal menghapus data mahasiswa");
        }
    }

    // Mengambil Input dari User
    private static String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
}