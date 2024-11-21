package puppy.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import strategies.PowerUpStrategy;
import strategies.ShieldStrategy;
import strategies.SpeedBoostStrategy;

public class PantallaJuego implements Screen {

    private Random random;
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private NaveSeleccionada naveSeleccionada;
    private List<PowerUp> powerUps;

    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    private BitmapFont font;
    private Texture pauseBackground;
    private boolean isPaused;
    private int seleccionActual = 0;
    private String[] opciones = {"Volver al juego", "Cambiar nave", "Volver al menú"};
    private ArrayList<Updatable> updatables;

    // Agregar instancia de ScreenManager
    private ScreenManager screenManager;

    public PantallaJuego(SpaceNavigation game, NaveSeleccionada naveSeleccionada, int ronda, int score,
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        this.naveSeleccionada = naveSeleccionada;
        this.powerUps = new ArrayList<>();
        this.random = new Random();
        updatables = new ArrayList<>();
        pauseBackground = new Texture(Gdx.files.internal("pause_background.png"));

        Texture[] shieldFrames = { new Texture("shield1.png"), new Texture("shield2.png") };
        PowerUpStrategy shieldStrategy = new ShieldStrategy();
        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(generateRandomX(), generateRandomY(), 10f, shieldFrames, shieldStrategy);

        Texture[] speedFrames = { new Texture("Speed1.png"), new Texture("Speed2.png") };
        PowerUpStrategy speedBoostStrategy = new SpeedBoostStrategy(2.0f);
        SpeedBoostPowerUp speedBoostPowerUp = new SpeedBoostPowerUp(generateRandomX(), generateRandomY(), 10f, speedFrames, speedBoostStrategy);

        // Agrega ambos power-ups a la lista de `updatables`
        updatables.add(shieldPowerUp);
        updatables.add(speedBoostPowerUp);

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        // Cargar sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Inicializar la nave en el centro de la pantalla
        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
            new Texture(Gdx.files.internal(naveSeleccionada.texturaNave)),
            Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
            new Texture(Gdx.files.internal(naveSeleccionada.texturaShoot)),
            Gdx.audio.newSound(Gdx.files.internal(naveSeleccionada.soundShoot)),
            naveSeleccionada.speed);
        nave.setVidas(naveSeleccionada.vida);

        // Crear asteroides dentro de los límites de la pantalla y con velocidades iniciales no nulas
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            int xPosition = r.nextInt(Gdx.graphics.getWidth() - 40) + 20; // evitando los bordes
            int yPosition = 50 + r.nextInt(Gdx.graphics.getHeight() - 100);
            int xSpeed = velXAsteroides + r.nextInt(4) + 1; // asegurarse de que no sea cero
            int ySpeed = velYAsteroides + r.nextInt(4) + 1;
            Ball2 bb = new Ball2(xPosition, yPosition, 20 + r.nextInt(10), xSpeed, ySpeed,
                new Texture(Gdx.files.internal("aGreyMedium4.png")));
            balls1.add(bb);
            balls2.add(bb);
        }

        // Inicializar ScreenManager
        screenManager = ScreenManager.getInstance(game);
    }

    private float generateRandomX() {
        return random.nextInt(Gdx.graphics.getWidth() - 32);
    }

    private float generateRandomY() {
        return random.nextInt(Gdx.graphics.getHeight() - 32);
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 130, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pausar o reanudar música si se presiona ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                gameMusic.pause();
            } else {
                gameMusic.play();
            }
        }

        if (isPaused) {
            pause();
            return;
        }

        batch.begin();
        dibujaEncabezado();

        for (Updatable updatable : updatables) {
            updatable.update(delta);

            if (updatable instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) updatable;

                if (powerUp.isActive()) {
                    powerUp.draw(batch);

                    // Detectar colisión con la nave
                    if (powerUp.getBounds().overlaps(nave.getBounds())) {
                        powerUp.ejecutar(delta, nave);
                        powerUp.setActive(false); // Desactiva por 10 segundos después de ser usado
                    }
                }
            }
        }

        // Dibujar y actualizar estado de la nave, balas y asteroides
        nave.draw(batch, this);

        // Dibujar y actualizar balas
        for (Bullet b : balas) {
            b.update();
            b.draw(batch);

            for (int j = 0; j < balls1.size(); j++) {
                if (b.checkCollision(balls1.get(j))) {
                    explosionSound.play();
                    balls1.remove(j);
                    balls2.remove(j);
                    score += 10;
                    j--;
                }
            }
        }

        // Dibujar y actualizar asteroides
        for (Ball2 ball : balls1) {
            ball.update();
            ball.draw(batch);
        }

        // Comprobar colisiones entre la nave y los asteroides
        for (int i = 0; i < balls1.size(); i++) {
            if (nave.checkCollision(balls1.get(i))) {
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }

        // Comprobar si la nave está destruida
        if (nave.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            gameMusic.stop();
            screenManager.showGameOverScreen(1);
            dispose();
        }

        batch.end();

        // Pasar a la siguiente ronda si todos los asteroides fueron eliminados
        if (balls1.size() == 0) {
            screenManager.showNextRoundGame(ronda + 1, score, velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
            dispose();
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        batch.begin();
        batch.draw(pauseBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Dibujar las opciones del menú
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccionActual) {
                // Resaltar la opción seleccionada
                game.getFont().draw(game.getBatch(), "> " + opciones[i], 500, 400 - i * 50);
            } else {
                game.getFont().draw(game.getBatch(), opciones[i], 500, 400 - i * 50);
            }
        }

        // Navegación de opciones con las teclas de flecha
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccionActual = (seleccionActual - 1 + opciones.length) % opciones.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccionActual = (seleccionActual + 1) % opciones.length;
        }

        // Seleccionar opción con la tecla Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (seleccionActual) {
                case 0: // "Volver al juego"
                    isPaused = false;
                    gameMusic.play();
                    break;
                case 1: // "Cambiar nave"
                    screenManager.showElegirNaveScreen();
                    dispose();
                    break;
                case 2: // "Salir"
                    screenManager.showMainMenu();
                    dispose();
                    break;
            }
        }

        // Dibuja el encabezado "PAUSA"
        font.draw(batch, "PAUSA", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 100);
        batch.end();
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        explosionSound.dispose();
    }
}
