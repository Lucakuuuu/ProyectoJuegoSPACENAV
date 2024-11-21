package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class ElegirNave implements Screen {
    private final SpaceNavigation game;
    private OrthographicCamera camera;
    private Texture naveImagen;
    private Sound cambioNaveSound;
    private int indiceNaveActual = 0;
    private Texture pauseBackground;
    private int seleccionActual = 0;
    private String[] opciones = {"Volver a la selección", "Volver al menú"};
    private boolean isPaused;
    private ScreenManager screenManager;

    private Nave[] naves = {
        new Nave("Falcon", 3, 5, "MainShip.png",
            "Rocket2.png", "pop-sound.mp3"),

        new Nave("Explorer", 6, 2, "Ship_2.png",
            "Ship_2_shoot.png", "shoot 3.mp3"),

        new Nave("Sonic", 1, 10, "Ship_3.png",
            "Ship_3_shoot.png", "shoot 3.mp3")
    };

    public ElegirNave(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        cargarNave(indiceNaveActual);
        cambioNaveSound = Gdx.audio.newSound(Gdx.files.internal("slash.mp3"));
        pauseBackground = new Texture(Gdx.files.internal("pause_background.png"));
        this.isPaused = false;
        screenManager = ScreenManager.getInstance(game);
    }

    // Clase Nave para almacenar los datos de cada nave
    class Nave {
        String nombre;
        int vida;
        int speed;
        String texturaNave;
        String texturaShoot;
        public String soundShoot;

        public Nave(String nombre, int vida, int speed, String texturaNave,
                    String texturaShoot, String soundShoot) {
            this.nombre = nombre;
            this.vida = vida;
            this.speed = speed;
            this.texturaNave = texturaNave;
            this.texturaShoot = texturaShoot;
            this.soundShoot = soundShoot;
        }
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

        // Dibuja la imagen de la nave en el centro
        game.getBatch().draw(naveImagen, 300, 150, 200, 200);

        // Dibuja el título y la información de la nave
        game.getFont().draw(game.getBatch(), "Nave " + (indiceNaveActual + 1) + " / " + naves.length, 50, 450);
        game.getFont().draw(game.getBatch(), "Nombre: " + naves[indiceNaveActual].nombre, 50, 400);
        game.getFont().draw(game.getBatch(), "Vida: " + naves[indiceNaveActual].vida, 50, 370);
        game.getFont().draw(game.getBatch(), "Velocidad: " + naves[indiceNaveActual].speed, 50, 340);

        game.getBatch().end();

        // Navegar entre naves usando las flechas derecha e izquierda
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            indiceNaveActual = (indiceNaveActual + 1) % naves.length;
            cargarNave(indiceNaveActual);
            cambioNaveSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            indiceNaveActual = (indiceNaveActual - 1 + naves.length) % naves.length;
            cargarNave(indiceNaveActual);
            cambioNaveSound.play();
        }

        // Seleccionar la nave actual con Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            System.out.println("Nave seleccionada: " + naves[indiceNaveActual].nombre);
            cambiarNave(naves[indiceNaveActual].vida, naves[indiceNaveActual].speed, naves[indiceNaveActual].texturaNave,
                naves[indiceNaveActual].texturaShoot, naves[indiceNaveActual].soundShoot);
        }
    }

    private void cargarNave(int indice) {
        if (naveImagen != null) {
            naveImagen.dispose();
        }
        naveImagen = new Texture(Gdx.files.internal(naves[indice].texturaNave));
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

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {
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
                    //gameMusic.play();
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
    @Override public void resume() {}

    void cambiarNave(int vida, int speed, String texturaNave, String texturaShoot, String soundShoot) {

        NaveSeleccionada naveSeleccionada = new NaveSeleccionada(vida, speed, texturaNave,
            texturaShoot, soundShoot);
        game.setNaveSeleccionada(naveSeleccionada);
        screenManager.showMainMenu();
    }
}