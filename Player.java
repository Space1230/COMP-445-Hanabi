
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
	private Hand myHand;
	private CardKnowledge[] knowledge;
	private Board boardState;
	private boolean hasColorHinted[];
	private boolean hasNumberHinted[];

	public Player() {
		myHand = new Hand();
		knowledge = new CardKnowledge[5];
		boardState = new Board();
		hasColorHinted = new boolean[5];
		Arrays.fill(hasColorHinted, false);
		hasNumberHinted = new boolean[5];
		Arrays.fill(hasNumberHinted, false);
	}

	/**
	 * This method runs whenever your partner discards a card.
	 * @param startHand The hand your partner started with before discarding.
	 * @param discard The card he discarded.
	 * @param disIndex The index from which he discarded it.
	 * @param draw The card he drew to replace it; null, if the deck is empty.
	 * @param drawIndex The index to which he drew it.
	 * @param finalHand The hand your partner ended with after redrawing.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex,
								   Hand finalHand, Board boardState) {
		// If the partner discarded a card, update knowledge
		if (discard != null) {
			hasNumberHinted[disIndex] = false; // reset hints for new index
			hasColorHinted[disIndex] = false; // reset hints for new index
			// If the partner discarded a card, it means that card is not playable
			//knowledge.eliminateCard(discard);
		}
		// If the partner drew a card, update knowledge
		if (draw != null) {
			// If the partner drew a card, add it to knowledge
			//knowledge.eliminateCard(draw);
		}
	}

	/**
	 * This method runs whenever you discard a card, to let you know what you discarded.
	 * @param discard The card you discarded.
	 * @param disIndex The index from which you discarded it.
	 * @param drawIndex The index to which you drew the new card (if drawSucceeded)
	 * @param drawSucceeded true if there was a card to draw; false if the deck was empty
	 * @param boardState The state of the board after play.
	 */
	public void tellYourDiscard(Card discard, int disIndex, int drawIndex, boolean drawSucceeded, Board boardState) {
		// If you discarded a card, update knowledge
		if (discard != null) {
			// If you discarded a card, it means that card is not playable
			//knowledge.eliminateCard(discard);
		}
		// If you drew a card, update knowledge
		if (drawSucceeded) {
			// If you drew a card, add it to knowledge
			//knowledge.eliminateCard(boardState.discards.get(boardState.discards.size() - 1));
		}
	}

	/**
	 * This method runs whenever your partner played a card
	 * @param startHand The hand your partner started with before playing.
	 * @param play The card she played.
	 * @param playIndex The index from which she played it.
	 * @param draw The card she drew to replace it; null, if the deck was empty.
	 * @param drawIndex The index to which she drew the new card.
	 * @param finalHand The hand your partner ended with after playing.
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex,
								Hand finalHand, boolean wasLegalPlay, Board boardState) {
		// If the partner played a card, update knowledge
		if (play != null) {
			hasColorHinted[playIndex] = false; // reset hints for new index
			hasNumberHinted[playIndex] = false; // reset hints for new index
			if (wasLegalPlay) {
				// If the partner played a card legally, update knowledge
				//knowledge.eliminateCard(play);
			} else {
				// If the partner played a card illegally, it must have been discarded
				//knowledge.eliminateCard(play);
			}
		}
		// If the partner drew a card, update knowledge
		if (draw != null) {
			// If the partner drew a card, add it to knowledge
			//knowledge.eliminateCard(draw);
		}
	}

	/**
	 * This method runs whenever you play a card, to let you know what you played.
	 * @param play The card you played.
	 * @param playIndex The index from which you played it.
	 * @param drawIndex The index to which you drew the new card (if drawSucceeded)
	 * @param drawSucceeded  true if there was a card to draw; false if the deck was empty
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourPlay(Card play, int playIndex, int drawIndex, boolean drawSucceeded,
							 boolean wasLegalPlay, Board boardState) {
		// If you played a card, update knowledge
		if (play != null) {
			if (wasLegalPlay) {
				// If you played a card legally, update knowledge
				//knowledge.eliminateCard(play);
			} else {
				// If you played a card illegally, it must have been discarded
				//knowledge.eliminateCard(play);
			}
		}
		// If you drew a card, update knowledge
		if (drawSucceeded) {
			// If you drew a card, add it to knowledge
			//knowledge.eliminateCard(boardState.discards.get(boardState.discards.size() - 1));
			//knowledge.eliminateCard(play);
		}
	}

	/**
	 * This method runs whenever your partner gives you a hint as to the color of your cards.
	 * @param color The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
	 * @param indices The indices (from 0-4) in your hand with that color.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		// If partner provided a color hint, update knowledge
		for (Integer index : indices) {

			//knowledge.knowColor(color);
		}
	}

	/**
	 * This method runs whenever your partner gives you a hint as to the numbers on your cards.
	 * @param number The number hinted, from 1-5.
	 * @param indices The indices (from 0-4) in your hand with that number.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
		// If partner provided a number hint, update knowledge
		for (Integer index : indices) {
			//knowledge.knowValue(number);
		}
	}

	/**
	 * This method runs when the game asks you for your next move.
	 * @param yourHandSize How many cards you have in hand.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The current state of the board.
	 * @return A string encoding your chosen action. Actions should have one of the following formats; in all cases,
	 *  "x" and "y" are integers.
	 * 	a) "PLAY x y", which instructs the game to play your card at index x and to draw a card back to index y. You
	 *     should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Illegal plays will consume a fuse; at 0 fuses, the game ends with a score of 0.
	 *  b) "DISCARD x y", which instructs the game to discard the card at index x and to draw a card back to index y.
	 *     You should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Discarding returns one hint if there are fewer than the maximum number available.
	 *  c) "NUMBERHINT x", where x is a value from 1-5. This command informs your partner which of his cards have a value
	 *     of the chosen number. An error will result if none of his cards have that value, or if no hints remain.
	 *     This command consumes a hint.
	 *  d) "COLORHINT x", where x is one of the RED, YELLOW, BLUE, GREEN, or WHITE constant values in Colors.java.
	 *     This command informs your partner which of his cards have the chosen color. An error will result if none of
	 *     his cards have that color, or if no hints remain. This command consumes a hint.
	 *
	 * Andy's Ideas for what we should do
	 * + discard from the right
	 * + if there are any 1's at the beginning, hint at the color
	 *   - if at the beginning, just play color hinted cards if only one
	 * + only hint if card is valuable
	 *   - try to choose color or number based on what is avaliable
	 *   - don't care the cards at the beginning unless there is only one
	 *     more card
	 */
	public String ask(int yourHandSize, Hand partnerHand, Board boardState) {
		//If this is the start of the game and none of the hints have been used, check to see if there are any fives in your partner's hands and hint them to him
		double precentage_of_non_empty_spaces = getPercentageOfNonEmptySpaces(boardState);

		// beginning of the game
		if (precentage_of_non_empty_spaces < .5) {
			boolean will_number_hint = false;
			ArrayList<Integer> number_hint_indices = new ArrayList<Integer>();

			for (int i = 0; i < partnerHand.size(); i++) {
				Card card = partnerHand.get(i);
				// checking to see if it is a 1, no other color matches, and hint hasn't been given before
				if (card.value == 1 && countColorMatches(card, partnerHand) < 2 && !hasColorHinted[i]) {
					hasColorHinted[i] = true; // this card has been hinted at
					return "COLORHINT " + card.color;
				}
				// will do a number hint; doesn't make sense if already done
				else if (card.value == 1 && !hasNumberHinted[i]) {
					will_number_hint = true;
					number_hint_indices.add(i);
				}
			}
			// hint all 1's and add them to the number hinted array
			if (will_number_hint) {
				for (int index : number_hint_indices) {
					hasNumberHinted[index] = true;
				}

				return "NUMBERHINT 1";
			}
			// discard the rightmost card
			// probably wrong syntax
			else {
				return "DISCARD 4 4";
			}
		}

		if (boardState.numHints == 8){
			for (int i = 0;i < partnerHand.size();i++){
				if (partnerHand.get(i).value == 5){
					return "NUMBERHINT 5";
				}
			}
		}
//		for (int i = 0;i < yourHandSize;i++){
//			if (knowledge.getValue(myHand.get(i)) ==  5){
//				return "DISCARD " + i + " " + i;
//			}
//		}
		return "DISCARD 0 0"; // Discard the first card in hand
	}

	public double getPercentageOfNonEmptySpaces(Board boardState) {
		int number_of_non_empty_spaces = 0;
		for (Integer card : boardState.tableau) {
			if (card > 0) {
				number_of_non_empty_spaces++;
			}
		}

		return (double)number_of_non_empty_spaces/(double)boardState.tableau.size();
	}

	public int countColorMatches(Card card, Hand hand) {
		int matches = 0;
		for (int i = 0; i < hand.size(); i++) {
			Card comparisonCard = hand.get(i);
			if (card.color == comparisonCard.color) {
				matches++;
			}
		}
		return matches;
	}

	public int countNumberMatches(Card card, Hand hand) {
		int matches = 0;
		for (int i = 0; i < hand.size(); i++) {
			Card comparisonCard = hand.get(i);
			if (card.value == comparisonCard.value) {
				matches++;
			}
		}
		return matches;
	}

}
