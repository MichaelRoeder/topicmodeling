package org.aksw.simba.topicmodeling.algorithms;

import java.io.Serializable;

public interface Model extends Serializable {

	public void setVersion(int version);

	public int getVersion();
}
