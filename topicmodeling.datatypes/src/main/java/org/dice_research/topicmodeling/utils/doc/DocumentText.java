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

/**
 * Class DocumentText
 */
public class DocumentText extends AbstractDocumentProperty
        implements ParseableDocumentProperty, StringContainingDocumentProperty {

    private static final long serialVersionUID = -7559209221259716397L;

    protected String text;

    public DocumentText() {
    }

    public DocumentText(String text) {
        this.text = text;
    }

    /**
     * Set the value of text
     * 
     * @param text the new value of text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the value of text
     * 
     * @return the value of text
     */
    public String getText() {
        return text;
    }

    @Override
    public Object getValue() {
        return getText();
    }

    @Override
    public void parseValue(String value) {
        this.text = value;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (arg0 instanceof DocumentText) {
            if (this.text == null) {
                return ((DocumentText) arg0).text == null;
            } else {
                return this.text.equals(((DocumentText) arg0).text);
            }
        } else {
            return super.equals(arg0);
        }
    }

    @Override
    public String getStringValue() {
        return text;
    }
}
