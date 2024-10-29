package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaInstrucciones implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Texture imagenMovimiento;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private boolean completado;
    NaveSeleccionada naveSeleccionada;

    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaInstrucciones(SpaceNavigation game, NaveSeleccionada naveSeleccionada, int ronda, int score,
                                 int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        this.completado = false;
        this.naveSeleccionada = naveSeleccionada;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

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
        System.out.println(naveSeleccionada.speed);

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

    public void mostrarControles() {
        boolean bottomLEFTPressed = false;
        boolean bottomRIGHTPressed = false;
        boolean bottomUPPressed = false;
        boolean bottomDOWNPressed = false;

        if (ronda == 1) {
            // Verifica los controles solo en la primera ronda
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bottomLEFTPressed = true;
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bottomRIGHTPressed = true;
            if(Gdx.input.isKeyPressed(Input.Keys.UP)) bottomUPPressed = true;
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) bottomDOWNPressed = true;

            if(bottomLEFTPressed && bottomRIGHTPressed && bottomUPPressed && bottomDOWNPressed) {
                completado = true;
            } else {
                batch.draw(imagenMovimiento, 250, 450, 700, 250);
            }
        } else {
            completado = true; // Avanzar automáticamente en rondas posteriores
        }
    }/*
            case 2:
            case 3:
            default:
        }
    }*/

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

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

        // Nivel completado
        if (completado) {
            Screen ss = new PantallaInstrucciones(game, naveSeleccionada, ronda + 1, score,
                velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 1);
            ss.resize(1280, 720);
            game.setScreen(ss);
            dispose();
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        imagenMovimiento = new Texture(Gdx.files.internal("Movimiento.png"));
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
    }
}
