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
public class GestionUtilisateursFrame extends JFrame {
    private JTable tableUtilisateurs;
    private DefaultTableModel tableModel;
    private JButton ajouterButton;
    private JButton modifierButton;
    private JButton supprimerButton;
    private JButton retourButton;

    public GestionUtilisateursFrame() {
        setTitle("Gestion des Utilisateurs");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Email", "Abonné"}, 0);
        tableUtilisateurs = new JTable(tableModel);

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

        panel.add(new JScrollPane(tableUtilisateurs), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        add(panel);
        chargerUtilisateurs();

        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame().setVisible(true);
                dispose(); // Fermer la fenêtre de gestion des utilisateurs
            }
        });

        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = JOptionPane.showInputDialog("Nom de l'utilisateur:");
                if (nom == null || nom.trim().isEmpty()) return; // Annulation ou champ vide

                String email = JOptionPane.showInputDialog("Email:");
                if (email == null || email.trim().isEmpty()) return; // Annulation ou champ vide

                String motDePasse = JOptionPane.showInputDialog("Mot de passe:");
                if (motDePasse == null || motDePasse.trim().isEmpty()) return; // Annulation ou champ vide

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO Utilisateurs (nom, email, motDePasse, estAbonne, codePersonnel) VALUES (?, ?, ?, false, NULL)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, nom);
                    stmt.setString(2, email);
                    stmt.setString(3, motDePasse);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Utilisateur ajouté avec succès!");
                    chargerUtilisateurs(); // Recharger la liste des utilisateurs
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableUtilisateurs.getSelectedRow();
                if (selectedRow != -1) {
                    int utilisateurId = (int) tableUtilisateurs.getValueAt(selectedRow, 0);
                    String nouveauNom = JOptionPane.showInputDialog("Nouveau nom de l'utilisateur:", tableUtilisateurs.getValueAt(selectedRow, 1));
                    if (nouveauNom == null) return; // Annulation

                    String nouvelEmail = JOptionPane.showInputDialog("Nouvel email:", tableUtilisateurs.getValueAt(selectedRow, 2));
                    if (nouvelEmail == null) return; // Annulation

                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "UPDATE Utilisateurs SET nom = ?, email = ? WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, nouveauNom);
                        stmt.setString(2, nouvelEmail);
                        stmt.setInt(3, utilisateurId);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Utilisateur modifié avec succès!");
                        chargerUtilisateurs(); // Recharger la liste des utilisateurs
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un utilisateur à modifier.");
                }
            }
        });

        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableUtilisateurs.getSelectedRow();
                if (selectedRow != -1) {
                    int utilisateurId = (int) tableUtilisateurs.getValueAt(selectedRow, 0);
                    int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer cet utilisateur?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try (Connection conn = DatabaseConnection.getConnection()) {
                            String query = "DELETE FROM Utilisateurs WHERE id = ?";
                            PreparedStatement stmt = conn.prepareStatement(query);
                            stmt.setInt(1, utilisateurId);
                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès!");
                            chargerUtilisateurs(); // Recharger la liste des utilisateurs
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un utilisateur à supprimer.");
                }
            }
        });
    }

    private void chargerUtilisateurs() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, nom, email, estAbonne FROM Utilisateurs";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getBoolean("estAbonne")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
