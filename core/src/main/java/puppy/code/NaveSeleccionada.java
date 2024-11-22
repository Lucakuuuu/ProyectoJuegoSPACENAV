package puppy.code;

public class NaveSeleccionada {
    private final String nombre;
    private final int vida;
    private final int speed;
    private final String texturaNave;
    private final String texturaShoot;
    private final String soundShoot;

    private NaveSeleccionada(Builder builder) {
        this.nombre = builder.nombre;
        this.vida = builder.vida;
        this.speed = builder.speed;
        this.texturaNave = builder.texturaNave;
        this.texturaShoot = builder.texturaShoot;
        this.soundShoot = builder.soundShoot;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getSpeed() { return speed; }
    public String getTexturaNave() { return texturaNave; }
    public String getTexturaShoot() { return texturaShoot; }
    public String getSoundShoot() { return soundShoot; }

    // Clase interna Builder
    public static class Builder {
        private String nombre;
        private int vida;
        private int speed;
        private String texturaNave;
        private String texturaShoot;
        private String soundShoot;

        public Builder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder setVida(int vida) {
            this.vida = vida;
            return this;
        }

        public Builder setSpeed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder setTexturaNave(String texturaNave) {
            this.texturaNave = texturaNave;
            return this;
        }

        public Builder setTexturaShoot(String texturaShoot) {
            this.texturaShoot = texturaShoot;
            return this;
        }

        public Builder setSoundShoot(String soundShoot) {
            this.soundShoot = soundShoot;
            return this;
        }

        public NaveSeleccionada build() {
            return new NaveSeleccionada(this);
        }
    }
}
