package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceNavigation extends Game {
    private static final String PREFS_NAME = "space_navigation_prefs";
    private static final String HIGH_SCORE_KEY = "high_score";

    private SpriteBatch batch;
    private BitmapFont font;
    private int highScore;
    private NaveSeleccionada naveSeleccionada; // Atributo para almacenar la nave seleccionada

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Usa Arial font por defecto
        font.getData().setScale(2f);

        loadHighScore(); // Carga el high score al inicio
        naveSeleccionada = new NaveSeleccionada(3, 3, "MainShip.png",
            "Rocket2.png", "pop-sound.mp3"); // Inicializa con valores predeterminados o vacíos
        setScreen(new PantallaMenu(this)); // Muestra el menú principal
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    // Getter y Setter para el high score
    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    // Métodos para guardar y cargar el high score usando Preferences
    private void saveHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putInteger(HIGH_SCORE_KEY, highScore);
        prefs.flush();
    }

    private void loadHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        highScore = prefs.getInteger(HIGH_SCORE_KEY, 0); // Cargar o iniciar en 0 si no existe
    }

    // Método auxiliar para cambiar la pantalla
    public void changeScreen(Screen newScreen) {
        if (getScreen() != null) {
            getScreen().dispose(); // Libera la pantalla actual
        }
        setScreen(newScreen); // Cambia a la nueva pantalla
    }

    // Getter y Setter para naveSeleccionada
    public NaveSeleccionada getNaveSeleccionada() {
        return naveSeleccionada;
    }

    public void setNaveSeleccionada(NaveSeleccionada naveSeleccionada) {
        this.naveSeleccionada = naveSeleccionada;
    }
}

