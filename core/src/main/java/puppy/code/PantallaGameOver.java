package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;

    public PantallaGameOver(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720); // Resolución ajustada a 1280x720
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Game Over !!!", 440, 400); // Centrado ajustado para 1280x720
        game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar...", 400, 300);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaJuego(game, game.getNaveSeleccionada(), 1, 0, 1, 1, 10));
            dispose(); // Liberar recursos de la pantalla actual antes de pasar a la nueva
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
        dispose(); // Liberar recursos al ocultar esta pantalla
    }

    @Override
    public void dispose() {
        // Liberación adicional de recursos si es necesario
    }
}
