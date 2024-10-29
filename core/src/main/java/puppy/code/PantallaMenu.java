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
    private Texture tituloImagen;

    // Opciones del menú
    private String[] opciones = {"Jugar", "Aprender a jugar", "Elegir Nave", "Cerrar Juego"};
    private int seleccionActual = 0; // Índice de la opción seleccionada actualmente

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        // Dibujar la imagen del título
        if (tituloImagen != null) {
            game.getBatch().draw(tituloImagen, 250, 450, 700, 250); // Ajustar según sea necesario
        }

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

        // Navegación de opciones con las teclas de flecha
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccionActual = (seleccionActual - 1 + opciones.length) % opciones.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccionActual = (seleccionActual + 1) % opciones.length;
        }

        // Seleccionar opción con Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (seleccionActual) {
                case 0: // "Jugar"
                    game.setScreen(new PantallaJuego(game, game.getNaveSeleccionada(), 1, 0, 1, 1, 10));
                    dispose();
                    break;
                case 1: // "Aprender a jugar"
                    // Se podría implementar una pantalla adicional para mostrar instrucciones
                    game.setScreen(new PantallaInstrucciones(game, game.getNaveSeleccionada(), 1, 0, 0, 0, 0));
                    dispose();
                    break;
                case 2:
                    game.setScreen(new elegirNave(game));
                    dispose();
                    break;
                case 3: // "Cerrar Juego"
                    Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        // Liberar recursos si la imagen se ha cargado
        if (tituloImagen != null) {
            tituloImagen.dispose();
        }
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
