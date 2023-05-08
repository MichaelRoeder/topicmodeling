package org.dice_research.topicmodeling.utils.doc;

/**
 * Utility methods to ease the work with {@link DocumentProperty} instances.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class DocumentPropertyUtils {

    /**
     * This method either returns the value that the given {@link DocumentProperty}
     * has for the given document, or {@code null} if the given document does not
     * have this property.
     * 
     * @param document the document that should contain a value for the given
     *                 document property
     * @param clazz    the property for which the value should be returned
     * @return the value of the property or {@code null} if the given document does
     *         not have this property
     */
    public static Object getValueOrNull(Document document, Class<? extends DocumentProperty> clazz) {
        DocumentProperty property = document.getProperty(clazz);
        if (property == null) {
            return null;
        } else {
            return property.getValue();
        }
    }

    /**
     * This method either returns the String value that the given
     * {@link StringContainingDocumentProperty} has for the given document, or
     * {@code null} if the given document does not have this property.
     * 
     * @param document the document that should contain a value for the given
     *                 document property
     * @param clazz    the property for which the value should be returned
     * @return the String value of the property or {@code null} if the given
     *         document does not have this property
     */
    public static String getStringOrNull(Document document, Class<? extends StringContainingDocumentProperty> clazz) {
        StringContainingDocumentProperty property = document.getProperty(clazz);
        if (property == null) {
            return null;
        } else {
            return property.getStringValue();
        }
    }

}
