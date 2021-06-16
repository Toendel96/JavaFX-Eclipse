package domene;
/** Kodet av 7074, kontrollert og godkjent av 7104 */
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
	public String toString() {
		return "Billett [billettkode=" + billettkode + ", visningsnr=" + visningsnr + ", erBetalt=" + erBetalt + "]";
	}
	
	
}
