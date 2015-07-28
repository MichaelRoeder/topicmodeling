package org.aksw.simba.topicmodeling.lang;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.carrotsearch.hppc.BitSet;

public class TermProperties implements Externalizable {

    private static final long serialVersionUID = 1L;

    private static final int NOUN_INDEX = 0;
    private static final int VERB_INDEX = 1;
    private static final int ADJECTIVE_INDEX = 2;
    private static final int NUMBER_INDEX = 3;

    private BitSet properties = new BitSet(4);

    public void setNoun(boolean flag) {
        if (flag) {
            properties.set(NOUN_INDEX);
        } else {
            properties.clear(NOUN_INDEX);
        }
    }

    public boolean isNoun() {
        return properties.get(NOUN_INDEX);
    }

    public void setVerb(boolean flag) {
        if (flag) {
            properties.set(VERB_INDEX);
        } else {
            properties.clear(VERB_INDEX);
        }
    }

    public boolean isVerb() {
        return properties.get(VERB_INDEX);
    }

    public void setAdjective(boolean flag) {
        if (flag) {
            properties.set(ADJECTIVE_INDEX);
        } else {
            properties.clear(ADJECTIVE_INDEX);
        }
    }

    public boolean isAdjective() {
        return properties.get(ADJECTIVE_INDEX);
    }

    public void setNumber(boolean flag) {
        if (flag) {
            properties.set(NUMBER_INDEX);
        } else {
            properties.clear(NUMBER_INDEX);
        }
    }

    public boolean isNumber() {
        return properties.get(NUMBER_INDEX);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("prop(");
        if (isNoun()) {
            builder.append("-,");
        } else {
            builder.append("n,");
        }
        if (isVerb()) {
            builder.append("-,");
        } else {
            builder.append("v,");
        }
        if (isAdjective()) {
            builder.append("-,");
        } else {
            builder.append("a,");
        }
        if (isNumber()) {
            builder.append("-)");
        } else {
            builder.append("nr)");
        }
        return builder.toString();
    }

    public long getAsLong() {
        return properties.bits[0];
    }

    public void set(long bits) {
        properties.bits[0] = bits;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        properties.bits[0] = in.readLong();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(properties.bits[0]);
    }
}
