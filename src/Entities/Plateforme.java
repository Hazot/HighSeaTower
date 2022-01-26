package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plateforme extends Entities {

    private String fonction;
    private boolean enCollision;

    /**
     * Constructeur de plateforme comprenant 3 arguments
     * @param x position en x
     * @param y position en y
     * @param largeur largeur de la platforme
     */
    public Plateforme(double x, double y, double largeur, boolean lastPlatRouge) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = 10;

        // Si la plateforme d'avant est rouge, les probabilités changent.
        if (lastPlatRouge) {
            double proba = Math.random();
            if (proba < 0.55) {
                this.fonction = "normal";
                this.color = Color.rgb(230, 134, 58);
            } else if (proba < 0.95) {
                this.fonction = "rebondissante";
                this.color = Color.LIGHTGREEN;
            } else {
                this.fonction = "accelerante";
                this.color = Color.rgb(230, 221, 58);
            }
        } else {
            double proba = Math.random();
            if (proba < 0.65) {
                this.fonction = "normal";
                this.color = Color.rgb(230, 134, 58);
            } else if (proba < 0.85) {
                this.fonction = "rebondissante";
                this.color = Color.LIGHTGREEN;
            } else if (proba < 0.95) {
                this.fonction = "accelerante";
                this.color = Color.rgb(230, 221, 58);
            } else {
                this.fonction = "solide";
                this.color = Color.rgb(184, 15, 36);
            }
        }

    }

    /**
     * Constructeur de plateformes permettant de choisir sa couleur d'avance et non sa fonction
     * Celui-ci est utilisé pour faire le plancher
     * @param x position en x
     * @param y position en y
     * @param largeur largeur
     * @param color couleur de la plateforme
     */
    public Plateforme(double x, double y, double largeur, Color color) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = 10;
        this.color = color;

        if (Color.rgb(230, 134, 58).equals(color)) {
            this.fonction = "normal";
        } else if (Color.LIGHTGREEN.equals(color)) {
            this.fonction = "rebondissante";
        } else if (Color.rgb(184, 15, 36).equals(color)) {
            this.fonction = "solide";
        } else {
            this.fonction = "accelerante";
        }
    }

    /**
     * Dessine l'affichage d'une plateforme sur le canevas
     * @param context contexte graphique général du canevas de l'aplication
     * @param fenetreY différence entre la hauteur du bas de la fenêtre et la position Y du plancher de départ du jeu
     */
    public void draw(GraphicsContext context, double fenetreY) {
        double yAffiche = this.y - fenetreY;
        context.setFill(this.color);
        context.fillRect(x, yAffiche, largeur, hauteur);
    }

    /**
     * Dessine l'affichage d'une plateforme sur le canevas selon une certaine couleur
     * @param context contexte graphique général du canevas de l'aplication
     * @param fenetreY différence entre la hauteur du bas de la fenêtre et la position Y du plancher de départ du jeu
     * @param color couleur d'affichage définie
     */
    public void draw(GraphicsContext context, double fenetreY, Color color) {
        double yAffiche = this.y - fenetreY;
        context.setFill(color);
        context.fillRect(x, yAffiche, largeur, hauteur);
    }

    /*
     * Getters et setters
     */
    public String getFonction() {
        return fonction;
    }

    public boolean getEnCollision() {
        return enCollision;
    }

    public void setEnCollision(boolean enCollision) {
        this.enCollision = enCollision;
    }

}
