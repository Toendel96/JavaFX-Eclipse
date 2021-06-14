package domene;

public class Film {
	private int filmnr;
	private String filmnavn;
	
	
	public int getFilmnr() {
		return filmnr;
	}
	public void setFilmnr(int filmnr) {
		this.filmnr = filmnr;
	}
	public String getFilmnavn() {
		return filmnavn;
	}
	public void setFilmnavn(String filmnavn) {
		this.filmnavn = filmnavn;
	}
	
	@Override
	public String toString() {
		return "Film [filmnr=" + filmnr + ", filmnavn=" + filmnavn + "]";
	}
	
	

}
