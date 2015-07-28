package org.aksw.simba.topicmodeling.io;

import java.io.File;


public abstract class AbstractModelFilesWriter implements ModelWriter {
	
	protected File folder;
	
	public AbstractModelFilesWriter(File folder) {
		this.folder = folder;
	}

	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}
}
