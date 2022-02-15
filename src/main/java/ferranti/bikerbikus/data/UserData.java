package ferranti.bikerbikus.data;

import ferranti.bikerbikus.models.Utente;

public class UserData {

	private Utente utente;

	private static UserData instance = null;

	private UserData() {
	}

	public static UserData getInstance() {
		if (instance == null) {
			instance = new UserData();
		}
		return instance;
	}

	public Utente getUser() {
		return utente;
	}

	public void setUser(Utente utente) {
		this.utente = utente;
	}

	public boolean isMaestro() {
		if (utente == null)
			return false;
		return utente.getTipoUtente().getId() == 2;
	}

	public boolean isMaestroAvanzato() {
		if (utente == null)
			return false;
		return utente.getTipoUtente().getId() == 3;
	}
}
