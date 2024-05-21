import java.sql.*;
import java.util.Scanner;

public class ViewMahasiswa {
    public static void main(String[] args) {
        String jdbUrl = "jdbc:mysql://localhost:3306/dbmahasiswa";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbUrl, username, password)) {
            Statement statement = connection.createStatement();

            // Menampilkan semua data Mahasiswa
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

            // Menambahkan data Mahasiswa baru
            String nama = getInput("Masukkan nama mahasiswa: ");
            String npm = getInput("Masukkan NPM: ");
            String alamat = getInput("Masukkan alamat: ");

            String insertQuery = String.format("INSERT INTO mahasiswa (npm, nama, alamat) VALUES ('%s', '%s', '%s')", npm, nama, alamat);

            int rowsAffectedInsert = statement.executeUpdate(insertQuery);

            if (rowsAffectedInsert > 0) {
                System.out.println("Data mahasiswa berhasil ditambahkan!");
            } else {
                System.out.println("Gagal menambahkan data mahasiswa");
            }

            // Mengecek apakah NPM ada dalam database
            String checkQuery = String.format("SELECT * FROM mahasiswa WHERE npm = %s", npm);
            ResultSet rs = statement.executeQuery(checkQuery);
            if (!rs.next()) {
                System.out.println("NPM mahasiswa tidak ditemukan.");
                return;
            }

            // Mengubah data Mahasiswa
            String namaBaru = getInput("Masukkan nama baru: ");
            String alamatBaru = getInput("Masukkan alamat baru: ");

            String updateQuery = String.format("UPDATE mahasiswa SET nama = '%s', alamat = '%s' WHERE npm = %s",
             namaBaru, alamatBaru, npm);

            int rowsAffectedUpdate = statement.executeUpdate(updateQuery);

            if (rowsAffectedUpdate > 0) {
                System.out.println("Data mahasiswa berhasil diperbarui!");
            } else {
                System.out.println("Gagal memperbarui data mahasiswa");
            }

            // Input NPM Mahasiswa yang akan dihapus
            String npmHapus = getInput("Masukkan NPM mahasiswa yang akan dihapus: ");

            // Mengecek apakah NPM ada dalam database
            String checkQueryHapus = String.format("SELECT * FROM mahasiswa WHERE npm = %s", npm);
            ResultSet rsHapus = statement.executeQuery(checkQueryHapus);
            if (!rsHapus.next()) {
                System.out.println("NPM mahasiswa tidak ditemukan.");
                return;
            }

            // Menghapus data Mahasiswa
            String deleteQuery = String.format("DELETE FROM mahasiswa WHERE npm = %s" ,npm);
            int rowsAffectedDelete = statement.executeUpdate(deleteQuery);

            if (rowsAffectedUpdate > 0) {
                System.out.println("Data mahasiswa berhasil dihapus!");
            } else {
                System.out.println("Gagal menghapus data mahasiswa");
            }

            rsHapus.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Mengambil Input dari User
    private static String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
}