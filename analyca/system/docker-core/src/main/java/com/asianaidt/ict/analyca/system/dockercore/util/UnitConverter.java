package com.asianaidt.ict.analyca.system.dockercore.util;

import java.util.Arrays;

public class UnitConverter {
    private UnitConverter() {
    }

    /**
     * 소수점과 문자가 섞여있는 문자열에서 소수점만 추출하는 함수.
     * 제외되는 문자는 다음과 같다.
     * - 영어 : 소문자, 대문자
     * - 특수문자 : ':', '%'
     *
     * @param value 소수점과 문자가 섞인 문자열
     * @return 추출 성공 시 소수점, 실패 시 0
     */
    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[\\s+a-zA-Z :%]", ""));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return 0.0;
    }

    /**
     * 단위가 포함된 저장용량을 바이트 단위로 계산해서 반환하는 함수.
     *
     * @param value 단위가 포함 된 용량 (64Kb, 128.2MiB 등)
     * @return 성공 시 바이트로 계산된 값, 실패 시 0을 반환.
     * 실패 조건은 용량이 0이거나 단위가 맞지 않을 때. (MB, MiB와 같은 SI, Binary 타입만 제공)
     */
    public static double toByte(String value) {
        double number = toDouble(value);
        String unit = value.replace(String.valueOf(number), "").toUpperCase();
        UnitGroup unitGroup = UnitGroup.getUnit(unit);
        if (number == 0 || unitGroup == UnitGroup.NONE) return 0;
        return number * unitGroup.size();
    }

    public static double[] toByte(String value, String delimiter) {
        return Arrays.stream(value.replaceAll("\\s", "").split(delimiter))
                .mapToDouble(UnitConverter::toByte)
                .toArray();
    }
}