package org.dice_research.topicmodeling.wordnet;

import java.io.Serializable;

public enum WordNetRelatednessCalculationMethods implements Serializable {
    /**
     * Hirst and St-Onge (1998)<br>
     * Two lexicalized concepts are semantically close if their WordNet synsets are connected by a path that is not too
     * long and that "does not change direction too often".
     */
    HIRST_ST_ONGE,
    /**
     * Leacock and Chodorow (1998)<br>
     * Rely on the length of the shortest path between two synsets for their measure of similarity. They limit their
     * attention to IS-A links and scale the path length by the overall depth D of the taxonomy.
     */
    LEACOCK_CHODOROW,
    /**
     * Banerjee and Pedersen (2002)<br>
     * Lesk (1985) proposed that the relatedness of two words is proportional to to the extent of overlaps of their
     * dictionary definitions. Banerjee and Pedersen (2002) extended this notion to use WordNet as the dictionary for
     * the word definitions.
     */
    LESK_BANERJEE_PEDERSEN,
    /**
     * Wu and Palmer (1994)<br>
     * The Wu & Palmer measure calculates relatedness by considering the depths of the two synsets in the WordNet
     * taxonomies, along with the depth of the LCS.
     */
    WU_PALMER,
    /**
     * Like {@link #WU_PALMER}, but to the power of 10.
     */
    WU_PALMER_POW_10,
    /**
     * Resnik (1995)<br>
     * Resnik defined the similarity between two synsets to be the information content of their lowest super-ordinate
     * (most specific common subsumer)
     */
    RESNIK,
    /**
     * Jiang and Conrath<br>
     * (1997) Also uses the notion of information content, but in the form of the conditional probability of
     * encountering an instance of a child-synset given an instance of a parent synset: 1 / jcn_distance, where
     * jcn_distance is equal to IC(synset1) + IC(synset2) - 2 * IC(lcs).
     */
    JIANG_CONRATH,
    /**
     * A normalization of Jiang and Conrath<br>
     * Basically it uses the maximum distance for transforming the distance into a normalized similarity with
     * <code>sim = 1 - (dist / dist_max)</code>.
     */
    NORMALIZED_JIANG_CONRATH,
    /**
     * Lin (1998)<br>
     * Math equation is modified a little bit from Jiang and Conrath: 2 * IC(lcs) / (IC(synset1) + IC(synset2)). Where
     * IC(x) is the information content of x. One can observe, then, that the relatedness value will be greater-than or
     * equal-to zero and less-than or equal-to one.
     */
    LIN,
    /**
     * This method computes the semantic relatedness of word senses by counting the number of nodes along the shortest
     * path between the senses in the 'is-a' hierarchies of WordNet. The path lengths include the end nodes.
     * 
     * Since a longer path length indicate less relatedness, the relatedness value returned is the multiplicative
     * inverse of the path length (distance) between the two concepts: relatedness = 1 / distance. If the two concepts
     * are identical, then the distance between them is one; therefore, their relatedness is also 1. If no path is
     * found, then -1 is returned
     */
    PATH,
    /**
     * This method is based on {@link #WU_PALMER}, but takes care of the special case that two given words could be
     * inside of the same hierarchy.
     */
    TBK
}
