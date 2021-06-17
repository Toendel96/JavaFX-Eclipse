package kontroll;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import domene.Film;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import domene.Billett;
import domene.Film;
import domene.Kinosal;
import domene.Plass;
import domene.Plassbillett;
import domene.Visning;
import grensesnitt.Main;
import hjelpeklasser.Filer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	    private ObservableList<Billett> ubillettlisteFrafil = FXCollections.observableArrayList();
	    private ObservableList<Billett> ubetaltBillettListe = FXCollections.observableArrayList();

	
	//------------------------ aapne/Lukke forbindelse --------------------------------
	    /** Kodet av 7088, kontrollert og godkjent av 7074*/
    public void lagForbindelse() throws Exception {
        try {
            forbindelse = DriverManager.getConnection(databasenavn, brukernavn, passord);
            //Tilkobling til database fungerte
        } catch (Exception e) {
            throw new Exception("Kan ikke oppnaa kontakt med databasen");
        }
    }
    /** Kodet av 7088, kontrollert og godkjent av 7044*/
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
	
	public void settBillettNy(ObservableList<Billett> billett) {
		this.billett = billett;
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

	public ObservableList<Integer> getAntallLedigePlasserListe() {
		return antallLedigePlasserListe;
	}

	public void setAntallLedigePlasserListe(int verdi) {
		antallLedigePlasserListe.add(verdi);
	}
	/** Kodet av 7088 , kontrollert og godkjent av 7079 */
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
	
	public void settPlassbillettNy(ObservableList<Plassbillett> plassbillett) {
		this.plassbillett = plassbillett;
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
	
	
	public ObservableList<Plass> getTempreservasjon() {
		return tempreservasjon;
	}

	public void setTempreservasjon(ObservableList<Plass> tempreservasjon) {
		this.tempreservasjon = tempreservasjon;
	}
	
	public void setTempreservasjonNull() {
		tempreservasjon.clear();
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
	/** Kodet av 7079, kontrollert og godkjent av 7085 */
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
	/** Kodet av 7074, kontrollert og godkjent av 7088*/
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
	/** Kodet av 7079, kontrollert og godkjent av 7104*/
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
	/** Kodet av 7074, kontrollert og godkjent av 7085*/
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
	
	/** Kodet av 7074, kontrollert og godkjent av 7104 */
	
	public boolean giBestillingBekreftelse(String visningsnr, boolean erKinoBetjent) {
		boolean status = false;
	    String string = "";
	    String filmnavn = null;
	    int filmnr1 = 0;
	    int kinosalnrtemp = 0;
	    String totalpris = regnutpris(visningsnr);
		
		ObservableList<Plass> tempreservasjon = getTempreservasjon();
		
		for (Plass p : tempreservasjon) {
			kinosalnrtemp = p.getKinosalnr();
			//System.out.println(kinosalnrtemp);
		}
		
		ObservableList<Billett> billett = getBillett();
		
		int index = billett.size()-1;
		Billett billettVerdi = billett.get(index);
		int billettkode = Integer.parseInt(billettVerdi.getBillettkode());
		billettkode = billettkode+1;
		
	    ObservableList<Film> film = getFilm();
	    ObservableList<Plass> plass = getPlass();
	    ObservableList<Plassbillett> plassbillett = getPlassbillett();
	    ObservableList<Visning> alleVisninger = FXCollections.observableArrayList();
	    
		string = "                                                                      Vil du bekrefte bestillingen?\n\n";
		string = string + " " + "visningsnr" + "     ";
		string = string + " " + "billettkode" + "     ";
		string = string + " " + "filmnr "+ "       ";
		string = string + " " + "filmnavn "+ "          ";
		string = string + " " + "kinosalnr" + "         ";
		string = string + " " + "dato" + "               ";
		string = string + " " + "starttid" + "           ";
		string = string + " " + "pris" + "         \n";
		
		
		for (Visning v : getAlleVisninger()) {
			if (visningsnr.equals(String.valueOf(v.getVisningnr()))) {
				filmnr1 = v.getFilmnr();
			}
			
			for (Film f : getFilm()) {
				if (filmnr1 == f.getFilmnr()) {
					filmnavn = f.getFilmnavn();
				}
			}
			
			String filmnr = String.valueOf(filmnr1);
			int kinosalnr1 = v.getKinosalnr();
			
			String kinosalnr = String.valueOf(kinosalnr1);
			String dato = String.valueOf(v.getDato());
			String starttid = String.valueOf(v.getStarttid());
			String pris = String.valueOf(v.getPris());
			

			string = string + " " + visningsnr + "                           ";
			string = string + " " + billettkode + "                ";
			string = string + " " + filmnr + "               ";
			string = string + " " + filmnavn + "                  ";
			string = string + " " + kinosalnr + "          ";
			string = string + " " + dato + "      ";
			string = string + " " + starttid + "          ";
			string = string + " " + totalpris + "     \n";
			
			string = string + "\n";
			string = string + " " + "radnr" + "     ";
			string = string + " " + "setenr" + "     \n";
			
			for (Plass p : tempreservasjon) {
				if (kinosalnrtemp == p.getKinosalnr()) {
				string = string + "     " + String.valueOf(p.getRadnr());
				string = string + "           " + String.valueOf(p.getSetenr()) + "\n";	
				
				setPlassbillett(p.getRadnr(), p.getSetenr(), Integer.parseInt(kinosalnr), String.valueOf(billettkode));
				}
			}
			settBillett(String.valueOf(billettkode), Integer.parseInt(visningsnr), erKinoBetjent);
			break;
		}
		
		
		Alert avbrytEllerBekreft = new Alert(AlertType.CONFIRMATION);
		Alert ok = new Alert(AlertType.INFORMATION);
		Alert avbrutt = new Alert(AlertType.INFORMATION);
		avbrytEllerBekreft.getDialogPane().setMinHeight(400);
		avbrytEllerBekreft.getDialogPane().setMinWidth(800);
		ok.getDialogPane().setMinWidth(600);
		
		avbrytEllerBekreft.setContentText(string);
		
		String string2 = "";
		
		string2 = "Bestilling bekreftet\n";
		string2 = string2 + "Billettene m� hentes senest 30 minutter f�r forestillingen\n";
		string2 = string2 + "Du m� oppgi billettkoden n�r du kommer";
		
		ok.setContentText(string2);
		avbrutt.setContentText("Du fullforte ikke bestillingen");
		
		 Optional<ButtonType> result = avbrytEllerBekreft.showAndWait();
		 if (result.isPresent() && result.get() == ButtonType.OK) {
			 status = true;
			 ok.show();
		 } else {
			avbrutt.show();
			status = false;
		 }
		 
		 if (status) {
			 bestillingBekreftetLeggInnIListe(visningsnr);
			 return true; 
		 } else {
			 
			//Skal slettes --------------------------
			 for (Billett b : getBillett()) {
					System.out.println(b.toString());
				}
				
				for (Plassbillett pb : getPlassbillett()) {
					System.out.println(pb.toString());
				}
			
			
			ObservableList<Billett> billettTemp = FXCollections.observableArrayList();
			ObservableList<Plassbillett> plassbillettTemp = FXCollections.observableArrayList();
			 
			 
			 for (Billett b : getBillett()) {
				 System.out.println("getBillettkode: " + b.getBillettkode());
				 System.out.println("billettkode: " + billettkode);
					if (Integer.parseInt(b.getBillettkode()) == billettkode) {
						System.out.println("treff");
					} else {
						System.out.println("ikke treff");
						billettTemp.add(new Billett(b.getBillettkode(), b.getVisningsnr(), b.getErBetalt()));
					}
				}
				
				for (Plassbillett pb : getPlassbillett()) {
					if (Integer.parseInt(pb.getBillettkode()) == billettkode) {
					} else {
						plassbillettTemp.add(new Plassbillett(pb.getRadnr(), pb.getSetenr(), pb.getKinosalnr(), pb.getBillettkode()));
					}
				}
				
				billett.clear();
				plassbillett.clear();
				
				settBillettNy(billettTemp);
				settPlassbillettNy(plassbillettTemp);
				
				
				//Skal slettes --------------------------
				System.out.println("Slette");
				for (Billett b : getBillett()) {
					System.out.println(b.toString());
				}
				
				for (Plassbillett pb : getPlassbillett()) {
					System.out.println(pb.toString());
				}
				
			 return false;
		 }	 
	}
	
	public void bestillingBekreftetLeggInnIListe(String visningsnr) {
		
	}
	
	public String regnutpris(String visningsnr){
		float pris=0;
		for (Visning v: alleVisninger) {
			if(v.getVisningnr()==Integer.parseInt(visningsnr)) {
				pris=v.getPris();
				break;
			}
		}
		int antallseter=tempreservasjon.size();
		float totalpris=antallseter*pris;
		String faktiskpris=String.valueOf(totalpris);
		return faktiskpris;
	}
	
	public ObservableList<Plass> fjernplass(int radnr, int setenr) {
		int index=99999999;
		for (Plass p:tempreservasjon) {
			//Fjerner �nsket plass fra listen
			if(p.getRadnr()==radnr && p.getSetenr()==setenr) {
				index= tempreservasjon.indexOf(p);
			}
		}
		showMessageDialog(null, "Du fjernet en billett");
		tempreservasjon.remove(index);
		return tempreservasjon;
	}
	/** Kodet av 7104 , kontrollert og godkjent av 7074*/
	public int finnLedigePlasserForKinosal(String visningsnr, int kinosalnr) {
		ObservableList<Plass> ledigplass = hentledigplass(visningsnr, kinosalnr);
		int teller = 0;
		for (Plass p : ledigplass) {
				teller++;
		}
		return teller;
	}
	/** Kodet av 7079, kontrollert og godkjent av 7085*/
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
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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

	/** Kodet av 7088, kontrollert og godkjent av 7079 */
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
	/** Kodet av 7074, kontrollert og godkjent av 7085*/
	public ObservableList<Billett> hentUbetalteBilletter() {
		//Returner en liste med ubetalte billetter
		ubetaltBillettListe.clear();
		for (Billett b:billett) {
			for (Visning v: alleVisninger) {
				if(b.getVisningsnr()==v.getVisningnr()) {
					//Kall p� metode som sjekker mot 30 min
					if(sjekkOmDatoTidErFremtid(v.getStarttid(),v.getDato(), 30)) {
						//Billetten er lengere enn 30min fram i tid
					}else {	
						if(!b.getErBetalt()) {
							ubetaltBillettListe.add(b);
						}
					}
				}
			}
		}
		return ubetaltBillettListe;
	}
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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
	/** Kodet av 7088, kontrollert og godkjent av 7085 */
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
	/** Kodet av 7085 , kontrollert og godkjent av 7079 */
public String getStatistikkFilm(String filmNr) {

		
		String string = "";
		String filmnr = filmNr;
		int visningnr = 0;
		int antallSett = 0;
		String billettKode = null;
		
		
		string = string + " " + "Visningnr" + "         ";
		string = string + " " + "Antall sett" + "         ";	
		string = string + " " + "Prosent kinosal" + "         ";	
		string = string + " " + "Bestilling slettet" + "\n";
		
		for (Visning v : getAlleVisninger()) {
			if (String.valueOf(v.getFilmnr()).equals(filmnr)) {
			visningnr = v.getVisningnr();
			string = string + "   " + visningnr+" ";
			
			}
			Date dato = v.getDato();
			Time tid = v.getStarttid();
			boolean status = sjekkOmDatoTidErFremtid(tid,dato,0);
			if(!status) {
				
			
			for (Billett b : getBillett()) {
				if (b.getVisningsnr()==(visningnr)) {
					if (b.getErBetalt()) {
						billettKode = b.getBillettkode();
					}
				}
			}
			for (Plassbillett pb : getPlassbillett()) {
				if (String.valueOf(pb.getBillettkode()).equals(billettKode)) {
					antallSett++;
				}
				else {
					antallSett=0;
				}
			}
			}
			//string = string + " " + visningnr + "  "; 
			string = string + " " + antallSett + "\n";	
		}
		
								
		return string;
	}	

/** Kodet av 7085 , kontrollert og godkjent av 7079 */
public String getStatistikkKino(String kinosalNr) {

		
		String string = "";
		String kinosalnr = kinosalNr;
		int antallVisninger = 0;
		int antallPlasser = 0;
		int totalPlasser = 0;
		int antallSolgt = 0;
		int visningnr = 0;
		int salProsent = 0;
		
		string = string + " " + "Antall visninger" + "         ";		
		string = string + " " + "Prosent sal" + "\n";
		
		
		//Henter alle visninger for spesifikk kinosal
		for (Visning v : getAlleVisninger()) {
			if (String.valueOf(v.getKinosalnr()).equals(kinosalnr)) {
				//Finner visninger som har vært
				Date dato = v.getDato();
				Time tid = v.getStarttid();
				boolean status = sjekkOmDatoTidErFremtid(tid,dato, 0);
				if (!status) {
					antallVisninger++;
				}
			}			
		}
		//Henter alle plasser som finnes i spesifikk kinosal
		for (Plass p : getPlass()) {
			if (String.valueOf(p.getKinosalnr()).equals(kinosalnr)) {
				antallPlasser++;	
			}
		}
		//Henter alle billetter solgt til en kinosal
		for (Visning v : getAlleVisninger()) {
			if (String.valueOf(v.getKinosalnr()).equals(kinosalnr)) {
				visningnr = v.getVisningnr();
				Date dato = v.getDato();
				Time tid = v.getStarttid();
				boolean status = sjekkOmDatoTidErFremtid(tid,dato, 0);
				if(!status) {
					visningnr = v.getVisningnr();	
				}
			}
		}
		for (Billett b : getBillett()) {
			if (b.getVisningsnr()==(visningnr));
			if (b.getErBetalt()) {
				antallSolgt++;			
			} 
		} 
		totalPlasser = antallVisninger * antallPlasser;
		if (totalPlasser != 0) {
		salProsent = antallSolgt/totalPlasser*100;
		}
			string = string + "             " + antallVisninger + "                          ";	
			string = string + " " + salProsent + "\n";
		return string;
	}
		
	@Override
	public boolean leggTilPlassbillett(String filmnavn) {
		// TODO Auto-generated method stub
		return false;
	}
	/** Kodet av 7074, kontrollert og godkjent av 7104*/
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
		}catch(Exception e) {
			throw new Exception("Kan ikke hente fra databasen");
		}
	}
	
	@Override
	public boolean finnSpesifikkKinosal(int kinosalnr) {
		return false;
		// TODO Auto-generated method stub
	}
	/** Kodet av 7074, kontrollert og godkjent av 7085*/
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

	/** Kodet av 7074, kontrollert og godkjent av 7104 */
	public void slettAlleBilletter(ObservableList<Billett> ubetaltBillettListe) {
		if (ubetaltBillettListe.isEmpty()) {
			showMessageDialog(null, "Finnes ingen ubetalte billetter");
		}else {
			lagreSlettetBillett();
		for (Billett u: ubetaltBillettListe) {
			billett.remove(u);
			}
		showMessageDialog(null, "Ubetalte billetter er slettet");
		ubetaltBillettListe.clear();
		}
	}
	public void lesslettingerfrafil() {
		ubillettlisteFrafil.clear();
		try {
			BufferedReader innfil = Filer.lagLeseForbindelse();
			String linje = innfil.readLine();
			while(linje != null) {
				StringTokenizer innhold = new StringTokenizer(linje,",");
				//Henter ut de 3 feltene og legger dem i hver sin variabel
				String billettkode = innhold.nextToken();
				String visningsnr = innhold.nextToken();
				ubillettlisteFrafil.add(new Billett(billettkode, Integer.parseInt(visningsnr),false));
				linje = innfil.readLine();
			} //l�kke
			for(Billett b:ubillettlisteFrafil) {
			}
			innfil.close();
		}catch(Exception e) {}
	}
	
	public void lagreSlettetBillett(){
		try{
			for(Billett b:ubetaltBillettListe) {
				ubillettlisteFrafil.add(b);	
			}
			PrintWriter utfil = Filer.lagSkriveForbindelse("slettinger.dat");
			for (Billett u: ubillettlisteFrafil) {
				for (Visning v:alleVisninger) {
					if (v.getVisningnr()==u.getVisningsnr()) {
						utfil.println(u.toFile());
					}
				}
			}
			utfil.close();
		}catch(Exception e) {}
	}

	@Override
	public ResultSet finnSpesifikkBillett(String billettKode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/** Kodet av 7104 , kontrollert og godkjent av 7074*/
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
				showMessageDialog(null, b.toString() + "\n"  + "Billetten er naa satt til betalt");
				
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
	/** Kodet av 7079, kontrollert og godkjent av 7085*/
	@Override
	public boolean leggTilFilm(String filmnavn) {
		int sisteFilm = film.size() - 1;
		int sisteFilmNr = film.get(sisteFilm).getFilmnr();
		int nyttFilmNr = sisteFilmNr + 1;
		setFilm(nyttFilmNr,filmnavn);
		return false;
	}

	/** Kodet av 7079, kontrollert og godkjent av 7174  */
	public ChoiceBox<String> visFilmerChoice() {
		//Returnerer en choicebox med alle filmnavn
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (Film f: film) {
			cb.getItems().add(f.getFilmnavn());
		}
		return cb;
	}
	/** Kodet av 7079, kontrollert og godkjent av 7104  */
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
	/** Kodet av 7085 , kontrollert og godkjent av 7079 */
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

	/** Kodet av 7088, kontrollert og godkjent av 7085  */
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
	
	/** Kodet av 7079, kontrollert og godkjent av 7104  */
	public ChoiceBox<String> visVisningerChoice() {
		//Returnerer en choicebox med alle visningsnre
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (Visning v: visning) {
			cb.getItems().add(String.valueOf(v.getVisningnr()));
		}
		return cb;
	}
	/** Kodet av 7088, kontrollert og godkjent av 7085  */
	public boolean sjekkOmDatoTidErFremtid(Time starttid, Date dato, int minutter) {
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
     		   if(differanseITid >= minutter) {
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
	/** Kodet av 7088, kontrollert og godkjent av 7074 */
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
	/** Kodet av 7074, kontrollert og godkjent av 7104*/
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
	
	public boolean oppdaterVisningFilmnr(String visningsnr, String filmnr) {
		return false;
	}
	
	public boolean oppdaterVisningKinosalnr(String visningsnr, String kinosalnr) {
		return false;
	}
	
	public boolean oppdaterVisningDato(String visningsnr, String dato) {
		return false;
	}
	
	public boolean oppdaterVisningStarttid(String visningsnr, String starttid) {
		return false;
	}
	
	public boolean oppdaterVisningPris(String visningsnr, String pris) {
		return false;
	}
	
	//------------------------------------ Sletter alt innhold i databasen (kjores n�r applikasjonen avsluttes) --------------------------------------------
	/** Kodet av 7088, kontrollert og godkjent av 7085  */
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
	/** Kodet av 7079, kontrollert og godkjent av 7104 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7085 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7088 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7088 */
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
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
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
		
	public void leggAltInnItblFilmVedAvslutning() throws Exception {
        try {
            //}catch(Exception e) {throw new Exception("Kan ikke lagre data");}
        }catch(Exception e) {throw new Exception(e);}
	}
}
