package com.visma.homework.VideoPoker;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {

		List<Card> hand = new LinkedList<>();
		Deck deck = new Deck();
		Scanner scanner = new Scanner(System.in);

		// draw 5 cards from deck to hand:
		for (int i = 0; i < 5; i++) {
			drawCard(deck, hand);
		}

		System.out.println("Cards drawn:");
		printCards(hand);

		// Ask for indexes of cards to change:
		System.out.println(
				"\nPlease enter numbers of cards you would like to change (separated by a space) or press ENTER if no changes required: ");
		String changes = scanner.nextLine();
		String[] indexes = changes.split(" ");
		Arrays.sort(indexes, Collections.reverseOrder());

		// check for valid input
		boolean validInput = false;

		// check for empty input. If empty, evaluate hand and finish game
		if (changes.equals(null) || changes.equals("")) {
			validInput = true;
			evaluateHand(hand);

		}
		while (!validInput) {
			// check if input has too many items
			if (indexes.length > 5) {
				validInput = false;
				System.out.println(
						"Invalid input (Too many numbers provided). Please repeat.\nPlease enter numbers of cards you would like to change (separated by a space) or press ENTER if no changes required:");
			} else if(checkForDuplicates(indexes)) {
				validInput = false;
				System.out.println(
						"Invalid input (Card number duplicates found). Please repeat.\nPlease enter numbers of cards you would like to change (separated by a space) or press ENTER if no changes required:");
			} else {
				// check if input are numbers from 1 to 5
				validInput = true;
				for (String indexStr : indexes) {
					if (isInteger(indexStr)) {
						if (Integer.parseInt(indexStr) > 5 || Integer.parseInt(indexStr) <= 0) {
							validInput = false;
						}
					} else {
						validInput = false;
					}
				}
				if (!validInput) {
					System.out.println(
							"Invalid input (Please enter card numbers from 1 to 5).\nPlease enter numbers of cards you would like to change (separated by a space) or press ENTER if no changes required:");
				}
			}

			if (validInput) {
				// if input is valid change cards, print them out to console, evaluate hand and
				// finish game.
				for (String indexStr : indexes) {
					changeCard(deck, hand, Integer.parseInt(indexStr) - 1);
				}
				System.out.println("\nCards after change:");
				printCards(hand);
				evaluateHand(hand);
			} else {
				// if input is not valid ask for a new input
				changes = scanner.nextLine();
				indexes = changes.split(" ");
				Arrays.sort(indexes, Collections.reverseOrder());
			}

		}

		scanner.close();

	}

	private static void drawCard(Deck deck, List<Card> hand) {
		// pick a card from deck to hand
		int cardIndex = new Random().nextInt(deck.getDeck().size());
		hand.add(deck.getDeck().get(cardIndex));
		deck.getDeck().remove(cardIndex);
	}

	private static void changeCard(Deck deck, List<Card> hand, int index) {
		// remove card from hand with defined index:
		hand.remove(index);
		// draw new card
		drawCard(deck, hand);
	}

	private static void evaluateHand(List<Card> hand) {
		// This method evaluates hand combination and prints out to console the
		// combination and prize money

		// sort hand by card value for further evaluations
		hand.sort(new MyCardComparator());

		// get second high card value (needed for royal flush evaluation)
		int secondHighCard = hand.get(3).getValue().getValueInt();

		// count of cards with the same value:
		int[] valuesCountArr = new int[13];
		for (int i = 0; i < valuesCountArr.length; i++) {
			valuesCountArr[i] = 0;
		}
		for (int i = 0; i < 5; i++) {
			valuesCountArr[hand.get(i).getValue().getValueInt() - 2]++;
		}

		// count pairs, check if there is a three-of-a-kind or four-of-a-kind
		int pairCount = 0;
		boolean fourOfaKind = false;
		boolean threeOfaKind = false;
		boolean pairOfJacks = false;
		for (int i = 0; i < valuesCountArr.length; i++) {
			if (valuesCountArr[i] == 2) {
				pairCount++;
				if (i > 8) {
					pairOfJacks = true;
				}
			}
			if (valuesCountArr[i] == 3) {
				threeOfaKind = true;
			}
			if (valuesCountArr[i] == 4) {
				fourOfaKind = true;
			}
		}

		// check for a full-house:
		boolean fullHouse = false;
		if (pairCount == 1 && threeOfaKind) {
			fullHouse = true;
		}

		// check if there is a straight
		boolean isStraight = false;
		// ace as highest card scenario:
		if (hand.get(0).getValue().getValueInt() + 1 == hand.get(1).getValue().getValueInt()
				&& hand.get(1).getValue().getValueInt() + 1 == hand.get(2).getValue().getValueInt()
				&& hand.get(2).getValue().getValueInt() + 1 == hand.get(3).getValue().getValueInt()
				&& hand.get(3).getValue().getValueInt() + 1 == hand.get(4).getValue().getValueInt()) {
			isStraight = true;
			// as a a lowest card scenario:
		} else if (hand.get(4).getValue().getValueInt() - 12 == hand.get(0).getValue().getValueInt()
				&& hand.get(0).getValue().getValueInt() + 1 == hand.get(1).getValue().getValueInt()
				&& hand.get(1).getValue().getValueInt() + 1 == hand.get(2).getValue().getValueInt()
				&& hand.get(2).getValue().getValueInt() + 1 == hand.get(3).getValue().getValueInt()) {
			isStraight = true;
		}

		// check if there is a flush
		boolean isFlush = true;
		for (int i = 0; i < 4; i++) {
			if (hand.get(i).getType() != hand.get(i + 1).getType()) {
				isFlush = false;
			}
		}

		// check for royal flush
		System.out.println();
		if (isFlush && isStraight && secondHighCard == 13) {
			System.out.println("ROAYL FLUSH: you won 800 €");
			// check for straight flush
		} else if (isFlush && isStraight) {
			System.out.println("STRAIGHT FLUSH: you won 50 €");
			// check for four of a kind
		} else if (fourOfaKind) {
			System.out.println("FOUR OF A KIND: you won 25 €");
			// check for a full house
		} else if (fullHouse) {
			System.out.println("FULL HOUSE: you won 9 €");
			// check if it is only a flush
		} else if (isFlush) {
			System.out.println("FLUSH: you won 6 €");
			// check if it is only a straight
		} else if (isStraight) {
			System.out.println("STRAIGHT: you won 4 €");
			// check if it is only three of a kind
		} else if (threeOfaKind) {
			System.out.println("THREE OF A KIND: you won 3 €");
			// check if it is two pairs
		} else if (pairCount == 2) {
			System.out.println("TWO PAIR: you won 2 €");
			// check if it is only jacks or higher
		} else if (pairCount == 1 && pairOfJacks) {
			System.out.println("JACKS OR HIGHER: you won 1 €");
			// no combination, game lost
		} else {
			System.out.println("Sorry, you lost");
		}
	}

	private static void printCards(List<Card> hand) {
		hand.sort(new MyCardComparator());
		int cardNumber = 1;
		for (final Card card : hand) {
			System.out.println(
					"Nr.: " + cardNumber + " | " + card.getValue().toString() + " of " + card.getType().toString());
			cardNumber++;
		}
	}

	// method is used instead of Integer.parseInt() because of better performance
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static boolean checkForDuplicates(String[] array) {
		Set<String> temp = new HashSet<>();
		for (String string : array) {
			if (temp.contains(string)) {
				return true;
			}
			temp.add(string);
		}
		return false;
	}
}