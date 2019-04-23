package game.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Micha³ Koœcielak
 *
 */
public class CardTest {

	@Test
	public void getNumberTest() {
		boolean czyJestNumber = false;
		Card aGetNumber = new Card(10);

		try {
			aGetNumber.getNumber();
			czyJestNumber = true;
		} catch (Exception e) {
			/*
			 * fail("Numer karty poza zakresem "); czyJestNumber = false;
			 */
		}
		assertTrue(czyJestNumber);
	}

	@Test
	public void getCardNameTest() {
		boolean czyJestKarta = false;
		Card aGetKarta = new Card(11, 2010);

		try {
			aGetKarta.getCardName();
			czyJestKarta = true;
		} catch (Exception e) {
			/*
			 * Assert.fail("Numer karty poza zakresem " + e); czyJestKarta = false;
			 */
		}
		assertTrue(czyJestKarta);
	}

	@Test
	public void getColorTest1() {
		boolean czyJestKolor = false;

		Card bGetKolor = new Card(6, 2016);

		try {
			bGetKolor.getColor();
			czyJestKolor = true;
		} catch (Exception e) {
			/*
			 * Assert.fail("Kolor karty poza zakresem " + e); czyJestKolor = false;
			 */
		}
		assertTrue(czyJestKolor);
		assertNotNull(czyJestKolor);
	}

	@Test
	public void getColorNameTest() {
		boolean czyKolorNameInteger = false;

		try {
			Card.getColorName(2011);
			Card.getColorName(2018);
			Card.getColorName(2000);

			czyKolorNameInteger = true;

		} catch (Exception e) {
			/*
			 * Assert.fail("Kolor karty poza zakresem wskazanym przez metode " + e);
			 * czyKolorNameInteger = false;
			 */
		}

		assertTrue(czyKolorNameInteger);
	}

	@Test
	public void getCardNameTestInteger() {
		boolean czyNameInteger = false;

		try {
			Card.getCardName(-1);
			Card.getColorName(2000);
			Card.getCardName(9);
			Card.getCardName(14);
			czyNameInteger = true;
		} catch (Exception e) {
			/*
			 * Assert.fail("Kolor karty poza zakresem wskazanym przez metode " + e);
			 * czyNameInteger = false;
			 */
		}
		assertTrue(czyNameInteger);
	}

	@Test
	public void getColorNameTestJestKolor() {

		boolean czyJestKolor = true;
		// Tworzymy obiekt color jako ze w metodzie nie ma parametru przypisania do
		// koloru
		// wazne sa ustawienia parametrukoloru w konstruktorze
		Card color = new Card(2, 2012);

		try {
			color.getColorName();
			czyJestKolor = true;
		} catch (Exception e) {
			/*
			 * Assert.fail("Kolor karty poza zakrezem " + e); czyJestKolor = false;
			 */
		}
		assertTrue(czyJestKolor);
	}

	@Test
	public void getCardNameTestJestNazwaKarty() {

		boolean czyJestNazwaKarty = false;
		Card nazwaKarty = new Card(-1);

		try {
			nazwaKarty.getCardName();
			czyJestNazwaKarty = true;
		} catch (Exception e) {
			/*
			 * Assert.fail("Nazwa karty poza zakresem " + e); czyJestNazwaKarty = false;
			 */
		}
		assertTrue(czyJestNazwaKarty);
	}

	@Test
	public void getActionItegerTest() {

		boolean czyJestAction = false;
		Card action = new Card(15);

		try {
			action.getAction();
			czyJestAction = true;

		} catch (Exception e) {
			/*
			 * Assert.fail("Blad w Integer Action " + e); czyJestAction = false;
			 */
		}
		assertTrue(czyJestAction);
	}

	@Test
	public void hasColorPositive() {
		Card hasColor = new Card(2, 2012);
		assertTrue(hasColor.hasColor());
		assertNotNull(hasColor.hasColor());
	}

	@Test
	public void hasColorNegative() {
		Card hasColor = new Card(2, null);
		assertFalse(hasColor.hasColor());
	}

	@Test
	public void isNormalCardPositive() {
		Card actionReturn = new Card(1);
		assertTrue(actionReturn.isNormalCard());
		assertNotNull(actionReturn.hasColor());
	}

	@Test
	public void isNormalCardNegative() {
		Card actionReturn = new Card(14);
		assertFalse(actionReturn.isNormalCard());
	}

	@Test
	public void testToString() {
		Card hasColorToStrig = new Card(2, null);
		String expected = hasColorToStrig.getCardName();
		Assert.assertEquals(expected, hasColorToStrig.toString());
	}

	@Test
	public void testToString1() {
		Card hasColorToStrig = new Card(2, 2012);
		String expected = hasColorToStrig.getCardName();
		Assert.assertNotEquals(expected, hasColorToStrig.toString());
	}

	@Test
	public void testColorName() {
		Card colorName = new Card(2, 2011);
		colorName.getColorName();
	}

	@Test
	public void testCardName() {
		Card card = new Card(2);
		card.getCardName();
	}
}
