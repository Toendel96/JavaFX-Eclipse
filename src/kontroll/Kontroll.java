package kontroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
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
	    private ObservableList<Integer> antallLedigePlasserListe = FXCollections.observableArrayList();
	    private ObservableList<Plass> tempreservasjon = FXCollections.observableArrayList();


	
	//------------------------ aapne/Lukke forbindelse --------------------------------
    public void lagForbindelse() throws Exception {
        try {
            forbindelse = DriverManager.getConnection(databasenavn, brukernavn, passord);
            //Tilkobling til database fungerte
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
	
	public ComboBox<String> hentrader(String visningsnr, int kinosalnr){
		ObservableList<Plass> ledigplass=hentledigplass(visningsnr,kinosalnr);
		ComboBox<String> cb = new ComboBox<String>();
		if(ledigplass.isEmpty()) {
			//Finnes ingen ledige plasser");
			cb.getItems().add("Ingen ledige rader");
		}else {
			int erLik=0;
			for (Plass p: ledigplass) {
				if(p.getRadnr()!=erLik) {
					cb.getItems().add(Integer.toString(p.getRadnr()));
					erLik=p.getRadnr();
				}else {
				}

			}
		}
		return cb;
	}
	
	public ObservableList<Plass> hentledigplass(String visningsnr,int kinosalnr){
		try {
		ObservableList<Plass> opptattplass = FXCollections.observableArrayList();
		ObservableList<Plass> ledigplass = FXCollections.observableArrayList();
		ObservableList<Plass> faktiskledigplass = FXCollections.observableArrayList();
		for(Plass p: plass) {
			if(p.getKinosalnr()==kinosalnr) {
				ledigplass.add(new Plass(p.getRadnr(),p.getSetenr(),p.getKinosalnr()));
			}
		}
		for (Billett b: billett) {
			if(Integer.toString(b.getVisningsnr()).equals(visningsnr)) {
				for (Plassbillett p: plassbillett) {
					if(p.getBillettkode().equals(b.getBillettkode())) {
						opptattplass.add(new Plass(p.getRadnr(),p.getSetenr(),p.getKinosalnr()));
					}
				}
			}
		}
		boolean finnes= false;
		for(Plass l: ledigplass) {
			for(Plass o:opptattplass) {
				if(o.getRadnr()==l.getRadnr() && o.getSetenr()==l.getSetenr()) {
					finnes=true;
					break;
				} else {
					finnes=false;
				}
			}if(!finnes) {faktiskledigplass.add(new Plass(l.getRadnr(),l.getSetenr(),l.getKinosalnr()));}	
		}
		return faktiskledigplass;
		}catch (Exception e){ e.printStackTrace(); return null;}
	}
	
	public ComboBox<String> hentseter(String visningsnr, String radnr, int kinosalnr){
		ComboBox<String> cb = new ComboBox<String>();
		ObservableList<Plass> ledigplasser=hentledigplass(visningsnr,kinosalnr);
		//ObservableList<String> rader=hentrader(radnr, kinosalnr).getItems();
		for(Plass p: ledigplasser) {
			if(p.getRadnr()==Integer.parseInt(radnr)) {
				cb.getItems().add(Integer.toString(p.getSetenr()));
			}
		}
		return cb;
	}
	
	public ObservableList<Plass> leggTilSete(String sete,String rad, int kinosalnr){
		if(tempreservasjon.isEmpty()) {
			tempreservasjon.add(new Plass(Integer.parseInt(rad), Integer.parseInt(sete), kinosalnr));
		}else {
			boolean finnes=false;
			for(Plass p:tempreservasjon ) {
				if(p.getRadnr()==Integer.parseInt(rad) && p.getSetenr()==Integer.parseInt(sete)) {
					showMessageDialog(null, "Du har allerede lagt til denne plassen");
					finnes=true;
				}
			}if(!finnes) {
				tempreservasjon.add(new Plass(Integer.parseInt(rad), Integer.parseInt(sete), kinosalnr));
			}
		}return tempreservasjon;
	}
	
	public String regnutpris(String visningsnr){
		float pris=0;
		for (Visning v: alleVisninger) {
			if(v.getVisningnr()==Integer.parseInt(visningsnr)) {
				pris=v.getPris();
				break;
			}
		}
		System.out.println("prisen for filmen er "+ pris);
		int antallseter=tempreservasjon.size();
		System.out.println("Du har reservert: "+antallseter + " seter");
		float totalpris=antallseter*pris;
		System.out.println("totalpris er : "+totalpris);
		return "";
	}
	
	public int finnLedigePlasserForKinosal(String visningsnr, int kinosalnr) {
		ObservableList<Plass> ledigplass = hentledigplass(visningsnr, kinosalnr);
		int teller = 0;
		
		for (Plass p : ledigplass) {
				teller++;
		}
		
		return teller;
	}
	
	public String getFormattertString1() {
		
		ObservableList<Visning> visningOrdinar = getAlleVisninger();
		visningOrdinar.sort(Comparator.comparingInt(Visning::getVisningnr));
		
		String string = "";
		String antallLedigePlasser = null;
		String filmnavn = null;
		
		string = string + " " + "visningsnr" + "     ";
		string = string + " " + "filmnr "+ "       ";
		string = string + " " + "filmnavn "+ "          ";
		string = string + " " + "kinosalnr" + "         ";
		string = string + " " + "dato" + "               ";
		string = string + " " + "starttid" + "           ";
		string = string + " " + "pris" + "         ";
		string = string + " " + "antallLedigePlasser" + "\n";
		
		for (Visning v : visningOrdinar) {
			String visningsnr = String.valueOf(v.getVisningnr());
			int filmnr1 = v.getFilmnr();
			
			for (Film f : getFilm()) {
				if (filmnr1 == f.getFilmnr()) {
					filmnavn = f.getFilmnavn();
				}
			}
			
			String filmnr = String.valueOf(filmnr1);
			int kinosalnr1 = v.getKinosalnr();
			antallLedigePlasser = String.valueOf(finnLedigePlasserForKinosal(visningsnr, kinosalnr1));
			
			String kinosalnr = String.valueOf(kinosalnr1);
			String dato = String.valueOf(v.getDato());
			String starttid = String.valueOf(v.getStarttid());
			String pris = String.valueOf(v.getPris());
			

			string = string + " " + visningsnr + "                    ";
			string = string + " " + filmnr + "                 ";
			string = string + " " + filmnavn + "                 ";
			string = string + " " + kinosalnr + "          ";
			string = string + " " + dato + "        ";
			string = string + " " + starttid + "        ";
			string = string + " " + pris + "                    ";
			string = string + " " + antallLedigePlasser + "\n";
		}
		
		return string;
	}
	
	//Sortert paa film
	public String getFormattertString2() {
		
	ObservableList<Visning> visningOrdinar = getAlleVisninger();
	visningOrdinar.sort(Comparator.comparingInt(Visning::getFilmnr).reversed());
		
		String string = "";
		String antallLedigePlasser = null;
		String filmnavn = null;
		
		string = string + " " + "visningsnr" + "     ";
		string = string + " " + "filmnr "+ "       ";
		string = string + " " + "filmnavn "+ "          ";
		string = string + " " + "kinosalnr" + "         ";
		string = string + " " + "dato" + "               ";
		string = string + " " + "starttid" + "           ";
		string = string + " " + "pris" + "         ";
		string = string + " " + "antallLedigePlasser" + "\n";
		
		
		for (Visning v : getAlleVisninger()) {
			String visningsnr = String.valueOf(v.getVisningnr());
			int filmnr1 = v.getFilmnr();
			
			for (Film f : getFilm()) {
				if (filmnr1 == f.getFilmnr()) {
					filmnavn = f.getFilmnavn();
				}
			}
			
			String filmnr = String.valueOf(filmnr1);
			int kinosalnr1 = v.getKinosalnr();
			antallLedigePlasser = String.valueOf(finnLedigePlasserForKinosal(visningsnr, kinosalnr1));
			
			String kinosalnr = String.valueOf(kinosalnr1);
			String dato = String.valueOf(v.getDato());
			String starttid = String.valueOf(v.getStarttid());
			String pris = String.valueOf(v.getPris());
			

			string = string + " " + visningsnr + "                    ";
			string = string + " " + filmnr + "                 ";
			string = string + " " + filmnavn + "                 ";
			string = string + " " + kinosalnr + "          ";
			string = string + " " + dato + "        ";
			string = string + " " + starttid + "        ";
			string = string + " " + pris + "                    ";
			string = string + " " + antallLedigePlasser + "\n";
		}
		
		return string;
	}

	//Sortert basert paa tidspunkt (dato og tid)
	public String getFormattertString3() {
		
		String string = "";
		String antallLedigePlasser = null;
		String filmnavn = null;
		
		string = string + " " + "visningsnr" + "     ";
		string = string + " " + "filmnr "+ "       ";
		string = string + " " + "filmnavn "+ "          ";
		string = string + " " + "kinosalnr" + "         ";
		string = string + " " + "dato" + "               ";
		string = string + " " + "starttid" + "           ";
		string = string + " " + "pris" + "         ";
		string = string + " " + "antallLedigePlasser" + "\n";
		
		
		ObservableList<Visning> visningOrdinar = getAlleVisninger();
		visningOrdinar.sort(Comparator.comparing(Visning::getDato).reversed());
		
		
		for (Visning v : visningOrdinar) {
			String visningsnr = String.valueOf(v.getVisningnr());
			int filmnr1 = v.getFilmnr();
			
			for (Film f : getFilm()) {
				if (filmnr1 == f.getFilmnr()) {
					filmnavn = f.getFilmnavn();
				}
			}
			
			String filmnr = String.valueOf(filmnr1);
			int kinosalnr1 = v.getKinosalnr();
			antallLedigePlasser = String.valueOf(finnLedigePlasserForKinosal(visningsnr, kinosalnr1));
			
			String kinosalnr = String.valueOf(kinosalnr1);
			String dato = String.valueOf(v.getDato());
			String starttid = String.valueOf(v.getStarttid());
			String pris = String.valueOf(v.getPris());
			
	
			string = string + " " + visningsnr + "                    ";
			string = string + " " + filmnr + "                 ";
			string = string + " " + filmnavn + "                 ";
			string = string + " " + kinosalnr + "          ";
			string = string + " " + dato + "        ";
			string = string + " " + starttid + "        ";
			string = string + " " + pris + "                    ";
			string = string + " " + antallLedigePlasser + "\n";
		}
		
		return string;
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
	
	public ResultSet hentKinoStatistikk(String kinosalnr) throws Exception {
		
		
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

				String salP = Integer.toString(salProsent);
				String antallV = Integer.toString(antallVisninger);

			}
		}
		
		return null;
	}
	
public String getStatistikkString(String kinosalNr) {
		
		String string = "";
		String kinosalnr = kinosalNr;
		int antallVisninger = 0;
		int antallPlasser = 0;
		int antallSolgt = 0;
		int visningnr = 0;
		//String antallLedigePlasser = null;
		
		string = string + " " + "Antall visninger" + "         ";		
		string = string + " " + "Prosent sal" + "\n";
		
		
		
		for (Visning v : getAlleVisninger()) {
			if (String.valueOf(v.getKinosalnr()).equals(kinosalnr)) {
				antallVisninger++;
				
			}
			/*System.out.println(v.toString());
			String visningsnr = String.valueOf(v.getVisningnr());
			String filmnr = String.valueOf(v.getFilmnr());
			int kinosalnr1 = v.getKinosalnr();
			//antallLedigePlasser = String.valueOf(finnLedigePlasserForKinosal(kinosalnr1));
			
			//String kinosalnr = String.valueOf(kinosalnr1);
			String dato = String.valueOf(v.getDato());
			String starttid = String.valueOf(v.getStarttid());
			System.out.println(starttid);
			String pris = String.valueOf(v.getPris());*/
			
		}	
		
		for (Plass p : getPlass()) {
			if (String.valueOf(p.getKinosalnr()).equals(kinosalnr)) {
				antallPlasser++;
				
			}
		}
		
		for (Visning v : getVisning()) {
			if (String.valueOf(v.getKinosalnr()).equals(kinosalnr)) {
				
				
			}
		}
			string = string + " " + antallVisninger + "  ";	
			string = string + " " + antallPlasser + "\n";
			
		return string;
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
	public boolean finnSpesifikkKinosal(int kinosalnr) {
		return false;
		// TODO Auto-generated method stub
	}
	
	public void hentAntallLedigePlasserSporring() {
		try {
			resultat = null;
			String sql = "SELECT COUNT(p_setenr), p_kinosalnr AS Antall_seter from tblplass";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			
			while(resultat.next()) {
				int ledigePlasser = resultat.getInt(1);
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


	public void slettAlleBilletter(ObservableList<Billett> ubetaltBillettListe) {
		if (ubetaltBillettListe.isEmpty()) {
			showMessageDialog(null, "Finnes ingen ubetalte billetter");
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
				b.setErBetalt(true);
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

	
	public ChoiceBox<String> visFilmerChoice() {
		//Returnerer en choicebox med alle filmnavn
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (Film f: film) {
			cb.getItems().add(f.getFilmnavn());
		}
		return cb;
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
            	   //Dato frem i tid
            	   if (erDatoSammeDag) {
            		   if(differanseITid >= 30) {
                		   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
                    	   //Mer enn tretti min
                	   } else {
                		   //kke mer enn tretti min
                	   }
            	   } else {
            		   setVisning(visningnr, filmnr, kinosalnr, dato, starttid, pris);
            	   }
               } else {
            	   //Under tretti eller tidligere dato
               }
               //Alle
		}
		return resultat;
	}
	
	public boolean sjekkOmDatoTidErFremtid(Time starttid, Date dato) {
		LocalDate date = LocalDate.now(); 
		LocalDate datoFormat = toLocalDate(dato);
		
		LocalTime startidLocalTime = toLocalTime(starttid);
		
		LocalDateTime naavarendeTid = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");  
        String formatString = naavarendeTid.format(format);  
        LocalTime naavarendeTidFormat = LocalTime.parse(formatString);
        
        boolean erDatoFremITid = false;
        boolean erDatoSammeDag = false;
        
        long differanseITid = Duration.between(naavarendeTidFormat, startidLocalTime).toMinutes();
        int sjekkDatoer = datoFormat.compareTo(date);
        
        if (sjekkDatoer > 0) {
     	   //dato fra database/objekt er senere enn naavarende dato
     	   erDatoFremITid = true;
        } else if (sjekkDatoer < 0) {
     	   //dato fra database/objekt er tidligere enn naavarende dato
     	   erDatoFremITid = false;
     	   return false;
        } else {
     	   //Samme dag
     	   erDatoFremITid = true;
     	   erDatoSammeDag = true;
        }
        
        if (erDatoFremITid) {
     	   //Dato frem i tid
     	   if (erDatoSammeDag) {
     		   if(differanseITid >= 0) {
         		   return true;
             	   //Visning har ikke begynt enda
         	   } else {
         		   //Visning har startet for samme dag
         		   return false;
         	   }
     	   } else {
     		   
     	   }
        } else {
     	   //Under tretti eller tidligere dato
        	return false;
        }
		return true;
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
		try {
		if(visningsnr1.isEmpty())  { 
			showMessageDialog(null, "Tomt felt");
		} else {
			int visningsnr = Integer.parseInt(visningsnr1);
			ObservableList<Visning> visning = getAlleVisninger();
			boolean finnes=false;
			for (Visning v : visning) {
				if (v.getVisningnr() == visningsnr) {
					finnes=true;
					return v.getKinosalnr();
				} 
			}
			if(!finnes) {
				showMessageDialog(null, "Visningsnummeret finnes ikke");
			}
		}
		return 0;
		}catch (NumberFormatException e) {showMessageDialog(null, "Visningsnummer skal bare inneholde tall"); return 0;}
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
	public boolean finnSpesifikkVisning(String visningsnr) {
		boolean finnes=false;
		for(Visning v: alleVisninger) {
			if(Integer.toString(v.getVisningnr()).equals(visningsnr)) {
				finnes=true;
				}	
			} 
		if(!finnes) {
		showMessageDialog(null, "Visningsnummeret finnes ikke");
		}
		return finnes;
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
	}

	
}
