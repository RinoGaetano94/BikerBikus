package ferranti.bikerbikus.models;

import ferranti.bikerbikus.utils.Utils;

public class Utente {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private TipoUtente tipoUtente;

    public Utente() {
    }

    public Utente(int id, String nome, String cognome, String email, String password, TipoUtente tipoUtente) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.tipoUtente = tipoUtente;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoUtente getTipoUtente() {
        return tipoUtente;
    }

    public void setTipoUtente(TipoUtente tipoUtente) {
        this.tipoUtente = tipoUtente;
    }
    
    @Override
    public String toString() {
    	return Utils.uppercase(nome) + " " + Utils.uppercase(cognome);
    }
}
