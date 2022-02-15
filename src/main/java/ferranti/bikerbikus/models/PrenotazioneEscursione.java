package ferranti.bikerbikus.models;

import java.time.LocalDateTime;

public class PrenotazioneEscursione {

	private Utente utente;
	private Escursione escursione;
	private LocalDateTime data;

	public PrenotazioneEscursione() {
	}

	public PrenotazioneEscursione(Utente utente, Escursione escursione, LocalDateTime data) {
		this.utente = utente;
		this.escursione = escursione;
		this.data = data;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Escursione getEscursione() {
		return escursione;
	}

	public void setEscursione(Escursione escursione) {
		this.escursione = escursione;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
