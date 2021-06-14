package kontroll;

import java.sql.ResultSet;
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
	public ResultSet hentFilmer() throws Exception;	
	public ResultSet finnSpesifikkFilm(String filmnr) throws Exception;
	
	//Kinosal
	ResultSet hentKinosaler() throws Exception;
	ResultSet finnSpesifikkKinosal(String kinosalnr) throws Exception;
	
	//Plassbillett
	boolean leggTilPlassbillett(String filmnavn);
	ResultSet hentPlassbilletter(String billettkode) throws Exception;
	ResultSet finnSpesifikkPlassbillett(String kundenr1) throws Exception;
	
	//Visninger
	boolean leggTilVisning(String filmnr, String kinosalnr, String dato, String starttid, String pris);
	ResultSet hentVisninger(String billettkode) throws Exception;
	ResultSet finnSpesifikkVisning(String kundenr1) throws Exception;


}
