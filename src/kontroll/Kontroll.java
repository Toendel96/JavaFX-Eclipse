package kontroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import domene.Billett;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Kontroll implements kontrollInterface {
	 private String databasenavn = "jdbc:mysql://localhost:3306/kino";
	    private String databasedriver = "com.mysql.jdbc.Driver";
	    private Connection forbindelse;
	    private ResultSet resultat;
	    private PreparedStatement preparedStatement;
	    private String brukernavn = "Case";
	    private String passord = "Esac";
	    private ObservableList<Billett> billettListe = FXCollections.observableArrayList();
	
	//------------------------ �pne/Lukke forbindelse --------------------------------
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
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObservableList<Billett> hentUbetalteBilletter() {
		//Returner en liste med ubetalte billetter
		try {
			ResultSet resultat = lesUbetalteBilletter();
			while(resultat.next()) {
				String billettkode = resultat.getString(1);
				int visningsnr = resultat.getInt(2);
				boolean erBetalt = resultat.getBoolean(3);
				/*boolean erBetalt;
				if (erBetalt1==0) {
					erBetalt=false;
				}else {
					erBetalt=true;
				}*/
				billettListe.add(new Billett(billettkode, visningsnr, erBetalt));
			}
		}catch(Exception e) {System.out.println(e.getMessage());}
		return billettListe;
	}
	
	 public ResultSet lesUbetalteBilletter() throws Exception {
	    	try {
	    		ResultSet resultat = null;
		    	String sql = "SELECT * FROM tblbillett";
	    		preparedStatement = forbindelse.prepareStatement(sql);
	    		resultat = preparedStatement.executeQuery(sql);
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
	public ResultSet hentFilmer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet finnSpesifikkFilm(String filmnr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet hentKinosaler() throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	public ResultSet hentPlassbilletter(String billettkode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet finnSpesifikkPlassbillett(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leggTilVisning(String filmnr, String kinosalnr, String dato, String starttid, String pris) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet hentVisninger(String billettkode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet finnSpesifikkVisning(String kundenr1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    

}
