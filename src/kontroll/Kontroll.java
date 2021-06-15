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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
            throw new Exception("Kan ikke oppnå kontakt med databasen");
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

	public void setFilm(int filmnr, String filmnavn) {
		film.add(new Film(filmnr, brukernavn));
	}

	public ObservableList<Kinosal> getKinosal() {
		return kinosal;
	}
	public void setKinosal(int kinosalnr, String kinonavn, String kinosalnavn) {
		kinosal.add(new Kinosal(kinosalnr, kinonavn, kinosalnavn)); 
	}

	public ObservableList<Plass> getPlass() {
		return plass;
	}

	public void setPlass(int radnr, int setenr, int kinosalnr) {
		plass.add(new Plass(radnr, setenr, kinosalnr));
	}

	public ObservableList<Plassbillett> getPlassbillett() {
		return plassbillett;
	}

	public void setPlassbillett(int radnr, int setenr, int kinosalnr, String billettkode) {
		plassbillett.add(new Plassbillett(radnr, setenr, kinosalnr, billettkode));
	}

	public ObservableList<Visning> getVisning() {
		return visning;
	}

	public void setVisning(int visningnr, int filmnr, int kinosalnr, Date dato, Time starttid, float pris) {
		visning.add(new Visning(visningnr, filmnr, kinosalnr, dato, starttid, pris));
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
		ObservableList<Billett> ubetaltBillettListe = FXCollections.observableArrayList();
		for (Billett b:billett) {
			if(!b.getErBetalt()) {
				ubetaltBillettListe.add(b);
			}
		}
		if (ubetaltBillettListe.isEmpty()) {
			return null;
		}else {
		return ubetaltBillettListe;
		}
	}


	public void slettAlleBestillinger(ObservableList<Billett> ubetaltBillettListe) {
		System.out.println("Du kom hit");
		ObservableList<Billett> tempbillett = FXCollections.observableArrayList();
		for (Billett u: ubetaltBillettListe) {
			for(Billett b: billett) {
				if (b.getBillettkode().equals(u.getBillettkode())) {
					break;
					//Ikke lagre dette objektet
				} else {
					System.out.println(b.toString() + "Har blitt lagt til i listen");
					tempbillett.add(b);
				}
			} 
		} 
		billett.clear();
		billett = tempbillett;
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
	public void hentFilmer() throws Exception {
		//Henter alle filmene som ligger i databasen og setter film Observablelisten
		try {
			ResultSet resultat = null;
			String sql = "SELECT * FROM tblfilm";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			while(resultat.next()) {
				int filmNr = resultat.getInt(1);
				String filmNavn = resultat.getString(2);
				setFilm(filmNr,filmNavn);
			}
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
	
	public ObservableList<List<String>> kinoStatistikk(String kinosalnr) throws Exception {
		
		final ObservableList<List<String>> statistikkKino = FXCollections.observableArrayList();
		
		int kinoSalnr = Integer.parseInt(kinosalnr);
		int antallVisninger = 0;
		int antallPlasser = 0;
		int antallSalg = 0;
		int muligePlasser = 0;
		int salProsent = 0;
		for (Kinosal k : kinosal) {
			if (k.getKinosalnr()==(kinoSalnr)) {
				resultat = null;
				String sql = "SELECT COUNT(v_visningnr) FROM tblvisning WHERE v_kinosalnr ="+kinoSalnr+" AND v_dato < SYSDATE();";
				preparedStatement = forbindelse.prepareStatement(sql);
				resultat = preparedStatement.executeQuery(sql);
				
				while (resultat.next()) {
					antallVisninger = resultat.getInt(1);
					
				}
				String sql1 = "SELECT COUNT(p_setenr) AS Antall_seter from tblplass where p_kinosalnr ="+kinoSalnr+" ;";
				preparedStatement = forbindelse.prepareStatement(sql1);
				resultat = preparedStatement.executeQuery(sql1);
				
				while (resultat.next() ) {
					antallPlasser = resultat.getInt(1);
					
				}
				String sql2 = "SELECT COUNT(b_billettkode) AS Antall_billetter\n"
						+ "From tblbillett\n"
						+ "INNER JOIN tblvisning ON tblbillett.b_visningsnr=tblvisning.v_visningnr\n"
						+ "WHERE b_erBetalt = '1' AND v_kinosalnr ="+kinoSalnr+" AND v_dato < SYSDATE();";
				preparedStatement = forbindelse.prepareStatement(sql2);
				resultat = preparedStatement.executeQuery(sql2);
				
				while(resultat.next()) {
					antallSalg = resultat.getInt(1);
					
				}
				
				muligePlasser = antallVisninger * antallPlasser;
				salProsent = antallSalg / antallPlasser * 100;
				
				
				
				
			}
		}
		
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
	public ResultSet leggInnVisningerIListe() throws Exception {
		resultat = null;
		String sql = "SELECT v_visningnr, v_filmnr, v_kinosalnr, v_dato, v_starttid, v_pris FROM tblvisning WHERE v_dato >= CURDATE()";
		preparedStatement = forbindelse.prepareStatement(sql);
		resultat = preparedStatement.executeQuery(sql);
		
		while (resultat.next()) {
			//visningnr, int filmnr, int kinosalnr, Date dato, Time starttid, float pris
			   int visningnr = resultat.getInt(1);
               int filmnr = resultat.getInt(2);
               int kinosalnr = resultat.getInt(3);
               Date dato = resultat.getDate(4);
               Time starttid = resultat.getTime(5);
               float pris = resultat.getFloat(6);
               
               LocalTime startidLocalTime = toLocalTime(starttid);
               
               LocalDateTime naavarendeTid = LocalDateTime.now();
               DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");  
               String formatString = naavarendeTid.format(format);  
               LocalTime naavarendeTidFormat = LocalTime.parse(formatString);
               
               //System.out.println(startidLocalTime.getClass().getName());
               //System.out.println(naavarendeTidFormat.getClass().getName());
               
               long differanseITid = Duration.between(naavarendeTidFormat, startidLocalTime).toMinutes();
               
               if (differanseITid >= 30) {
            	   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
            	   System.out.println("Mer enn tretti min");
            	   System.out.println(differanseITid);
            	   System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris); 
               } else {
            	   System.out.println("Under tretti:");
            	   System.out.println(differanseITid);
            	   System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
               }
               System.out.println();
               
               //System.out.println("startidLocalTime " + startidLocalTime);
               //System.out.println("naavarendeTidFormat " + naavarendeTidFormat);
               //System.out.println("Alle:");
               //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
		}
		return resultat;
	}
	
	public static LocalTime toLocalTime(java.sql.Time time) {
	    return time.toLocalTime();
	  }

	@Override
	public ResultSet finnSpesifikkVisning(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilVisning(String filmnr, String kinosalnr, String dato, String starttid, String pris) {
		// TODO Auto-generated method stub
		return false;
	}
    

}
