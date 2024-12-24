/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package discothequemanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminFrame extends JFrame {
    private JButton deconnexionButton;
    private JButton gestionDisquesButton;
    private JButton gestionUtilisateursButton;
    private JButton voirStatistiquesButton;
    private JButton promouvoirButton;

    public AdminFrame() {
        setTitle("Tableau de Bord Administrateur");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants

        gestionDisquesButton = new JButton("Gérer les Disques");
        gestionUtilisateursButton = new JButton("Gérer les Utilisateurs");
        voirStatistiquesButton = new JButton("Voir les Statistiques");
        promouvoirButton = new JButton("Promouvoir Admin");
        deconnexionButton = new JButton("Déconnexion");

        // Disposer les boutons
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(gestionDisquesButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(gestionUtilisateursButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(promouvoirButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(voirStatistiquesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Étendre sur deux colonnes
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(deconnexionButton, gbc);

        add(panel);

        gestionDisquesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionDisquesFrame().setVisible(true);
                dispose(); // Fermer le tableau de bord
            }
        });

        gestionUtilisateursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUtilisateursFrame().setVisible(true);
                dispose(); // Fermer le tableau de bord
            }
        });

        voirStatistiquesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StatistiquesFrame().setVisible(true);
            }
        });

        promouvoirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JOptionPane.showInputDialog("Email de l'utilisateur à promouvoir:");
                if (email != null && !email.trim().isEmpty()) {
                    promouvoirAdmin(email);
                }
            }
        });

        deconnexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnexionFrame().setVisible(true);
                dispose(); // Fermer le tableau de bord
            }
        });
    }

    private void promouvoirAdmin(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Roles SET role = 'admin' WHERE utilisateurId = (SELECT id FROM Utilisateurs WHERE email = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Utilisateur promu au rôle d'administrateur avec succès!");
            } else {
                JOptionPane.showMessageDialog(null, "Utilisateur non trouvé ou déjà administrateur.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la promotion de l'utilisateur.");
        }
    }
}
