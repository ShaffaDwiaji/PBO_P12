import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login1 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;   
    private JButton btnNewButton;
    private JPanel contentPane; 
    private JFrame crudFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login(); 
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setBounds(100, 70, 1014, 597); 
        setResizable(false); 
        contentPane = new JPanel(); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        setContentPane(contentPane); 
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login"); 
        lblNewLabel.setForeground(Color.BLACK); 
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 46)); 
        lblNewLabel.setBounds(423, 13, 273, 93); 
        contentPane.add(lblNewLabel);
        
        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32)); 
        textField.setBounds(481, 170, 281, 68); 
        contentPane.add(textField); 
        textField.setColumns(10);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32)); 
        passwordField.setBounds(481, 286, 281, 68); 
        contentPane.add(passwordField);
        
        JLabel lblUsername = new JLabel("Username"); 
        lblUsername.setBackground(Color.BLACK); 
        lblUsername.setForeground(Color.BLACK); 
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31)); 
        lblUsername.setBounds(250, 166, 193, 52); 
        contentPane.add(lblUsername);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK); 
        lblPassword.setBackground(Color.CYAN); 
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31)); 
        lblPassword.setBounds(250, 286, 193, 52); 
        contentPane.add(lblPassword);
        
        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(481, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText(); 
                char[] password = passwordField.getPassword();
                String passwordString = String.valueOf(password);
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa",
                        "root", "");
                    
                    PreparedStatement st = connection 
                        .prepareStatement("select name, password from user where name=? and password=?");

                    st.setString(1, userName);
                    st.setString(2, passwordString);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        String name = rs.getString("name");
                        showCrudFrame();
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton, "Login gagal. Username atau Password salah");
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }        
            }
        });
        
        contentPane.add(btnNewButton);
    }

    private void showCrudFrame() {
        if (crudFrame == null) {
            crudFrame = new JFrame("CRUD Mahasiswa");
            crudFrame.setBounds(100, 70, 800, 600);
            crudFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crudFrame.getContentPane().setLayout(null);

            JButton btnView = new JButton("View Data");
            btnView.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnView.setBounds(50, 50, 200, 50);
            btnView.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayMahasiswa();
                }
            });
            crudFrame.getContentPane().add(btnView);

            JButton btnAdd = new JButton("Tambah Data");
            btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnAdd.setBounds(50, 120, 200, 50);
            btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tambahMahasiswa();
                }
            });
            crudFrame.getContentPane().add(btnAdd);

            JButton btnUpdate = new JButton("Ubah Data");
            btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnUpdate.setBounds(50, 190, 200, 50);
            btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ubahMahasiswa();
                }
            });
            crudFrame.getContentPane().add(btnUpdate);

            JButton btnDelete = new JButton("Hapus Data");
            btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnDelete.setBounds(50, 260, 200, 50);
            btnDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hapusMahasiswa();
                }
            });
            crudFrame.getContentPane().add(btnDelete);

            JButton btnLogout = new JButton("Logout");
            btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 26));
            btnLogout.setBounds(600, 450, 150, 50);
            btnLogout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    crudFrame.dispose(); 
                    setVisible(true); 
                    textField.setText(""); 
                    passwordField.setText(""); 
                }
            });
            crudFrame.getContentPane().add(btnLogout);

            setVisible(false);
            crudFrame.setVisible(true);
        }
    }

    private void displayMahasiswa() {
        JFrame viewFrame = new JFrame("Tampilkan Data Mahasiswa");
        viewFrame.setBounds(100, 70, 600, 400);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.getContentPane().setLayout(new BorderLayout());
    
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        viewFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mahasiswa");
    
            while (resultSet.next()) {
                String npm = resultSet.getString("npm");
                String nama = resultSet.getString("nama");
                String alamat = resultSet.getString("alamat");
                textArea.append("NPM: " + npm + "\nNama: " + nama + "\nAlamat: " + alamat + "\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        viewFrame.setVisible(true);
    }    

    private void tambahMahasiswa() {
        JFrame addFrame = new JFrame("Tambah Data Mahasiswa");
        addFrame.setBounds(100, 70, 400, 300);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.getContentPane().setLayout(new GridLayout(4, 2));

        JLabel lblNpm = new JLabel("NPM:");
        JTextField txtNpm = new JTextField();
        JLabel lblNama = new JLabel("Nama:");
        JTextField txtNama = new JTextField();
        JLabel lblAlamat = new JLabel("Alamat:");
        JTextField txtAlamat = new JTextField();
        JButton btnSave = new JButton("Simpan");

        addFrame.getContentPane().add(lblNpm);
        addFrame.getContentPane().add(txtNpm);
        addFrame.getContentPane().add(lblNama);
        addFrame.getContentPane().add(txtNama);
        addFrame.getContentPane().add(lblAlamat);
        addFrame.getContentPane().add(txtAlamat);
        addFrame.getContentPane().add(new JLabel()); // Empty cell
        addFrame.getContentPane().add(btnSave);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String npm = txtNpm.getText();
                String nama = txtNama.getText();
                String alamat = txtAlamat.getText();
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa", "root", "");
                    Statement statement = connection.createStatement();
                    String query = String.format("INSERT INTO mahasiswa (npm, nama, alamat) VALUES ('%s', '%s', '%s')", npm, nama, alamat);
                    statement.executeUpdate(query);
                    JOptionPane.showMessageDialog(addFrame, "Data berhasil ditambahkan!");
                    addFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(addFrame, "Gagal menambahkan data.");
                }
            }
        });

        addFrame.setVisible(true);
    }

    private void ubahMahasiswa() {
        JFrame updateFrame = new JFrame("Ubah Data Mahasiswa");
        updateFrame.setBounds(100, 70, 400, 400);
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.getContentPane().setLayout(new GridLayout(5, 2));

        JLabel lblNpmSearch = new JLabel("Cari NPM:");
        JTextField txtNpmSearch = new JTextField();
        JButton btnSearch = new JButton("Cari");

        JLabel lblNpm = new JLabel("NPM:");
        JTextField txtNpm = new JTextField();
        JLabel lblNama = new JLabel("Nama:");
        JTextField txtNama = new JTextField();
        JLabel lblAlamat = new JLabel("Alamat:");
        JTextField txtAlamat = new JTextField();
        JButton btnSave = new JButton("Simpan");

        updateFrame.getContentPane().add(lblNpmSearch);
        updateFrame.getContentPane().add(txtNpmSearch);
        updateFrame.getContentPane().add(new JLabel()); // Empty cell
        updateFrame.getContentPane().add(btnSearch);
        updateFrame.getContentPane().add(lblNpm);
        updateFrame.getContentPane().add(txtNpm);
        updateFrame.getContentPane().add(lblNama);
        updateFrame.getContentPane().add(txtNama);
        updateFrame.getContentPane().add(lblAlamat);
        updateFrame.getContentPane().add(txtAlamat);
        updateFrame.getContentPane().add(new JLabel()); // Empty cell
        updateFrame.getContentPane().add(btnSave);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String npmSearch = txtNpmSearch.getText();
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa", "root", "");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM mahasiswa WHERE npm = '" + npmSearch + "'");
                    if (rs.next()) {
                        txtNpm.setText(rs.getString("npm"));
                        txtNama.setText(rs.getString("nama"));
                        txtAlamat.setText(rs.getString("alamat"));
                    } else {
                        JOptionPane.showMessageDialog(updateFrame, "NPM tidak ditemukan.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(updateFrame, "Gagal mencari data.");
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String npm = txtNpm.getText();
                String nama = txtNama.getText();
                String alamat = txtAlamat.getText();
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa", "root", "");
                    Statement statement = connection.createStatement();
                    String query = String.format("UPDATE mahasiswa SET nama = '%s', alamat = '%s' WHERE npm = '%s'", nama, alamat, npm);
                    statement.executeUpdate(query);
                    JOptionPane.showMessageDialog(updateFrame, "Data berhasil diubah!");
                    updateFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(updateFrame, "Gagal mengubah data.");
                }
            }
        });

        updateFrame.setVisible(true);
    }

    private void hapusMahasiswa() {
        JFrame deleteFrame = new JFrame("Hapus Data Mahasiswa");
        deleteFrame.setBounds(100, 70, 400, 200);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.getContentPane().setLayout(new GridLayout(3, 2));

        JLabel lblNpm = new JLabel("NPM:");
        JTextField txtNpm = new JTextField();
        JButton btnDelete = new JButton("Hapus");

        deleteFrame.getContentPane().add(lblNpm);
        deleteFrame.getContentPane().add(txtNpm);
        deleteFrame.getContentPane().add(new JLabel()); // Empty cell
        deleteFrame.getContentPane().add(btnDelete);

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String npm = txtNpm.getText();
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmahasiswa", "root", "");
                    Statement statement = connection.createStatement();
                    String query = String.format("DELETE FROM mahasiswa WHERE npm = '%s'", npm);
                    int rowsAffected = statement.executeUpdate(query);
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(deleteFrame, "Data berhasil dihapus!");
                    } else {
                        JOptionPane.showMessageDialog(deleteFrame, "NPM tidak ditemukan.");
                    }
                    deleteFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(deleteFrame, "Gagal menghapus data.");
                }
            }
        });

        deleteFrame.setVisible(true);
    }
}