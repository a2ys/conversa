package dev.a2ys.conversa.utils;

import java.time.LocalDateTime;

public class AgeCalculator {

    private final int currentYear;
    private final int currentMonth;
    private final int currentDayOfMonth;

    public AgeCalculator() {
        LocalDateTime now = LocalDateTime.now();
        currentYear = now.getYear();
        currentMonth = now.getMonthValue();
        currentDayOfMonth = now.getDayOfMonth();
    }

    public int getAge(int year, int month, int dayOfMonth) {
        int age = currentYear - year;

        if (currentMonth - month < 0 || (currentMonth == month && currentDayOfMonth - dayOfMonth < 0)) {
            age--;
        }

        return age;
    }
}
