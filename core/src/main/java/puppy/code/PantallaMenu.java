package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Texture tituloImagen;  // Textura para la imagen del título

    // Opciones del menú
    private String[] opciones = {"Jugar", "Aprender a jugar", "Cerrar Juego"};
    private int seleccionActual = 0; // Índice de la opción seleccionada actualmente

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        // Dibujar la imagen del título
        game.getBatch().draw(tituloImagen, 250, 280, 700, 500); // Ajustar las posiciones y tamaños según sea necesario

        // Dibujar las opciones del menú
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccionActual) {
                // Resaltar la opción seleccionada
                game.getFont().draw(game.getBatch(), "> " + opciones[i], 500, 400 - i * 50);
            } else {
                game.getFont().draw(game.getBatch(), opciones[i], 500, 400 - i * 50);
            }
        }
        game.getBatch().end();

        // Navegar con las flechas
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccionActual = (seleccionActual - 1 + opciones.length) % opciones.length; // Ir hacia arriba
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccionActual = (seleccionActual + 1) % opciones.length; // Ir hacia abajo
        }

        // Seleccionar opción con Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (seleccionActual) {
                case 0: // "Jugar"
                    game.setScreen(new PantallaJuego(game, 1, 3, 0, 1, 1, 10));
                    dispose();
                    break;
                case 1: // "Aprender a jugar"
                    // Aquí podrías crear otra pantalla con instrucciones
                    System.out.println("Mostrando instrucciones para jugar...");
                    break;
                case 2: // "Cerrar Juego"
                    Gdx.app.exit(); // Cerrar el juego
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        // Liberar los recursos de la imagen
        tituloImagen.dispose();
    }

    @Override
    public void show() {
        tituloImagen = new Texture(Gdx.files.internal("SPACE-NAV_1.png"));
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
