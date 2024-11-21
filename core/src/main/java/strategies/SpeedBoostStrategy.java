package strategies;
import puppy.code.Nave4;

public class SpeedBoostStrategy implements PowerUpStrategy
{
    private float multiplier;

    public SpeedBoostStrategy(float multiplier)
    {
        this.multiplier = multiplier;
    }

    @Override
    public void activar(Nave4 nave)
    {
        nave.setSpeedMultiplier(multiplier);
    }

    @Override
    public void aplicarEfecto(Nave4 nave, float delta)
    {

    }

    public void resetEffect(Nave4 nave)
    {
        nave.setSpeedMultiplier(1);
    }
}

