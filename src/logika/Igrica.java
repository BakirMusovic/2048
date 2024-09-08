package logika;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Igrica implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ubaciBrojNakonPoteza;
	private int bodovi = 0;
	private List<Integer> topRezultati = new ArrayList<>();

	int[][] matrica = new int[4][4];
	int[][] prethodniPotez = new int[4][4];
	
	
	public Igrica(){
		/**Igrica zapocne sa dva broja na matrici, te cu to da postavim u konstruktor, te
		 * cu da stavim broj poteza na nultu vrijednost*/
		int prviBrojX = ThreadLocalRandom.current().nextInt(0, 4);
		int prviBrojY = ThreadLocalRandom.current().nextInt(0, 4);//Postavljam koordinate za prvi broj
		int drugiBrojX;
		int drugiBrojY;
		while(true) { /**Stavljam u petlju da mi se ne bi poklopili vec postojece koordinate*/
			drugiBrojX = ThreadLocalRandom.current().nextInt(0, 4);
			drugiBrojY = ThreadLocalRandom.current().nextInt(0, 4);
			if (prviBrojX != drugiBrojX || prviBrojY != drugiBrojY) break;
		}
		this.vratiBrojNakonPoteza();
		matrica[prviBrojX][prviBrojY] = ubaciBrojNakonPoteza;
		this.vratiBrojNakonPoteza();
		matrica[drugiBrojX][drugiBrojY] = ubaciBrojNakonPoteza;
		ucitajTopRezultate();
	};
	public void vratiBrojNakonPoteza() {
		/**Ova funkcija sluzi da nakon sto odradimo akciju, na ploci se stvori 
		 *ili broj dva ili broj cetiri, to sam uradio sa komandom 
		 *ThreadLocalRandom.current().nextInt koja radi u opsegu [1,11) te vraca 
		 *prirodni broj iz toga opsega. U slucaju da je broj 8 ili ispod, na tabeli
		 *se stvara 2, a u slucaju da je 9 ili 10 na tabeli se stvori 4. 
		 *To znaci u 20% slucajeva se stvori 4 te u 80% slucajeva se stvori 2.*/
		int postotakZaCetiri = ThreadLocalRandom.current().nextInt(1, 11);
		if (postotakZaCetiri < 9) ubaciBrojNakonPoteza = 2;
		else ubaciBrojNakonPoteza = 4;
	};
	
	public int[][] getMatrica() {
        return matrica;
    }
	
	public void potezLijevo () {
		/**Provjeri da li je moguc potez lijevo, te u slucaju da nije samo izademo iz funckije.
		 * U slucaju da je potez moguc prvo spasimo trenutno stanje kao prethodno da mi mogli 
		 * da se vratimo jedan potez u nazad. Nakon toga gledamo svaku celiju razlicitu od nule
		 * te preko interne varijabli temp gledamo gdje trebamo da stavimo taj broj. U slucaju 
		 * da su isti brojevi oni se saberu i doadju se bodovi, a u slucaju da ne nisu samo 
		 * se broj pomjeri do prvog ne nultog mjesta koji je na poziciji tmep
		 */
		if(!this.jeMogucPotezLijevo()) {
			return;
		}
		this.pohraniPrethodnoStanje();
		int temp;
		for (int i = 0; i < 4 ; i++) {
			temp = 0;
			 for (int j = 0; j < 4; j++) {
		            if (matrica[i][j] != 0) {
		                if (temp > 0 && matrica[i][j] == matrica[i][temp - 1]) {
		                	bodovi += matrica[i][temp - 1] * 2;
		                    matrica[i][temp-1] *= 2;
		                    matrica[i][j] = 0;
		                } else {
		                    matrica[i][temp] = matrica[i][j];
		                    if (j!=temp) {
		                        matrica[i][j] = 0;
		                    }
		                    temp++;
		                }
		            }
			 }
	        
		}
		this.unesiBroj();
		
	};
	
	public void potezDesno() {
	    if (!this.jeMogucPotezDesno()) {
	        return;
	    }
	    this.pohraniPrethodnoStanje();
	    for (int i = 0; i < 4; i++) {
	        int temp = 3;
	        for (int j = 3; j >= 0; j--) {
	            if (matrica[i][j] != 0) {
	                if (temp < 3 && matrica[i][j] == matrica[i][temp + 1]) {
	                    bodovi += matrica[i][temp + 1] * 2;
	                    matrica[i][temp + 1] *= 2;
	                    matrica[i][j] = 0;
	                } else {
	                    matrica[i][temp] = matrica[i][j];
	                    if (j != temp) {
	                        matrica[i][j] = 0;
	                    }
	                    temp--;
	                }
	            }
	        }
	    }
	    this.unesiBroj();
	}

	
	public void potezGore() {
		if(!this.jeMogucPotezGore()) {
			return;
		}
		this.pohraniPrethodnoStanje();
	    for (int j = 0; j < 4; j++) {
	        int temp = 0;
	        for (int i = 0; i < 4; i++) {
	            if (matrica[i][j] != 0) {
	                if (temp > 0 && matrica[i][j] == matrica[temp - 1][j]) {
	                	bodovi += matrica[i][temp - 1] * 2;
	                    matrica[temp - 1][j] *= 2;
	                    matrica[i][j] = 0;
	                } else {
	                    matrica[temp][j] = matrica[i][j];
	                    if (i != temp) {
	                        matrica[i][j] = 0;
	                    }
	                    temp++;
	                }
	            }
	        }
	    }
	    this.unesiBroj();   
	}
	
	public void potezDole() {
	    if (!this.jeMogucPotezDole()) {
	        return;
	    }
	    this.pohraniPrethodnoStanje();
	    for (int j = 0; j < 4; j++) {
	        int temp = 3;
	        for (int i = 3; i >= 0; i--) {
	            if (matrica[i][j] != 0) {
	                if (temp < 3 && matrica[i][j] == matrica[temp + 1][j]) {
	                    bodovi += matrica[temp + 1][j] * 2;
	                    matrica[temp + 1][j] *= 2;
	                    matrica[i][j] = 0;
	                } else {
	                    matrica[temp][j] = matrica[i][j];
	                    if (i != temp) {
	                        matrica[i][j] = 0;
	                    }
	                    temp--;
	                }
	            }
	        }
	    }
	    this.unesiBroj();
	}

	
	public void unesiBroj() {
		/**Funkcija za unos broja nakon poteza.*/
		this.vratiBrojNakonPoteza();
		int brojKojiUnosimo = this.ubaciBrojNakonPoteza;
		/**Trazim mjesto gdje mogu da ubacim broj, to jeste trazim nulu u matrici*/
		while(true) {
			int prviBrojX = ThreadLocalRandom.current().nextInt(0, 4);
			int prviBrojY = ThreadLocalRandom.current().nextInt(0, 4);
			if(matrica[prviBrojX][prviBrojY] == 0) {
				matrica[prviBrojX][prviBrojY] = brojKojiUnosimo;
				break;
			}
		}
	};
	
	private void pohraniPrethodnoStanje() {
		/**Funkcija koja nam spašava jedan potez u nazad u privatnom dijelu
		 * klase, koju kasnije koristimo za vraćanje jednog poteza u nazad*/
	    for (int i = 0; i < 4; i++) {
	        System.arraycopy(matrica[i], 0, prethodniPotez[i], 0, 4);
	    }
	}
	public void vratiNaPrethodnoStanje() {
		/**Funkcija koja nam omogućava vracanje na iz prethodnog poteza na nacin 
		 * da kopira matricu prethodniPotez u nasu amtricu za igru.*/
	    for (int i = 0; i < 4; i++) {
	        System.arraycopy(prethodniPotez[i], 0, matrica[i], 0, 4);
	    }
	}
	public boolean jeMogucPotezLijevo() {
	    /**Ova funkcija radi na nacin da provjeri svaki broj sa ljeve strane nekog 
	     * elementa koji nije 0, te u slucaju da je sa njegove ljeve strane 0 ili
	     * taj isti broj vrati true to jeste potez je moguc, te u slucaju da nije
	     * moguc vrati false*/
	    for (int i = 0; i < 4; i++) {
	        for (int j = 1; j < 4; j++) {
	            if  (matrica[i][j] != 0 && 
	            	(matrica[i][j - 1] == 0 || matrica[i][j - 1] == matrica[i][j])) {
	                return true;
	            }
	        }
	    }

	    return false;
	}
	public boolean jeMogucPotezDesno() {
	    for (int i = 0; i < 4; i++) {
	        for (int j = 2; j >= 0; j--) {
	            if  (matrica[i][j] != 0 && 
	            	(matrica[i][j + 1] == 0 || matrica[i][j + 1] == matrica[i][j])) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	public boolean jeMogucPotezGore() {
	    for (int j = 0; j < 4; j++) {
	        for (int i = 1; i < 4; i++) {
	            if (matrica[i][j] != 0 && (matrica[i - 1][j] == 0 || matrica[i - 1][j] == matrica[i][j])) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	public boolean jeMogucPotezDole() {
	    for (int j = 0; j < 4; j++) {
	        for (int i = 2; i >= 0; i--) {
	            if (matrica[i][j] != 0 && (matrica[i + 1][j] == 0 || matrica[i + 1][j] == matrica[i][j])) {
	                return true;
	            }
	        }
	    }

	    return false;
	}
	
	public boolean nemaLegalnihPoteza() {
		/**Provjerim da postoji makar jedan potez koji je moguc, u slucaju da ne postoji
		 * vrati false, i to prekine igru.*/
		if(!(this.jeMogucPotezLijevo() || this.jeMogucPotezDesno() 
			|| this.jeMogucPotezGore() || this.jeMogucPotezDole()))
			return false;
		return true;
	}
	
	public int najvecaVrijednostPlocice() {
	    int najvecaVrijednost = 0;
	    /**Vrati najvecu vrijednost plocice, ona se koristi da kada dodemo do 2048 prekine
	     * igricu*/
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            if (matrica[i][j] > najvecaVrijednost) {
	                najvecaVrijednost = matrica[i][j];
	            }
	        }
	    }

	    return najvecaVrijednost;
	}
	public int dajBodove () {return bodovi;}
	
	public void spremiStanjeIgre() {
		/**Metoda otvara BufferedWriter za pisanje u datoteku "stanjeIgre.txt",
		 * nakong toga u njoj zapisuje broj bodova, te linuju po liniju zapisuje 
		 * brojeve matrice u istom redu. Na kraju zatvar BufferedWriter. Ako dode do iznime 
		 * ispisuje poruku o iznicmi*/
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("stanjeIgre.txt"))) {
            writer.write("Bodovi: " + bodovi + "\n");
            writer.write("Matrica:\n");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    writer.write(matrica[i][j] + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public void ucitajStanjeIgre() {
		/**Metoda otvara BufferedReader za citanje iz datoteke "stanjeIgre.txt".
		  *	Cita linije iz datoteke sve dok ima linija.
	  	  * Ako linija zapocinje s "Bodovi:", parsira preostali dio linije i postavlja bodove.
		  * Ako linija je "Matrica:", cita matricu iz narednih 4 linija u datoteci.
		  * Zatvara BufferedReader.
		  * Ako dođe do iznimke tipa IOException ili NumberFormatException, ispisuje poruku o iznimci.*/
        try (BufferedReader reader = new BufferedReader(new FileReader("stanjeIgre.txt"))) {
            String linija;
            while ((linija = reader.readLine()) != null) {
                if (linija.startsWith("Bodovi:")) {
                    bodovi = Integer.parseInt(linija.substring("Bodovi:".length()).trim());
                } else if (linija.equals("Matrica:")) {
                    for (int i = 0; i < 4; i++) {
                        linija = reader.readLine();
                        String[] elementi = linija.trim().split(" ");
                        for (int j = 0; j < 4; j++) {
                            matrica[i][j] = Integer.parseInt(elementi[j]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	 private void ucitajTopRezultate() {
		 /**Brise trenutni sadrzaj liste topRezultati.
		  * Provjerava postojanje datoteke "topRezultati.txt". Ako ne postoji, inicijalizira top rezultate i vraca se.
		  * Otvara BufferedReader za citanje iz datoteke "topRezultati.txt".
		  * Cita linije iz datoteke sve dok ima linija, pretvara ih u brojeve i dodaje u listu topRezultati.
		  * Zatvara BufferedReader.
	      * Ako dode do iznimke tipa IOException ili NumberFormatException, ispisuje poruku o iznimci.
		  * Ako je lista topRezultati prazna nakon ucitavanja, inicijalizira top rezultate.*/
		 
	        topRezultati.clear();

	        File datoteka = new File("topRezultati.txt");

	        if (!datoteka.exists()) {
	            inicijalizirajTopRezultate();
	            return;
	        }

	        try (BufferedReader reader = new BufferedReader(new FileReader(datoteka))) {
	            String linija;
	            while ((linija = reader.readLine()) != null) {
	                topRezultati.add(Integer.parseInt(linija));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        if (topRezultati.isEmpty()) {
	            inicijalizirajTopRezultate();
	        }
	    }

	    private void inicijalizirajTopRezultate() {
	    	/**Napravimo dodam 10 0 top rezultate, i zatim ih spremim u datoteku*/
	        for (int i = 0; i < 10; i++) {
	            topRezultati.add(0);
	        }
	        spremiTopRezultate();
	    }

	    private void spremiTopRezultate() {
	    	/**Provjeri da li postoji datotekatopRezultati ako ne postoji napravi ga.
	    	 * Nakon toga upisuje top rezultate u datoteku*/
	        File datoteka = new File("topRezultati.txt");

	        if (!datoteka.exists()) {
	            try {
	                datoteka.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
	        }

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(datoteka))) {
	            for (int rezultat : topRezultati) {
	                writer.write(String.valueOf(rezultat));
	                writer.newLine();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void azurirajRangListu(int osvojeniBodovi) {
	    	/**Dodaje osvojeneBodove u topRezultate, nakon toga ih sortira, te samo 
	    	 * spasava 10 nabjoljih. Nakon toga poziva funkciju spremiTopRezultate te ih
	    	 * zapisuje u datoteku.*/
	        topRezultati.add(osvojeniBodovi);
	        Collections.sort(topRezultati, Collections.reverseOrder());
	        topRezultati = topRezultati.subList(0, Math.min(topRezultati.size(), 10));

	        spremiTopRezultate();
	    }

	    public List<Integer> getTopRezultati() {
	        return topRezultati;
	    }
	    
	    public Igrica kopirajStanje() {
	        Igrica novaIgrica = new Igrica();
	        novaIgrica.bodovi = this.bodovi;

	        for (int i = 0; i < 4; i++) {
	            System.arraycopy(this.matrica[i], 0, novaIgrica.matrica[i], 0, 4);
	        }

	        return novaIgrica;
	    }
}
	



