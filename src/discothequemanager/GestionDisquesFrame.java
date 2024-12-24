/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author Abdoul Rachard
 */
public class GestionDisquesFrame extends JFrame {
    private JTable tableDisques;
    private DefaultTableModel tableModel;
    private JButton ajouterButton;
    private JButton modifierButton;
    private JButton supprimerButton;
    private JButton retourButton;

    public GestionDisquesFrame() {
        setTitle("Gestion des Disques");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Titre", "Artiste", "Genre", "Stock", "Prix"}, 0);
        tableDisques = new JTable(tableModel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        ajouterButton = new JButton("Ajouter");
        modifierButton = new JButton("Modifier");
        supprimerButton = new JButton("Supprimer");
        retourButton = new JButton("Retour");

        buttonPanel.add(ajouterButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(modifierButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(supprimerButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(retourButton);

        panel.add(new JScrollPane(tableDisques), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        add(panel);
        chargerDisques();

        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame().setVisible(true);
                dispose(); // Fermer la fenêtre de gestion des disques
            }
        });

        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titre = JOptionPane.showInputDialog("Titre du disque:");
                if (titre == null || titre.trim().isEmpty()) return; // Annulation ou champ vide

                String artiste = JOptionPane.showInputDialog("Artiste:");
                if (artiste == null || artiste.trim().isEmpty()) return; // Annulation ou champ vide

                String genre = JOptionPane.showInputDialog("Genre:");
                if (genre == null || genre.trim().isEmpty()) return; // Annulation ou champ vide

                String stockStr = JOptionPane.showInputDialog("Stock:");
                if (stockStr == null || stockStr.trim().isEmpty()) return; // Annulation ou champ vide
                int stock;
                try {
                    stock = Integer.parseInt(stockStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Le stock doit être un nombre entier.");
                    return;
                }

                String prixStr = JOptionPane.showInputDialog("Prix:");
                if (prixStr == null || prixStr.trim().isEmpty()) return; // Annulation ou champ vide
                double prix;
                try {
                    prix = Double.parseDouble(prixStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Le prix doit être un nombre.");
                    return;
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO Disques (titre, artiste, genre, stock, prix) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, titre);
                    stmt.setString(2, artiste);
                    stmt.setString(3, genre);
                    stmt.setInt(4, stock);
                    stmt.setDouble(5, prix);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Disque ajouté avec succès!");
                    chargerDisques(); // Recharger la liste des disques
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableDisques.getSelectedRow();
                if (selectedRow != -1) {
                    int disqueId = (int) tableDisques.getValueAt(selectedRow, 0);
                    String nouveauTitre = JOptionPane.showInputDialog("Nouveau titre du disque:", tableDisques.getValueAt(selectedRow, 1));
                    if (nouveauTitre == null) return; // Annulation

                    String nouvelArtiste = JOptionPane.showInputDialog("Nouvel artiste:", tableDisques.getValueAt(selectedRow, 2));
                    if (nouvelArtiste == null) return; // Annulation

                    String nouveauGenre = JOptionPane.showInputDialog("Nouveau genre:", tableDisques.getValueAt(selectedRow, 3));
                    if (nouveauGenre == null) return; // Annulation

                    String stockStr = JOptionPane.showInputDialog("Nouveau stock:", tableDisques.getValueAt(selectedRow, 4));
                    if (stockStr == null) return; // Annulation
                    int nouveauStock;
                    try {
                        nouveauStock = Integer.parseInt(stockStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Le stock doit être un nombre entier.");
                        return;
                    }

                    String prixStr = JOptionPane.showInputDialog("Nouveau prix:", tableDisques.getValueAt(selectedRow, 5));
                    if (prixStr == null) return; // Annulation
                    double nouveauPrix;
                    try {
                        nouveauPrix = Double.parseDouble(prixStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Le prix doit être un nombre.");
                        return;
                    }

                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "UPDATE Disques SET titre = ?, artiste = ?, genre = ?, stock = ?, prix = ? WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, nouveauTitre);
                        stmt.setString(2, nouvelArtiste);
                        stmt.setString(3, nouveauGenre);
                        stmt.setInt(4, nouveauStock);
                        stmt.setDouble(5, nouveauPrix);
                        stmt.setInt(6, disqueId);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Disque modifié avec succès!");
                        chargerDisques(); // Recharger la liste des disques
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un disque à modifier.");
                }
            }
        });

        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableDisques.getSelectedRow();
                if (selectedRow != -1) {
                    int disqueId = (int) tableDisques.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer ce disque?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DatabaseConnection.getConnection()) {
                            String query = "DELETE FROM Disques WHERE id = ?";
                            PreparedStatement stmt = conn.prepareStatement(query);
                            stmt.setInt(1, disqueId);
                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Disque supprimé avec succès!");
                            chargerDisques(); // Recharger la liste des disques
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un disque à supprimer.");
                }
            }
        });
    }

    private void chargerDisques() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Disques";
            PreparedStatement stmt = conn.prepareStatement(query);
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
        }
    }
}
