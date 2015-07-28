package org.aksw.simba.topicmodeling.utils.corpus.properties;


/**
 * This interface describes {@link CorpusProperty} classes with the ability to
 * parse their value from a given String. This can be useful for reading /
 * parsing a corpus and its properties from a text without knowing the exact
 * way in which the different properties have to be parsed.
 * 
 * @author m.roeder
 * 
 */
public interface ParseableCorpusProperty extends CorpusProperty {

	/**
	 * This method is used to let the CorpusProperty parse its value from a
	 * given String. After performing this method the CorpusProperty should
	 * have the value contained inside this String.
	 * 
	 * @param value
	 */
	public void parseValue(String value);
}
