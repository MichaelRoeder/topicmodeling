package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class EntityTermMapping {

    public List<NamedEntityInText> entities = new ArrayList<NamedEntityInText>();
    public List<Term[]> terms = new ArrayList<Term[]>();
}
