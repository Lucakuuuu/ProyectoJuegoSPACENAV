package puppy.code;

import java.util.ArrayList;
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

public class PantallaInstrucciones implements Screen {

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
    private Texture imagenMovimiento;
    private Texture imagenDisparo;
    private Texture imagenHUD;

    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    private BitmapFont font;
    private Texture pauseBackground;
    private boolean isPaused;
    private int seleccionActual = 0; // Mover seleccionActual a nivel de clase
    private String[] opciones = {"Volver al juego", "Cambiar nave", "Salir"};

    public PantallaInstrucciones(SpaceNavigation game, NaveSeleccionada naveSeleccionada, int ronda, int score,
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        this.naveSeleccionada = naveSeleccionada;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        pauseBackground = new Texture(Gdx.files.internal("pause_background.png"));

        // Cargar sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Inicializar la nave en el centro de la pantalla
        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, new Texture(Gdx.files.internal(naveSeleccionada.texturaNave)),
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
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 130, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    public void mostrarControles() {
        if (ronda == 1) {
            game.getBatch().draw(imagenMovimiento, 250, 450, 700, 250);
        } else if (ronda == 2) {
            game.getBatch().draw(imagenDisparo, 250, 450, 700, 250);
        } else if (ronda == 3) {
            game.getBatch().draw(imagenHUD, 250, 450, 700, 250);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        if(ronda == 3) dibujaEncabezado();
        mostrarControles();

        if (!nave.estaHerido()) {
            // Colisiones entre balas y asteroides
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {
                    if (b.checkCollision(balls1.get(j))) {
                        explosionSound.play();
                        balls1.remove(j);
                        balls2.remove(j);
                        j--;
                        score += 10;
                    }
                }

                if (b.isDestroyed()) {
                    balas.remove(b);
                    i--;
                }
            }

            // Actualizar movimiento de asteroides
            for (Ball2 ball : balls1) {
                ball.update();
            }

            // Colisiones entre asteroides y rebotes
            for (int i = 0; i < balls1.size(); i++) {
                Ball2 ball1 = balls1.get(i);
                for (int j = i + 1; j < balls2.size(); j++) {
                    Ball2 ball2 = balls2.get(j);
                    ball1.checkCollision(ball2);
                }
            }
        }

        // Dibujar balas
        for (Bullet b : balas) {
            b.draw(batch);
        }

        // Dibujar nave y verificar colisiones con asteroides
        nave.draw(batch, this);
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            b.draw(batch);

            if (nave.checkCollision(b)) {
                balls1.remove(i);
                balls2.remove(i);
                i--;
            }
        }

        // Comprobar si la nave ha sido destruida
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1280, 720);
            game.setScreen(ss);
            dispose();
        }

        batch.end();

        if(ronda == 1 && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            Screen ss = new PantallaInstrucciones(game, game.getNaveSeleccionada(), ronda + 1, score,
                velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 1);
            ss.resize(1280, 720);
            game.setScreen(ss);
            dispose();
        }
        if(ronda == 2 && balls1.size() == 0 && Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            Screen ss = new PantallaInstrucciones(game, game.getNaveSeleccionada(), ronda + 1, score,
                velXAsteroides + 0, velYAsteroides + 0, cantAsteroides + 4);
            ss.resize(1280, 720);
            game.setScreen(ss);
            dispose();
        }
        if(ronda == 3 && balls1.size() == 0){
            isPaused = !isPaused;
            if (isPaused) {
                gameMusic.pause();
            } else {
                gameMusic.play();
            }
            if (isPaused) {
                pause();
                return;
            }
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        imagenMovimiento = new Texture(Gdx.files.internal("Movimiento.png"));
        imagenDisparo = new Texture(Gdx.files.internal("Disparo.png"));
        imagenHUD = new Texture(Gdx.files.internal("HUD.png"));
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        if(ronda == 3){
            batch.begin();
            game.getBatch().draw(pauseBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.getFont().draw(game.getBatch(), "> Volver al menú", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 100);
            font.draw(batch, "FELICIDADES\nTUTORIAL COMPLETADO", Gdx.graphics.getWidth() / 2- 100, Gdx.graphics.getHeight() / 2);
            batch.end();
            if( Gdx.input.isKeyPressed(Input.Keys.ENTER)){
                Screen ss = new PantallaMenu(game);
                ss.resize(1280, 720);
                game.setScreen(ss);
                dispose();
            }
        }
        else {
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
                        // Implementar la lógica para cambiar nave
                        break;
                    case 2: // "Salir"
                        Screen ss = new PantallaMenu(game);
                        ss.resize(1280, 720);
                        game.setScreen(ss);
                        dispose();
                        break;
                }
            }

            // Dibuja el encabezado "PAUSA"
            font.draw(batch, "PAUSA", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 100);
            batch.end();
        }
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
        /* Liberación adicional de recursos de texturas y sonidos de la nave y asteroides
        nave.dispose();
        for (Ball2 ball : balls1) {
            ball.dispose();
        }
        for (Bullet bala : balas) {
            bala.dispose();
        }
         */
    }
}
