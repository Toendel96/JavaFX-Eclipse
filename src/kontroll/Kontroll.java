package kontroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            throw new Exception("Kan ikke oppnaa kontakt med databasen");
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
		film.add(new Film(filmnr, filmnavn));
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
		return ubetaltBillettListe;
	}


	public void slettAlleBestillinger(ObservableList<Billett> ubetaltBillettListe) {
		for (Billett u: ubetaltBillettListe) {
			billett.remove(u);
			} 
		for (Billett b:billett) {
		}
		ubetaltBillettListe.clear();
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
			System.out.println(film);
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
		int siste = visning.size() -1;
		int nrPaaSiste = visning.get(siste).getVisningsnr();
		int nyttVisningsNr = nrPaaSiste +1;
		
		int filmNr = Integer.parseInt(filmnr);
		int kinoSalNr = Integer.parseInt(kinosalnr);
		//Konverter LocalDate til Date
		Date datoDate = Date.valueOf(dato);
		
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime localTime = LocalTime.parse(starttid, parser);
		Time startTid = Time.valueOf(localTime);
		
		Float prisF = Float.parseFloat(pris);
		
		setVisning(nyttVisningsNr,filmNr,kinoSalNr,datoDate,startTid,prisF);
		return false;
	}


	@Override
	public ResultSet leggInnVisningerIListe() throws Exception {
		resultat = null;
		//String sql = "SELECT v_visningnr, v_filmnr, v_kinosalnr, v_dato, v_starttid, v_pris FROM tblvisning WHERE v_dato >= CURDATE()";
		String sql = "SELECT v_visningnr, v_filmnr, v_kinosalnr, v_dato, v_starttid, v_pris FROM tblvisning";
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
               
               LocalDate date = LocalDate.now();  
               LocalDate datoFormat = toLocalDate(dato);
               //System.out.println("datoFormat: " + datoFormat.getClass().getName());
               //System.out.println("date: " + date.getClass().getName());
               
               LocalTime startidLocalTime = toLocalTime(starttid);
               
               LocalDateTime naavarendeTid = LocalDateTime.now();
               DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");  
               String formatString = naavarendeTid.format(format);  
               LocalTime naavarendeTidFormat = LocalTime.parse(formatString);
               
               //System.out.println(startidLocalTime.getClass().getName());
               //System.out.println(naavarendeTidFormat.getClass().getName());
               
               boolean erDatoFremITidEllerSammeDag = false;
               
               long differanseITid = Duration.between(naavarendeTidFormat, startidLocalTime).toMinutes();
               int sjekkDatoer = datoFormat.compareTo(date);
               
               if (sjekkDatoer > 0) {
            	   //dato fra database er senere enn naavarende dato
            	   erDatoFremITidEllerSammeDag = true;
               } else if (sjekkDatoer < 0) {
            	   //dato fra databsaae er tidligere enn naavarende dato
            	   erDatoFremITidEllerSammeDag = false;
               } else {
            	   //Samme dag
            	   erDatoFremITidEllerSammeDag = true;
               }
               
               if (erDatoFremITidEllerSammeDag) {
            	   if(differanseITid >= 30) {
            		   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
                	   //Mer enn tretti min og samme eller nyere dag
                	   //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
            	   }
               } else {
            	   //Under tretti eller tidligere dato
            	   //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
               }
               System.out.println();
               //Alle
               //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
		}
		return resultat;
	}
	
	//Metode for aa konvertere timer fra database til LocalTime. Trenger det for aa sammenligne
	public static LocalTime toLocalTime(java.sql.Time time) {
	    return time.toLocalTime();
	  }
	
	//Metode for aa konvertere datoer fra database til LocalDate. Trenger det for aa sammenligne
	public static LocalDate toLocalDate(java.sql.Date date) {
	    return date.toLocalDate();
	  }

	@Override
	public ResultSet finnSpesifikkVisning(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void hentVisninger() throws Exception {
		ResultSet resultat = null;
		String sql = "SELECT * FROM tblvisning";
		preparedStatement = forbindelse.prepareStatement(sql);
		resultat = preparedStatement.executeQuery(sql);
		while(resultat.next()) {
			int visningnr = resultat.getInt(1);
            int filmnr = resultat.getInt(2);
            int kinosalnr = resultat.getInt(3);
            Date dato = resultat.getDate(4);
            Time starttid = resultat.getTime(5);
            float pris = resultat.getFloat(6);
			setVisning(visningnr,filmnr,kinosalnr,dato,starttid,pris);
		}
	}
	
	//------------------------------------ Sletter alt innhold i databasen (kjores n�r applikasjonen avsluttes) --------------------------------------------
	public void slettinnholdAlleTabeller() throws Exception {
		try {
            //Execute SQL query
            String sql1 = "DELETE FROM tblplassbillett";
            String sql2 = "DELETE FROM tblplass";
            String sql3 = "DELETE FROM tbllogint";
            String sql4 = "DELETE FROM tblbillett";
            String sql5 = "DELETE FROM tblvisning";
            String sql6 = "DELETE FROM tblkinosal";
            String sql7 = "DELETE FROM tblfilm";

            preparedStatement = forbindelse.prepareStatement(sql1);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql2);
            preparedStatement.executeUpdate();

            preparedStatement = forbindelse.prepareStatement(sql3);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql4);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql5);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql6);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql7);
            preparedStatement.executeUpdate();
            
        } catch (Exception e) { throw new Exception(e); }
	}
	
	//------------------------------------ Legger alt innhold i databasen --------------------------------------------
	

}
