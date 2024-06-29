package GUI;

import java.awt.Color;

/**
 * Predstavlja pojedinacnu plocicu u igri. Ima int vrijednost i boju.
 *
 * Klasa za igranje igre u prozoru
 * 
 * @author Aldin Kajmovic Projekt u akademskoj godini 2023/24
 */
public class Plocica {
	int vrijednost;
	Color bojaPlocice;

	/**
	 * Prazan konstruktor koji postavlja vrijednost na nulu
	 */
	public Plocica() {
		vrijednost = 0;
	}

	/**
	 * Konstruktor koji prima int parametar i postavlja vrijednost (varijablu klase)
	 * na vrijednost koju dobiva kao parametar
	 *
	 * @param broj broj za postavljanje vrijednosti
	 */
	public Plocica(int broj) {
		vrijednost = broj;
	}

	/**
	 * Getter za vrijednost plocice
	 *
	 * @return vrijednost
	 */
	public int getVrijednost() {
		return vrijednost;
	}

	/**
	 * Setter za vrijednost plocice, koristi se prilikom spajanja dvije plocice
	 * 
	 * @param vrijednost1 vrijednost za postavljanje
	 */
	public void setVrijednost(int vrijednost1) {
		this.vrijednost = vrijednost1;
	}

	/**
	 * Pretvara plocicu u string, koristi se u GUI-u
	 */
	public String toString() {
		return "" + vrijednost;
	}

	/**
	 * Mijenja boju plocice ovisno o njezinoj vrijednosti
	 */
	public void setColor() {
		if (this.getVrijednost() == 2) {
			bojaPlocice = new Color(0, 179, 255); // svijetloplava boja
		} else if (this.getVrijednost() == 4) {
			bojaPlocice = new Color(0, 34, 255); // tamnoplava
		} else if (this.getVrijednost() == 8) {
			bojaPlocice = new Color(145, 0, 255); // svijetlo ljubicasto-roza
		} else if (this.getVrijednost() == 16) {
			bojaPlocice = new Color(102, 44, 135); // tamnoljubicasta
		} else if (this.getVrijednost() == 32) {
			bojaPlocice = new Color(255, 0, 17); // crvena
		} else if (this.getVrijednost() == 64) {
			bojaPlocice = new Color(135, 38, 45); // tamnocrvena
		} else if (this.getVrijednost() == 128) {
			bojaPlocice = new Color(161, 161, 161); // siva
		} else if (this.getVrijednost() == 256) {
			bojaPlocice = new Color(204, 102, 0); // narancasta
		} else if (this.getVrijednost() == 512) {
			bojaPlocice = new Color(0, 153, 0); // zelena
		} else if (this.getVrijednost() == 1024) {
			bojaPlocice = new Color(255, 51, 255); // roza
		} else {
			bojaPlocice = new Color(255, 153, 204); // svijetlo roza
		}
	}

	/**
	 * Getter za boju
	 * 
	 * @return bojaPlocice
	 */
	public Color getColor() {
		this.setColor();
		return bojaPlocice;
	}
}