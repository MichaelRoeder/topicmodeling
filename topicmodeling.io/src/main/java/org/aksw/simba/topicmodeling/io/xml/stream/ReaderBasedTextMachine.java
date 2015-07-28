package org.aksw.simba.topicmodeling.io.xml.stream;

import java.io.Reader;

public interface ReaderBasedTextMachine {

    public void analyze(Reader reader, ReaderBasedTextMachineObserver observer);
    
    public void stop();
}
