package dev.a2ys.conversa.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class AgeCalculator {
    public int getAge(int year, int month, int dayOfMonth) {
        int age = getDate()[0] - year;

        if (getDate()[1] - month < 0) {
            age--;
        } else if (getDate()[1] == month) {
            if (getDate()[2] - dayOfMonth < 0) {
                age--;
            }
        }

        return age;
    }

    private int[] getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = dtf.format(now).split(" ")[0];

        String[] dateTimeStringList = dateTimeString.split("/");

        return new int[]{Integer.parseInt(dateTimeStringList[0]), Integer.parseInt(dateTimeStringList[1]), Integer.parseInt(dateTimeStringList[2])};
    }
}
