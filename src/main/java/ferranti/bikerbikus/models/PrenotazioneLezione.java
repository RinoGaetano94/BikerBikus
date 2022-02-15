package ferranti.bikerbikus.models;

import java.time.LocalDateTime;

public class PrenotazioneLezione {

	private Utente utente;
	private Lezione lezione;
	private LocalDateTime data;

	public PrenotazioneLezione() {
	}

	public PrenotazioneLezione(Utente utente, Lezione lezione, LocalDateTime data) {
		this.utente = utente;
		this.lezione = lezione;
		this.data = data;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Lezione getLezione() {
		return lezione;
	}

	public void setLezione(Lezione lezione) {
		this.lezione = lezione;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
