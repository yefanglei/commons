package me.own.learn.commons.base.utils.price;

import java.math.BigDecimal;

/**
 * Created by CN-LEBOOK-YANGLI on 1/13/2017.
 */
public class PriceUtils {

    /***
     * Convert float value to decimal with specified bits behind point
     * @param value
     * @param numOfPoints the number of bits behind point
     * @return
     */
    public static float ConvertDecimalPoint(float value, int numOfPoints){
        if(numOfPoints < 0){
            return value;
        }

        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue();
    }

    /***
     * Convert double value to decimal with specified bits behind point
     * @param value
     * @param numOfPoints the number of bits behind point
     * @return
     */
    public static double ConvertDecimalPoint(double value, int numOfPoints){
        if(numOfPoints < 0){
            return value;
        }

        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /***
     * Eliminate the dot number of float value
     * @param number
     * @return
     */
    public static float MathRound(float number) {
        return (float)(Math.round(number * 100))/100;
    }

    /***
     * Eliminate the dot number of double
     * @param number
     * @return
     */
    public static double MathRound(double number) {
        return (double)(Math.round(number * 100))/100;
    }
}
