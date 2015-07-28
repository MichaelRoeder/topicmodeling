package org.aksw.simba.topicmodeling.io;

import org.aksw.simba.topicmodeling.algorithms.Model;

public interface ModelWriter {

	public void writeModelToFiles(Model model);
	
//	public void writeModelToFiles(Model model, Corpus corpus);
}
