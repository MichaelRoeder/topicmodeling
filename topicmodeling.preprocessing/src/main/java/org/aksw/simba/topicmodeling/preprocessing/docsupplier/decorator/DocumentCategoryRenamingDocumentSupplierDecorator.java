package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.HashSet;
import java.util.Set;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCategory;


public class DocumentCategoryRenamingDocumentSupplierDecorator extends
        AbstractPropertyEditingDocumentSupplierDecorator<DocumentCategory> {

    public static final String POSITIVE_CATEGORY_NAME = "positive";
    public static final String NEGATIVE_CATEGORY_NAME = "negative";

    private Set<String> positiveNames;

    public DocumentCategoryRenamingDocumentSupplierDecorator(DocumentSupplier documentSource,
            String positiveCategoryNames[]) {
        super(documentSource, DocumentCategory.class);
        positiveNames = new HashSet<String>(positiveCategoryNames.length);
        for (int i = 0; i < positiveCategoryNames.length; ++i) {
            positiveNames.add(positiveCategoryNames[i].toLowerCase());
        }
    }

    @Override
    protected void editDocumentProperty(DocumentCategory category) {
        if (positiveNames.contains(category.get().toLowerCase())) {
            category.set(POSITIVE_CATEGORY_NAME);
        } else {
            category.set(NEGATIVE_CATEGORY_NAME);
        }
    }
}
