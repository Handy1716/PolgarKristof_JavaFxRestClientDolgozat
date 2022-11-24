package hu.petrik.peoplerestclientjavafx;

import com.google.gson.annotations.Expose;

public class Rendeles {
    private int id;
    @Expose
    private String nev;
    @Expose
    private String cim;
    @Expose
    private int meret;

    public Rendeles(int id, String nev, String cim, int meret) {
        this.id = id;
        this.nev = nev;
        this.cim = cim;
        this.meret = meret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public int getMeret() {
        return meret;
    }

    public void setMeret(int meret) {
        this.meret = meret;
    }
}
