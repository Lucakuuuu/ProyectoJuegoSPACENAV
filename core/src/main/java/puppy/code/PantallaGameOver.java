package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;

    private ScreenManager screenManager;

    private int seleccionActual = 0;
    private String[] opciones = {"Empezar de nuevo", "Cambiar Nave", "Volver al menú"};
    private int identificacion;

    public PantallaGameOver(SpaceNavigation game, int identificacion) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720); // Resolución ajustada a 1280x720
        this.screenManager = ScreenManager.getInstance(game);
        this.identificacion = identificacion;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);  // Fondo de la pantalla

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();

        // Título grande "PERDISTE"
        game.getFont().getData().setScale(3f);  // Hacer el texto más grande
        game.getFont().draw(game.getBatch(), "PERDISTE", 440, 500);  // Centrado ajustado

        // Dibujar las opciones y resaltar la seleccionada
        game.getFont().getData().setScale(2f);  // Reducir el tamaño de la fuente para las opciones
        for (int i = 0; i < opciones.length; i++) {
            if (i == seleccionActual) {
                // Resaltar la opción seleccionada
                game.getFont().draw(game.getBatch(), "> " + opciones[i], 500, 300 - i * 50);
            } else {
                game.getFont().draw(game.getBatch(), opciones[i], 500, 300 - i * 50);
            }
        }

        game.getBatch().end();

        // Navegar entre las opciones con las teclas de flecha
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccionActual = (seleccionActual - 1 + opciones.length) % opciones.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccionActual = (seleccionActual + 1) % opciones.length;
        }

        // Seleccionar opción con la tecla Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (seleccionActual) {
                case 0: // "Empezar de nuevo"
                    switch (identificacion) {
                        case 1:
                            screenManager.showGameScreen();
                            break;
                        case 2:
                            screenManager.showPracticeScreen();
                            break;
                    }
                    break;
                case 1: // "Cambiar Nave"
                    screenManager.showElegirNaveScreen();  // Puedes cambiar este a una pantalla de selección de nave si lo prefieres
                    break;
                case 2: // "Salir"
                    screenManager.showMainMenu();
                    break;
            }
            dispose();  // Liberar recursos antes de cambiar de pantalla
        }
    }

    @Override
    public void show() {
        // No se requiere acción específica en 'show'
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // No se requiere acción específica en 'pause'
    }

    @Override
    public void resume() {
        // No se requiere acción específica en 'resume'
    }

    @Override
    public void hide() {
        dispose();  // Liberar recursos al ocultar esta pantalla
    }

    @Override
    public void dispose() {
        // Liberación adicional de recursos si es necesario
    }
}
