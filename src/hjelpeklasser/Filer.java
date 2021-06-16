package hjelpeklasser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Filer {
	
	public static PrintWriter lagSkriveForbindelse(String filnavn) {
		try {
			FileWriter filForbindelse = new FileWriter(filnavn);				
			BufferedWriter skriveBuffer = new BufferedWriter(filForbindelse);
			PrintWriter skriver = new PrintWriter(skriveBuffer);
			return skriver;			
		}catch(IOException e) {return null;}
	} //Metode
	
	public static BufferedReader lagLeseForbindelse(String filnavn) {
		try {
			FileReader filForbindelse = new FileReader(filnavn);
			BufferedReader leser = new BufferedReader(filForbindelse);
			return leser;
		}catch(IOException e){return null;}		
	} //metode{

}

