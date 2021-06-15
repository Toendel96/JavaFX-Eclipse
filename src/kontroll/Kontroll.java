package kontroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import domene.Film;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import domene.Billett;
import domene.Film;
import domene.Kinosal;
import domene.Plass;
import domene.Plassbillett;
import domene.Visning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import domene.Billett;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Kontroll implements kontrollInterface {
	 private String databasenavn = "jdbc:mysql://localhost:3306/kino";
	    private String databasedriver = "com.mysql.jdbc.Driver";
	    private Connection forbindelse;
	    private ResultSet resultat;
	    private PreparedStatement preparedStatement;
	    private Statement utsagn;
	    private String brukernavn = "Case";
	    private String passord = "Esac";
	    private ObservableList<Billett> billett = FXCollections.observableArrayList();
	    private ObservableList<Film> film = FXCollections.observableArrayList();
	    private ObservableList<Kinosal> kinosal = FXCollections.observableArrayList();
	    private ObservableList<Plass> plass = FXCollections.observableArrayList();
	    private ObservableList<Plassbillett> plassbillett = FXCollections.observableArrayList();
	    private ObservableList<Visning> visning = FXCollections.observableArrayList();
	
	//------------------------ aapne/Lukke forbindelse --------------------------------
    public void lagForbindelse() throws Exception {
        try {
            forbindelse = DriverManager.getConnection(databasenavn, brukernavn, passord);
            System.out.println("Tilkobling til database fungerte");
        } catch (Exception e) {
            throw new Exception("Kan ikke oppn� kontakt med databasen");
        }
    }

    public void lukk() throws Exception {
        try {
            if(forbindelse != null) {
                forbindelse.close();
                resultat.close();
                //preparedStatement.close();
                //utsagn.close();
            }
        }catch(Exception e) {
            throw new Exception("Kan ikke lukke databaseforbindelse");
        }
    }    
    
	public ObservableList<Billett> getBillett() {
		return billett;
	}

	public void settBillett(String billettkode, int visningsnr, boolean erBetalt) {
		billett.add(new Billett(billettkode, visningsnr, erBetalt));
    }

	public ObservableList<Film> getFilm() {
		return film;
	}

	public void setFilm(ObservableList<Film> film) {
		this.film = film;
	}

	public ObservableList<Kinosal> getKinosal() {
		return kinosal;
	}

	public void setKinosal(int kinosalNr, String kinoNavn, String kinoSalNavn) {
		kinosal.add(new Kinosal(kinosalNr,kinoNavn,kinoSalNavn));
	}

	public ObservableList<Plass> getPlass() {
		return plass;
	}

	public void setPlass(ObservableList<Plass> plass) {
		this.plass = plass;
	}

	public ObservableList<Plassbillett> getPlassbillett() {
		return plassbillett;
	}

	public void setPlassbillett(ObservableList<Plassbillett> plassbillett) {
		this.plassbillett = plassbillett;
	}

	public ObservableList<Visning> getVisning() {
		return visning;
	}

	public void setVisning(ObservableList<Visning> visning) {
		this.visning = visning;
	}

	@Override
	public boolean sletteBillett(String billettKode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int leggTilBillett(String billetNavn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet hentBilletter() throws Exception {
        resultat = null;
        String sql = "SELECT * FROM tblbillett";
        preparedStatement = forbindelse.prepareStatement(sql);
        resultat = preparedStatement.executeQuery(sql);
        
        while (resultat.next()) {
        	String billettKode = resultat.getString(1);
        	int visningsnr = resultat.getInt(2);
        	boolean erBetalt = resultat.getBoolean(3);
        	System.out.println(billettKode + " " + visningsnr + " " + erBetalt);
        	settBillett(billettKode, visningsnr, erBetalt);
        }
        return null;
	}
	
	public boolean settBillettSomBetalt(String billettKode) {
		//
		return true;
	}
	
	
	public ObservableList<Billett> getDataBillettListe() {
        return billett;
    }
	
	public ObservableList<Billett> hentUbetalteBilletter() {
		//Returner en liste med ubetalte billetter
		ObservableList<Billett> billettListe = FXCollections.observableArrayList();
		for (Billett b:billett) {
			if(!b.getErBetalt()) {
				
			}
		}
		return billettListe;
	}
	
	 public ResultSet lesUbetalteBilletter() throws Exception {
	    	try {
	    		ResultSet resultat = null;
		    	String sql = "SELECT * FROM tblbillett";
	    		utsagn = forbindelse.createStatement();
	    		resultat = utsagn.executeQuery(sql);
	    		return resultat;
	    	}catch(Exception e) {throw new Exception("Kan ikke �pne databasetabell");}
	 }

	@Override
	public ResultSet finnSpesifikkBillett(String billettKode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilFilm(String filmnavn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ObservableList<Film> hentFilmer() throws Exception {
		//Henter alle filmene som ligger i databasen og setter film Observablelisten
		try {
			ResultSet resultat = null;
			String sql = "SELECT * FROM tblfilm";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			while(resultat.next()) {
				int filmNr = resultat.getInt(1);
				String filmNavn = resultat.getString(2);
				film.add(new Film(filmNr,filmNavn));
			}
			return film;
		}catch(Exception e) {
			throw new Exception("Kan ikke hente fra databasen");
		}
	}
	
	public int hentFilmnrFraNavn(String filmNavn) {
		int filmNr = 0;
		for (Film f: film) {
			if (f.getFilmnavn().equals(filmNavn)) {
				filmNr = f.getFilmnr();
			}
		}
		return filmNr;
	}
	
	public ChoiceBox<String> visFilmerChoice() {
		//Returnerer en choicebox med alle filmnavn
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (Film f: film) {
			cb.getItems().add(f.getFilmnavn());
		}
		return cb;
	}
	

	@Override
	public ResultSet finnSpesifikkFilm(String filmnr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservableList<Kinosal> hentKinosaler() throws Exception {
		ResultSet resultat = null;
		String sql = "SELECT * FROM tblkinosal";
		preparedStatement = forbindelse.prepareStatement(sql);
		resultat = preparedStatement.executeQuery(sql);
		while(resultat.next()) {
			int kinosalNr = resultat.getInt(1);
			String kinoNavn = resultat.getString(2);
			String kinoSalNavn = resultat.getString(3);
			setKinosal(kinosalNr,kinoNavn,kinoSalNavn);
		}
		return kinosal;
	}
	
	public ChoiceBox<String> visKinosalerChoice() {
		//Returnerer en choicebox med alle kinosaler
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (Kinosal k: kinosal) {
			cb.getItems().add(String.valueOf(k.getKinosalnr()));
		}
		return cb;
	}

	@Override
	public ResultSet finnSpesifikkKinosal(String kinosalnr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilPlassbillett(String filmnavn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet hentPlassbilletter() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet finnSpesifikkPlassbillett(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilVisning(String filmnr, String kinosalnr, LocalDate dato, String starttid, String pris) {
		
		return false;
	}

	@Override
	public ResultSet hentVisninger() throws Exception {
		resultat = null;
		
		LocalDateTime now = LocalDateTime.now();  
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = now.format(format);
		
		String sql = "SELECT v_visningnr, v_filmnr, f_filmnavn, v_pris, v_dato, v_starttid, NOW()- INTERVAL 1 HOUR"
				+ "FROM tblvisning, tblbillett, tblplass, tblfilm\r\n"
				+ "WHERE v_visningnr = b_visningsnr\r\n"
				+ "	AND v_kinosalnr = p_kinosalnr\r\n"
				+ "			AND v_dato >= CURDATE()\r\n"
				+ "				AND f_filmnr = v_filmnr\r\n"
				+ "GROUP BY v_visningnr, v_filmnr, f_filmnavn, v_pris, v_dato;";
		preparedStatement = forbindelse.prepareStatement(sql);
		resultat = preparedStatement.executeQuery(sql);
		
		while (resultat.next()) {
			//visningnr, int filmnr, int kinosalnr, Date dato, Time starttid, float pris
			   int visningnr = resultat.getInt(1);
               int filmnr = resultat.getInt(2);
               int kinosalnr = resultat.getInt(3);
               Date dato = resultat.getDate(4);
               Time starttid = resultat.getTime(5);
               boolean erBetalt = resultat.getBoolean(5);
               int totalFakturaPris = resultat.getInt(6);
               Date datoKlokkeslett = resultat.getDate(7);
               
               //System.out.println(fakturanummer + " " + kundenummer + " " + dagensDato + " " + forfallsdato + " " + erBetalt); //test print
               //dataFaktura.add(new Faktura(fakturanummer, kundenummer, dagensDato, forfallsdato, erBetalt, totalFakturaPris));
		}
		
		return resultat;
		
	}

	@Override
	public ResultSet finnSpesifikkVisning(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    

}
