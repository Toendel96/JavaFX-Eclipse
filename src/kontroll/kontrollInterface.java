package kontroll;

import java.sql.ResultSet;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import domene.*;

public interface kontrollInterface {
	//Lister
	public ObservableList<Billett> billett = FXCollections.observableArrayList();
    public ObservableList<Film> film = FXCollections.observableArrayList();
    public ObservableList<Kinosal> kinosal = FXCollections.observableArrayList();
    public ObservableList<Plass> plass = FXCollections.observableArrayList();
    public ObservableList<Plassbillett> plassbillett = FXCollections.observableArrayList();
    public ObservableList<Visning> visning = FXCollections.observableArrayList();
    
  //Billett
  	public boolean sletteBillett(String billettKode);
  	public int leggTilBillett(String billetNavn);
  	public ResultSet hentBilletter() throws Exception;
	public ResultSet finnSpesifikkBillett(String billettKode) throws Exception;
	
	//Film
	public boolean leggTilFilm(String filmnavn);
	public void hentFilmer() throws Exception;	
	public ResultSet finnSpesifikkFilm(String filmnr) throws Exception;
	
	//Kinosal
	ObservableList<Kinosal> hentKinosaler() throws Exception;
	boolean finnSpesifikkKinosal(int kinosalnr) throws Exception;
	
	//Plassbillett
	boolean leggTilPlassbillett(String filmnavn);
	void hentPlassbilletter() throws Exception;
	ResultSet finnSpesifikkPlassbillett(String kundenr1) throws Exception;
	
	//Visninger
	boolean leggTilVisning(String filmnr, String kinosalnr, LocalDate dato, String starttid, String pris);
	ResultSet leggInnVisningerIListe() throws Exception;
	boolean finnSpesifikkVisning(String visningsnr);


}
