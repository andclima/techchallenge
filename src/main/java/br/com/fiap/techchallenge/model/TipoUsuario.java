package br.com.fiap.techchallenge.model;

public enum TipoUsuario {
  CLIENTE {
        @Override
        public Usuario criar() {
            return new Cliente();
        }
    },
    DONO {
        @Override
        public Usuario criar() {
            return new DonoRestaurante();
        }
    };

    public abstract Usuario criar();

    public static TipoUsuario from(String tipo) {
        try {
            return TipoUsuario.valueOf(tipo.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
