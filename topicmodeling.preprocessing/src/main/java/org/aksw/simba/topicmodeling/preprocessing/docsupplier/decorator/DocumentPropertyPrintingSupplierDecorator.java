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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.io.File;
import java.io.IOException;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;

public class DocumentPropertyPrintingSupplierDecorator<T extends DocumentProperty> extends
        AbstractDocumentPropertyMapCreator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentPropertyPrintingSupplierDecorator.class);

    private final String OUTPUT_FILE;

    public DocumentPropertyPrintingSupplierDecorator(DocumentSupplier documentSource, Class<T> propertyClass,
            String outputFile) {
        super(documentSource, propertyClass);
        OUTPUT_FILE = outputFile;
    }

    @Override
    protected void mapCreated(IntObjectOpenHashMap<T> properties) {
        StringBuilder data = new StringBuilder();
        for (IntObjectCursor<T> cursor : properties) {
            data.append(cursor.key);
            data.append(',');
            data.append(cursor.value.toString());
            data.append('\n');
        }
        try {
            FileUtils.write(new File(OUTPUT_FILE), data);
        } catch (IOException e) {
            LOGGER.error("Couldn't print document property list.", e);
            e.printStackTrace();
        }
    }

}
