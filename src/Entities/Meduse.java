package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Meduse extends Entities {

    // Attributs pour afficher le "sprite" de la méduse
    private Image[] framesD;
    private Image[] framesG;
    private Image imageD;
    private Image imageG;
    private double frameRate = 8; // 8 frame par sec
    private double tempsTotal = 0;
    boolean orienteDroite = true;

    // Paramètre liés aux collisions
    private boolean parterre = true;
    private boolean jaune = false;

    /**
     * Constructeur de méduse avec 2 paramètres++
     * @param x position en x
     * @param y position en y
     */
    public Meduse(double x, double y) {
        this.x = x;
        this.y = y;
        this.largeur = 50;
        this.hauteur = 50;
        this.ay = 1200;

        // Chargement des images de droite
        framesD = new Image[] {
                new Image("/jellyfish1.png", 50, 50, true, false),
                new Image("/jellyfish2.png", 50, 50, true, false),
                new Image("/jellyfish3.png", 50, 50, true, false),
                new Image("/jellyfish4.png", 50, 50, true, false),
                new Image("/jellyfish5.png", 50, 50, true, false),
                new Image("/jellyfish6.png", 50, 50, true, false),
        };
        imageD = framesD[0];

        // Chargement des images de gauche
        framesG = new Image[] {
                new Image("/jellyfish1g.png", 50, 50, true, false),
                new Image("/jellyfish2g.png", 50, 50, true, false),
                new Image("/jellyfish3g.png", 50, 50, true, false),
                new Image("/jellyfish4g.png", 50, 50, true, false),
                new Image("/jellyfish5g.png", 50, 50, true, false),
                new Image("/jellyfish6g.png", 50, 50, true, false),
        };
        imageG = framesG[0];
    }

    /**
     * Mise à jour de l'affichage de la méduse
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        // Physique du personnage
        super.update(dt);
        setOrienteDroite();

        // Mise à jour de l'image affichée
        tempsTotal += dt;
        int frame = (int) (tempsTotal * frameRate);

        imageD = framesD[frame % framesD.length];
        imageG = framesG[frame % framesG.length];
    }

    /**
     * Dessine l'affichage de la méduse sur le canevas
     * @param context contexte graphique général du canevas de l'aplication
     * @param fenetreY différence entre la hauteur du bas de la fenêtre et la position Y du plancher de départ du jeu
     */
    public void draw(GraphicsContext context, double fenetreY) {
        if (orienteDroite) {
            context.drawImage(imageD, x, y - fenetreY, largeur, hauteur);
        } else {
            context.drawImage(imageG, x, y - fenetreY, largeur, hauteur);
        }
    }

    /**
     * Permet de n'afficher que l'image de la méduse et non son sprite avant de bouger (ou au départ de l'application)
     * @param context contexte graphique général du canevas de l'aplication
     */
    public void startingPoint(GraphicsContext context) { context.drawImage(framesD[1], x, y, largeur, hauteur); }

    /**
     * Oriente la méduse vers la direction à laquelle elle se dirige et
     * la laisse dans sa dernière orientation si sa vitesse en x est de 0
     */
    public void setOrienteDroite() {
        if (super.vx > 0) {
            orienteDroite = true;
        } else if (super.vx < 0) {
            orienteDroite = false;
        }
    }

    /**
     * Gère les collisions pour tous les types de plateformes
     * @param other plateforme en train d'être évalué en fonction de la méduse
     */
    public void testCollision(Plateforme other) {

        // Plateformes solides (rouges)
        if ("solide".equals(other.getFonction()) && intersects(other)
                && Math.abs(this.y + hauteur - other.y) > 10 && vy < 0) {
            if ((this.x + largeur < other.x || this.x < other.x + other.largeur)
                    && (this.y < other.y - other.hauteur || this.y - this.hauteur > other.y)) {
                other.setEnCollision(true);
                this.vx *= -1;
                return;
            }
            other.setEnCollision(true);
            this.jaune = false;
            this.vy = 0;
            this.parterre = false;

            return;
        }

        if(intersects(other) && Math.abs(this.y + hauteur - other.y) < 10 && vy > 0) {

            // plateformes rebondissantes (vertes)
            if ("rebondissante".equals(other.getFonction())) {
                other.setEnCollision(true);
                this.jaune = false;
                this.parterre = true;
                if (vy >= 100) {
                    vy *= -1.5;
                } else {
                    vy = 100 * -1.5;
                }

                // plateformes accélérantes (jaunes)
            } else if ("accelerante".equals(other.getFonction())) {
                other.setEnCollision(true);
                this.jaune = true;
                pushOut(other);
                // plateformes normales (oranges)
            } else {
                other.setEnCollision(true);
                this.jaune = false;
                pushOut(other);
            }
        }
    }

    /**
     * Évalue si une plateforme de la méduse s'entrecroiserait
     * @param other plateforme en évaluation
     * @return true si il y a un intersection entre la méduse et une plateforme
     */
    public boolean intersects(Plateforme other) {
        return !(x + largeur < other.x
                        || other.x + other.largeur < this.x
                        || y + hauteur < other.y
                        || other.y + other.hauteur < this.y);
    }

    /**
     * Repousse le personnage vers le haut (sans déplacer la plateforme)
     * @param other plateforme en collision avec la méduse
     */
    public void pushOut(Plateforme other) {
        this.y = other.y - this.hauteur;
        this.vy = 0;
        this.parterre = true;
    }

    /*
     * Les prochaines fonctions permettent de gérer les inputs de l'utilisateur face au jeu
     */
    public void jump() {
        if (parterre) {
            vy = -600;
        }
    }

    public void droite() { ax = -1200; }

    public void gauche() { ax = 1200; }

    public void stop() { ax = 0; vx = 0; }

    /*
     * Getters and setters utilisés
     */
    public void setParterre(boolean parterre) {
        this.parterre = parterre;
    }

    public boolean getParterre() {
        return parterre;
    }

    public boolean getJaune() { return this.jaune; }

    public void setJaune(boolean jaune) {
        this.jaune = jaune;
    }
}
