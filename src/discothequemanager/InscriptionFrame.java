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
public class InscriptionFrame extends JFrame {
    private JTextField nomField;
    private JTextField emailField;
    private JPasswordField motDePasseField;
    private JButton inscrireButton;
    private JButton dejaCompteButton;

    public InscriptionFrame() {
        setTitle("Inscription");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomField = new JTextField(15);
        emailField = new JTextField(15);
        motDePasseField = new JPasswordField(15);
        inscrireButton = new JButton("S'inscrire");
        dejaCompteButton = new JButton("Déjà un compte?");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(motDePasseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(inscrireButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(dejaCompteButton, gbc);

        inscrireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getPassword());

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "INSERT INTO Utilisateurs (nom, email, motDePasse, estAbonne, codePersonnel) VALUES (?, ?, ?, false, NULL)";
                    PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, nom);
                    stmt.setString(2, email);
                    stmt.setString(3, motDePasse);
                    stmt.executeUpdate();

                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);

                        String roleQuery = "INSERT INTO Roles (utilisateurId, role) VALUES (?, 'user')";
                        PreparedStatement roleStmt = conn.prepareStatement(roleQuery);
                        roleStmt.setInt(1, userId);
                        roleStmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Inscription réussie!");
                    dispose(); // Fermer la fenêtre d'inscription
                    new ConnexionFrame().setVisible(true); // Ouvrir la fenêtre de connexion
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'inscription.");
                }
            }
        });

        dejaCompteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConnexionFrame().setVisible(true);
                dispose(); // Fermer la fenêtre d'inscription
            }
        });

        add(panel);
    }
}
