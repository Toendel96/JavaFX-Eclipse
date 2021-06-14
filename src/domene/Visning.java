package domene;

import java.sql.Date;
import java.sql.Time;

public class Visning {
	private int visningnr;
	private int filmnr;
	private int kinosalnr;
	private Date dato;
	private Time starttid;
	private float pris;
	
	public Visning(int visningsnr, int filmnr, int kinosalnr, Date dato, Time starttid, float pris) {
		super();
		this.visningnr = visningsnr;
		this.filmnr = filmnr;
		this.kinosalnr = kinosalnr;
		this.dato = dato;
		this.starttid = starttid;
		this.pris = pris;
	}
	
	public int getVisningsnr() {
		return visningnr;
	}
	public void setVisningsnr(int visningsnr) {
		this.visningnr = visningsnr;
	}
	public int getFilmnr() {
		return filmnr;
	}
	public void setFilmnr(int filmnr) {
		this.filmnr = filmnr;
	}
	public int getKinosalnr() {
		return kinosalnr;
	}
	public void setKinosalnr(int kinosalnr) {
		this.kinosalnr = kinosalnr;
	}
	public Date getDato() {
		return dato;
	}
	public void setDato(Date dato) {
		this.dato = dato;
	}
	public Time getStarttid() {
		return starttid;
	}
	public void setStarttid(Time starttid) {
		this.starttid = starttid;
	}
	public float getPris() {
		return pris;
	}
	public void setPris(float pris) {
		this.pris = pris;
	}
	
	@Override
	public String toString() {
		return "Visning [visningnr=" + visningnr + ", filmnr=" + filmnr + ", kinosalnr=" + kinosalnr + ", dato="
				+ dato + ", starttid=" + starttid + ", pris=" + pris + "]";
	}
	

}
