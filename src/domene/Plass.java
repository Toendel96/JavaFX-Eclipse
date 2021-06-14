package domene;

public class Plass {
	private int radnr; 
	private int setenr; 
	private int kinosalnr;
	
	public Plass(int radnr, int setenr, int kinosalnr) {
		super();
		this.radnr = radnr;
		this.setenr = setenr;
		this.kinosalnr = kinosalnr;
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

	@Override
	public String toString() {
		return "Plass [radnr=" + radnr + ", setenr=" + setenr + ", kinosalnr=" + kinosalnr + "]";
	}

}
