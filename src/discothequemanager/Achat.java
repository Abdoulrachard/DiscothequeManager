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
public class Achat {
    private Utilisateur utilisateur;
    private Disque disque;
    private int quantite;
    private Date dateAchat;

    // Constructeur
    public Achat(Utilisateur utilisateur, Disque disque, int quantite, Date dateAchat) {
        this.utilisateur = utilisateur;
        this.disque = disque;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
    }

    // Getters et Setters
    // ...
}
