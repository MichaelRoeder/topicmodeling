package org.aksw.simba.topicmodeling.lang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.aksw.simba.topicmodeling.io.CorpusBasedFileProcessor;
import org.aksw.simba.topicmodeling.lang.Language;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hppc.HppcModule;

public class DictionaryDictCC implements Dictionary, CorpusBasedFileProcessor {

    private static final Logger logger = Logger.getLogger(DictionaryDictCC.class);

    private static final boolean REQUEST_UNKNOWN_WORDS = false;

    protected static final String DICTIONARY_FILE_NAME = "dict_cc_$APPENDIX$.object";

    protected static DictionaryDictCC singletonDict;

    protected DictCcTranslater translater = new DictCcTranslater();

    protected ObjectObjectOpenHashMap<String, String> dictionary = new ObjectObjectOpenHashMap<String, String>();

    protected boolean localDictionaryChanged = false;

    protected DictionaryDictCC() {
    }

    public static synchronized DictionaryDictCC getInstance() {
        if (singletonDict == null) {
            singletonDict = readFromObjectFile();
        }
        return singletonDict;
    }

    // @Deprecated
    // public void readCsvFile(File dictCsvFile) {
    // FileReader fin;
    // CSVReader reader = null;
    // try {
    // fin = new FileReader(dictCsvFile);
    // reader = new CSVReader(fin, '\t');
    // String line[];
    // Vector<String> tempVector;
    // while ((line = reader.readNext()) != null) {
    // line = clearLine(line);
    // if (line != null) {
    // if (dictionary.containsKey(line[0])) {
    // dictionary.get(line[0]).add(line[1]);
    // } else {
    // tempVector = new Vector<String>();
    // tempVector.add(line[1]);
    // dictionary.put(line[0], tempVector);
    // }
    // }
    // }
    // reader.readNext();
    // } catch (IOException e) {
    // logger.error("Error reading file.", e);
    // } finally {
    // try {
    // reader.close();
    // } catch (Exception e) {
    // }
    // }
    // }

    @Override
    public void saveAsObjectFile() {
        if (localDictionaryChanged) {
            FileOutputStream fout = null;
            try {
                String filename = DICTIONARY_FILE_NAME;
                if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                    filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
                } else {
                    filename = filename.replace("$APPENDIX$", "");
                }
                fout = new FileOutputStream(filename);
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new HppcModule());
                mapper.writeValue(fout, dictionary);
                localDictionaryChanged = false;
            } catch (IOException e) {
                logger.error("Error while writing the serialized dictionary to file.", e);
            } finally {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static DictionaryDictCC readFromObjectFile() {
        FileInputStream fin = null;
        DictionaryDictCC dict = new DictionaryDictCC();
        try {
            String filename = DICTIONARY_FILE_NAME;
            if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
            } else {
                filename = filename.replace("$APPENDIX$", "");
            }
            fin = new FileInputStream(filename);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dict.dictionary = mapper.readValue(fin, dict.dictionary.getClass());
            fin.close();
        } catch (IOException e) {
            logger.error("Error while trying to read serialized dictionary from file", e);
        } finally {
            try {
                fin.close();
            } catch (Exception e) {
            }
        }
        return dict;
    }

    @Override
    public String translate(String word) {
        if (dictionary.containsKey(word)) {
            return dictionary.get(word);
        } else {
            logger.trace("Couldn't find a local translation for \"" + word + "\"");
            if (REQUEST_UNKNOWN_WORDS) {
                String uppercasedWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
                String translation = translater.getTranslation(uppercasedWord);
                if (translation != null) {
                    dictionary.put(word, translation);
                    localDictionaryChanged = true;
                }
                return translation;
            } else {
                return null;
            }
        }
    }

    private static class DictCcTranslater {
        private static final Logger logger = Logger.getLogger(DictionaryDictCC.class);

        private static final String URL = "http://de-en.dict.cc/";

        /**
         * Min time to wait after last connection
         */
        private static final long DELAY_BETWEEN_REQUESTS = 2000L;

        private HttpClient client;
        private HtmlCleaner cleaner = new HtmlCleaner();

        public DictCcTranslater() {
            client = new HttpClient();
        }

        /**
         * Timestamp of last connection
         */
        private long lastRequest = 0L;

        public String getTranslation(String word) {

            GetMethod get = new GetMethod(URL);
            get.setRequestHeader("Accept", "text/plain, text/html");// text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
            // get.setRequestHeader("Accept-Encoding", "gzip, deflate");
            get.setRequestHeader("Accept-Language", "de-de,de;q=0.8,en-us;q=0.5,en;q=0.3");
            get.setRequestHeader("Cache-Control", "max-age=0");
            // get.setRequestHeader("Connection", "keep-alive");
            get.setRequestHeader("Host", "de-en.dict.cc");
            // get.setRequestHeader("If-None-Match",
            // "\"7b6b8d4b5d400ba24b24519023f37e9b\"");
            get.setRequestHeader("User-Agent",
                    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:11.0) Gecko/20100101 Firefox/11.0");
            get.setQueryString(new NameValuePair[] { new NameValuePair("s", word) });
            try {
                Thread.sleep(Math.max(0, DELAY_BETWEEN_REQUESTS - (System.currentTimeMillis() - lastRequest)));
            } catch (InterruptedException e) {
            }
            int returnCode;
            try {
                returnCode = client.executeMethod(get);
            } catch (IOException e) {
                logger.error("Error while requesting translation of \"" + word + '"', e);
                get.releaseConnection();
                return null;
            }
            lastRequest = System.currentTimeMillis();

            String result = null;
            if (returnCode != HttpStatus.SC_OK) {
                logger.error("Got an unexpected HTTP status code (" + returnCode + ") for the word \"" + word + '"');
            } else {
                result = analysePage(get, word);
            }
            get.releaseConnection();
            return result;
        }

        private String analysePage(GetMethod get, String word) {
            try {
                TagNode page = cleaner.clean(get.getResponseBodyAsStream());
                // System.out.println(get.getResponseBodyAsString());

                Object trNodes[] = findTrNodesByHeadline(page, "Substantive");
                if (trNodes == null) {
                    trNodes = findTrNodesById(page);
                    if (trNodes == null) {
                        logger.error("Couldn't find a translation for \"" + word + '"');
                        return null;
                    }
                }

                String wordPair[];
                for (int i = 0; i < trNodes.length; ++i) {
                    wordPair = parseTrNode((TagNode) trNodes[i]);
                    if (word.equals(wordPair[0])) {
                        return wordPair[1];
                    }
                }
                logger.warn("Got no translation for the word \"" + word + "\".");
            } catch (IOException e) {
                logger.error("Error while parsing response from dict.cc.", e);
            } catch (XPatherException e) {
                logger.error(
                        "Error while evaluating the following XPath: \"/body/div[@id='maincontent']/table/tbody/tr[@id='tr1']\"",
                        e);
            }
            return null;
        }

        protected Object[] findTrNodesById(TagNode page) throws XPatherException {
            // Try to find the first result line
            Object nodes[] = page.evaluateXPath("/body/div[@id='maincontent']/table/tbody/tr[@id]");
            if (nodes.length == 0) {
                logger.warn("couldn't find tr tags containing results");
                return null;
            }
            return nodes;
        }

        protected Object[] findTrNodesByHeadline(TagNode page, String headline) throws XPatherException {
            // Try to find the headline
            Object temp[] = page.evaluateXPath("/body/div[@id='maincontent']/table/tbody/tr[/td[@class='td6']/b]");
            for (int i = 0; i < temp.length; ++i) {
                if (headline.equals(parseLine(((TagNode) temp[i]).evaluateXPath("data(/td[@class='td6']/b)")))) {
                    TagNode tr = (TagNode) temp[i];
                    TagNode tBody = tr.getParent();
                    return page.evaluateXPath("/body/div[@id='maincontent']/table/tbody/tr[position() > "
                            + tBody.getChildIndex(tr) + "][@id]");
                }
            }
            return null;
        }

        protected String[] parseTrNode(TagNode tr) throws XPatherException {
            Object temp[] = tr.evaluateXPath("data(/td[3]/a)");
            String wordPair[] = new String[2];
            if (temp.length > 0) {
                wordPair[0] = parseLine(temp);
            } else {
                return null;
            }
            temp = tr.evaluateXPath("data(/td[2]/a)");
            if (temp.length > 0) {
                wordPair[1] = parseLine(temp);
            } else {
                return null;
            }
            return wordPair;
        }

        protected String parseLine(Object line[]) {
            if (line.length > 0) {
                String word = line[0].toString();
                for (int i = 1; i < line.length; i++) {
                    word += " " + line[i];
                }
                return clearLine(word);
            } else {
                return "";
            }
        }

        protected String clearLine(String word) {
            // remove everything in parenthesis
            word = word.replaceAll("(\\[.*\\])", "");
            word = word.replaceAll("(\\(.*\\))", "");
            word = word.replaceAll("(\\{.*\\})", "");
            word = word.replaceAll("(\\{.*\\})", "");
            word = word.replaceAll("(&lt;.*\\&gt;)", "");
            word = word.trim();
            // remove lines or punctuations at the end of the word
            word = word.replaceAll("([-\\.!]$)", "");
            // remove special characters
            word = word.replaceAll("([®])", "");
            return word;
        }

        // protected String[] clearLine(String line[]) {
        // // if this is a comment, if it is to short, if this is not a noun,
        // // if
        // // this is a plural
        // if (line[0].startsWith("#") || (line.length < 3)
        // || (!(line[2].equals("noun"))) || line[0].contains("{pl}")) {
        // return null;
        // }
        // // remove everything in parenthesis
        // line[0] = line[0].replaceAll("(\\[.*\\])", "");
        // line[0] = line[0].replaceAll("(\\(.*\\))", "");
        // line[0] = line[0].replaceAll("(\\{.*\\})", "");
        // line[1] = line[1].replaceAll("(\\[.*\\])", "");
        // line[1] = line[1].replaceAll("(\\(.*\\))", "");
        //
        // line[0] = line[0].trim();
        // line[1] = line[1].trim();
        // // if the german part is more than one word
        // if (line[0].contains(" ")) {
        // return null;
        // }
        // return line;
        // }
    }

    @Override
    public Language getSourceLanguage() {
        return Language.GER;
    }

    @Override
    public Language getDestinationLanguage() {
        return Language.ENG;
    }
}
