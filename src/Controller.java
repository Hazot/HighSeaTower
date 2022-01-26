
import javafx.scene.canvas.GraphicsContext;

public class Controller {

    private static Game game;

    public Controller() {
        game = new Game();
    }

    public void draw(GraphicsContext context) { game.draw(context); }

    public void update(double deltaTime) { game.update(deltaTime); }

    // Les 6 prochaines fonctions gèrent les inputs de l'utilisateur
    public void jump() { game.jump(); }

    public void left() { game.gauche(); }

    public void right() { game.droite(); }

    public void debug() { game.modeDebug(); }

    public void stop() { game.stop(); }

    public static void restart() { game = new Game(); }
}
