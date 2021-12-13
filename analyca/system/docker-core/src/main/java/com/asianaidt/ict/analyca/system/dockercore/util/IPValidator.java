package com.asianaidt.ict.analyca.system.dockercore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPValidator {
    private IPValidator() {
    }

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    public static boolean validate(final String ip) {
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
