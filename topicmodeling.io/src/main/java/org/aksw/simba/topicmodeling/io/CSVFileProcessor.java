package org.aksw.simba.topicmodeling.io;

import au.com.bytecode.opencsv.CSVWriter;

public interface CSVFileProcessor {

    public static final char SEPARATOR = ';';
    public static final char QUOTECHAR = CSVWriter.NO_QUOTE_CHARACTER;
    public static final char ESCAPECHAR = CSVWriter.NO_ESCAPE_CHARACTER;
}
