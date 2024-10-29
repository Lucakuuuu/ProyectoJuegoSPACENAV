package puppy.code;

abstract class Ships {
    int vida;
    int speed;
    String texturaNave;
    String texturaShoot;
    String soundShoot;

    public Ships(int vida, int speed, String texturaNave,
                 String texturaShoot, String soundShoot) {
        this.vida = vida;
        this.speed = speed;
        this.texturaNave = texturaNave;
        this.texturaShoot = texturaShoot;
        this.soundShoot = soundShoot;
    }

    abstract void cambiarNave(int vida, int speed, String texturaNave, String texturaShoot, String soundShoot);
}
