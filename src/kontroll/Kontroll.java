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
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import domene.Billett;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static javax.swing.JOptionPane.showMessageDialog;

public class Kontroll implements kontrollInterface {
	 private String databasenavn = "jdbc:mysql://localhost:3306/kino";
	    private String databasedriver = "com.mysql.jdbc.Driver";
	    private Connection forbindelse;
	    private ResultSet resultat;
	    private PreparedStatement preparedStatement;
	    private Statement utsagn;
	    private final String brukernavn = "Case";
	    private final String passord = "Esac";
	    private ObservableList<Billett> billett = FXCollections.observableArrayList();
	    private ObservableList<Film> film = FXCollections.observableArrayList();
	    private ObservableList<Kinosal> kinosal = FXCollections.observableArrayList();
	    private ObservableList<Plass> plass = FXCollections.observableArrayList();
	    private ObservableList<Plassbillett> plassbillett = FXCollections.observableArrayList();
	    private ObservableList<Visning> visning = FXCollections.observableArrayList();

	    private ObservableList<Visning> alleVisninger = FXCollections.observableArrayList();
	    private ObservableList<List<String>> visningString = FXCollections.observableArrayList();
	    ObservableList<Integer> antallLedigePlasserListe = FXCollections.observableArrayList();

	
	//------------------------ aapne/Lukke forbindelse --------------------------------
    public void lagForbindelse() throws Exception {
        try {
            forbindelse = DriverManager.getConnection(databasenavn, brukernavn, passord);
            //System.out.println("Tilkobling til database fungerte");
        } catch (Exception e) {
            throw new Exception("Kan ikke oppnaa kontakt med databasen");
        }
    }

    public void lukk() throws Exception {
        try {
            if(forbindelse != null) {
                forbindelse.close();
                resultat.close();
                preparedStatement.close();
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
	
	public ObservableList<Integer> getAntallLedigePlasserListe() {
		return antallLedigePlasserListe;
	}

	public void setAntallLedigePlasserListe(int verdi) {
		antallLedigePlasserListe.add(verdi);
	}

	public ResultSet hentPlasser() throws Exception {
        resultat = null;
        String sql = "SELECT * FROM tblplass";
        preparedStatement = forbindelse.prepareStatement(sql);
        resultat = preparedStatement.executeQuery(sql);
        
        while (resultat.next()) {
        	int radnr = resultat.getInt(1);
        	int setenr = resultat.getInt(2);
        	int kinosalnr = resultat.getInt(3);
        	setPlass(radnr, setenr, kinosalnr);
        }
        return null;
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

	public ObservableList<Visning> getAlleVisninger() {
		return alleVisninger;
	}

	public void setAlleVisninger(int visningnr, int filmnr, int kinosalnr, Date dato, Time starttid, float pris) {
		alleVisninger.add(new Visning(visningnr, filmnr, kinosalnr, dato, starttid, pris));
	}

	public ObservableList<List<String>> getVisningString() {
		return visningString;
	}

	public void setVisningString(ObservableList<List<String>> visningString) {
		this.visningString = visningString;
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
	
	public ComboBox<String> hentrader(){
		ObservableList<Plass> opptattplass = FXCollections.observableArrayList();
		for (Plassbillett p: plassbillett) {
			opptattplass.add(new Plass(p.getRadnr(),p.getSetenr(),p.getKinosalnr()));
		}
		for(Plass p: opptattplass) {
			plass.remove(p);
		}
		ComboBox<String> cb = new ComboBox<String>();
		int erLik=0;
		for (Plass p: plass) {
			if(p.getRadnr()!=erLik) {
				cb.getItems().add(Integer.toString(p.getRadnr()));
				erLik=p.getRadnr();
			}else {
			}
		}
		return cb;
	}
	
	public ComboBox<String> hentseter(String radnr){
		ObservableList<Plass> opptattplass = FXCollections.observableArrayList();
		for (Plassbillett p: plassbillett) {
			opptattplass.add(new Plass(p.getRadnr(),p.getSetenr(),p.getKinosalnr()));
		}
		for(Plass p: opptattplass) {
			plass.remove(p);
		}
		ComboBox<String> cb = new ComboBox<String>();
		int erLik=0;
		for (Plass p: plass) {
			if(p.getSetenr()!=erLik) {
				cb.getItems().add(Integer.toString(p.getRadnr()));
				erLik=p.getRadnr();
			}else {
			}
		}
		return cb;
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
        	//System.out.println(billettKode + " " + visningsnr + " " + erBetalt);
        	settBillett(billettKode, visningsnr, erBetalt);
        }
        return null;
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


	public void slettAlleBilletter(ObservableList<Billett> ubetaltBillettListe) {
		if (ubetaltBillettListe.isEmpty()) {
			showMessageDialog(null, "Finnes ingen ubetalte lister");
		}else {
		for (Billett u: ubetaltBillettListe) {
				billett.remove(u);
			}
		showMessageDialog(null, "Ubetalte billetter er fjernet");
		ubetaltBillettListe.clear();
		}
	}

	@Override
	public ResultSet finnSpesifikkBillett(String billettKode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean settBillettSomBetalt(String billettKode) {
		//Tar imot billettkode som skal settes til betalt
		boolean billettFinnes= false;
		for(Billett b: billett) {
			if(billettKode.equals(b.getBillettkode())){
				if(b.getErBetalt()) {
					billettFinnes=true;
					showMessageDialog(null, "Billetten er allerede betalt");

				} else {
				System.out.println(b.toString());
				b.setErBetalt(true);
				System.out.println(b.toString());
				showMessageDialog(null, b.toString() + "\n"  + "Billetten er n� satt til betalt");
				
				billettFinnes=true;
				break;
				}
			}
		}
		if(billettFinnes==false) {
			showMessageDialog(null, "Billetten finnes ikke");
			
		}
		return true;
	}

	@Override
	public boolean leggTilFilm(String filmnavn) {
		int sisteFilm = film.size() - 1;
		int sisteFilmNr = film.get(sisteFilm).getFilmnr();
		int nyttFilmNr = sisteFilmNr + 1;
		setFilm(nyttFilmNr,filmnavn);
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
			throw new Exception("Kan ikke hente filmer fra databasen");
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
		try {
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
		}catch(Exception e) {
			throw new Exception("Kan ikke hente kinosaler");
		}
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
	public void hentPlassbilletter() throws Exception {
		try {
			ResultSet resultat = null;
			String sql = "SELECT * FROM tblplassbillett";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			while(resultat.next()) {
				int radnr = resultat.getInt(1);
				int setenr = resultat.getInt(2);
				int kinosalnr = resultat.getInt(3);
				String billettkode = resultat.getString(4);
				setPlassbillett(radnr, setenr, kinosalnr, billettkode);
			}
			//System.out.println(plassbillett);
		}catch(Exception e) {
			throw new Exception("Kan ikke hente fra databasen");
		}
	}

	@Override
	public ResultSet finnSpesifikkPlassbillett(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilVisning(String filmnr, String kinosalnr, LocalDate dato, String starttid, String pris) {
		int siste = visning.size() -1;
		int nrPaaSiste = visning.get(siste).getVisningnr();
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
               
               setAlleVisninger(visningnr, filmnr, kinosalnr, dato, starttid, pris);
               
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
               
               boolean erDatoFremITid = false;
               boolean erDatoSammeDag = false;
               
               long differanseITid = Duration.between(naavarendeTidFormat, startidLocalTime).toMinutes();
               int sjekkDatoer = datoFormat.compareTo(date);
               
               if (sjekkDatoer > 0) {
            	   //dato fra database er senere enn naavarende dato
            	   erDatoFremITid = true;
               } else if (sjekkDatoer < 0) {
            	   //dato fra databsaae er tidligere enn naavarende dato
            	   erDatoFremITid = false;
               } else {
            	   //Samme dag
            	   erDatoFremITid = true;
            	   erDatoSammeDag = true;
               }
               
               if (erDatoFremITid) {
            	   //System.out.println("Dato frem i tid");
            	   if (erDatoSammeDag) {
            		   if(differanseITid >= 30) {
                		   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
                    	   //System.out.println("Mer enn tretti min");
                    	   //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
                	   } else {
                		   //System.out.println("Ikke mer enn tretti min");
                	   }
            	   } else {
            		   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
            	   }
               } else {
            	   //System.out.println("Under tretti eller tidligere dato");
            	   //System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris);
               }
               //System.out.println();
               /* System.out.println("Alle");
               System.out.println(visningnr + " " + filmnr + " " + kinosalnr + " " + dato + " " + starttid + " " + pris); */
		}
		return resultat;
	}
	
	public void hentAntallLedigePlasserSporring() {
		try {
			resultat = null;
			String sql = "SELECT COUNT(p_setenr), p_kinosalnr AS Antall_seter from tblplass";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			
			while(resultat.next()) {
				int ledigePlasser = resultat.getInt(1);
				System.out.println(ledigePlasser);
	            //int setenr = resultat.getInt(2);
	            int kinosalnr = resultat.getInt(2);
	            
	            setAntallLedigePlasserListe(ledigePlasser);
	            //setAntallLedigePlasserListe(setenr);
	            setAntallLedigePlasserListe(kinosalnr);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public ObservableList<List<String>> visningMedFilmnnavnOgAntallLedigePlasser() {
	public void visningMedFilmnnavnOgAntallLedigePlasser() {
		ObservableList<Film> film = getFilm();
		ObservableList<Visning> visninger = getAlleVisninger();
		ObservableList<List<String>> visningString = FXCollections.observableArrayList();
		//final String slutt = "slutt";
		
		
		
		setVisningString(visningString);
	}
	
	public int finnKinosalnrBasertPaaVisningsnr(String visningsnr1) {
		int visningsnr = Integer.parseInt(visningsnr1);
		ObservableList<Visning> visning = getAlleVisninger();
		
		for (Visning v : visning) {
			if (v.getVisningnr() == visningsnr) return v.getKinosalnr();
		}
		return 0;
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

	@Override
	public boolean leggTilVisning(String filmnr, String kinosalnr, String dato, String starttid, String pris) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//------------------------------------ Sletter alt innhold i databasen (kjores n�r applikasjonen avsluttes) --------------------------------------------
	public void slettinnholdAlleTabeller() throws Exception {
		try {
            //Execute SQL query
            String sql1 = "DELETE FROM tblplassbillett";
            String sql2 = "DELETE FROM tblplass";
            //String sql3 = "DELETE FROM tbllogin";
            String sql4 = "DELETE FROM tblbillett";
            String sql5 = "DELETE FROM tblvisning";
            String sql6 = "DELETE FROM tblkinosal";
            String sql7 = "DELETE FROM tblfilm";

            preparedStatement = forbindelse.prepareStatement(sql1);
            preparedStatement.executeUpdate();
            
            preparedStatement = forbindelse.prepareStatement(sql2);
            preparedStatement.executeUpdate();

            //preparedStatement = forbindelse.prepareStatement(sql3);
            //preparedStatement.executeUpdate();
            
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

	public void lagreFilmDB() throws Exception {
		int success = 0;
		int feil = 0;
		String sql = "INSERT INTO tblfilm "
				+ "(f_filmnr,f_filmnavn)"
				+ "VALUES(?,?)";
		preparedStatement = forbindelse.prepareStatement(sql);
		for (Film f: film) {
			preparedStatement.setInt(1, f.getFilmnr());
			preparedStatement.setString(2, f.getFilmnavn());
			int insert = preparedStatement.executeUpdate();
			if(insert == 1) {
				success += 1;
			}
			else {feil += 1;
			}
		}
		//System.out.print("Suksess film: " + success + "\n");
		//System.out.print("Feil film: " + feil +"\n");
	}
	
	public void lagreKinosalDB() throws Exception {
		int success1 = 0;
		int feil1 = 0;
		String sql1 = "INSERT INTO tblkinosal"
				+ "(k_kinosalnr,k_kinonavn,k_kinosalnavn)"
				+ "VALUES(?,?,?)";
		preparedStatement = forbindelse.prepareStatement(sql1);
		for (Kinosal ks: kinosal) {
			preparedStatement.setInt(1, ks.getKinosalnr());
			preparedStatement.setString(2, ks.getKinonavn());
			preparedStatement.setString(3, ks.getKinosalnavn());
			int insert1 = preparedStatement.executeUpdate();
			if(insert1 == 1) {
				success1 += 1;
			}else {
				feil1 += 1;
			}
		}
		//System.out.print("Suksess kinosal: " + success1 + "\n");
		//System.out.print("Feil kinosal: " + feil1 + "\n");
	}
	
	public void lagrePlassDB() throws Exception {
		int success2 = 0;
		int feil2 = 0;
		String sql2 = "INSERT INTO tblplass"
				+ "(p_radnr,p_setenr,p_kinosalnr)"
				+ "VALUES(?,?,?)";
		preparedStatement = forbindelse.prepareStatement(sql2);
		for (Plass p: plass) {
			preparedStatement.setInt(1, p.getRadnr());
			preparedStatement.setInt(2, p.getSetenr());
			preparedStatement.setInt(3, p.getKinosalnr());
			int insert2 = preparedStatement.executeUpdate();
			if(insert2 == 1) {
				success2 += 1;
			}else {
				feil2 += 1;
			}
		}
		//System.out.print("Suksess plass: " + success2 + "\n");
		//System.out.print("Feil plass: " + feil2 + "\n");
	}
	
	public void lagreVisningDB() throws Exception {
		int success3 = 0;
		int feil3 = 0;
		String sql3 = "INSERT INTO tblvisning"
				+ "(v_visningnr,v_filmnr,v_kinosalnr,v_dato,v_starttid,v_pris)"
				+ "VALUES(?,?,?,?,?,?)";
		preparedStatement = forbindelse.prepareStatement(sql3);
		for (Visning v: alleVisninger) {
			preparedStatement.setInt(1, v.getVisningnr());
			preparedStatement.setInt(2, v.getFilmnr());
			preparedStatement.setInt(3, v.getKinosalnr());
			preparedStatement.setDate(4, v.getDato());
			preparedStatement.setTime(5, v.getStarttid());
			preparedStatement.setFloat(6, v.getPris());
			int insert3 = preparedStatement.executeUpdate();
			if(insert3 == 1) {
				success3 += 1;
			}else {
				feil3 += 1;
			}
		}
		//System.out.print("Suksess visning: " + success3 + "\n");
		//System.out.print("Feil visning: " + feil3 + "\n");
	}
	
	public void lagreBillettDB() throws Exception {
		int success4 = 0;
		int feil4 = 0;
		String sql4 = "INSERT INTO tblbillett"
				+ "(b_billettkode,b_visningsnr,b_erBetalt)"
				+ "VALUES(?,?,?)";
		preparedStatement = forbindelse.prepareStatement(sql4);
		for (Billett b: billett) {
			preparedStatement.setString(1, b.getBillettkode());
			preparedStatement.setInt(2, b.getVisningsnr());
			preparedStatement.setBoolean(3, b.getErBetalt());
			int insert4 = preparedStatement.executeUpdate();
			if(insert4 == 1) {
				success4 += 1;
			}else {
				feil4 += 1;
			}
		}
		//System.out.print("Suksess billett: " + success4 + "\n");
		//System.out.print("Feil billett: " + feil4 + "\n");
	}
	
	public void lagrePlassBillett() throws Exception {
		int success5 = 0;
		int feil5 = 0;
		String sql5 = "INSERT INTO tblplassbillett"
				+ "(pb_radnr,pb_setenr,pb_kinosalnr,pb_billettkode)"
				+ "VALUES(?,?,?,?)";
		preparedStatement = forbindelse.prepareStatement(sql5);
		for (Plassbillett pb: plassbillett) {
			preparedStatement.setInt(1, pb.getRadnr());
			preparedStatement.setInt(2, pb.getSetenr());
			preparedStatement.setInt(3, pb.getKinosalnr());
			preparedStatement.setString(4, pb.getBillettkode());
			int insert5 = preparedStatement.executeUpdate();
			if(insert5 == 1) {
				success5 += 1;
			}else {
				feil5 += 1;
			}
		}
		//System.out.print("Suksess plassbillett: " + success5 + "\n");
		//System.out.print("Feil plassbillett: " + feil5 + "\n");
	}
	
	
		
	public void leggAltInnItblFilmVedAvslutning() throws Exception {
        try {
            //}catch(Exception e) {throw new Exception("Kan ikke lagre data");}
        }catch(Exception e) {throw new Exception(e);}
	}


	
}
