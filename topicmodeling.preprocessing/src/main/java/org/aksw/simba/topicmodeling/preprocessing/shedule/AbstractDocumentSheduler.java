package org.aksw.simba.topicmodeling.preprocessing.shedule;

import java.util.LinkedList;
import java.util.Queue;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public abstract class AbstractDocumentSheduler implements DocumentSheduler {

	protected DocumentSupplier documentSource;
	protected PartialDocumentSupplier listOfParts[];
	
	public AbstractDocumentSheduler(DocumentSupplier documentSource,
			int numberOfParts) {
		this.documentSource = documentSource;
		listOfParts = new PartialDocumentSupplier[numberOfParts];
		for (int i = 0; i < numberOfParts; ++i) {
			listOfParts[i] = new PartialDocumentSupplier(i);
		}
	}

	protected abstract Document getNextDocument(int partId);

	@Override
	public int getNumberOfParts() {
		return listOfParts.length;
	}

	@Override
	public PartialDocumentSupplier getPartialDocumentSupplier(int partId) {
		return listOfParts[partId];
	}
	
	public class PartialDocumentSupplier extends AbstractDocumentSupplier {
		protected Queue<Document> queue = new LinkedList<Document>();
		protected int id;

		public PartialDocumentSupplier(int id) {
			this.id = id;
		}

		@Override
		public Document getNextDocument() {
			Document document = null;
			if (queue.size() > 0) {
				document = queue.poll();
			} else {
				document = AbstractDocumentSheduler.this.getNextDocument(id);
			}
			if(document != null)
			{
				document.setDocumentId(this.getNextDocumentId());
			}
			return document;
		}

		public int getId() {
			return id;
		}

		public void addDocumentToQueue(Document document) {
			queue.offer(document);
		}
	}
}
