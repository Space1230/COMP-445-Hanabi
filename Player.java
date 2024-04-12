import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;


public class Player {
	private CardKnowledge[] ourDeckKnowledge;
	private Set<Card> ourImpossibleCards;
	private CardKnowledge[] theirDeckKnowledge;
	private Set<Card> theirImpossibleCards;

	private boolean hasColorHinted[];
	private boolean hasNumberHinted[];

	public Player() {

		ourDeckKnowledge = new CardKnowledge[5];
		theirDeckKnowledge = new CardKnowledge[5]; // TODO implement this
		for (int i = 0; i < 5; i++) {
			ourDeckKnowledge[i] = new CardKnowledge();
			theirDeckKnowledge[i] = new CardKnowledge();
		}
		ourImpossibleCards = new HashSet<Card>();
		theirImpossibleCards = new HashSet<Card>();

		hasColorHinted = new boolean[5];
		hasNumberHinted = new boolean[5];
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
			this.removeCardAndUpdateImpossible(boardState, discard, theirDeckKnowledge, theirImpossibleCards);
		}
		// If the partner drew a card, update knowledge
		if (draw != null) {
			// If the partner drew a card, add it to knowledge
			theirDeckKnowledge[disIndex] = new CardKnowledge(theirImpossibleCards);
			//theirDeckKnowledge[disIndex].eliminateNonPlayableOptions(boardState);
		}
		else {
			assert false;
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
			this.removeCardAndUpdateImpossible(boardState, discard, ourDeckKnowledge, ourImpossibleCards);
		}
		// If you drew a card, update knowledge
		if (drawSucceeded) {
			// If you drew a card, add it to knowledge
			//knowledge.eliminateCard(boardState.discards.get(boardState.discards.size() - 1));
			ourDeckKnowledge[disIndex] = new CardKnowledge(ourImpossibleCards);
		}
		else {
			assert false;
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
				for (CardKnowledge knowledge : theirDeckKnowledge) {
					knowledge.eliminateCard(draw);
				}
			} else {
				// If the partner played a card illegally, it must have been discarded
				//knowledge.eliminateCard(play);
			}
			this.removeCardAndUpdateImpossible(boardState, draw, theirDeckKnowledge, theirImpossibleCards);
		}
		// If the partner drew a card, update knowledge
		if (draw != null) {
			// If the partner drew a card, add it to knowledge
			theirDeckKnowledge[playIndex] = new CardKnowledge(theirImpossibleCards);
		}
		else {
			assert false;
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
		if (play != null) {
			if (wasLegalPlay) {
				// If you played a card legally, update knowledge
				for (CardKnowledge knowledge : ourDeckKnowledge) {
					knowledge.eliminateCard(play);
					//ourImpossibleCards.add(play);
				}
			} else {
				// If you played a card illegally, it must have been discarded
			}
			this.removeCardAndUpdateImpossible(boardState, play, ourDeckKnowledge, ourImpossibleCards);

		}
		// If you drew a card, update knowledge
		if (drawSucceeded) {
			// If you drew a card, add it to knowledge
			ourDeckKnowledge[playIndex] = new CardKnowledge(ourImpossibleCards);
		}
		else {
			assert false;
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
			ourDeckKnowledge[index].knowColor(color);
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
			ourDeckKnowledge[index].knowValue(number);
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
	 * + need to have proper card tracking stuff
	 *   - we need to add items
	 * + only hint if card is valuable
	 *   - try to choose color or number based on what is avaliable
	 *   - don't care the cards at the beginning unless there is only one
	 *     more card
	 */
	public String ask(int yourHandSize, Hand partnerHand, Board boardState) {
		//If this is the start of the game and none of the hints have been used, check to see if there are any fives in your partner's hands and hint them to him
		double precentage_of_non_empty_spaces = getPercentageOfNonEmptySpaces(boardState);
		boolean careAboutFives = (boardState.getTableauScore() >= 18) ?true :false;
//		System.out.println("");

		if (boardState.numHints == 0){
			int disc_idx = this.getDiscardIndex(ourDeckKnowledge, boardState, careAboutFives);
			return "DISCARD " + disc_idx + " " + disc_idx;
		}

		// beginning of the game
//		if (precentage_of_non_empty_spaces < 1) {
			//If not all the ones have been played in the tableue, keep checking and hinting for ones
			if (boardState.tableau.get(0) == 0 || boardState.tableau.get(1) == 0 || boardState.tableau.get(2) == 0 || boardState.tableau.get(3) == 0 || boardState.tableau.get(4) == 0) {
				String result = this.hintDiscard(partnerHand, boardState, 1,false);
				if (result != null) {
					return result;
				}

				result = this.play(boardState, 1);
				if (result != null) {
					return result;
				}

				result = this.hint(partnerHand, boardState, 1,false);
				if (result != null) {
					return result;
				}

				//If over half of the ones are filled, start hinting and playing 2s
				if (boardState.getTableauScore() >= 0) {
					result = this.hintDiscard(partnerHand, boardState, 2,false);
					if (result != null) {
						return result;
					}

					result = this.play(boardState, 2);
					if (result != null) {
						return result;
					}
					result = this.hint(partnerHand, boardState, 2,false);
					if (result != null) {
						return result;
					}
				}
			}


		//}
			// If not all the twos have been played in the tableue, keep checking and hinting for twos
		else if (boardState.tableau.get(0) == 1 || boardState.tableau.get(1) == 1 || boardState.tableau.get(2) == 1 || boardState.tableau.get(3) == 1 || boardState.tableau.get(4) == 1) {
			String result = this.hintDiscard(partnerHand, boardState, 2,false);
			if (result != null) {
				return result;
			}

			result = this.play(boardState, 2);
			if (result != null) {
				return result;
			}

			result = this.hint(partnerHand, boardState, 2,false);
			if (result != null) {
				return result;
			}

			//If over half of the twos are filled, start hinting and playing 3s
			if (boardState.getTableauScore() >= 8) {
				result = this.hintDiscard(partnerHand, boardState, 3,false);
				if (result != null) {
					return result;
				}

				result = this.play(boardState, 3);
				if (result != null) {
					return result;
				}
				result = this.hint(partnerHand, boardState, 3,false);
				if (result != null) {
					return result;
				}


			}

		}

		//If not all the threes have been played in the tableue, keep checking and hinting for threes
		else if (boardState.tableau.get(0) == 2 || boardState.tableau.get(1) == 2 || boardState.tableau.get(2) == 2 || boardState.tableau.get(3) == 2 || boardState.tableau.get(4) == 2) {
			String result = this.hintDiscard(partnerHand, boardState, 3,false);
			if (result != null) {
				return result;
			}

			result = this.play(boardState, 3);
			if (result != null) {
				return result;
			}

			result = this.hint(partnerHand, boardState, 3,false);
			if (result != null) {
				return result;
			}

			//If over half of the threes are filled, start hinting and playing 4s
			if (careAboutFives){
				result = this.hintDiscard(partnerHand, boardState, 4,false);
				if (result != null) {
					return result;
				}
				result = this.play(boardState, 4);
				if (result != null) {
					return result;
				}
				result = this.hint(partnerHand, boardState, 4,false);
				if (result != null) {
					return result;
				}


			}
		}
		//If not all the fours have been played in the tableue, keep checking and hinting for fours
		else if (boardState.tableau.get(0) == 3 || boardState.tableau.get(1) == 3 || boardState.tableau.get(2) == 3 || boardState.tableau.get(3) == 3 || boardState.tableau.get(4) == 3) {
			String result = this.hintDiscard(partnerHand, boardState, 4,false);
			if (result != null) {
				return result;
			}

			result = this.play(boardState, 4);
			if (result != null) {
				return result;
			}

			result = this.hint(partnerHand, boardState, 4,false);
			if (result != null) {
				return result;
			}

			//If over half of the threes are filled, start hinting and playing 4s
			if (boardState.getTableauScore() >= 18) {
				result = this.hintDiscard(partnerHand, boardState, 5,true);
				if (result != null) {
					return result;
				}
				result = this.play(boardState, 5);
				if (result != null) {
					return result;
				}
				result = this.hint(partnerHand, boardState, 5,true);
				if (result != null) {
					return result;
				}


			}
		}

		//If not all the fives have been played in the tableue, keep checking and hinting for fives
		else if (boardState.tableau.get(0) == 4 || boardState.tableau.get(1) == 4 || boardState.tableau.get(2) == 4 || boardState.tableau.get(3) == 4 || boardState.tableau.get(4) == 4) {
			String result = this.hintDiscard(partnerHand, boardState, 5,true);
			if (result != null) {
				return result;
			}

			result = this.play(boardState, 5);
			if (result != null) {
				return result;
			}

			result = this.hint(partnerHand, boardState, 5,true);
			if (result != null) {
				return result;
			}
		}
//		System.out.println("Fuses: " + boardState.numFuses);
			int discardIndex = this.getDiscardIndex(ourDeckKnowledge, boardState, careAboutFives);
			assert false;

//		if (boardState.numHints == 8){
//			for (int i = 0;i < partnerHand.size();i++){
//				if (partnerHand.get(i).value == 5){
//					return "NUMBERHINT 5";
//				}
//			}
//		}
//		for (int i = 0; i < yourHandSize; i++){
//			 if (ourDeckKnowledge[i].getKnownValue() == 5){
//			 	return "DISCARD " + i + " " + i;
//			 }
//		}

		return "DISCARD 0 0"; // Discard the first card in hand
	}

	public String hintDiscard(Hand partnerHand, Board boardState, int importantValue, boolean careAboutFives) {
		int discardIndex = this.getPartnerDiscardIndex();
		Card rightmostCard = partnerHand.get(discardIndex);
		boolean rightmostCardIsImportant = this.cardIsImportant(boardState,
																rightmostCard,
																careAboutFives);
		if (rightmostCard.value == importantValue || rightmostCardIsImportant) {
			// check if the left card has more priority
			if (discardIndex > 0) { // valid left card
				Card leftCard = partnerHand.get(discardIndex - 1);
				boolean leftCardIsImportant = this.cardIsImportant(boardState,
																leftCard,
																careAboutFives);
				int importantIndex = -1;
				Card importantCard = null;
				if (rightmostCardIsImportant) {
//					System.out.println("rightmostCard: " + rightmostCard.toString());
//					System.out.println("rightImportant: " + rightmostCardIsImportant);

//					System.out.println("leftCard: " + leftCard.toString());
//					System.out.println("leftImportant: " + leftCardIsImportant);
					if (rightmostCard.value >= leftCard.value ||
						(rightmostCardIsImportant && !leftCardIsImportant)) {
						importantIndex = discardIndex;
						importantCard = rightmostCard;
					}
					else {
						importantIndex = discardIndex - 1;
						importantCard = leftCard;
					}
//					System.out.println("importantCard: " + importantCard.toString());

					// use a number hint unless it is immediately playable
					if (this.cardIsImmediatelyPlayable(leftCard, boardState)){
						this.hasColorHinted[importantIndex] = true;
						return "COLORHINT " + importantCard.color;
					}
					else{
						this.hasNumberHinted[importantIndex] = true;
						return "NUMBERHINT " + importantCard.value;
					}
				}
			}
		}
		return null;
	}

	public String play(Board boardState, int importantValue){
		int num_color[] = new int[5];
		Arrays.fill(num_color, -1);
		for (int i = 0; i < 5; i++) { // searching for hint
			int color = ourDeckKnowledge[i].getKnownColor();
			int value = ourDeckKnowledge[i].getKnownValue();
			// check to see if we know the color

			if (ourDeckKnowledge[i].hasBeenHinted && color != -1) { // if color hint
				if (num_color[color] != -2) { // unique color
					if (num_color[color] != -1) {
						num_color[color] = -2;
					}
					else {
						num_color[color] = i;
					}
				}
				// check to see if we know the number too
				if (value == importantValue) {
					Card card = new Card(color, value);
					if (this.cardIsImmediatelyPlayable(card, boardState)){
//					System.out.println("Deck Knowledge: " + ourDeckKnowledge[i].options);
						return "PLAY " + i + " " + i;
					}
					else {
						System.out.println("Testing");
						return "DISCARD " + i + " " + i;
					}
				}
			}
		}
		int card_index;
		for (int i = 0; i < 5; i++) {
			if ((card_index = num_color[i]) > -1) {
				// check to see if the play is valid
				if (ourDeckKnowledge[card_index].isDefinitelyPlayable(boardState)) {
//					System.out.println("Deck Knowledge: " + ourDeckKnowledge[card_index].options);
					return "PLAY " + card_index + " " + card_index;
				}
			}
		}
		return null;
	}

	public String hint(Hand partnerHand, Board boardState, int importantValue, boolean careAboutFives) {
		// Going to Use Color Hint
		// the goal: ensure only there is only one card in the
		// deck with one color
		//
		// this is indexed by the color
		// if a card has not been here before (the item is -1),
		//     it sets the item to its index in the deck
		// if a card sees that another card has set its
		//     index, then it knows that the entry is invalid
		//     and sets it to a -2
		// Going to Hint
		boolean will_number_hint = false;
		ArrayList<Integer> number_hint_indices = new ArrayList<Integer>();


		for (int i = 0; i < partnerHand.size(); i++) {
			Card card = partnerHand.get(i);
			// checking to see if it is a 1, no other color matches, hint hasn't been given before, and card is immediately playable

			// checking to see if it is a 1, no other color matches, and hint hasn't been given before
			if (card.value == importantValue &&
				this.shouldHint(boardState, partnerHand, this.getColorHintIndicies(boardState, partnerHand, card.color), careAboutFives) &&
				((countColorMatches(card, partnerHand) < 2 && !hasColorHinted[i]) || hasNumberHinted[i]) &&
				this.cardIsImmediatelyPlayable(card, boardState)) {
//				System.out.println("COLORHINT Card: " + card.toString());
				hasColorHinted[i] = true; // this card has been hinted at
				return "COLORHINT " + card.color;
			}
			// will do a number hint; doesn't make sense if already done
			else if (card.value == importantValue && !hasNumberHinted[i]) {
				will_number_hint = true;
				number_hint_indices.add(i);
			}
		}
		// hint all 1's and add them to the number hinted array
		if (will_number_hint  &&
			this.shouldHint(boardState, partnerHand, number_hint_indices, careAboutFives)) {
			for (int index : number_hint_indices) {
				hasNumberHinted[index] = true;
			}

			return "NUMBERHINT " + importantValue;
		}
		// discard the rightmost card
		// probably wrong syntax
		 else {
		 	for (int i = 4; i > -1; i--) {
		 		if (!ourDeckKnowledge[i].hasBeenHinted) {
		 			return "DISCARD " + i + " " + i;
		 		}
		 	}
		 }
		return null;
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

	public int discardMatches (Board boardState, Card card) {
		int matches = 0;
		for (Card discard : boardState.discards) {
			if (discard.equals(card)) {
				matches++;
			}
		}
		return matches;
	}

	// call this method when you remove a card.
	//
	// It handles removing the card if all the cards in the
	// group are known
	public void removeCardAndUpdateImpossible(Board boardState, Card card,
											  CardKnowledge knowledge[],
											  Set<Card> impossibleCards) {
		int[] avaliable_cards = { 3, 2, 2, 2, 1 };
		int matches = discardMatches(boardState, card);
		if (matches + 1 == avaliable_cards[card.value - 1]) { // TODO fix
			for (CardKnowledge know : knowledge) { // inefficent, but works
				know.eliminateCard(card);
			}
			impossibleCards.add(card);
		}
	}

	public boolean cardIsImportant(Board boardState, Card card, boolean careAboutFives) {
		if (card.value == 5 && !careAboutFives) {
			return false;
		}

		int[] avaliable_cards = { 3, 2, 2, 2, 1 };
		int matches = discardMatches(boardState, card);
		if (matches + 1 == avaliable_cards[card.value - 1]) {
//			System.out.println("Important: " + card.toString() + " = true");
			return true;
		}

//		System.out.println("Important: " + card.toString() + " = false");
		return false;
	}

	public int getPartnerDiscardIndex() {
		for (int i = 4; i > -1; i--) {
			if (!this.hasColorHinted[i] && !this.hasNumberHinted[i]){
				return i;
			}
		}

		assert false; // TODO implement this later
		return 0;
	}

	public int getDiscardIndex(CardKnowledge knowledge[], Board boardState, boolean careAboutFives) {
		for (int i = 4; i > -1; i--) {
			if (!knowledge[i].hasBeenHinted) {
				return i;
			}
		}
		// TODO choose the best card to discard out of the hints
		// We want to discard the card that we know the most information
		// about, as long as it is not important
		//
		// If it is important, then we should discard the highest
		int highestKnownCard = 0;
		int highestKnownCardIndex = 0; // rightmost highest known card
		int highestValueOnly = 0;
		int highestValueOnlyIndex = 0; // rightmost highest value only
		for (int i = 4; i > -1; i--) {
			int color = knowledge[i].getKnownColor();
			int value = knowledge[i].getKnownValue();

			// if we know the card
			if (color != -1 && value != -1) {
				// if it isn't important, we get rid of it
				if (!this.cardIsImportant(boardState, new Card(color, value), careAboutFives)) {
					return i;
				}
				else {
					if (value >= highestKnownCard) {
						highestKnownCard = value;
						highestKnownCardIndex = i;
					}
				}
			}
			// we only know the value
			else if (value != -1) {
				if (value >= highestValueOnly) {
					highestValueOnly = value;
					highestValueOnlyIndex = i;
				}
			}
		}

		// choose the unknown card if value is higher
		if (highestValueOnly > highestKnownCard) {
			return highestValueOnlyIndex;
		}

		// choose the highest known important card
		return highestKnownCardIndex;
	}

	public boolean cardIsImmediatelyPlayable(Card card, Board boardState) {
		int currentPlayedCard = boardState.tableau.get(card.color);
		boolean result = currentPlayedCard + 1 == card.value;
//		System.out.println("cardImmediately: " + card.toString() + " " + result);
		return result;
	}

	public boolean shouldHint(Board boardState, Hand partnerHand,
							  ArrayList<Integer> hintIndicies, boolean careAboutFives) {
		// get the new discard index if hint is applied
		hintIndicies.sort(Comparator.reverseOrder());
//		System.out.println("Hint Indicies: " + hintIndicies.toString());

		int discardIndex = this.getPartnerDiscardIndex();
		int newDiscardIndex = discardIndex;

//		System.out.println("Discard Index: " + discardIndex);

		for (int index : hintIndicies) {
			// left of discard index
			if (index == newDiscardIndex) {
				newDiscardIndex = index - 1;
			}
		}

//		System.out.println("New Discard Index: " + newDiscardIndex);

		// see if next left card is important
		if (newDiscardIndex > -1 && this.cardIsImportant(boardState, partnerHand.get(newDiscardIndex),
								 careAboutFives)) {
//			System.out.println("Card was Important");
			return false;
		}

		return true;
	}

	public ArrayList<Integer> getColorHintIndicies(Board boardState, Hand partnerHand, int color) {
//		System.out.println("GetColorHintIndicesInput: " + color);
		ArrayList<Integer> output = new ArrayList<Integer>();

		for (int i = 0; i < 5; i++) {
			Card card = partnerHand.get(i);
			if (card.color == color) {
				output.add(i);
			}
		}
		return output;
	}

	public ArrayList<Integer> getNumberHintIndicies(Board boardState, Hand partnerHand, int color) {
		ArrayList<Integer> output = new ArrayList<Integer>();

		for (int i = 0; i < 5; i++) {
			Card card = partnerHand.get(i);
			if (card.color == color) {
				output.add(i);
			}
		}
		return output;
	}
}
