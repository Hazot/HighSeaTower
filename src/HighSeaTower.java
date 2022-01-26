import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HighSeaTower extends Application {

    // Taille de l'application (résolution)
    private static final int WIDTH = 350, HEIGHT = 480;

    /**
     * fonction main
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Démarre les processus de l'application
     * @param primaryStage fenêtre de l'application
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialisation de la base de l'affichage graphique du jeu
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        Controller controller = new Controller();

        // Touches de l'utilisateur (Ajout de la touche R pour restart)
        scene.setOnKeyPressed((value) -> {
            switch (value.getCode()) {
                case ESCAPE:
                    Platform.exit();
                case LEFT:
                    controller.left();
                    break;
                case RIGHT:
                    controller.right();
                    break;
                case UP:
                case SPACE:
                    controller.jump();
                    break;
                case T:
                    controller.debug();
                    break;
                case R:
                    controller.restart();
            }
        });

        // Empêche la méduse de continuer son mouvement lorsqu'on lâche les touches
        scene.setOnKeyReleased((value) -> {
            switch (value.getCode()) {
                case LEFT:
                case RIGHT:
                    controller.stop();
                    break;
            }
        });

        // Timer de l'aplication calculant les "deltaTimes"
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            private final double maxDt = 0.01;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;

                while (deltaTime > maxDt) {
                    controller.update(maxDt);
                    deltaTime -= maxDt;
                }

                controller.update(deltaTime);
                controller.draw(context);

                lastTime = now;
            }

        };
        timer.start();

        // Paramètres de la fenêtre de l'application
        primaryStage.setTitle("                                 High Sea Tower");
        primaryStage.getIcons().add(new Image("/jellyfish1.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
