package strategies;

import puppy.code.Nave4;

public interface PowerUpStrategy
{
    void activar(Nave4 nave);
    void aplicarEfecto(Nave4 nave, float delta);
}

