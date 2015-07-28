package org.aksw.simba.topicmodeling.utils.doc.ner;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityCandidate;


public class NamedEntityCandidatesInDocument implements DocumentProperty {

    private static final long serialVersionUID = -2029263266533563292L;

    private List<NamedEntityCandidate> namedEntities;

    public NamedEntityCandidatesInDocument() {
        this(new ArrayList<NamedEntityCandidate>());
    }

    public NamedEntityCandidatesInDocument(List<NamedEntityCandidate> namedEntities) {
        this.namedEntities = namedEntities;
    }

    @Override
    public Object getValue() {
        return namedEntities;
    }

    public List<NamedEntityCandidate> getNamedEntities() {
        return namedEntities;
    }

    public void setNamedEntities(List<NamedEntityCandidate> namedEntities) {
        this.namedEntities = namedEntities;
    }

    public void addNamedEntity(NamedEntityCandidate namedEntity) {
        this.namedEntities.add(namedEntity);
    }
}