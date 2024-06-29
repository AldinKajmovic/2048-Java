package logika;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import GUI.Plocica;

/**
 * Predstavlja plocu za igru 2048.
 * Sadrzi konstruktore, metode, funkcije koje ce reagovati na odredjene pritiske
 * tipki Klasa za igranje igrice u prozoru.
 * 
 * @author Aldin Kajmovic
 * Projekat u akademskoj godini 2023/24
 */
public class Tabla {
    /**
     * Matrica plocica tj 2D niz gdje je svaki element Plocica koji se sastoji od
     * vrijednosti i boje.
     */
    public Plocica[][] ploca;

    /** Dimenzija ploce (broj redova i kolona). */
    int dimenzija = 4;

    /** Granica koja se koristi pri odredjivanju kretanja po ploci. */
    int granica = 0;

    /** Trenutni rezultat igre. */
    public int rezultat = 0;

    /** Lista rezultata postignutih tokom igranja. */
    private List < Integer > rezultati = new ArrayList < > ();

    /** 
     * Defaultni konstruktor koji generise 4x4 matricu koja ce predstavljati plocu za igranje.
     */
    public Tabla() {
        pokreniIgru();
    }

    /**
     * Pokrece igru postavljajuci inicijalne vrijednosti i generise prvu dvicu.
     */
    public void pokreniIgru() {
        ploca = new Plocica[4][4];
        for (int i = 0; i < ploca.length; i++) {
            for (int j = 0; j < ploca[i].length; j++) {
                ploca[i][j] = new Plocica();
            }
        }
        rezultat = 0;
        krajIgre = false;
        generisiDvicu();
    }

    /**
     * Vraca trenutnu plocu.
     * 
     * @return ploca
     */
    public Plocica[][] getPloca() {
        return ploca;
    }

    /**
     * Vraca trenutni rezultat igre.
     * 
     * @return rezultat
     */
    public int getRezultat() {
        return rezultat;
    }

    /**
     * Vraca najvecu vrijednost plocice na ploci.
     * 
     * @return Najveca vrijednost plocice
     */
    public int getNajvecaPlocica() {
        int max = ploca[0][0].getVrijednost();
        for (int i = 0; i < ploca.length; i++) {
            for (int j = 0; j < ploca[i].length; j++) {
                if (ploca[i][j].getVrijednost() > max) {
                    max = ploca[i][j].getVrijednost();
                }
            }
        }
        return max;
    }

    /**
     * Vraca trenutno stanje ploce u obliku Stringa.
     * Svaki red predstavlja red na ploci, a svaka vrijednost odvojena je razmakom.
     * Prazna mjesta na ploci oznacena su nulom. Nakon svakog reda dodaje se novi red.
     * 
     * @return String trenutno stanje ploce
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < ploca.length; i++) {
            for (int j = 0; j < ploca[i].length; j++) {
                s += ploca[i][j].toString() + " ";
            }
            s += "\n";
        }
        return s;
    }

    /**
     * Generise novu plocicu sa vrijednoscu 2 na slucajnom praznom mjestu na ploci.
     */
    public void generisiDvicu() {
        boolean uslov = true;
        while (uslov) {
            int red = (int)(Math.random() * 4);
            int kolona = (int)(Math.random() * 4);
            double x = Math.random();
            if (ploca[red][kolona].getVrijednost() == 0) {
                ploca[red][kolona] = new Plocica(2);
                uslov = false;
            }

        }

    }

    /**
     * 
     * Funkcija koja provjerava je li ploca popunjena(nema praznih mjesta) i igra se
     * zavrsava. Korisniku nudi opciju da restartuje igru. Posto se radi o matrici
     * 4x4 i imamo brojac koji broji neprazna polja, ako je taj brojac jednak
     * red*kolona(4x4 u nasem slucaju) onda je kraj igre i vraca true inace vraca false
     *
     * 
     * @return True ako je ploca popunjena, inace false.
     */
    public boolean jeLiPopunjenaPloca() {
        int count = 0;
        for (int i = 0; i < ploca.length; i++) {
            for (int j = 0; j < ploca[i].length; j++) {
                if (ploca[i][j].getVrijednost() > 0) {
                    count++;
                }
            }
        }
        if (count == 16) {
            return true;
        }
        return false;
    }

    /**
     * Provjerava da li je igra gotova (GAME OVER) na nacin da kombinuje plocice
     * koje su iste.
     * 
     * @return boolean - vraca true ako je igra gotova, inace false. Ivice ploce
     *         odnose se na prve i zadnje redove i kolone matrice, dok se uglovi
     *         ploce odnose na cetiri coska i to su nam glavni edge case-ovi za
     *         probleme.
     */
    public boolean krajIgre() {
        int brojac = 0;
        for (int i = 0; i < ploca.length; i++) {
            for (int j = 0; j < ploca[i].length; j++) {
                if (ploca[i][j].getVrijednost() > 0) {
                    if (i == 0 && j == 0) {
                        if (ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (i == 0 && j == 3) {
                        if (ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (i == 3 && j == 3) {
                        if (ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (i == 3 && j == 0) {
                        if (ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (i == 0 && (j == 1 || j == 2)) {
                        if (ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (i == 3 && (j == 1 || j == 2)) {
                        if (ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost()) {
                            brojac++;
                        }
                    } else if (j == 0 && (i == 1 || i == 2)) {
                        if (ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost()) {
                            brojac++;
                        }
                    } else if (j == 3 && (i == 1 || i == 2)) {
                        if (ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost()) {
                            brojac++;
                        }
                    } else {
                        if (ploca[i][j].getVrijednost() != ploca[i][j - 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i][j + 1].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i - 1][j].getVrijednost() &&
                            ploca[i][j].getVrijednost() != ploca[i + 1][j].getVrijednost()) {
                            brojac++;
                        }
                    }
                }
            }
        }
        if (brojac == 16) {
            return true;
        }
        return false;
    }

    /**
     * Obrada dogadjaja kada je pritisnuta tipka "W" ili strelica prema gore.
     * Provjerava gornju uspravnu granicu kako ne bi izasla iz opsega. 
     * Ako je uslov zadovoljen, poziva funkcijuPokretGoreDolje koja izvrsava odredjene akcije.
     * 
     * @see #funkcijaPokretGoreDolje(int, int, String)
     */
    public void pritisnutaTipkaGore() {
        for (int i = 0; i < dimenzija; i++) {
            granica = 0;
            for (int j = 0; j < dimenzija; j++) {
                if (ploca[j][i].getVrijednost() != 0) {
                    if (granica <= j) {
                        funkcijaPokretGoreDolje(j, i, "gore");
                    }
                }
            }
        }
    }

    /**
     * Obrada dogadjaja kada je pritisnuta tipka "S" ili strelica prema dolje.
     * Provjerava donju uspravnu granicu kako ne bi izasla iz opsega. 
     * Ako je uslov zadovoljen, poziva funkcijuPokretGoreDolje koja izvrsava odredjne akcije.
     * 
     * @see #funkcijaPokretGoreDolje(int, int, String)
     */
    public void pritisnutaTipkaDolje() {
        for (int i = 0; i < dimenzija; i++) {
            granica = (dimenzija - 1);
            for (int j = dimenzija - 1; j >= 0; j--) {
                if (ploca[j][i].getVrijednost() != 0) {
                    if (granica >= j) {
                        funkcijaPokretGoreDolje(j, i, "dolje");
                    }
                }
            }
        }
    }

    /**
     * Funkcija izvrsava poteze pomicanja plocica prema gore ili dolje na osnovu dogadjaja pritiska tipke.
     * Ako potez nije moguc, granice se pomicu prema dolje ili gore, ovisno o smjeru, kako bi se omogucilo
     * daljnje spajanje plocica.
     * 
     * @param red    Red trenutne plocice
     * @param kolona Kolona trenutne plocice
     * @param smjer  Smjer poteza ("gore" ili "dolje")
     */
    private void funkcijaPokretGoreDolje(int red, int kolona, String smjer) {
        Plocica prvaPlocica = ploca[granica][kolona];
        Plocica drugaPlocica = ploca[red][kolona];

        // Provjeri da li potez moze dovesti do spajanja plocica
        if (prvaPlocica.getVrijednost() == 0 || prvaPlocica.getVrijednost() == drugaPlocica.getVrijednost()) {
            // Ako je potez prema dolje i plocice se mogu spojiti ili potez prema gore i plocice su iste,
            // izvrsi spajanje
            if (red > granica || (smjer.equals("dolje") && (red < granica))) {
                int azurirajRezultat = prvaPlocica.getVrijednost() + drugaPlocica.getVrijednost();

                // Dodaj rezultat
                if (prvaPlocica.getVrijednost() != 0) {
                    rezultat += azurirajRezultat;
                }

                // Azuriraj vrijednosti plocica
                prvaPlocica.setVrijednost(azurirajRezultat);
                drugaPlocica.setVrijednost(0);

                // Provjeri je li nova vrijednost 2048
                if (azurirajRezultat == 2048) {
                    krajIgre = true;
                }
            }
        } else {
            // Ako potez nije moguc, pomakni granice prema dolje ili gore, ovisno o smjeru
            if (smjer.equals("dolje")) {
                granica--;
            } else {
                granica++;
            }

            // Rekurzivno pozovi funkciju s novim granicama
            funkcijaPokretGoreDolje(red, kolona, smjer);
        }
    }

    /**
     * Metoda se poziva kada se pritisne tipka a ili strelica prema lijevo. Provjerava
     * kolonu kao granicu kako bi sprijecila izlazak iz opsega. Ako je uslov zadovoljen,
     * poziva se funkcijaPokretLijevoDesno koja izvrsava odredjene akcije pomicanja plocica
     * prema lijevo.
     */
    public void pritisnutaTipkaLijevo() {
        for (int i = 0; i < dimenzija; i++) {
            granica = 0;
            for (int j = 0; j < dimenzija; j++) {
                if (ploca[i][j].getVrijednost() != 0) {
                    if (granica <= j) {
                        funkcijaPokretLijevoDesno(i, j, "lijevo");
                    }
                }
            }
        }
    }

    /**
     * Metoda se poziva kada se pritisne tipka d ili strelica prema desno. Provjerava
     * kolonu kao granicu kako bi sprijecila izlazak iz opsega. Ako je uslov zadovoljen,
     * poziva se funkcijaPokretLijevoDesno koja izvrsava odredjene akcije pomicanja plocica
     * prema desno.
     */
    public void pritisnutaTipkaDesno() {
        for (int i = 0; i < dimenzija; i++) {
            granica = (dimenzija - 1);
            for (int j = (dimenzija - 1); j >= 0; j--) {
                if (ploca[i][j].getVrijednost() != 0) {
                    if (granica >= j) {
                        funkcijaPokretLijevoDesno(i, j, "desno");
                    }
                }
            }
        }
    }


    /**
     * Funkcija poredi vrijednost dvije plocice i ako su iste ili je jedna od njih
     * prazna sabrace njihove vrijednosti, koristimo rekurziju. Ako uslovi nisu
     * ispunjeni, rekurzija pomjera granice prema lijevo ili desno zavisi od poteza.
     * 
     * @param red    red trenutne plocice
     * @param kolona kolona trenutne plocice
     * @param smjer  usmjerenje trenutne plocice
     */

    /**
     * Funkcija poredi vrijednost dvije plocice i, ako su iste ili je jedna od njih prazna,
     * sabira njihove vrijednosti koristeci rekurziju. Ako uslovi nisu ispunjeni, rekurzija
     * pomjera granice prema lijevoj ili desnoj strani ovisno o smjeru pomicanja.
     * 
     * @param red    Red trenutne plocice.
     * @param kolona Kolona trenutne plocice.
     * @param smjer  Smjer pomicanja trenutne plocice ("lijevo" ili "desno").
     */
    private void funkcijaPokretLijevoDesno(int red, int kolona, String smjer) {
        Plocica prvaPlocica = ploca[red][granica];
        Plocica drugaPlocica = ploca[red][kolona];

        if (prvaPlocica.getVrijednost() == 0 || prvaPlocica.getVrijednost() == drugaPlocica.getVrijednost()) {
            if (kolona > granica || (smjer.equals("desno") && (kolona < granica))) {
                int azurirajRezultat = prvaPlocica.getVrijednost() + drugaPlocica.getVrijednost();
                if (prvaPlocica.getVrijednost() != 0) {
                    rezultat += azurirajRezultat;
                }
                prvaPlocica.setVrijednost(azurirajRezultat);
                drugaPlocica.setVrijednost(0);

                // Provjeri je li nova vrijednost 2048
                if (azurirajRezultat == 2048) {
                    // Igra se zaustavlja ako je dostignuta plocica 2048
                    krajIgre = true;
                }
            }
        } else {
            if (smjer.equals("desno")) // pokret desno
            {
                granica--;
            } else // pokret lijevo
            {
                granica++;
            }
            funkcijaPokretLijevoDesno(red, kolona, smjer);
        }
    }


    private boolean krajIgre = false;

    /**
     * Metoda provjerava je li igra zavrsena (game over). Poziva se prije vracanja statusa igre.
     * Dodatno, poziva metodu za dodavanje trenutnog rezultata u listu rezultata igre.
     * 
     * @return {@code true} ako je igra zavrsena, inace {@code false}.
     */
    public boolean jeLiGotovaIgra() {
        dodajRezultat();
        return krajIgre;
    }

    /**
     * Dodaje trenutni rezultat u listu rezultata igre, sortira listu silazno te ogranicava broj rezultata na 10.
     */
    private void dodajRezultat() {
        rezultati.add(rezultat);
        Collections.sort(rezultati, Collections.reverseOrder());

        // Ogranici broj rezultata na 10
        if (rezultati.size() > 10) {
            rezultati = rezultati.subList(0, 10);
        }
    }

    /**
     * Vraca listu najboljih rezultata igre.
     * 
     * @return Lista najboljih rezultata.
     */
    public List < Integer > getNajboljiRezultati() {
        return rezultati;
    }


}