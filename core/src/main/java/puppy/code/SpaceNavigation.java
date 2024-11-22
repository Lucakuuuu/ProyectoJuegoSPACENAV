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
    private NaveSeleccionada naveSeleccionada; // Almacena la nave seleccionada
    private ScreenManager screenManager; // Singleton para manejar pantallas

    @Override
    public void create() {
        // Inicialización
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);

        // Cargar High Score
        loadHighScore();

        // Inicializar la nave seleccionada con el Builder
        naveSeleccionada = new NaveSeleccionada.Builder()
            .setNombre("Default Ship")
            .setVida(3)
            .setSpeed(3)
            .setTexturaNave("MainShip.png")
            .setTexturaShoot("Rocket2.png")
            .setSoundShoot("pop-sound.mp3")
            .build();

        // Configurar ScreenManager
        screenManager = ScreenManager.getInstance(this);
        screenManager.showMainMenu(); // Mostrar menú principal
    }

    @Override
    public void render() {
        super.render(); // Llama al renderizado de la pantalla activa
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    // Getters
    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHighScore() {
        return highScore;
    }

    public NaveSeleccionada getNaveSeleccionada() {
        return naveSeleccionada;
    }

    // Setters
    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    public void setNaveSeleccionada(NaveSeleccionada naveSeleccionada) {
        this.naveSeleccionada = naveSeleccionada;
    }

    // Manejo de High Score con Preferences
    private void saveHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putInteger(HIGH_SCORE_KEY, highScore);
        prefs.flush();
    }

    private void loadHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        highScore = prefs.getInteger(HIGH_SCORE_KEY, 0); // Por defecto, inicia en 0
    }

    // Cambio de pantallas con manejo de recursos
    public void changeScreen(Screen newScreen) {
        if (getScreen() != null) {
            getScreen().dispose(); // Libera recursos de la pantalla anterior
        }
        setScreen(newScreen);
    }
}
