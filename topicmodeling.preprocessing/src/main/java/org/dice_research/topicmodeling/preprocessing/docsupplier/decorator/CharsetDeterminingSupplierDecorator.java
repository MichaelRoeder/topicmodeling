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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.nio.charset.Charset;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCharset;
import org.dice_research.topicmodeling.utils.doc.DocumentRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class CharsetDeterminingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharsetDeterminingSupplierDecorator.class);

    public CharsetDeterminingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentRawData data = document.getProperty(DocumentRawData.class);
        if (data == null) {
            LOGGER.error("Got a document (#" + document.getDocumentId()
                    + ") without the needed DocumentRawData property. Ignoring it.");
        } else {
            Charset charset = detectCharset(data.getData());
            if (charset != null) {
                document.addProperty(new DocumentCharset(charset));
            }
        }
        return document;
    }

    private Charset detectCharset(byte[] data) {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        try {
            return Charset.forName(match.getName());
        } catch (Exception e) {
            LOGGER.warn("Couldn't determine the charset of the given data. Returning null.");
        }
        return null;
    }
}
