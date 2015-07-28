package org.aksw.simba.topicmodeling.lang.postagging;

import java.util.List;
import java.util.StringTokenizer;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


public class SimpleWhitespaceTagger extends AbstractPosTagger {

    public SimpleWhitespaceTagger() {
    }

    public SimpleWhitespaceTagger(PosTaggingTermFilter filter) {
        setFilter(filter);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        String token;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            terms.add(new Term(token));
        }
        return ttText;
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        String token;
        Term term;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            term = new Term(token);
            if (filter.isTermGood(term)) {
                terms.add(term);
            }
        }
        return ttText;
    }

}
