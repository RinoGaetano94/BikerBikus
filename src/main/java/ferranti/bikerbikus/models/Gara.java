package ferranti.bikerbikus.models;

import java.time.LocalDateTime;

public class Gara {

	private int id;
	private Stagione stagione;
	private LocalDateTime data;

	public Gara() {
	}

	public Gara(int id, Stagione stagione, LocalDateTime data) {
		this.id = id;
		this.stagione = stagione;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stagione getStagione() {
		return stagione;
	}

	public void setStagione(Stagione stagione) {
		this.stagione = stagione;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
}
