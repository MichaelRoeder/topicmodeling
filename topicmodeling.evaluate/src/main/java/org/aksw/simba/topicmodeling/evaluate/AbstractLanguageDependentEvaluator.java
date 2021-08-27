package org.aksw.simba.topicmodeling.evaluate;

import java.util.Iterator;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.lang.Dictionary;
import org.dice_research.topicmodeling.lang.DictionaryFactory;
import org.dice_research.topicmodeling.lang.Language;
import org.dice_research.topicmodeling.utils.vocabulary.MultipleSpellingVocabulary;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public abstract class AbstractLanguageDependentEvaluator extends AbstractEvaluator implements
        LanguageDependentEvaluator {

    private Language corpusLanguage;

    private Dictionary dictionary = null;

    public AbstractLanguageDependentEvaluator(Language corpusLanguage) {
        this.corpusLanguage = corpusLanguage;
    }

    @Override
    public Language getLanguageOfCorpus() {
        return corpusLanguage;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        if (dictionary == null) {
            dictionary = DictionaryFactory.getDictionary(getLanguageOfCorpus(), getLanguageForEvaluation());
        }
        return evaluateModel(model, previousResults, dictionary);
    }

    protected abstract EvaluationResult evaluateModel(Model model, ManagedEvaluationResultContainer previousResults,
            Dictionary dictionary);

    protected IntObjectOpenHashMap<String[]> getLocalTranslationsMap(Vocabulary vocabulary) {
        if (vocabulary instanceof MultipleSpellingVocabulary) {
            return getLocalTranslationsMap((MultipleSpellingVocabulary) vocabulary);
        } else {
            String word, translation;
            IntObjectOpenHashMap<String[]> translations = new IntObjectOpenHashMap<String[]>(vocabulary.size());
            Iterator<String> iterator = vocabulary.iterator();
            int wordId;
            while (iterator.hasNext()) {
                word = iterator.next();
                wordId = vocabulary.getId(word);
                // translate
                if ((word != null) && (word.length() > 0)) {
                    translation = dictionary.translate(word);
                    if ((translation != null) && (translation.length() > 0)) {
                        translations.put(wordId, new String[] { translation });
                    } else {
                        translations.put(wordId, new String[] { word });
                    }
                }
            }
            dictionary.saveAsObjectFile();
            return translations;
        }
    }

    protected IntObjectOpenHashMap<String[]> getLocalTranslationsMap(MultipleSpellingVocabulary vocabulary) {
        String word;
        String spellings[], translationArray[];
        IntObjectOpenHashMap<String[]> translations = new IntObjectOpenHashMap<String[]>(vocabulary.size());
        Iterator<String> iterator = vocabulary.iterator();
        int wordId;
        while (iterator.hasNext()) {
            word = iterator.next();
            wordId = vocabulary.getId(word);
            spellings = vocabulary.getSpellingsForWord(wordId).toArray(new String[0]);
            translationArray = new String[spellings.length + 1];
            // translate
            translationArray[0] = dictionary.translate(word);
            if ((translationArray[0] == null) || (translationArray[0].length() == 0)) {
                translationArray[0] = word;
            }
            for (int i = 0; i < spellings.length; ++i) {
                translationArray[i + 1] = dictionary.translate(spellings[i]);
                if ((translationArray[i + 1] == null) || (translationArray[i + 1].length() == 0)) {
                    translationArray[i + 1] = spellings[i];
                }
            }
            translations.put(wordId, translationArray);
        }
        dictionary.saveAsObjectFile();
        return translations;
    }
}
