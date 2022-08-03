package com.wx.demo.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

    private static DecimalFormat oneDecimalsFormat = new DecimalFormat("0.0");
    private static DecimalFormat twoDecimalsFormat = new DecimalFormat("0.00");
    private static DecimalFormat fiveDecimalsFormat = new DecimalFormat("0.00000");

    public static String reserveOneDecimals(double value) {
        return reserveDecimals(oneDecimalsFormat, value);
    }

    public static String reserveTwoDecimals(double value) {
        return reserveDecimals(twoDecimalsFormat, value);
    }

    public static String reserveFiveDecimals(double value) {
        return reserveDecimals(fiveDecimalsFormat, value);
    }

    private static String reserveDecimals(DecimalFormat format, double value) {
        String result = format.format(value);
        //为解决俄语下符号问题，将 "," 转化为 "."
        return result.replace(",", ".");
    }

    public static String subZeroAndDot(double number) {
        return subZeroAndDot(String.valueOf(number));
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    private static int getMiddle(double[] numbers, int low, int high) {
        double temp = numbers[low];
        while (low < high) {
            while (low < high && numbers[high] > temp) {
                high--;
            }
            numbers[low] = numbers[high];
            while (low < high && numbers[low] < temp) {
                low++;
            }
            numbers[high] = numbers[low];
        }
        numbers[low] = temp;
        return low;
    }

    private static void quick(double[] numbers, int low, int high) {
        if (low < high) {
            int middle = getMiddle(numbers, low, high);
            quick(numbers, low, middle - 1);
            quick(numbers, middle + 1, high);
        }
    }

    public static void quickAscSort(double[] numbers) {
        if (numbers.length > 0) {
            quick(numbers, 0, numbers.length - 1);
        }
    }

    /**
     * 按需保留小数位数
     *
     * @param number       数值
     * @param saveCount    保留小数的个数
     * @param roundingMode RoundingMode.CEILING -> 向上， RoundingMode.FLOOR -> 向下
     * @return 保留后的文本
     */
    public static String reserveDecimals(double number, int saveCount, RoundingMode roundingMode) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(saveCount);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(roundingMode);
        String format = decimalFormat.format(number);
        //为解决俄语下符号问题，将 "," 转化为 "."
        return format.replace(",", ".");
    }

}
