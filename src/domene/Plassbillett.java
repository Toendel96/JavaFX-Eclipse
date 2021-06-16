package domene;

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plassbillett other = (Plassbillett) obj;
		if (billettkode == null) {
			if (other.billettkode != null)
				return false;
		} else if (!billettkode.equals(other.billettkode))
			return false;
		if (kinosalnr != other.kinosalnr)
			return false;
		if (radnr != other.radnr)
			return false;
		if (setenr != other.setenr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Plassbillett [radnr=" + radnr + ", setenr=" + setenr + ", kinosalnr=" + kinosalnr + ", billettkode="
				+ billettkode + "]";
	}
	
}
