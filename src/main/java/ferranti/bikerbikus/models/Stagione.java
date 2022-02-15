package ferranti.bikerbikus.models;

import java.time.LocalDate;

public class Stagione {

	private int id;
	private String nome;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private Campionato campionato;

	public Stagione() {
	}

	public Stagione(int id, String nome, LocalDate dataInizio, LocalDate dataFine, Campionato campionato) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.campionato = campionato;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}

	public Campionato getCampionato() {
		return campionato;
	}

	public void setCampionato(Campionato campionato) {
		this.campionato = campionato;
	}

	@Override
	public String toString() {
		return "Campionato " + campionato.getNome() + " - Stagione " + nome;
	}
}
