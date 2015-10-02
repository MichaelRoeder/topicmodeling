/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.utils.corpus;

import java.util.Iterator;
import java.util.List;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentClassificationResult;


@Deprecated
public abstract class ClassifiedTestCorpus extends AbstractCorpusDecorator {

    private static final long serialVersionUID = 6321416556291722432L;

    protected ClassifiedTestCorpus(Corpus corpus) {
        super(corpus);
    }

    @SuppressWarnings("unchecked")
    public static ClassifiedTestCorpus createClassifiedTestCorpus(Corpus corpus) {
        if (corpus instanceof DocumentListCorpus) {
            return new CTCorpusForDocumentListCorpus(
                    (DocumentListCorpus<List<Document>>) DecoratedCorpusHelper.getUndecoratedCorpus(corpus));
        } else {
            return new CTCorpusForCorpusWithoutDocumentObjects(corpus);
        }
    }

    public abstract void addClassificationResult(int documentId,
            DocumentClassificationResult classification);

    public abstract DocumentClassificationResult getClassificationResult(
            int documentId);

    public abstract int getModelVersion();

    protected static class CTCorpusForDocumentListCorpus extends ClassifiedTestCorpus {
        private static final long serialVersionUID = 6753561814465682117L;

        protected DocumentListCorpus<List<Document>> corpus;

        protected CTCorpusForDocumentListCorpus(
                DocumentListCorpus<List<Document>> corpus) {
            super(corpus);
            this.corpus = corpus;
        }

        @Override
        public void addClassificationResult(int documentId,
                DocumentClassificationResult classification) {
            this.corpus.getDocument(documentId).addProperty(classification);
        }

        @Override
        public DocumentClassificationResult getClassificationResult(
                int documentId) {
            return this.corpus.getDocument(documentId).getProperty(
                    DocumentClassificationResult.class);
        }

        @Override
        public int getModelVersion() {
            DocumentClassificationResult classification;
            Iterator<Document> iterator = this.corpus.iterator();
            do {
                classification = iterator.next().getProperty(
                        DocumentClassificationResult.class);
            } while ((iterator.hasNext()) && (classification == null));

            if (classification != null) {
                return classification.getModelVersion();
            } else {
                return 0;
            }
        }
    }

    protected static class CTCorpusForCorpusWithoutDocumentObjects extends
            ClassifiedTestCorpus {
        private static final long serialVersionUID = -6878602863912950188L;

        private int modelVersion = 0;
        protected DocumentClassificationResult classifications[];

        protected CTCorpusForCorpusWithoutDocumentObjects(Corpus corpus) {
            super(corpus);
        }

        @Override
        public void addClassificationResult(int documentId,
                DocumentClassificationResult classification) {
            if (classifications == null) {
                classifications = new DocumentClassificationResult[corpus
                        .getNumberOfDocuments()];
            }
            classifications[documentId] = classification;

            if (classification.getModelVersion() > modelVersion) {
                modelVersion = classification.getModelVersion();
            }
        }

        @Override
        public DocumentClassificationResult getClassificationResult(
                int documentId) {
            if ((classifications == null)
                    || (documentId >= classifications.length)
                    || (documentId < 0)) {
                return null;
            } else {
                return classifications[documentId];
            }
        }

        @Override
        public int getModelVersion() {
            return modelVersion;
        }
    }
}
