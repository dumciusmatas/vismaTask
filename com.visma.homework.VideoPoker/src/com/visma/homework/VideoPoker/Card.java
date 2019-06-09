package com.visma.homework.VideoPoker;

public class Card {
	private final Type type;
	private final Value value;

	// available types of cards:
	enum Type {

		HEARTS, DIAMONDS, SPADES, CLUBS

	}

	// available values of cards:
	enum Value {

		TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13),
		ACE(14);

		private final int cardValue;

		private Value(final int cardValue) {
			this.cardValue = cardValue;
		}

		public int getValueInt() {
			return this.cardValue;
		}

	}

	public Card(Type type, Value value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return this.type;
	}

	public Value getValue() {
		return this.value;
	}

}
