package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logika.Tabla;

/**
 * Predstavlja panel za igru u prozoru igre "2048". Implementira interfejs
 * KeyListener za obradu dogadjaja sa tastaure. Panel sadrzi plocu, trenutni
 * rezultat, najbolji rezultat te osnovne komponente GUI-a. Omogucava korisniku
 * igranje igre "2048" pomocu tastature.
 *
 * @author Aldin Kajmovic Projekat u akademskoj godini 2023/24
 */
public class MojPanel extends JPanel implements KeyListener {
	/** Instanca ploce. */
	Tabla ploca = new Tabla();

	/** Instanca klase MojPanel koja predstavlja novu igru. */
	static MojPanel novaIgra = new MojPanel();

	/** Glavni okvir igre. */
	static JFrame okvir = new JFrame("2048");

	/** Tekstualna reprezentacija ploce za pracenje promjena. */
	String plocaZaIgranje = ploca.toString();

	/** Trenutni rezultat igre. */
	int trenutniRezultat = 0;

	/** Najbolji postignuti rezultat igre. */
	int najboljiRezultat = 0;

	/** Labela koja prikazuje trenutni i najbolji rezultat. */
	JLabel rezultatLabela;

	/**
	 * Postavljamo GUI, dodajmo KeyListener koji vrsi funkcije kada se odredjena
	 * tipka pritisne U okvir dodajemo sadrzaj, podesimo velicinu i da je vidljiv, i
	 * najbitnije kada se prozor zatvori da se u konzoli terminira.
	 */
	public static void postaviGUI() {
		okvir.addKeyListener(novaIgra);
		okvir.getContentPane().add(novaIgra);
		okvir.setSize(600, 400);
		okvir.setVisible(true);
		okvir.setResizable(false);
		okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Konstruktor koji postavlja polje za ispisivanje rzultata
	 */
	public MojPanel() {
		rezultatLabela = new JLabel();
		rezultatLabela.setBounds(10, 10, 200, 0);
		rezultatLabela.setFont(new Font("Arial", Font.PLAIN, 14));
		this.add(rezultatLabela);
	}

	/**
	 * Metoda koja se poziva kada je pritisnuta tipka na tastaturi. Obradjuje
	 * pritisak tipki i azurira stanje ploce, te provjerava zavrsetak igre.
	 *
	 * @param e Objekt koji predstavlja dogadjaj pritiska tipke.
	 * @see KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// Obrada tipki i azuriranje ploce
		int stariRezultat = ploca.getRezultat();

		if (e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {
			ploca.pritisnutaTipkaGore();
		} else if (e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
			ploca.pritisnutaTipkaDolje();
		} else if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
			ploca.pritisnutaTipkaLijevo();
		} else if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			ploca.pritisnutaTipkaDesno();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			ploca = new Tabla();
			ploca.generisiDvicu();
		}

		// Provjeri je li doslo do promjene na ploci
		if (!ploca.toString().equals(plocaZaIgranje)) {
			ploca.generisiDvicu();
			plocaZaIgranje = ploca.toString();
		}

		// Azuriranje trenutnog i najboljeg rezultata
		trenutniRezultat = ploca.getRezultat();
		if (trenutniRezultat > najboljiRezultat) {
			najboljiRezultat = trenutniRezultat;
		}

		// Azuriranje teksta na rezultatLabela
		rezultatLabela
				.setText("Trenutni rezultat: " + trenutniRezultat + "   |   Najbolji rezultat: " + najboljiRezultat);

		// Provjera zavrsetka igre
		if (ploca.jeLiGotovaIgra()) {
			Object[] options = { "Da", "Ne" };
			int choice = JOptionPane.showOptionDialog(this,
					"Zelite li ponovo igrati?\nRezultat: " + ploca.getRezultat(), "Igra zavrsena",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (choice == JOptionPane.YES_OPTION) {
				ploca = new Tabla();
				ploca.generisiDvicu();
			} else {
				System.exit(0);
			}
		}
		okvir.repaint();
	}

	/**
	 * Metoda koja crta stanje igre na prozoru.
	 *
	 * @param grafika Graficki objekt na kojem se vrsi crtanje.
	 * @see Graphics
	 */
	public void paint(Graphics grafika) {
		super.paint(grafika);
		Graphics2D grafika2D = (Graphics2D) grafika;

		// Crta naslov igre "2048"
		grafika2D.drawString("2048", 250, 40);

		// Informacije o kontroli igre
		grafika2D.drawString("Pritisni Enter da pokrenes igricu", 210, 315);
		grafika2D.drawString("Koristi 'wasd' ili strelice da se kreces", 180, 335);

		// Poruka o restartu ako je ploca popunjena
		if (ploca.jeLiPopunjenaPloca()) {
			grafika2D.drawString("Pritisni Enter da restartujes", 200, 355);
		}

		// Postavljanje boje pozadine ploce
		grafika2D.setColor(Color.black);
		grafika2D.fillRect(140, 50, 250, 250);

		// Crta plocice na igracoj ploci
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				crtajPolja(grafika, ploca.ploca[i][j], j * 60 + 150, i * 60 + 60);
			}
		}
	}

	/**
	 * Metoda koja crta pojedinacnu plocicu na ploci.
	 *
	 * @param grafika Graficki objekt na kojem se vrsi crtanje.
	 * @param plocica Plocica koja se crta.
	 * @param x       X-koordinata na kojoj se plocica crta.
	 * @param y       Y-koordinata na kojoj se plocica crta.
	 * @see Graphics
	 * @see Plocica
	 */
	public void crtajPolja(Graphics grafika, Plocica plocica, int x, int y) {
		int vrijednostPlocice = plocica.getVrijednost();

		// Odredjivanje duzine teksta (broj cifri) kako bi se ispravno pozicionirao tekst
		// u sredini plocice.
		int length = String.valueOf(vrijednostPlocice).length();

		Graphics2D grafika2D = (Graphics2D) grafika;

		// Postavljanje boje pozadine plocice
		grafika2D.setColor(Color.white);
		grafika2D.fillRoundRect(x, y, 50, 50, 5, 5);

		// Crta plocicu samo ako ima vrijednost vecu od 0
		if (vrijednostPlocice > 0) {
			// Postavljanje boje plocice prema njezinoj vrijednosti
			grafika2D.setColor(plocica.getColor());
			grafika2D.fillRoundRect(x, y, 50, 50, 5, 5);

			// Postavljanje boje teksta na crno
			grafika2D.setColor(Color.black);

			// Ispisuje vrijednost plocice u sredini
			grafika.drawString("" + vrijednostPlocice, x + 25 - 3 * length, y + 25);
		}
	}

	/**
	 * Metoda koja pokrece glavnu aplikaciju za igru "2048". Postavlja osnovne
	 * komponente GUI pomocu metode {@link #postaviGUI()}.
	 *
	 * @param args Argumenti komandne linije (ne koriste se u ovom kontekstu).
	 */
	public static void main(String[] args) {
		postaviGUI();
	}

	/**
	 * Mora se implementirati jer je takav interfejs
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Mora se implementirati jer je takav interfejs
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}