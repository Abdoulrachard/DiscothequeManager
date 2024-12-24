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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Abdoul Rachard
 */
public class StatistiquesFrame extends JFrame {

    public StatistiquesFrame() {
        setTitle("Statistiques");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel totalDisquesLabel = new JLabel();
        JLabel totalUtilisateursLabel = new JLabel();
        JLabel totalEmpruntsLabel = new JLabel();
        JLabel totalAchatsLabel = new JLabel();
        JButton retourButton = new JButton("Retour");

        panel.add(totalDisquesLabel);
        panel.add(totalUtilisateursLabel);
        panel.add(totalEmpruntsLabel);
        panel.add(totalAchatsLabel);
        panel.add(Box.createVerticalStrut(10)); // Espace entre les statistiques et le bouton
        panel.add(retourButton);

        add(panel);

        chargerStatistiques(totalDisquesLabel, totalUtilisateursLabel, totalEmpruntsLabel, totalAchatsLabel);

        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame().setVisible(true);
                dispose(); // Fermer la fenêtre des statistiques
            }
        });
    }

    private void chargerStatistiques(JLabel totalDisquesLabel, JLabel totalUtilisateursLabel, JLabel totalEmpruntsLabel, JLabel totalAchatsLabel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Total des disques
            String queryDisques = "SELECT COUNT(*) AS total FROM Disques";
            PreparedStatement stmtDisques = conn.prepareStatement(queryDisques);
            ResultSet rsDisques = stmtDisques.executeQuery();
            if (rsDisques.next()) {
                totalDisquesLabel.setText("Total des disques: " + rsDisques.getInt("total"));
            }

            // Total des utilisateurs
            String queryUtilisateurs = "SELECT COUNT(*) AS total FROM Utilisateurs";
            PreparedStatement stmtUtilisateurs = conn.prepareStatement(queryUtilisateurs);
            ResultSet rsUtilisateurs = stmtUtilisateurs.executeQuery();
            if (rsUtilisateurs.next()) {
                totalUtilisateursLabel.setText("Total des utilisateurs: " + rsUtilisateurs.getInt("total"));
            }

            // Total des emprunts
            String queryEmprunts = "SELECT COUNT(*) AS total FROM Emprunts";
            PreparedStatement stmtEmprunts = conn.prepareStatement(queryEmprunts);
            ResultSet rsEmprunts = stmtEmprunts.executeQuery();
            if (rsEmprunts.next()) {
                totalEmpruntsLabel.setText("Total des emprunts: " + rsEmprunts.getInt("total"));
            }

            // Total des achats
            String queryAchats = "SELECT COUNT(*) AS total FROM Achats";
            PreparedStatement stmtAchats = conn.prepareStatement(queryAchats);
            ResultSet rsAchats = stmtAchats.executeQuery();
            if (rsAchats.next()) {
                totalAchatsLabel.setText("Total des achats: " + rsAchats.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
