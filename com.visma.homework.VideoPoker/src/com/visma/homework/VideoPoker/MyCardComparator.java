package com.visma.homework.VideoPoker;

import java.util.Comparator;

public class MyCardComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		
		if(card1.getValue().getValueInt() < card2.getValue().getValueInt()) {
			return -1;
		} else if (card1.getValue().getValueInt() > card2.getValue().getValueInt()){
			return 1;
		} else {
			return 0;
		}
	}
}
