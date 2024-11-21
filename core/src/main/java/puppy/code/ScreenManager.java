package puppy.code;

public class ScreenManager {
    private static ScreenManager instance;
    private SpaceNavigation game;

    private ScreenManager(SpaceNavigation game) {
        this.game = game;
    }

    public static ScreenManager getInstance(SpaceNavigation game) {
        if (instance == null) {
            instance = new ScreenManager(game);
        }
        return instance;
    }

    public void showMainMenu() {
        game.setScreen(new PantallaMenu(game));
    }

    public void showGameScreen() {
        game.setScreen(new PantallaJuego(game, game.getNaveSeleccionada(), 1, 0, 1, 1, 10));
    }

    public void showPracticeScreen() {
        game.setScreen(new PantallaInstrucciones(game, game.getNaveSeleccionada(), 1, 0, 0, 0, 0));
    }

    public void showElegirNaveScreen() {
        game.setScreen(new ElegirNave(game));
    }

    public void showGameOverScreen(int identificacion) {
        game.setScreen(new PantallaGameOver(game, identificacion));
    }

    public void showNextRoundGame(int ronda, int score, int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        game.setScreen(new PantallaJuego(game, game.getNaveSeleccionada(), ronda, score, velXAsteroides, velYAsteroides, cantAsteroides));
    }

    public void showNextRoundPractice(int ronda, int score, int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        game.setScreen(new PantallaInstrucciones(game, game.getNaveSeleccionada(), ronda, score, velXAsteroides, velYAsteroides, cantAsteroides));
    }
}
