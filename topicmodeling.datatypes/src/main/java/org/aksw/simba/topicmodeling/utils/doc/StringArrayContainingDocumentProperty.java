package org.aksw.simba.topicmodeling.utils.doc;

public interface StringArrayContainingDocumentProperty extends ArrayContainingDocumentProperty,
		ParseableDocumentProperty, StringContainingDocumentProperty {

	public void set(String values[]);
	
	public String[] getValueAsStrings();
}
