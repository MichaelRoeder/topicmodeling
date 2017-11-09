package org.dice_research.topicmodeling.textmachine;

import org.apache.commons.lang3.StringEscapeUtils;

public class SimpleWikipediaMarkupDeletingMachine implements SimpleTextMachineObserver {

	/*
	 * Grammar:
	 * 
	 * 1 "\\{\\{[^\\}]\\}\\}"
	 * 
	 * 2 "\\[\\[[^\\]\\|]\\]\\]"
	 * 
	 * 3 "\\[\\[[^\\]\\|]\\|"
	 * 
	 * 4 "\\]{1,2}"
	 * 
	 * 5 "\\[[^\\] ]\\]"
	 * 
	 * 6 "\\[[^\\] ] "
	 * 
	 * 7 "<!--"
	 * 
	 * 8 "-->"
	 * 
	 * 9 "<[^>]*>"
	 * 
	 * 10 "</[^>]*>"
	 * 
	 * 11 "[=]{2,6}"
	 * 
	 * 12 "[']{2,5}"
	 * 
	 * 13 "\\n[*#;:]+"
	 * 
	 * 14 "\\n\\{\\|[^\\n]*\\n"
	 * 
	 * 15 "[\\n\\|][\\|\\!][^\\|\\n]*\\|"
	 * 
	 * 16 "[\\n\\|][\\|\\!][^\\|\\n]*\\n"
	 * 
	 * 17 "\\n\\|-[^\\n]*\\n"
     * 
     * 18 "\\n\\|[^\\}-]" --> deprecated!
	 * 
	 * 19 "\\n\\|\\}"
	 * 
	 * 20 "&[^;]*;"
     * 
     * 21 "\\}\\}"
	 */

	private SimpleTextMachine textMachine;
	private String originalText;
	private StringBuilder cleanText;
	private int pos;

	public SimpleWikipediaMarkupDeletingMachine() {
		textMachine = new SimpleWikipediaMarkupDetectingMachine();
		textMachine.registerObserver(this);
	}

	@Override
	public void foundPattern(int patternId, int startPos, int endPos) {
		// append the text between this and the last pattern
		if (startPos > pos) {
			cleanText.append(originalText.substring(pos, startPos));
		}

		switch (patternId) {
		case 2: {
			cleanText.append(originalText.substring(startPos + 2, endPos - 1));
			break;
		}
		case 5: {
			cleanText.append(originalText.substring(startPos + 1, endPos));
			break;
		}
		case 13:
		case 14:
		case 15:
		case 16:
		case 17: {
			cleanText.append('\n');
			break;
		}
		case 20: {
			cleanText.append(StringEscapeUtils.unescapeHtml4(originalText
					.substring(startPos, endPos + 1)));
			break;
		}
		}
		pos = endPos + 1;
	}
	
	@Deprecated
	public String getCleanText_old(String text) {
		originalText = text;
		if (originalText.startsWith("#")) {
			String temp = originalText.substring(0,
					originalText.length() < 14 ? originalText.length() : 14);
			temp = temp.toLowerCase();
			if (temp.startsWith("#redirect")) {
				pos = 9;
			} else if (temp.startsWith("#weiterleitung")) {
				pos = 14;
			}
		} else {
			pos = 0;
		}
		cleanText = new StringBuilder();

		textMachine.analyze(text);
		// append the rest of the text
		if (pos < text.length()) {
			cleanText.append(text.substring(pos));
		}

		String result = cleanText.toString();
		originalText = null;
		cleanText = null;
		return result;
	}
}
