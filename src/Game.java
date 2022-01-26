
import Entities.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.LinkedList;

public class Game {

    // Taille de l'application (résolution)
    public static final int WIDTH = 350, HEIGHT = 480;

    // Initialisation des entités
    private LinkedList<Plateforme> plateformes;
    private LinkedList<Bulle> bulles;
    private Meduse meduse;

    // Origine par rapport à la caméra
    private double fenetreY;
    private double fenetreVY = -50;
    private final double fenetreAY = -2;

    // Miscellaneous game things
    private boolean alive = false;
    private boolean debugStatus = false;
    private int nbPlatGen = 0;
    private double timeAlive = 0;
    private boolean bullesActives = false;

    /**
     * Génère le minimums d'entités possible pour que le jeu soit fonctionnel dès le départ
     */
    public Game() {

        plateformes = new LinkedList<>();
        bulles = new LinkedList<>();

        //plateforme de départ
        plateformes.add(new Plateforme(0, HEIGHT, WIDTH, Color.rgb(230, 134, 58)));

        // Créé 5 premières plateformes à des positions aléatoires dans les dimensions du niveau
        for (int i = 1; i < 5; i++) {
            nbPlatGen++;
            double largeurRandom = Math.random()*95+80;
            // On peut regarder dans l'array seulement s'il y a au moins une plateforme dans l'array
            if (i > 1 && Color.rgb(184, 15, 36).equals(plateformes.getLast().getColor())) {
                Plateforme p = new Plateforme((WIDTH - largeurRandom) * Math.random(), 480 - 100 * i, largeurRandom, true);
                plateformes.add(p);
            } else {
                Plateforme p = new Plateforme((WIDTH - largeurRandom) * Math.random(), 480 - 100 * i, largeurRandom, false);
                plateformes.add(p);
            }
        }

        // Génère les premières bulles dans le niveau
        ajouterBulles();

        //Initialise la méduse en bas de l'écran
        meduse = new Meduse(150, 430);
    }

    /**
     * Méthode qui ajoute 3 groupes de bulles aléatoires (soit 15 bulles) à une linkedList lorsque appelée
     */
    public void ajouterBulles() {
        // Génère 3 groupes de bulles avec des coordonnées de bases aléatoire
        for (int i = 1; i < 4; i++) {
            double baseX = Math.random() * WIDTH;
            // Génère 5 bulles de bulles sous le niveau
            for (int j = 1; j < 6; j++) {
                double randomX = baseX - 20 + 40 * Math.random();
                Bulle b = new Bulle(randomX);
                bulles.add(b);
            }
        }
    }

    /**
     * Méthode qui génère des bulles après chaque 3 secondes
     */
    public void genererBulles() {
        if ((int) timeAlive % 3 == 0 && !bullesActives) {
            bullesActives = true;
            ajouterBulles();
        }
        if ((int) timeAlive % 3 == 1) {
            bullesActives = false;
        }
    }

    /**
     *  Méthode qui permet de mettre à jour la position des graphiques par rapport au temps écoulé
     * @param dt temps écoulé depuis le dernier "update"
     */
    public void update(double dt) {

        if(alive) {

            // Temps en vie de la méduse
            timeAlive += dt;

            // Reset l'attribut parterre qui sera réévaluer par la suite
            meduse.setParterre(false);

            // On évalue les collisions pour chaque plateformes
            for (int i = 0; i < plateformes.size(); i++) {
                Plateforme p = plateformes.get(i);
                p.setEnCollision(false);

                // Si le personnage se trouve sur une plateforme, ça sera défini ici et parterre sera true
                meduse.testCollision(p);

                // Reset le paramètre boolean a false voulant dire que la méduse ne touche plus à jaune.
                if (!meduse.getParterre()) {
                    meduse.setJaune(false);
                }
            }

            // Ajoute des bulles
            genererBulles();

            // Update la position des bulles
            for (Bulle b : bulles) {
                b.update(dt);
            }

            // Pour gérer la mémoire des plateformes/bulles, mettre à jour la position de la meduse et de la caméra
            memoryManager();
            meduse.update(dt);

            // Monte l'écran à la même vitesse que la méduse quand elle risque d'être affichée à plus de 75% de hauteur
            if (meduse.getY() - fenetreY < HEIGHT*0.25 && meduse.getVy() < 0) {
                fenetreY += dt * meduse.getVy();
            }

            // Empèche la fenêtre de monter en mode debug
            if (debugStatus == false) {
                fenetreY += dt * fenetreVY * jaune();
                fenetreVY += dt * fenetreAY;
            }

            // Permet à la méduse de mourir lorsqu'elle sort de l'écran vers le bas
            if (meduse.getY() - fenetreY > HEIGHT) {
                Controller.restart();
            }
        }
    }

    /**
     * Optimise la mémoire en effaçant les éléments non affichés.
     * Génère la plateformes au fur et à mesure que la méduse monte.
     */
    public void memoryManager() {

        // Enlève une plateforme qui se trouve 60 pixels en dessous de l'affichage
        if (plateformes.get(0).getY() - fenetreY > HEIGHT + meduse.getLargeur() + 10) {
            plateformes.removeFirst();
        }

        // Rajoute une plateforme lorsqu'il manque 10 pixels avant de voir apparaître une nouvelle plateforme
        if (plateformes.get(plateformes.size()-1).getY() - fenetreY > 90) {
            nbPlatGen++;
            double largeurRandom = Math.random() * 95 + 80;
            if (Color.rgb(184, 15, 36).equals(plateformes.getLast().getColor())) {
                Plateforme p = new Plateforme((WIDTH - largeurRandom) * Math.random(), 480 - 100 * nbPlatGen,
                        largeurRandom, true);
                plateformes.add(p);
            } else {
                Plateforme p = new Plateforme((WIDTH - largeurRandom) * Math.random(), 480 - 100 * nbPlatGen,
                        largeurRandom, false);
                plateformes.add(p);
            }
        }

        /**
         * Enlève les bulles lorsqu'elles sont 900 pixels au dessus de l'affichage,
         * ainsi il y a tout le temps au moins une bulle dans la liste de bulles
         */
        for (int i = 0; i < bulles.size(); i++) {
            if (bulles.get(i).getY() < -900) {
                bulles.remove(i);
            }
        }
    }

    /**
     * Retourne un multiplicateur permettant de gérer la vitesse sur une plateforme jaune
     * @return (Multiplicateur de vitesse) retourne la valeur 3 si la méduse touche à une plateforme jaune et 1 sinon.
     */
    public int jaune() {
        if(meduse.getJaune()) {
            return 3;
        } else {
            return 1;
        }
    }

    /**
     * Cette méthode permet d'afficher tous les éléments du jeu à la suite de la mise à jour
     * @param context Contexte graphique général du jeu permettant l'affichage sur le canevas
     */
    public void draw(GraphicsContext context) {

        // Affiche le fond marin
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        // Affiche les bulles
        for (Bulle b : bulles) {
            b.draw(context);
        }

        /**
         * Si la méduse a au moins bougé une fois, la partie commence et on
         * affiche alors la méduse animée au lieu de l'image simple.
         **/
        if (alive) {
            meduse.draw(context, fenetreY);
        } else {
            meduse.startingPoint(context);
        }

        // On affiche les plateformes
        for (Plateforme p : plateformes) {
            if (debugStatus && p.getEnCollision()) {
                p.draw(context, fenetreY, Color.YELLOW);
            } else {
                p.draw(context, fenetreY);
            }
        }

        // Affiche le mode débug s'il est activé
        if(debugStatus) {
            context.setTextAlign(TextAlignment.LEFT);
            context.setFill(Color.rgb(255, 0, 0, 0.4));
            context.fillRect(meduse.getX(), meduse.getY() - fenetreY, meduse.getLargeur(), meduse.getHauteur());
            context.setFill(Color.WHITE);
            context.setFont(Font.font("Verdana", 12));
            context.fillText("  Position = (" + (int) Math.abs(meduse.getX()) + ", " +
                    (int) Math.abs(meduse.getY()- HEIGHT + meduse.getLargeur()) + ")", 5, 15);
            context.fillText("  v = (" + (int) meduse.getVx() + ", " + (int) -meduse.getVy() + ")", 5, 30);
            context.fillText("  a = (" + (int) meduse.getAx() + ", " + (int) -meduse.getAy() + ")", 5, 45);
            if (meduse.getParterre()) {
                context.fillText("  Touche le sol : oui", 5,60);
            } else {
                context.fillText("  Touche le sol : non", 5,60);
            }
        }

        /**
         * Compteur de mètres en haut de la fenêtre ("score")
         */
        context.setFill(Color.WHITE);
        context.setFont(Font.font("Verdana", 26));
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText((int) Math.abs(fenetreY) + "m", 175, 50);
    }

    /**
     * Les prochaines fonctions permettent de gérer les inputs de l'utilisateur face au jeu.
     * De plus, les méthodes jump, gauche et droite permettent de mettre la méduse en vie et de démarrer la partie
     */
    public void jump() {
        meduse.jump();
        alive = true;
    }

    public void gauche() {
        meduse.droite();
        alive = true;
    }

    public void droite() {
        meduse.gauche();
        alive = true;
    }

    public void stop() {
        meduse.stop();
    }

    public void modeDebug() {
        debugStatus = !debugStatus;
    }
}
