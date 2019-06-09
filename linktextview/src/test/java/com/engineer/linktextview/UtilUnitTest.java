package com.engineer.linktextview;

import com.engineer.linktextview.internal.Util;
import kotlin.Pair;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author: zhuyongging
 * @since: 2019-06-09
 */
public class UtilUnitTest {

    @Test
    public void mappingTest() {
        ArrayList<String> list = new ArrayList<>();
        list.add("B");
        list.add("E");
        list.add("F");
        list.add("G");
        ArrayList<Pair<Integer, Integer>> result = Util.INSTANCE.mappingLink("ABCDEFG", list);


        int first = result.get(0).getFirst();
        int second = result.get(0).getSecond();
        assertEquals(first, 1);
        assertEquals(second, 2);

    }

    @Test
    public void mappingStrTest() {
        Pair<Integer, Integer> pair = Util.INSTANCE.
                mappingStrLink("/Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home/bin/java",
                        "Java");
        int first = pair.getFirst();
        int second = pair.getSecond();
        assertEquals(first, 9);
        assertEquals(second, 12);
    }
}
