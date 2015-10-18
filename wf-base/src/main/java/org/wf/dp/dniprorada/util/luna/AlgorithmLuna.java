package org.wf.dp.dniprorada.util.luna;

import org.apache.log4j.Logger;

import java.util.Random;

public class AlgorithmLuna {
    private static final Logger log = Logger.getLogger(AlgorithmLuna.class);

    private static int getLastDigit(Long inputNumber) {
        return (int) (inputNumber % 10);
    }

    private static int getCheckSumLastDigit(Long inputNumber) {
        int sum = sumDigitsByLuna(inputNumber);
        return sum % 10;//sumDigits(sum);
    }

    private static int sumDigitsByLuna(Long inputNumber) {
        int factor = 1;
        int sum = 0;
        int addend;
        while (inputNumber != 0) {
            addend = (int) (factor * (inputNumber % 10));
            factor = (factor == 2) ? 1 : 2;
            addend = addend > 9 ? addend - 9 : addend;
            sum += addend;
            inputNumber /= 10;
        }
        return sum;
    }

    // Calculate the sum of digits while it became one digit
    private static int sumDigits(int sum) {
        int result;
        do {
            result = 0;
            while (sum != 0) {
                result += sum % 10;
                sum /= 10;
            }
            sum = result;
        }
        while (result > 9);
        return result;
    }

    public static Long getProtectedNumber(Long inputNumber) {
        return getCheckSumLastDigit(inputNumber) + inputNumber * 10;
    }

    public static long getOriginalNumber(long protectedNumber) {
        return protectedNumber / 10;
    }

    public static boolean checkProtectedNumber(Long inputNumber) {
        long originalNumber = getOriginalNumber(inputNumber);

        log.info("inputNumber / 10=" + originalNumber);
        log.info("inputNumber=" + inputNumber);
        log.info("getLastDigit(inputNumber)=" + getLastDigit(inputNumber));
        log.info("getCheckSumLastDigit(inputNumber / 10)=" + getCheckSumLastDigit(originalNumber));
        return getCheckSumLastDigit(originalNumber) == getLastDigit(inputNumber);
    }

    public static void validateProtectedNumber(Long inputNumber) throws CRCInvalidException {
        if (!checkProtectedNumber(inputNumber)) {
            throw new CRCInvalidException();
        }
    }

    public static void validateProtectedNumber(Long inputNumber, String errorMessage) throws CRCInvalidException {
        if (!checkProtectedNumber(inputNumber)) {
            throw new CRCInvalidException(errorMessage);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int arrSize = 20;
        long[] testArray = new long[arrSize];
        long[] testProtectedArray = new long[arrSize];
        long currValue;
        System.out.println("Long.MAX_VALUE=" + Long.MAX_VALUE);
        for (int i = 0; i < arrSize; i++) {
            currValue = random.nextLong();
            while (currValue < 0 || currValue > Long.MAX_VALUE / 10)
                currValue = random.nextLong();
            testArray[i] = currValue;
            testProtectedArray[i] = getProtectedNumber(testArray[i]);
            System.out.println(">>test " + i
                    + ":   nID=" + testArray[i]
                    + ",   checkSum=" + getCheckSumLastDigit(testArray[i])
                    + ",   controlDigit=" + getCheckSumLastDigit(testArray[i] * 10)

                    + ",   nID_Protected=" + testProtectedArray[i]
                    + ",   checkResult=" + checkProtectedNumber(testProtectedArray[i]));
        }
    }

    public static String getProtectedString(Long inputNumber, Long nID_subject, String sID_status) {
         /*поле нИД генерируется автоматом
поле сИД --  генерируется (используя алг.Луна) на основе нИД И (если есть сИД_Статус) на основе поля сИД_Статус.
 еще в основу и вкладываем часть данных как логин и часть как пароль...
на самом деле все просто)
Вот калькулятор: http://planetcalc.ru/2464/
а) допустим у нас есть в nID число "12345678"
по алгоритму получаем:
Последний разряд контрольной суммы: 4
Следующая проверочная цифра: 2
б) так-же берем nID_Subject (как логин), например 16
по алгоритму получаем:
Последний разряд контрольной суммы: 8
Следующая проверочная цифра: 6
в) "собираем воедино все цифры":
12345678 & 4 & 2 & 16 & 8 & 6
т.е.: 12345678421686
г) превращаем это десятиричное число в 27+27+10=64-ричное, символьное из латинских больших(27) и маленьких(27) букв и цифр(10) 64-тиричная (0..9a..zA..Z)
получим что-то вроде G8hi3 (т.е. 5 символов)
д) остается еще 5 свободных (до 10-ти обозначенных в задании), для случаянно-сгенерированного пароля
например, 7Klg2
е) в итоге получаем уникальный sID="G8hi37Klg2", которы никогда не повторится, если nID и nID_Subject будут другими нежели в нашем случае (даже если когдато сгенерится такой-же пароль)
* */

        return "" + inputNumber;
    }
}
