package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.HashSet;
import java.util.Set;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;

import com.carrotsearch.hppc.IntArrayList;

public class UseNetTextExtractingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final String PGP_MESSAGE_START = "-----BEGIN PGP SIGNED MESSAGE-----";
    private static final String PGP_MESSAGE_END = "-----BEGIN PGP SIGNATURE-----";

    private static final Set<String> USE_NET_HEADING_KEYS = new HashSet<String>() {
        private static final long serialVersionUID = -3033226731898068935L;
        {
            add("article-i.d.");
            add("date");
            add("disclaimer");
            add("distribution");
            add("expires");
            add("followup-to");
            add("foolowup-to");
            add("from");
            add("important-info");
            add("in-reply-to");
            add("keywords");
            add("lines");
            add("message-id");
            add("news-software");
            add("newsgroups");
            add("nf-from");
            add("nf-id");
            add("nntp-posting-host");
            add("nntp-posting-user");
            add("organization");
            add("originator");
            add("path");
            add("references");
            add("reply-to");
            add("sender");
            add("subject");
            add("summary");
            add("supersedes");
            add("to");
            add("was subject");
            add("x-added");
            add("x-alt.reply-address");
            add("x-disclaimer");
            add("x-gated-by");
            add("x-header");
            add("x-mailer");
            add("x-md4-signature");
            add("x-newsreader");
            add("x-received");
            add("x-sender");
            add("x-sequence");
            add("x-telephone");
            add("x-to");
            add("x-useragent");
            add("x-us-mail");
            add("x-xxdate");
            add("x-xxmessage-id");
            add("xref");
            add("xuseragent");
            add("xxxdate");
        }
    };

    public UseNetTextExtractingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            throw new IllegalArgumentException("Got a document without the needed DocumentText property.");
        }
        String originalText = text.getText();

        String newText = identifyTextUsingPgpStatements(originalText);
        if (newText == null) {
            int startIndex = getTextStartIndex(originalText);
            // startIndex = removeAuthorLine(originalText, startIndex);
            newText = originalText.substring(startIndex, originalText.length());
            newText = removeAddresses(newText);
        }
        text.setText(newText);
        return document;
    }

    private int getTextStartIndex(String originalText) {
        int lineStartIndex, colonIndex, lineEndIndex = -1;
        do {
            lineStartIndex = lineEndIndex + 1;
            lineEndIndex = originalText.indexOf('\n', lineStartIndex);
            if (lineEndIndex < 0) {
                lineEndIndex = originalText.length();
            }
            colonIndex = originalText.indexOf(':', lineStartIndex);
            // while this line is empty or
            // there is a colon in the line
            // and the substring in front of the colon is a use net heading
            // and the end of the line is not the end of the text
        } while (((colonIndex > 0)
                && (USE_NET_HEADING_KEYS.contains(originalText.substring(lineStartIndex, colonIndex).toLowerCase()))
                && (lineEndIndex < originalText.length()))
                || ((lineStartIndex <= lineEndIndex) && (originalText.substring(lineStartIndex, lineEndIndex).trim()
                        .isEmpty())));

        if ((colonIndex > 0)
                && (USE_NET_HEADING_KEYS.contains(originalText.substring(lineStartIndex, colonIndex)))) {
            return originalText.length();
        }
        if (lineStartIndex > originalText.length()) {
            return originalText.length();
        } else {
            return lineStartIndex;
        }
    }

    @SuppressWarnings("unused")
    private int removeAuthorLine(String originalText, int lineStartIndex) {
        int lineEndIndex = originalText.indexOf('\n', lineStartIndex);
        int colonIndex = originalText.indexOf(':', lineStartIndex);
        if ((colonIndex > 0) && (colonIndex < lineEndIndex)) {
            String line = originalText.substring(lineStartIndex, colonIndex);
            if (line.endsWith("writes") || line.endsWith("wrote")) {
                return lineEndIndex + 1;
            }
        }
        return lineStartIndex;
    }

    private String removeAddresses(String originalText) {
        IntArrayList addressStartPositions = new IntArrayList();
        IntArrayList addressEndPositions = new IntArrayList();
        int addressStart = originalText.indexOf('@');
        int addressEnd;
        while (addressStart >= 0) {
            addressEnd = addressStart;
            // find the start
            while ((addressStart > 0) && (!Character.isWhitespace(originalText.charAt(addressStart - 1)))) {
                --addressStart;
            }
            // find the end
            while ((addressEnd < originalText.length()) && (!Character.isWhitespace(originalText.charAt(addressEnd)))) {
                ++addressEnd;
            }
            addressStartPositions.add(addressStart);
            addressEndPositions.add(addressEnd);
            addressStart = originalText.indexOf('@', addressEnd);
        }
        if (addressStartPositions.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder(originalText.length());
            stringBuilder.append(originalText.substring(0, addressStartPositions.get(0)));
            for (int i = 1; i < addressStartPositions.size(); ++i) {
                stringBuilder.append(originalText.substring(addressEndPositions.get(i - 1),
                        addressStartPositions.get(i)));
            }
            stringBuilder.append(originalText.substring(addressEndPositions.get(addressEndPositions.size() - 1)));
            return stringBuilder.toString();
        } else {
            return originalText;
        }
    }

    private String identifyTextUsingPgpStatements(String text) {
        String newText = null;
        int pgpMessageStart = text.indexOf(PGP_MESSAGE_START);
        if (pgpMessageStart > 0) {
            int pgpMessageEnd = text.indexOf(PGP_MESSAGE_END);
            if (pgpMessageEnd > 0) {
                newText = text.substring(pgpMessageStart + PGP_MESSAGE_START.length(), pgpMessageEnd);
            }
        }
        return newText;
    }

}
