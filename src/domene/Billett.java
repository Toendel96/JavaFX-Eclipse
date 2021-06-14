package domene;

public class Billett {

	private String billettkode;
	private int visningsnr;
	private int erBetalt;
	
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
	public int getErBetalt() {
		return erBetalt;
	}
	public void setErBetalt(int erBetalt) {
		this.erBetalt = erBetalt;
	}
	
	@Override
	public String toString() {
		return "Billett [billettkode=" + billettkode + ", visningsnr=" + visningsnr + ", erBetalt=" + erBetalt + "]";
	}
	
	
}
