package com.asianaidt.ict.analyca.system.dockercore.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DockerOptionParser {
    private DockerOptionParser() {
    }

    public static List<String> parsing(final String str) {
        if (str == null) return Collections.emptyList();
        final String[] split = str.split("\\s+");
        return Arrays.stream(split).flatMap(s -> Arrays.stream(s.split("="))).collect(Collectors.toList());
    }
}
