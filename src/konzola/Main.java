package konzola;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import logika.Igrica;

public class Main {
	static Scanner sc = new Scanner(System.in);

	    public static void main(String[] args) {
	    	 String nazivDatoteke = "stanjeIgre.ser";

	         File file = new File(nazivDatoteke);

	         try {
	             if (file.createNewFile()) {
	                 System.out.println("Datoteka je stvorena: " + file.getName());
	             } else {
	                 System.out.println("Datoteka već postoji.");
	             }
	         } catch (IOException e) {
	             System.out.println("Došlo je do greške prilikom stvaranja datoteke.");
	             e.printStackTrace();
	         }
	        Igrica igrica = new Igrica();
	        System.out.println("Da li želite nastaviti igru?\n1)Da\t2)Ne");
	        int nastavak = sc.nextInt();
	        if (nastavak == 1) igrica.ucitajStanjeIgre();
	        int[][] trenutnaMatrica = igrica.getMatrica();
	        do {
	        	trenutnaMatrica = igrica.getMatrica();
	        	for (int i = 0; i < 4; i++) {
	        	    for (int j = 0; j < 4; j++) {
	        	        System.out.print(trenutnaMatrica[i][j] + " ");
	        	    }
	        	    System.out.println(); 
	        	}
				System.out.println("Unesi:\n1)Lijevo\n2)Dole\n3)Desno\n5)Gore\n6)Vrati potez\n7)Spremi stanje");
				int broj = sc.nextInt();
				if(broj == 1) {
					igrica.potezLijevo();
				}
				else if(broj == 3) {
					igrica.potezDesno();
				}
				else if(broj == 5) {
					igrica.potezGore();
				}
				else if (broj == 2) {
					igrica.potezDole();
				}
				else if (broj == 6) {
					igrica.vratiNaPrethodnoStanje();
				}
				else if(broj == 7) {
					igrica.spremiStanjeIgre();
					break;
				}
				else {
					System.out.println("Unesite pravi broj molim vas.");
					continue;
				}
				System.out.println("Trenutni broj bodova: " + igrica.dajBodove());
				System.out.println("Trenutna najveca vrijednost: " + igrica.najvecaVrijednostPlocice());
			} while (igrica.nemaLegalnihPoteza() && igrica.najvecaVrijednostPlocice() != 2048);
	        System.out.println("-------------------");
	        for (int i = 0; i < 4; i++) {
	            for (int j = 0; j < 4; j++) {
	                System.out.print(trenutnaMatrica[i][j] + " ");
	            }
	            System.out.println();  
	        }
	        if(igrica.najvecaVrijednostPlocice() == 2048) System.out.println("Cestitamo, došli ste do 2048");
	        else System.out.println("Nema više legalnih poteza, kraj igre.");
	       
	        
	        

	        

	        

	        

	    }

}
