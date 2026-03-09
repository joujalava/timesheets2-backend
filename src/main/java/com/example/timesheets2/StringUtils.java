package com.example.timesheets2;

import java.util.Optional;

public class StringUtils {

    public static boolean isBlank(String str) {
        return Optional.ofNullable(str).orElse("").isBlank();
    }

}
