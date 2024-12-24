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
public class ConnexionFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField motDePasseField;
    private JButton connecterButton;
    private JButton pasDeCompteButton;

    public ConnexionFrame() {
        setTitle("Connexion");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(15);
        motDePasseField = new JPasswordField(15);
        connecterButton = new JButton("Se connecter");
        pasDeCompteButton = new JButton("Pas de compte?");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(motDePasseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(connecterButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(pasDeCompteButton, gbc);

        connecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getPassword());

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "SELECT id FROM Utilisateurs WHERE email = ? AND motDePasse = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, email);
                    stmt.setString(2, motDePasse);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        String roleQuery = "SELECT role FROM Roles WHERE utilisateurId = ?";
                        PreparedStatement roleStmt = conn.prepareStatement(roleQuery);
                        roleStmt.setInt(1, userId);
                        ResultSet roleRs = roleStmt.executeQuery();

                        if (roleRs.next()) {
                            String role = roleRs.getString("role");
                            if ("admin".equals(role)) {
                                new AdminFrame().setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Connexion réussie!");
                                new UserFrame(userId).setVisible(true);
                            }
                            dispose(); // Fermer la fenêtre de connexion
                        } else {
                            JOptionPane.showMessageDialog(null, "Rôle non défini pour cet utilisateur.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Email ou mot de passe incorrect.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        pasDeCompteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InscriptionFrame().setVisible(true);
                dispose(); // Fermer la fenêtre de connexion
            }
        });

        add(panel);
    }
}
