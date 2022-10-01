package com.project.tpfinal3;

public class Employe {

    private final Integer id;
    private final String nom;
    private final String prenom;
    private final String genre;
    private final String experience;
    private final Double salaire;

    public Employe(Integer id, String nom, String prenom, String genre, String experience, Double salaire) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.experience = experience;
        this.salaire = salaire;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getGenre() {
        return genre;
    }

    public String getExperience() {
        return experience;
    }

    public Double getSalaire() {
        return salaire;
    }
}
