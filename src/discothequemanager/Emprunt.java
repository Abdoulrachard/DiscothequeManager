/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package discothequemanager;

import java.util.Date;

/**
 *
 * @author Abdoul Rachard
 */
public class Emprunt {
    private Utilisateur utilisateur;
    private Disque disque;
    private Date dateEmprunt;
    private Date dateRetour;

    // Constructeur
    public Emprunt(Utilisateur utilisateur, Disque disque, Date dateEmprunt, Date dateRetour) {
        this.utilisateur = utilisateur;
        this.disque = disque;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    // Getters et Setters
    // ...
}
