/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package discothequemanager;

/**
 *
 * @author Abdoul Rachard
 */
public class Disque {
    private String titre;
    private String artiste;
    private String genre;
    private int stock;
    private double prix;

    // Constructeur
    public Disque(String titre, String artiste, String genre, int stock, double prix) {
        this.titre = titre;
        this.artiste = artiste;
        this.genre = genre;
        this.stock = stock;
        this.prix = prix;
    }

    // Getters et Setters
    // ...
}
