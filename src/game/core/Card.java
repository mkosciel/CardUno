package game.core;

/**
 * Opisy kart:
 *
 * CARD_ID   CARD_NAME   DESCRIPTION ACTION  ACTION_DESCRIPTION
 * 0 - 9     Number      ""          1       "Normalna karta. Mo?e by? po?o?ona na takim samym kolorze lub numerze."
 * 10        Skip        ""          2       "Mo?e by? po?o?ona na takim samym kolorze lub numerze. Pomija nast?pnego gracza."
 * 11        Reverse     ""          3       "Odwraca kierunek ruchu gry."
 * 12        DrawTwo     ""          4       "Obliguje nast?pnego gracza do dobrania dwóch kart."
 *
 * 13        Wild        ""          5       "Mo?e zosta? po?o?ona na dowoln? kart?. Zmienia kolor na stole."
 * 14        WildDraw    ""          6       "Mo?e zosta? po?o?ona na dowoln? kart?. Zmienia kolor na stole. Dodatkowo obliguje nast?pnego gracza do dobrania czterech kart."
 */

import java.util.ArrayList;

/**
 * @author Paulina Staniszewska-Tudruj
 *	
 */
public class Card {

	/**
	 * Kolory kart. Kolejno: kolor bazowy, czerwony, zielony, niebieski i ?ó?ty.
	 * Zapisy liczbowe w celu szybszego dost?pu
	 */
	final static public Integer ColorBase = 2010;
	final static public Integer ColorRed = ColorBase + 1;
	final static public Integer ColorGreen = ColorBase + 2;
	final static public Integer ColorBlue = ColorBase + 3;
	final static public Integer ColorYellow = ColorBase + 4;

	final static String[] ColorNames = { "Red", "Green", "Blue", "Yellow" }; // Zapisy kolor?w w pe?nych nazwach
	private final static String[] CardNames = { "Skip", "Reverse", "DrawTwo", "Wild", "WildDraw" }; // Zapisy kart w
																									// nazwach
	public final static ArrayList<Integer> NotColored = new ArrayList<Integer>() {
		{
			add(13);
			add(14);
		}
	}; // lista kart bez kolorów
	
	private final static Integer NumberOfCards = 14; // liczba kart w sumie

	private Integer number; // numer karty z tabelki
	private Integer color; // kolor karty
	private Integer action; // numer akcji wykonywanej przez kartê; akcje przypisane s¹ do numerów kart

	/**
	 * Konstruktor karty
	 *
	 * @param cardId    numer karty
	 * @param cardColor kolor karty
	 */
	public Card(Integer cardId, Integer cardColor) {
		try {
			number = cardId; // przypisanie numeru do karty
			color = cardColor; // przypisanie koloru do karty
			if (NotColored.contains(cardId))
				color = null;
			action = readActionForNumber(cardId); // odczywanie akcji z numeru karty
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Krótszy konstruktor karty
	 * 
	 * @param cardId numer karty
	 */
	Card(Integer cardId) {
		this(cardId, ColorBase);
	}

	/**
	 * @param cardId numer karty
	 *
	 * @return numer akcji karty zgody z tabelk¹
	 */
	private Integer readActionForNumber(Integer cardId) throws Exception {
		if (cardId < 0 || cardId > NumberOfCards) {
			throw new Exception("Bad Card Number!");
		}
		if (cardId <= 9) {
			return 1;
		} else {
			return cardId - 8;
		}
	}

	/**
	 * @return numer karty
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @return numer koloru karty
	 */
	public Integer getColor() {
		return color;
	}

	/**
	 * @return kolor karty w postaci String
	 */
	String getColorName() {
		if (color.equals(ColorBase))
			return null;
		return ColorNames[color - ColorBase - 1];
	}

	/**
	 * @return nazwa karty
	 */
	String getCardName() {
		if (number < 10) {
			return String.valueOf(number);
		} else {
			return CardNames[number - 10];
		}
	}

	/**
	 * @param color numer koloru karty
	 * @return nazwa koloru karty
	 */
	static String getColorName(Integer color) {
		if (color <= ColorBase || ColorYellow < color)
			return null;
		return ColorNames[color - ColorBase - 1];
	}

	/**
	 * @param cardId numer karty
	 * @return nazwa karty
	 */
	static String getCardName(Integer cardId) {
		if (cardId < 0 || cardId > NumberOfCards) {
			return null;
		}
		if (cardId < 10) {
			return String.valueOf(cardId);
		} else {
			return CardNames[cardId - 10];
		}
	}

	/**
	 * @return numer akcji karty
	 */
	Integer getAction() {
		return action;
	}

	/**
	 * @return sprawdza, czy karta posiada kolor
	 */
	boolean hasColor() {
		return (color != null);
	}

	/**
	 * @return sprawdza, czy karta nie wykonuje akcji specjalnej
	 */
	boolean isNormalCard() {
		return (action == 1);
	}

	/**
	 * @see java.lang.Object#toString() standardowy toString() pomocny przy
	 *      wypisywaniu
	 */
	@Override
	public String toString() {
		if (!hasColor())
			return getCardName();
		return getCardName() + " " + getColorName();
	}
}
