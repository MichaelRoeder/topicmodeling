package org.aksw.simba.topicmodeling.utils.doc;

import java.util.Arrays;

public abstract class AbstractStringArrayContainingDocumentProperty extends AbstractArrayContainingDocumentProperty
		implements StringArrayContainingDocumentProperty {

	private static final long serialVersionUID = -1L;

	@Override
	public Object[] getValueAsArray() {
		return getValueAsStrings();
	}

	@Override
	public void parseValue(String value) {
		if (value.startsWith("[")) {
			value = value.substring(1);
		}
		if (value.endsWith("]")) {
			value = value.substring(0, value.length() - 1);
		}
		set(value.split(","));
	}

	@Override
	public String getStringValue() {
		return Arrays.toString(getValueAsStrings());
	}
}
