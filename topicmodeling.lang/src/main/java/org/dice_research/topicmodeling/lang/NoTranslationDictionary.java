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

import org.dice_research.topicmodeling.lang.Language;

public class NoTranslationDictionary implements Dictionary {

    private Language language;

    public NoTranslationDictionary(Language language) {
    }

    @Override
    public String translate(String word) {
        return word;
    }

    @Override
    public void saveAsObjectFile() {
        /* nothing to do */
    }

    @Override
    public Language getSourceLanguage() {
        return language;
    }

    @Override
    public Language getDestinationLanguage() {
        return language;
    }

}
