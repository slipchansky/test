package com.upsic.kkc.utils;


import java.util.Comparator;

import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;

public class SortingUtils {

    private final static String FOLDER = "FOLDER";

    public final static Comparator<Sk11TreeNodeDto> SK11_TREE_NODE_SORTING = Comparator
            .comparing(Sk11TreeNodeDto::getObjectType, (s1, s2) -> {
                if (FOLDER.equalsIgnoreCase(s1) && !FOLDER.equalsIgnoreCase(s2)) {
                    return -1;
                }
                if (!FOLDER.equalsIgnoreCase(s1) && FOLDER.equalsIgnoreCase(s2)) {
                    return 1;
                }
                return s1.compareTo(s2);
            })
            .thenComparing(Sk11TreeNodeDto::getName);
}
