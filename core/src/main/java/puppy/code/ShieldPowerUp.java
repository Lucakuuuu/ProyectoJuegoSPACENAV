package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(float x, float y, float duration, Texture texture) {
        super(x, y, duration, texture);
    }

    @Override
    public void applyEffect(Nave4 nave) {
        nave.activateShield();
    }
}
