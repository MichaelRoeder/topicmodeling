package org.dice_research.topicmodeling.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dice_research.topicmodeling.collections.SimpleStack;
import org.junit.Assert;
import org.junit.Test;

public class SimpleStackTest {

    private Random rand = new Random();

    @Test
    public void test() {
        List<Integer> testObjects = new ArrayList<Integer>();
        for (int i = 0; i < 200; ++i) {
            testObjects.add(rand.nextInt());
        }

        SimpleStack<Integer> stack = new SimpleStack<Integer>();

        // test add(E), get() and get(int)
        for (int i = 0; i < testObjects.size(); ++i) {
            stack.add(testObjects.get(i));
            Assert.assertEquals(testObjects.get(i), stack.get());
            for (int j = 0, k = i; j <= i; ++j, --k) {
                Assert.assertEquals(testObjects.get(j), stack.get(k));
            }
        }

        // test SimpleStack<E>(Collection<E>)
        SimpleStack<Integer> stack2 = new SimpleStack<Integer>(stack);
        for (int i = 0; i < testObjects.size(); ++i) {
            Assert.assertEquals(stack.get(i), stack2.get(i));
        }

        // test remove()
        for (int i = testObjects.size() - 2; i >= 0; --i) {
            stack.remove();
            Assert.assertEquals(stack.get(), testObjects.get(i));
        }
        Assert.assertEquals(1, stack.size());
        stack.remove();
        Assert.assertEquals(0, stack.size());
    }
}
