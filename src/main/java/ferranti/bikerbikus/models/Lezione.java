package ferranti.bikerbikus.models;

import java.time.LocalDateTime;

public class Lezione {
	
	private int id;
	private LocalDateTime data;
	private Utente maestro;
	private TipoLezione tipo;
	private boolean privata;
	
	public Lezione() {
	}

	public Lezione(int id, LocalDateTime data, Utente maestro, TipoLezione tipo) {
		this.id = id;
		this.data = data;
		this.maestro = maestro;
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Utente getMaestro() {
		return maestro;
	}

	public void setMaestro(Utente maestro) {
		this.maestro = maestro;
	}

	public TipoLezione getTipo() {
		return tipo;
	}

	public void setTipo(TipoLezione tipo) {
		this.tipo = tipo;
	}

	public boolean isPrivata() {
		return privata;
	}

	public void setPrivata(boolean privata) {
		this.privata = privata;
	}
}
