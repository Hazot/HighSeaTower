package Entities;

import javafx.scene.paint.Color;

public abstract class Entities {

    // Resolution
    protected static final int WIDTH = 350, HEIGHT = 480;

    // Attributs présents dans la "majorité" des sous-classes
    protected double largeur, hauteur;
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;
    protected Color color;

    /**
     * Met à jour la position et la vitesse de l'entité
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;

        // Fait rebondir la méduse lorsqu'elle entre en contact avec les murs
        if (x + largeur > WIDTH || x < 0) {
            vx *= -1;
        }

        // Force à rester dans les bornes de l'écran
        x = Math.min(x, WIDTH - largeur);
        x = Math.max(x, 0);
    }

    /*
     * Getters and setters utilisés
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public Color getColor() {
        return color;
    }
}
