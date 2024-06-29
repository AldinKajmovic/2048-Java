package konzola;

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

/**
 * Akademska godina: 2023/2024
 * Konzolna verzija igrice 2048
 * Korisnik moze u kodu promjeniti da ima 4x4 ili 5x5 dimenziju
 * Kontrole su preko tastature unosom "gore", "dolje", "lijevo", "desno"
 * Ako unesete izlaz, prekida trenutnu partiju, ispisuje rezultat i pita da li zelite sljedecu igru
 * Unosom "da" ploca se resetuje, takodje unosom reset dobijamo isto
 * @author Aldin Kajmovic
 */

/**
 * Cilj igre je doci do plocice sa vrijednoscu 2048. Plocice se spajaju ako
 * imaju iste vrijednosti kada se pomicu u odredjenom smjeru. Igra zavrsava kada
 * korisnik dosegne plocicu s vrijednoscu 2048 ili kada nema vise poteza.
 * 
 * Klasa sadrzi konstante za smjerove kretanja plocica (GORE, DOLJE, LIJEVO,
 * DESNO), dvodimenzionalno polje koje predstavlja plocu igre, mapu smjerova
 * pomaka plocica, listu indeksa plocica duz ivica za svaki smjer, te trenutni i
 * najbolji rezultat.
 *
 * @param visinaPloce           Visina ploce.
 * @param sirinaPloce           Sirina ploce.
 * @param ploca                 Dvodimenzionalno polje koje predstavlja plocu.
 * @param OFFSETS               Mapa koja povezuje smjerove s odgovarajucim
 *                              ofsetima za pomak plocica.
 * @param strelicaGoreIndeksi   Lista indeksa plocica duz gornje ivice.
 * @param strelicaDoljeIndeksi  Lista indeksa plocica duz donje ivice.
 * @param strelicaLijevoIndeksi Lista indeksa plocica duz lijeve ivice.
 * @param strelicaDesnoIndeksi  Lista indeksa plocica duz desne ivice.
 * @param iviceIndeksi          Mapa koja povezuje smjerove s listama indeksa
 *                              plocica duz ivica.
 * @param trenutniRezultat      Trenutni rezultat postignut u igri.
 * @param najboljiRezultat      Najbolji rezultat postignut u igri.
 */

public class Igraj2048 {
	private static final int GORE = 1;
	private static final int DOLJE = 2;
	private static final int LIJEVO = 3;
	private static final int DESNO = 4;

	private int[][] ploca;
	private Map<Integer, int[]> OFFSETS = new HashMap<Integer, int[]>();
	private ArrayList<int[]> strelicaGoreIndeksi = new ArrayList<int[]>();
	private ArrayList<int[]> strelicaDoljeIndeksi = new ArrayList<int[]>();
	private ArrayList<int[]> strelicaLijevoIndeksi = new ArrayList<int[]>();
	private ArrayList<int[]> strelicaDesnoIndeksi = new ArrayList<int[]>();
	private Map<Integer, ArrayList<int[]>> iviceIndeksi = new HashMap<Integer, ArrayList<int[]>>();

	private int trenutniRezultat;
	private int najboljiRezultat;

	/**
	 * Poziva metodu igrajIgru.
	 * 
	 */
	public static void main(String[] args) {
		igrajIgru();
	}

	/**
	 * Metoda koja omogucava korisniku igranje igre 2048 putem teksualnih komandi.
	 * Korisnik moze unositi smjerove ("gore", "dolje", "lijevo", "desno") kako bi
	 * pomicao polja na ploci. Igra se izvrsava dok korisnik ne postigne pobjedu
	 * (2048 plocica) ili dok ne zavrsi igru (nema vise mogucih poteza). Nakon svake
	 * igre, korisniku se nudi opcija ponovnog igranja ili izlaska iz igre. Takodje
	 * mozemo prisilno zavrsiti unosom "izlaz".
	 *
	 * @param unos          String koji sadrzi korisnicki unos smjera ili akcije.
	 * @param igra          Instanca klase Igraj2048 koja predstavlja trenutnu igru.
	 * @param igrajOpetUnos String koji sadrzi korisnicki unos za ponovno igranje
	 *                      ili izlaz iz igre.
	 */
	private static void igrajIgru() {
		String unos;
		Igraj2048 igra;

		String igrajOpetUnos;

		do {
			igra = new Igraj2048();
			igra.novoPolje();
			igra.prikaziPlocu();

			do {
				System.out.println("Molimo unesite smjer: gore, dolje, lijevo, desno");
				Scanner in = new Scanner(System.in);
				unos = in.nextLine();
				if ("gore".equalsIgnoreCase(unos)) {
					igra.pokret(GORE);
					igra.prikaziPlocu();
				} else if ("dolje".equalsIgnoreCase(unos)) {
					igra.pokret(DOLJE);
					igra.prikaziPlocu();
				} else if ("desno".equalsIgnoreCase(unos)) {
					igra.pokret(DESNO);
					igra.prikaziPlocu();
				} else if ("lijevo".equalsIgnoreCase(unos)) {
					igra.pokret(LIJEVO);
					igra.prikaziPlocu();
				} else if ("reset".equalsIgnoreCase(unos)) {
					System.out.println("Igra resetovana!");
					igra.reset();
					igra.prikaziPlocu();
				} else if ("izlaz".equalsIgnoreCase(unos)) {
					System.out.println("Prekinuli ste igru !");
					break;
				} else {
					System.out.println("Unesena nepoznata komanda");
				}
			} while (!igra.igraGotova());
			igra.prikaziRezultat();

			System.out.println("Da li zelite opet igrati? (da/ne)");
			Scanner igrajOpetScanner = new Scanner(System.in);
			igrajOpetUnos = igrajOpetScanner.nextLine().toLowerCase();
			igra.reset();
		} while ("da".equals(igrajOpetUnos));
	}

	/**
	 * Konstruktor klase s parametrima Igraj2048 koji inicijalizira igru s
	 * odredjenom visinom i sirinom ploce.
	 *
	 * @param visinaPloce Broj redova na ploci.
	 * @param sirinaPloce Broj kolona na ploci.
	 */
	public Igraj2048(int visinaPloce, int sirinaPloce) {
		// Populate plocu nulama
		ploca = new int[visinaPloce][sirinaPloce];

		// Popuni OFFSET mapu
		OFFSETS.put(GORE, new int[] { 1, 0 });
		OFFSETS.put(DOLJE, new int[] { -1, 0 });
		OFFSETS.put(LIJEVO, new int[] { 0, 1 });
		OFFSETS.put(DESNO, new int[] { 0, -1 });

		// Popuni indekse plocica na ivicama
		for (int i = 0; i < visinaPloce; i++) {
			strelicaLijevoIndeksi.add(new int[] { i, 0 });
			strelicaDesnoIndeksi.add(new int[] { i, getSirinaPloce() - 1 });
		}

		for (int i = 0; i < sirinaPloce; i++) {
			strelicaGoreIndeksi.add(new int[] { 0, i });
			strelicaDoljeIndeksi.add(new int[] { getVisinaPloce() - 1, i });
		}

		// Plocice na ivicama i mapa smjerova
		iviceIndeksi.put(GORE, strelicaGoreIndeksi);
		iviceIndeksi.put(DOLJE, strelicaDoljeIndeksi);
		iviceIndeksi.put(LIJEVO, strelicaLijevoIndeksi);
		iviceIndeksi.put(DESNO, strelicaDesnoIndeksi);
	}

	/**
	 * Konstruktor klase Igraj2048 koji inicijalizira igru s zadanim dimenzijama
	 * ploce (4x4). Postavlja trenutni i najbolji rezultat na pocetne vrijednosti
	 * (0). Mozemo promjeniti da bude i 5x5 dimenzija.
	 */
	public Igraj2048() {
		this(4, 4);
		trenutniRezultat = 0;
		najboljiRezultat = 0;
	}

	/**
	 * Resetuje trenutno stanje igre na pocetne vrijednosti. Postavlja trenutni
	 * rezultat na 0 i sve elemente ploce na nulu. Ako je trenutni rezultat veci od
	 * najboljeg rezultata, a igra je zavrsena, azurira najbolji rezultat.
	 */
	public void reset() {
		trenutniRezultat = 0;

		// Postavi sva polja ploce na 0
		for (int i = 0; i < getVisinaPloce(); i++) {
			for (int j = 0; j < getSirinaPloce(); j++) {
				setPolja(i, j, 0);
			}
		}
		// Azuriraj najbolji rezultat samo ako je trenutni rezultat veci i igra je
		// zavrsena
		if (trenutniRezultat > najboljiRezultat) {
			najboljiRezultat = trenutniRezultat;
		}
	}

	/**
	 * Izvrsava potez u odredjenom smjeru. Pomice sve plocice u datom smjeru i
	 * dodaje novu plocicu ako se ijedna plocica pomaknula.
	 *
	 * @param smjer Smjer kretanja plocica (GORE, DOLJE, LIJEVO, DESNO).
	 */
	public void pokret(int smjer) {
		boolean poljePromijenjeno = false;
		int duzinaLinije = 0;
		int[] offset = OFFSETS.get(smjer);
		ArrayList<int[]> iviceIndeksiSmjer = iviceIndeksi.get(smjer);

		// Odredjivanje duzine linije ovisno o smjeru
		if (smjer == GORE || smjer == DOLJE) {
			duzinaLinije = getVisinaPloce();
		} else {
			duzinaLinije = getSirinaPloce();
		}

		// Za svaki red ili kolonu na rubu ploce u odabranom smjeru
		for (int i = 0; i < iviceIndeksiSmjer.size(); i++) {
			int[] tempNiz = new int[duzinaLinije];
			int[] trenutniIndeksIvicePolja = iviceIndeksiSmjer.get(i);

			// Kopiranje vrijednosti plocica u privremeni niz
			for (int j = 0; j < duzinaLinije; j++) {
				tempNiz[j] = getPolje(trenutniIndeksIvicePolja[0] + j * offset[0],
						trenutniIndeksIvicePolja[1] + j * offset[1]);
			}

			// Spajanje plocica u novi niz
			int[] spojeniNiz = spoji(tempNiz);

			// Provjera promjene niza
			if (Arrays.equals(tempNiz, spojeniNiz)) {
				poljePromijenjeno = true;
			}

			// Modifikacija linije ploce za prikaz novih vrijednosti
			for (int j = 0; j < duzinaLinije; j++) {
				setPolja(trenutniIndeksIvicePolja[0] + j * offset[0], trenutniIndeksIvicePolja[1] + j * offset[1],
						spojeniNiz[j]);
			}
		}

		// Dodavanje nove plocice ako se ijedna plocica pomaknula
		if (poljePromijenjeno) {
			novoPolje();
		}
	}

	/**
	 * Spaja plocice u jednom redu ili koloni prema pravilima igre 2048. Samo prva
	 * dva uzastopna jednaka polja se spajaju, a rezultat spajanja je novo polje sa
	 * vrijednoscu dva puta vecom od spojenih polja. Dodatno, dodaje bodove za
	 * spojene plocice na trenutni rezultat igre.
	 *
	 * @param linija Niz koji predstavlja red ili kolonu plocica koje treba spojiti.
	 * @return Niz koji predstavlja rezultat spajanja plocica.
	 */
	private int[] spoji(int[] linija) {
		int[] linijaZaVratiti = new int[linija.length];
		int indeksRezultata = 0;
		boolean zadnjePoljeSpojeno = false;
		int zadnjePoljeDodano = 0;

		// Pomakni plocice. Ako su dva uzastopna polja jednaka, spoji ih. Ali spoji samo
		// prvi par.
		// Kada se spoji, pomnozi vrijednost plocice sa 2.
		for (int i = 0; i < linija.length; i++) {
			int trenutnoPolje = linija[i];
			if (trenutnoPolje > 0) {
				// Spajanje
				if ((trenutnoPolje == zadnjePoljeDodano) && !zadnjePoljeSpojeno) {
					linijaZaVratiti[indeksRezultata - 1] = trenutnoPolje * 2;
					trenutniRezultat += trenutnoPolje * 2; // Dodaj bodove za spojenu plocicu
					zadnjePoljeDodano = 0; // Resetuj vrijednost zadnje dodane plocice
					zadnjePoljeSpojeno = true;
				} else {
					// Ne spajaj
					linijaZaVratiti[indeksRezultata] = trenutnoPolje;
					zadnjePoljeDodano = trenutnoPolje;
					zadnjePoljeSpojeno = false;
					indeksRezultata += 1;
				}
			}
		}
		return linijaZaVratiti;
	}

	/**
	 * Postavlja novu plocicu sa vrijednoscu 2 na prazno polje na ploci. Dodaje
	 * bodove za novu plocicu na trenutni rezultat igre i provjerava da li je
	 * postignut novi najbolji rezultat.
	 */
	public void novoPolje() {
		ArrayList<int[]> listaPraznihPolja = new ArrayList<int[]>();

		// Dobija indekse praznih plocica
		for (int i = 0; i < getVisinaPloce(); i++) {
			for (int j = 0; j < getSirinaPloce(); j++) {
				if (getPolje(i, j) == 0) {
					int[] indeksPraznogPolja = { i, j };
					listaPraznihPolja.add(indeksPraznogPolja);
				}
			}
		}

		if (listaPraznihPolja.size() > 0) {
			Random rand = new Random();
			int randomIndex = rand.nextInt(listaPraznihPolja.size());
			int[] indeksPraznogPolja = listaPraznihPolja.get(randomIndex);
			setPolja(indeksPraznogPolja[0], indeksPraznogPolja[1], 2);
			// Dodaj bodove za novu plocicu
			trenutniRezultat += getPolje(indeksPraznogPolja[0], indeksPraznogPolja[1]);

			// Provjeri je li postignut novi najbolji rezultat
			if (trenutniRezultat > najboljiRezultat) {
				najboljiRezultat = trenutniRezultat;
			}
		}
	}

	/**
	 * Prikazuje trenutni rezultat i najbolji rezultat igre na ekranu.
	 */
	public void prikaziRezultat() {
		System.out.println("Rezultat: " + trenutniRezultat);
		System.out.println("Najbolji rezultat: " + najboljiRezultat);
	}

	/**
	 * Prikazuje trenutno stanje igre na ekranu, tj. prikazuje trenutnu plocu sa
	 * svim plocicama.
	 */
	public void prikaziPlocu() {
		for (int i = 0; i < getVisinaPloce(); i++) {
			for (int j = 0; j < getSirinaPloce(); j++) {
				System.out.print(ploca[i][j] + " || ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Provjerava da li je igra zavrsena. Igra zavrsava ako igrac dostigne plocicu
	 * sa vrijednoscu 2048 ili ako vise nema mogucih poteza.
	 * 
	 * @return true ako je igra zavrsena, inace false
	 */
	public boolean igraGotova() {
		// Ako je dostignuta plocica sa vrijednoscu 2048
		for (int i = 0; i < getVisinaPloce(); i++) {
			for (int j = 0; j < getSirinaPloce(); j++) {
				if (ploca[i][j] == 2048) {
					System.out.println("Pobijedili ste !");
					return true;
				}
			}
		}

		// Ako nema vise mogucih poteza. Provjerava se da li postoje duplikati u svakom
		// redu i koloni.

		// Provjera po redovima
		for (int i = 0; i < getVisinaPloce(); i++) {
			int prethodnoPolje = ploca[i][0];
			for (int j = 0; j < getSirinaPloce(); j++) {
				if (j > 0) {
					if (ploca[i][j] == prethodnoPolje) {
						return false; // Igra nije zavrsena jer postoje duplikati u redu
					}
					prethodnoPolje = ploca[i][j];
				}
			}
		}

		// Provjera po kolonama
		for (int i = 0; i < getSirinaPloce(); i++) {
			int prethodnoPolje = ploca[0][i];
			for (int j = 0; j < getVisinaPloce(); j++) {
				if (j > 0) {
					if (ploca[j][i] == prethodnoPolje) {
						return false; // Igra nije zavrsena jer postoje duplikati u koloni
					}
					prethodnoPolje = ploca[j][i];
				}
			}
		}

		System.out.println("Kraj igre, izgubili ste !");
		return true;
	}

	/**
	 * Postavlja vrijednost polja na odredjenom mjestu na ploci.
	 * 
	 * @param red        Red na ploci
	 * @param kolona     Kolona na ploci
	 * @param vrijednost Nova vrijednost polja
	 */
	private void setPolja(int red, int kolona, int vrijednost) {
		ploca[red][kolona] = vrijednost;
	}

	/**
	 * Getter vrijednosti polja na odredjenom mjestu na ploci.
	 * 
	 * @param red    Red na ploci
	 * @param kolona Kolona na ploci
	 * @return Vrijednost polja na odredjenom mjestu
	 */
	private int getPolje(int red, int kolona) {
		return ploca[red][kolona];
	}

	/**
	 * Getter visine ploce.
	 * 
	 * @return Visina ploce
	 */
	private int getVisinaPloce() {
		return ploca.length;
	}

	/**
	 * Getter sirine ploce.
	 * 
	 * @return Sirina ploce
	 */
	private int getSirinaPloce() {
		return ploca[0].length;
	}

}