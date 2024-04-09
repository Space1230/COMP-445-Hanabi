import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> cards;
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	public Hand(Hand h) {
		cards = new ArrayList<Card>(h.cards);
	}
	
	public Card get(int index) {
		if ((index >=0) && (index < cards.size())) {
			return new Card(cards.get(index));
		}
		else {
			throw new IllegalArgumentException("Hand.get() - index " + index + " out of bounds for hand size " + cards.size());
		}
	}

	public int countMatches(Card c) {
		int result = 0;
		for (Card otherCard : cards) {
			if (c.equals(otherCard)) {
				result++;
			}
		}
		return result;
	}
	
	public void add(int index, Card c) {
		if ((index >= 0) && (index <= cards.size())) {	// Note we can add past the end of the array.
			cards.add(index, c);
		}
		else {
			throw new IllegalArgumentException("Hand.add() - index " + index + " out of bounds for hand size " + cards.size());
		}
	}
	
	public Card remove(int index) {
		if ((index >= 0) && (index < cards.size())) {
			return cards.remove(index);
		}
		else {
			throw new IllegalArgumentException("Hand.remove() - index " + index + " out of bounds for hand size " + cards.size());
		}
	}
	
	public int size() {
		return cards.size();
	}
	
	@Override
	public String toString() {
		if (cards.size() == 0) {
			return "Empty";
		}
		else if (cards.size() == 1) {
			return cards.get(0).toString();
		}
		else if (cards.size() == 2) {
			return cards.get(0).toString() + " and " + cards.get(1).toString();
		}
		else {
			String result = "";
			for (int i = 0; i < cards.size(); i++) {
				result += cards.get(i).toString();
				if (i < cards.size() - 1) {
					result += ", ";
				}
				if (i == cards.size() - 2) {
					result += "and ";
				}
			}
			return result;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Hand)) {
			return false;
		}
		Hand h = (Hand) o;
		return this.cards.equals(h.cards);
	}

	@Override
	public int hashCode() {
		int result = 1;

		for (Card c: cards) {
			result = 31 * result + c.hashCode();
		}
		return result;
	}
}
