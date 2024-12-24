package discothequemanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFrame extends JFrame {
    private JTable tableDisques;
    private DefaultTableModel tableModel;
    private JButton emprunterButton, acheterButton, detailsButton, historiqueButton, deconnexionButton;
    private JTextField rechercheField;
    private JButton rechercheButton;
    private int currentUserId;

    public UserFrame(int userId) {
        this.currentUserId = userId;
        setTitle("Espace Utilisateur");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        addListeners();
        chargerDisques(null);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Réduire l'espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rechercheField = new JTextField();
        rechercheButton = new JButton("Rechercher");
        tableModel = new DefaultTableModel(new String[]{"ID", "Titre", "Artiste", "Genre", "Stock", "Prix"}, 0);
        tableDisques = new JTable(tableModel);

        emprunterButton = new JButton("Emprunter");
        acheterButton = new JButton("Acheter");
        detailsButton = new JButton("Voir Détails");
        historiqueButton = new JButton("Voir Historique");
        deconnexionButton = new JButton("Déconnexion");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(new JLabel("Rechercher un disque:"), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        gbc.weightx = 0.75; // 3/4 de la largeur
        panel.add(rechercheField, gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.weightx = 0.25; // 1/4 de la largeur
        panel.add(rechercheButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(tableDisques), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // Placer chaque bouton sur une nouvelle ligne
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(emprunterButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(acheterButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(detailsButton, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(historiqueButton, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(deconnexionButton, gbc);

        add(panel);
    }

    private void addListeners() {
        rechercheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recherche = rechercheField.getText();
                chargerDisques(recherche);
            }
        });

        emprunterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableDisques.getSelectedRow();
                if (selectedRow != -1) {
                    int disqueId = (int) tableDisques.getValueAt(selectedRow, 0);
                    emprunterDisque(disqueId);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un disque à emprunter.");
                }
            }
        });

        acheterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableDisques.getSelectedRow();
                if (selectedRow != -1) {
                    int disqueId = (int) tableDisques.getValueAt(selectedRow, 0);
                    acheterDisque(disqueId);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un disque à acheter.");
                }
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableDisques.getSelectedRow();
                if (selectedRow != -1) {
                    int disqueId = (int) tableDisques.getValueAt(selectedRow, 0);
                    afficherDetailsDisque(disqueId);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un disque pour voir les détails.");
                }
            }
        });

        historiqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherHistorique();
            }
        });

        deconnexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnexionFrame().setVisible(true);
                dispose();
            }
        });
    }

    private void chargerDisques(String recherche) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Disques";
            if (recherche != null && !recherche.isEmpty()) {
                query += " WHERE titre LIKE ? OR artiste LIKE ? OR genre LIKE ?";
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            if (recherche != null && !recherche.isEmpty()) {
                String recherchePattern = "%" + recherche + "%";
                stmt.setString(1, recherchePattern);
                stmt.setString(2, recherchePattern);
                stmt.setString(3, recherchePattern);
            }
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("artiste"),
                    rs.getString("genre"),
                    rs.getInt("stock"),
                    rs.getDouble("prix")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des disques.");
        }
    }

    private void emprunterDisque(int disqueId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Emprunts (utilisateurId, disqueId, dateEmprunt, dateRetour) VALUES (?, ?, CURDATE(), NULL)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            stmt.setInt(2, disqueId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Disque emprunté avec succès!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'emprunt du disque.");
        }
    }

    private void acheterDisque(int disqueId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Achats (utilisateurId, disqueId, quantite, dateAchat) VALUES (?, ?, 1, CURDATE())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            stmt.setInt(2, disqueId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Disque acheté avec succès!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'achat du disque.");
        }
    }

    private void afficherDetailsDisque(int disqueId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Disques WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, disqueId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String details = "Titre: " + rs.getString("titre") + "\n"
                               + "Artiste: " + rs.getString("artiste") + "\n"
                               + "Genre: " + rs.getString("genre") + "\n"
                               + "Stock: " + rs.getInt("stock") + "\n"
                               + "Prix: " + rs.getDouble("prix");
                JOptionPane.showMessageDialog(null, details, "Détails du Disque", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void afficherHistorique() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT 'Emprunt' AS type, d.titre, e.dateEmprunt, e.dateRetour FROM Emprunts e JOIN Disques d ON e.disqueId = d.id WHERE e.utilisateurId = ?"
                         + " UNION "
                         + "SELECT 'Achat' AS type, d.titre, a.dateAchat, NULL FROM Achats a JOIN Disques d ON a.disqueId = d.id WHERE a.utilisateurId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            stmt.setInt(2, currentUserId);
            ResultSet rs = stmt.executeQuery();
            StringBuilder historique = new StringBuilder();
            while (rs.next()) {
                historique.append(rs.getString("type")).append(": ")
                          .append("Titre: ").append(rs.getString("titre"))
                          .append(", Date: ").append(rs.getDate(3))
                          .append(rs.getDate(4) != null ? ", Retour: " + rs.getDate(4) : "")
                          .append("\n");
            }
            JOptionPane.showMessageDialog(null, historique.toString(), "Historique", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}