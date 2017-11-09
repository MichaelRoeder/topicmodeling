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
package org.dice_research.topicmodeling.utils.doc;

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
