package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;

public interface RelatednessCalculator {

	/**
	 * Calculates the relatedness of two words to each other. The calculation is
	 * done using all possible synset combinations of the words. The combination
	 * with the highest relatedness is used and its relatedness is returned.
	 * 
	 * @param word1
	 * @param word2
	 * @return relatedness of the synsets of the two words with the highest
	 *         relatedness. The relatedness ranges from
	 *         {@link #getMinPossibleValue()} to {@link #getMaxPossibleValue()}.
	 */
	public double calcRelatednessOfWords(String word1, POS word1PosTag, String word2, POS word2PosTag);

	/**
	 * Calculates the relatedness of two WordNet index words to each other. The calculation is
	 * done using all possible synset combinations of the words. The combination
	 * with the highest relatedness is used and its relatedness is returned.
	 * 
	 * @param word1
	 * @param word2
	 * @return relatedness of the synsets of the two words with the highest
	 *         relatedness. The relatedness ranges from
	 *         {@link #getMinPossibleValue()} to {@link #getMaxPossibleValue()}.
	 */
	public double calcRelatednessOfWords(IIndexWord word1, IIndexWord word2);

	/**
	 * Calculates the relatedness of two word meanings to each other.
	 * 
	 * @param word1
	 *            meaning of word1
	 * @param word2
	 *            meaning of word2
	 * @return relatedness of the synsets of the two word meanings. The
	 *         relatedness ranges from {@link #getMinPossibleValue()} to
	 *         {@link #getMaxPossibleValue()}.
	 */
	public double calcRelatednessOfWords(IWord word1, IWord word2);

	/**
	 * Calculates the relatedness of two synsets to each other.
	 * 
	 * @param synsets1
	 * @param synsets2
	 * @return relatedness of the synsets which ranges from
	 *         {@link #getMinPossibleValue()} to {@link #getMaxPossibleValue()}.
	 */
	public double calcRelatednessOfSynsets(ISynset synset1, ISynset synset2);

	/**
	 * Returns the maximum possible value for the measure of the relatedness
	 * calculator.
	 * 
	 * @return the maximum possible value for the measure of the relatedness
	 *         calculator.
	 */
	public double getMaxPossibleValue();

	/**
	 * Returns the minimum possible value for the measure of the relatedness
	 * calculator.
	 * 
	 * @return the minimum possible value for the measure of the relatedness
	 *         calculator.
	 */
	public double getMinPossibleValue();
}
