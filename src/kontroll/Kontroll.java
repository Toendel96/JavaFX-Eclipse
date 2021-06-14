package kontroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import domene.Film;

public class Kontroll implements kontrollInterface {
	 private String databasenavn = "jdbc:mysql://localhost:3306/kino";
	    private String databasedriver = "com.mysql.jdbc.Driver";
	    private Connection forbindelse;
	    private ResultSet resultat;
	    private PreparedStatement preparedStatement;
	    private String brukernavn = "Case";
	    private String passord = "Esac";
	
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
	public ArrayList<Film> hentFilmer() throws Exception {
		try {
			String sql = "SELECT * FROM tblfilm";
			preparedStatement = forbindelse.prepareStatement(sql);
			resultat = preparedStatement.executeQuery(sql);
			ArrayList<Film> filmer = new ArrayList<Film>();
			while(resultat.next()) {
				int filmNr = resultat.getInt(0);
				System.out.println(filmNr);
				String filmNavn = resultat.getString(1);
				System.out.println(filmNavn);
				filmer.add(new Film(filmNr,filmNavn));
			}
			return filmer;
		}catch(Exception e) {}
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
