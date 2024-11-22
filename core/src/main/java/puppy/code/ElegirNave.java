package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class elegirNave implements Screen {

    private final SpaceNavigation game;
    private final Texture pauseBackground;
    private OrthographicCamera camera;
    private Texture naveImagen;
    private Sound cambioNaveSound;
    private int indiceNaveActual = 0;
    private NaveSeleccionada[] naves;
    ScreenManager screenManager;
    private int seleccionActual = 0;
    private String[] opciones = {"Volver a la selección", "Volver al menú"};
    private boolean isPaused;

    public elegirNave(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Inicializar naves con Builder
        naves = new NaveSeleccionada[]{
            new NaveSeleccionada.Builder()
                .setNombre("Falcon")
                .setVida(3)
                .setSpeed(5)
                .setTexturaNave("MainShip.png")
                .setTexturaShoot("Rocket2.png")
                .setSoundShoot("pop-sound.mp3")
                .build(),
            new NaveSeleccionada.Builder()
                .setNombre("Explorer")
                .setVida(6)
                .setSpeed(2)
                .setTexturaNave("Ship_2.png")
                .setTexturaShoot("Ship_2_shoot.png")
                .setSoundShoot("shoot 3.mp3")
                .build(),
            new NaveSeleccionada.Builder()
                .setNombre("Sonic")
                .setVida(1)
                .setSpeed(10)
                .setTexturaNave("Ship_3.png")
                .setTexturaShoot("Ship_3_shoot.png")
                .setSoundShoot("shoot 3.mp3")
                .build()
        };

        cargarNave(indiceNaveActual);
        cambioNaveSound = Gdx.audio.newSound(Gdx.files.internal("slash.mp3"));
        // Inicializar ScreenManager
        screenManager = ScreenManager.getInstance(game);
        this.pauseBackground = new Texture(Gdx.files.internal("pause_background.png"));
        this.isPaused = false;
    }

    private void cargarNave(int indice) {
        if (naveImagen != null) {
            naveImagen.dispose();
        }
        naveImagen = new Texture(Gdx.files.internal(naves[indice].getTexturaNave()));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pausar o reanudar música si se presiona ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                //gameMusic.pause();
            } else {
                //gameMusic.play();
            }
        }

        if (isPaused) {
            pause();
            return;
        }

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        // Dibujar la nave y su información
        game.getBatch().draw(naveImagen, 300, 50, 200, 200);
        game.getFont().draw(game.getBatch(), "Selección de Naves", 300, 450);
        game.getFont().draw(game.getBatch(), "Para seleccionar una nave, Presiona ENTER", 300, 420);
        game.getFont().draw(game.getBatch(), "Nombre: " + naves[indiceNaveActual].getNombre(), 300, 380);
        game.getFont().draw(game.getBatch(), "Vida: " + naves[indiceNaveActual].getVida(), 300, 340);
        game.getFont().draw(game.getBatch(), "Velocidad: " + naves[indiceNaveActual].getSpeed(), 300, 300);
        game.getBatch().end();

        // Navegar entre naves
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            indiceNaveActual = (indiceNaveActual + 1) % naves.length;
            cargarNave(indiceNaveActual);
            cambioNaveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            indiceNaveActual = (indiceNaveActual - 1 + naves.length) % naves.length;
            cargarNave(indiceNaveActual);
            cambioNaveSound.play();
        }

        // Seleccionar la nave actual
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            NaveSeleccionada naveActual = naves[indiceNaveActual];
            game.setNaveSeleccionada(naveActual);
            screenManager.showMainMenu();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        if (naveImagen != null) naveImagen.dispose();
        if (cambioNaveSound != null) cambioNaveSound.dispose();
    }

    @Override
    public void show() {}
    @Override
    public void hide() {}
    @Override
    public void pause() {
        game.getBatch().begin();
        game.getBatch().draw(pauseBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
                    break;
                case 1: // "Salir"
                    screenManager.showMainMenu();
                    dispose();
                    break;
            }
        }

        // Dibuja el encabezado "PAUSA"
        game.getFont().draw(game.getBatch(), "PAUSA", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 100);
        game.getBatch().end();
    }
    @Override
    public void resume() {}
}
