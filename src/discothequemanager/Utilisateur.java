/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package discothequemanager;

/**
 *
 * @author Abdoul Rachard
 */
public class Utilisateur {
    private String nom;
    private String email;
    private String motDePasse;
    private boolean estAbonne;
    private String codePersonnel;

    // Constructeur
    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.estAbonne = false; // Par défaut, l'utilisateur n'est pas abonné
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isEstAbonne() {
        return estAbonne;
    }

    public void setEstAbonne(boolean estAbonne) {
        this.estAbonne = estAbonne;
    }

    public String getCodePersonnel() {
        return codePersonnel;
    }

    public void setCodePersonnel(String codePersonnel) {
        this.codePersonnel = codePersonnel;
    }
}
