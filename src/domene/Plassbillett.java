package domene;
/** Kodet av 7104 , kontrollert og godkjent av 7088 */
public class Plassbillett {
	private int radnr;
	private int setenr;
	private int kinosalnr;
	private String billettkode;
	
	public Plassbillett(int radnr, int setenr, int kinosalnr, String billettkode) {
		super();
		this.radnr = radnr;
		this.setenr = setenr;
		this.kinosalnr = kinosalnr;
		this.billettkode = billettkode;
	}

	public int getRadnr() {
		return radnr;
	}

	public void setRadnr(int radnr) {
		this.radnr = radnr;
	}
	public int getSetenr() {
		return setenr;
	}
	public void setSetenr(int setenr) {
		this.setenr = setenr;
	}
	public int getKinosalnr() {
		return kinosalnr;
	}
	public void setKinosalnr(int kinosalnr) {
		this.kinosalnr = kinosalnr;
	}
	public String getBillettkode() {
		return billettkode;
	}
	public void setBillettkode(String billettkode) {
		this.billettkode = billettkode;
	}
	@Override
	public String toString() {
		return "Plassbillett [radnr=" + radnr + ", setenr=" + setenr + ", kinosalnr=" + kinosalnr + ", billettkode="
				+ billettkode + "]";
	}
	
}
