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
package org.aksw.simba.topicmodeling.utils.vocabulary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hppc.HppcModule;

public class DecoratingMultipleSpellingVocabulary extends AbstractVocabularyDecorator implements
        MultipleSpellingVocabulary, Externalizable {

    private static final long serialVersionUID = 2678926896079867964L;

    private IntObjectOpenHashMap<Set<String>> spellings = new IntObjectOpenHashMap<Set<String>>();

    public DecoratingMultipleSpellingVocabulary() {
    }

    public DecoratingMultipleSpellingVocabulary(Vocabulary decoratedVocabulary) {
        super(decoratedVocabulary);
    }

    @Override
    public Set<String> getSpellingsForWord(int wordId) {
        if (spellings.containsKey(wordId)) {
            return spellings.get(wordId);
        } else {
            return new HashSet<String>();
        }
    }

    @Override
    public void addSpelling(int wordId, String spelling) {
        Set<String> spellingsSet;
        if (spellings.containsKey(wordId)) {
            spellingsSet = spellings.get(wordId);
        } else {
            spellingsSet = new HashSet<String>();
            spellings.put(wordId, spellingsSet);
        }
        spellingsSet.add(spelling);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        int length = in.readInt();
        byte buffer[] = new byte[length];
        in.readFully(buffer);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HppcModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        IntObjectOpenHashMap<Collection<String>> loadedHashMap = mapper.readValue(new ByteArrayInputStream(buffer),
                IntObjectOpenHashMap.class);

        spellings = new IntObjectOpenHashMap<Set<String>>();
        Iterator<IntObjectCursor<Collection<String>>> iterator = loadedHashMap.iterator();
        IntObjectCursor<Collection<String>> cursor;
        while (iterator.hasNext()) {
            cursor = iterator.next();
            if (cursor.value instanceof Set<?>) {
                spellings.put(cursor.key, (Set<String>) cursor.value);
            } else {
                spellings.put(cursor.key, new HashSet<String>((Collection<String>) cursor.value));
            }
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HppcModule());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, spellings);
        byte buffer[] = outputStream.toByteArray();
        out.writeInt(buffer.length);
        out.write(buffer);
    }

}
