package strategies;
import puppy.code.Nave4;

public class ShieldStrategy implements PowerUpStrategy
{
    @Override
    public void activar(Nave4 nave)
    {
        nave.activateShield();
    }

    @Override
    public void aplicarEfecto(Nave4 nave, float delta)
    {

    }
}
