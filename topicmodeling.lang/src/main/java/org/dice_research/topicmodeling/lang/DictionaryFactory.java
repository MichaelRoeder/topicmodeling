/**
 * This file is part of topicmodeling.lang.
 *
 * topicmodeling.lang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.lang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.lang.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.lang;

import org.apache.commons.lang3.NotImplementedException;
import org.dice_research.topicmodeling.lang.Language;

public class DictionaryFactory {

    public static Dictionary getDictionary(Language fromLanguage, Language toLanguage) {
        switch (fromLanguage) {
        case GER: {
            switch (toLanguage) {
            case GER:
                return new NoTranslationDictionary(fromLanguage);
            case ENG:
                return DictionaryDictCC.getInstance();
            default:
                break;
            }
        }
        case ENG: {
            switch (toLanguage) {
            case GER:
                break;
            case ENG:
                return new NoTranslationDictionary(fromLanguage);
            default:
                break;
            }
        }
        default:
            break;
        }
        throw new NotImplementedException("There is no dictionary for translations from " + fromLanguage.toString()
                + " to " + toLanguage.toString());
    }
}
