package domene;

public class Billett {
	private String billettkode;
	private int visningsnr;
	private boolean erBetalt;
	
	public Billett(String billettkode, int visningsnr, boolean erBetalt) {
		super();
		this.billettkode = billettkode;
		this.visningsnr = visningsnr;
		this.erBetalt = erBetalt;
	}
	public String getBillettkode() {
		return billettkode;
	}
	public void setBillettkode(String billettkode) {
		this.billettkode = billettkode;
	}
	public int getVisningsnr() {
		return visningsnr;
	}
	public void setVisningsnr(int visningsnr) {
		this.visningsnr = visningsnr;
	}
	public boolean getErBetalt() {
		return erBetalt;
		
	}
	public void setErBetalt(boolean erBetalt) {
		this.erBetalt = erBetalt;
	}
	
	public String toFile() {
		return(billettkode+ "," + visningsnr);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Billett other = (Billett) obj;
		if (billettkode == null) {
			if (other.billettkode != null)
				return false;
		} else if (!billettkode.equals(other.billettkode))
			return false;
		if (erBetalt != other.erBetalt)
			return false;
		if (visningsnr != other.visningsnr)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Billett [billettkode=" + billettkode + ", visningsnr=" + visningsnr + ", erBetalt=" + erBetalt + "]";
	}
	
	
}
