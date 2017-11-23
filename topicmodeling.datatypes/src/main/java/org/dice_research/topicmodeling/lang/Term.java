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
package org.dice_research.topicmodeling.lang;

import java.io.Serializable;

public class Term implements Serializable {

    private static final long serialVersionUID = 1L;

    // The attribute names are kept short for better serialization
    /**
     * The original word the term is representing.
     */
    public String w;
    /**
     * The lemma of the word.
     */
    public String l;
    /**
     * The POS tag of the word.
     */
    public String p;
    /**
     * The properties of the word.
     */
    public TermProperties prop = new TermProperties();

    public Term(String wordForm) {
        this.w = wordForm;
        this.l = wordForm.toLowerCase();
    }

    public Term(String wordForm, String lemma) {
        this.w = wordForm;
        this.l = lemma;
    }

    public Term(String wordForm, String lemma, String posTag) {
        this.w = wordForm;
        this.l = lemma;
        this.p = posTag;
    }

    public String getWordForm() {
        return w;
    }

    public void setWordForm(String term) {
        this.w = term;
    }

    public String getLemma() {
        return l;
    }

    public void setLemma(String lemma) {
        this.l = lemma;
    }

    public String getPosTag() {
        return p;
    }

    public void setPosTag(String posTag) {
        this.p = posTag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((l == null) ? 0 : l.hashCode());
        result = prime * result + ((p == null) ? 0 : p.hashCode());
        result = prime * result + ((w == null) ? 0 : w.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Term other = (Term) obj;
        if (l == null) {
            if (other.l != null)
                return false;
        } else if (!l.equals(other.l))
            return false;
        if (p == null) {
            if (other.p != null)
                return false;
        } else if (!p.equals(other.p))
            return false;
        if (w == null) {
            if (other.w != null)
                return false;
        } else if (!w.equals(other.w))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Term (term=");
        builder.append(w);
        builder.append(", lemma=");
        builder.append(l);
        builder.append(", posTag=");
        builder.append(p);
        builder.append(")");
        return builder.toString();
    }

}
