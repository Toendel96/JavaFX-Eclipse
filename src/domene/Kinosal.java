package domene;

public class Kinosal {
	private int kinosalnr;
	private String kinonavn;
	private String kinosalnavn;
	
	public Kinosal(int kinosalnr, String kinonavn, String kinosalnavn) {
		super();
		this.kinosalnr = kinosalnr;
		this.kinonavn = kinonavn;
		this.kinosalnavn = kinosalnavn;
	}
	public int getKinosalnr() {
		return kinosalnr;
	}
	public void setKinosalnr(int kinosalnr) {
		this.kinosalnr = kinosalnr;
	}
	public String getKinonavn() {
		return kinonavn;
	}
	public void setKinonavn(String kinonavn) {
		this.kinonavn = kinonavn;
	}
	public String getKinosalnavn() {
		return kinosalnavn;
	}
	public void setKinosalnavn(String kinosalnavn) {
		this.kinosalnavn = kinosalnavn;
	}
	
	@Override
	public String toString() {
		return "Kinosal [kinosalnr=" + kinosalnr + ", kinonavn=" + kinonavn + ", kinosalnavn=" + kinosalnavn + "]";
	}
	
}
