package domene;

public class Kinosal {
	private int kinosalnr;
	private String kinonavn;
	private String kinosalnavn;
	
<<<<<<< HEAD
	public Kinosal(int kinosalnr, String kinonavn, String kinosalnavn) {
		super();
		this.kinosalnr = kinosalnr;
		this.kinonavn = kinonavn;
		this.kinosalnavn = kinosalnavn;
	}
=======
	
>>>>>>> 8ae9bda5da14b278c29f8fa49b01b5017a691df3
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
