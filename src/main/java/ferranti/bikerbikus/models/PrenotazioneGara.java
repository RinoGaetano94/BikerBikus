package ferranti.bikerbikus.models;

import java.time.LocalDateTime;

public class PrenotazioneGara {

	private Utente utente;
	private Gara gara;
	private int posizione;
	private LocalDateTime data;

	public PrenotazioneGara() {
	}

	public PrenotazioneGara(Utente utente, Gara gara, int posizione, LocalDateTime data) {
		super();
		this.utente = utente;
		this.gara = gara;
		this.posizione = posizione;
		this.data = data;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Gara getGara() {
		return gara;
	}

	public void setGara(Gara gara) {
		this.gara = gara;
	}

	public int getPosizione() {
		return posizione;
	}

	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
