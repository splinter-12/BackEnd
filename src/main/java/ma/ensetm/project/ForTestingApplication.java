package ma.ensetm.project;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ForTestingApplication {
    public static void main(String[] args) {
        System.out.println(keep3Digits("15.99"));
    }

    public static String keep3Digits(String mark) {
        float grade = Float.parseFloat(mark);
        BigDecimal bigDecimal = new BigDecimal(grade);
        BigDecimal decimal = bigDecimal.setScale(3, RoundingMode.DOWN);
        return String.valueOf(decimal.floatValue());
    }
}
