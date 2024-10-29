package puppy.code;

public class NaveSeleccionada {
    public int speed;
    public int vida;
    public String texturaNave;
    public String texturaShoot;
    public String soundShoot;

    public NaveSeleccionada(int vida, int speed, String texturaNave, String texturaShoot, String soundShoot) {
        this.vida = vida;
        this.speed = speed;
        this.texturaNave = texturaNave;
        this.texturaShoot = texturaShoot;
        this.soundShoot = soundShoot;
    }
}
