/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.aksw.simba.topicmodeling.utils.doc.StringContainingDocumentProperty;

public class StringContainingDocumentPropertyBasedFilter<T extends StringContainingDocumentProperty> extends
        AbstractDocumentPropertyBasedFilter<T> {

    public static enum StringContainingDocumentPropertyBasedFilterType {
        EQUALS,
        EQUALS_NOT,
        CONTAINS,
        CONTAINS_NOT,
        ENDS_WITH,
        ENDS_NOT_WITH,
        STARTS_WITH,
        STARTS_NOT_WITH,
        MATCHES_PATTERN,
        MATCHES_NOT_PATTERN
    }

    private final StringContainingDocumentPropertyBasedFilterType FILTER_TYPE;
    private String pattern;
    private final boolean IGNORE_CASE;

    public StringContainingDocumentPropertyBasedFilter(StringContainingDocumentPropertyBasedFilterType filterType,
            Class<T> propertyClass, String pattern) {
        this(filterType, propertyClass, pattern, false);
    }

    public StringContainingDocumentPropertyBasedFilter(StringContainingDocumentPropertyBasedFilterType filterType,
            Class<T> propertyClass, String pattern, boolean ignoreCase) {
        super(propertyClass);
        this.FILTER_TYPE = filterType;
        if (ignoreCase) {
            this.pattern = pattern.toLowerCase();
        } else {
            this.pattern = pattern;
        }
        this.IGNORE_CASE = ignoreCase;
    }

    @Override
    protected boolean isDocumentPropertyGood(T property) {
        String givenName = property.getStringValue();
        if (IGNORE_CASE) {
            givenName = givenName.toLowerCase();
        }
        switch (FILTER_TYPE) {
        case EQUALS: {
            return givenName.equals(pattern);
        }
        case EQUALS_NOT: {
            return !givenName.equals(pattern);
        }
        case CONTAINS: {
            return givenName.contains(pattern);
        }
        case CONTAINS_NOT: {
            return !givenName.contains(pattern);
        }
        case ENDS_WITH: {
            return givenName.endsWith(pattern);
        }
        case ENDS_NOT_WITH: {
            return !givenName.endsWith(pattern);
        }
        case STARTS_WITH: {
            return givenName.startsWith(pattern);
        }
        case STARTS_NOT_WITH: {
            return !givenName.startsWith(pattern);
        }
        case MATCHES_PATTERN: {
            return givenName.matches(pattern);
        }
        case MATCHES_NOT_PATTERN: {
            return !givenName.matches(pattern);
        }
        default: {
            throw new IllegalStateException("Unknown filter type " + FILTER_TYPE + "!");
        }
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public StringContainingDocumentPropertyBasedFilterType getFilterType() {
        return FILTER_TYPE;
    }

    public boolean isIgnoreCase() {
        return IGNORE_CASE;
    }
}
