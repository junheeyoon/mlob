package com.asianaidt.ict.analyca.system.dockercore.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum UnitGroup {
    B("B", 1, Arrays.asList("B")),
    KiB("KiB", B.size * 1024, Arrays.asList("KIB", "KB")),
    MiB("MiB", KiB.size * 1024, Arrays.asList("MIB", "MB")),
    GiB("GiB", MiB.size * 1024, Arrays.asList("GIB", "GB")),
    TiB("TiB", GiB.size * 1024, Arrays.asList("TIB", "TB")),
    NONE("None", 0, Collections.emptyList());

    private final String name;
    private final long size;
    private final List<String> unitList;

    UnitGroup(String name, long size, List<String> unitList) {
        this.name = name;
        this.size = size;
        this.unitList = unitList;
    }

    public static UnitGroup getUnit(final String unit) {
        return Arrays.stream(UnitGroup.values())
                .filter(unitGroup -> unitGroup.hasUnitName(unit))
                .findAny()
                .orElse(NONE);
    }

    public long size() {
        return size;
    }

    public boolean hasUnitName(String name) {
        return unitList.stream().anyMatch(unit -> unit.equals(name));
    }
}
