package com.visma.homework.VideoPoker;

import java.util.LinkedList;
import java.util.List;

import com.visma.homework.VideoPoker.Card.Value;

public class Deck {
	
	//initiate a deck of cards
	private List<Card> deck = new LinkedList<>();
	
	//on creating deck add 52 standard cards to the deck
	public Deck() {
		
		for(final Card.Type type : Card.Type.values()) {
			for(final Value value : Card.Value.values()) {
				Card card = new Card(type, value);
				deck.add(card);
			}
		}
		
	}

	public List<Card> getDeck() {
		return deck;
	}
}
