package ferranti.bikerbikus.models;

public class GaraExtended extends Gara {

	private int partecipanti;
	private String nomeVincitore;
	private String cognomeVincitore;
	
	public GaraExtended() {
		
	}

	public GaraExtended(int partecipanti, String vincitore) {
		super();
		this.partecipanti = partecipanti;
		this.nomeVincitore = vincitore;
	}

	public int getPartecipanti() {
		return partecipanti;
	}

	public void setPartecipanti(int partecipanti) {
		this.partecipanti = partecipanti;
	}

	public String getNomeVincitore() {
		return nomeVincitore;
	}

	public void setNomeVincitore(String nomeVincitore) {
		this.nomeVincitore = nomeVincitore;
	}
	
	public String getCognomeVincitore() {
		return cognomeVincitore;
	}

	public void setCognomeVincitore(String cognomeVincitore) {
		this.cognomeVincitore = cognomeVincitore;
	}
}
