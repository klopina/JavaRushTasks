package com.javarush.task.task20.task2025;


import java.util.*;

/* 
Алгоритмы-числа
Необходимо найти все числа Армстронга в диапазоне не превышаещем Long.MAX_VALUE

Ограничение по врмени - 10 с.
Ограничение по памяти - 50 Мб.

MacBook Pro (Retina, 13-inch, Early 2013)
2,6 GHz 2‑ядерный процессор IntelCore i5

Результат:
memory 4757
time = 7
*/

public class Solution {

    // Массив со степенями чисел от 0 до 9, чтобы не вычислять каждый раз
    public static long[][] pows = new long[10][20];
    static {
        for (int i = 0; i < pows.length; i++) {
            for (int j = 0; j < pows[i].length; j++) {
                pows[i][j] = binpow(i, j);
            }
        }
    }
    // Функция получения количества цифр в заданном числе
    public static int getCountsOfDigits(long number) {
        return(number == 0) ? 1 : (int) Math.ceil(Math.log10(Math.abs(number) + 0.5));
    }
    // Функция бинарного возведения в степень. Говорят Math.pow() на больших значения ошибается
    public static long binpow(long value, int pow) {
        if (pow == 0)
            return 1;
        if (pow % 2 == 1)
            return binpow(value, pow - 1) * value;
        else {
            long smallValue = binpow(value, pow / 2);
            return smallValue * smallValue;
        }
    }
    // Функция разложения числа на цифры. Чтобы не вычислять повторно степенную сумму,
    // а сравнивать только массивы, введен параметр типа boolean
    // если он true (т.е. проверяемое значение от которого взята степенная сумма содержит 0)
    // в возвращаемом результате 0 так же остаются
    // Возвращаемый результат отсортирован
    public static byte[] decomposeIntoDigits(long value, boolean zeroPresents) {
        byte valueLength = (byte)getCountsOfDigits(value);
        byte[] result = new byte[valueLength];
        // взятие последней цифры из числа
        for (int i = 0; i < valueLength; i++) {
            result[i] = (byte)(value % 10);
            value /= 10;
        }
        // Убираем 0, если необходимо
        if (!zeroPresents) {
            byte counter = 0;
            for (int i = 0; i < result.length; i++) {
                if (result[i] == 0)
                    counter++;
            }
            byte[] zippedResult = new byte[valueLength - counter];
            byte i = 0;
            for (byte n : result) {
                if (n != 0) {
                    zippedResult[i] = n;
                    i++;
                }
            }
            Arrays.sort(zippedResult);
            return zippedResult;
        } else {
            Arrays.sort(result);
            return result;
        }
    }
    // Функция проверки: является ли переданное число (в виде массива) числом Армстронга
    // В случае положительного решения возвращает число Армстронга, в противно случае -1
    public static Long armstrongNumber(byte[] num) {
        long degreeSum = 0L;
        byte zeroCounts = 0;
        byte numLength = (byte)num.length;
        // Так как при наличии 0 необходимо проверить различные длины, определяем их кол-во
        for (byte n : num) {
            if (n == 0) {
                zeroCounts++;
            }
        }
        // Если 0 нет
        if (zeroCounts == 0) {
            // Вычисляем сумму
            for (int i = 0; i < numLength; i++) {
                degreeSum += pows[num[i]][numLength];
                if (degreeSum < 0) return -1L;
            }
            // Разбиваем сумму на числа
            byte[] degreeSumDigits = decomposeIntoDigits(degreeSum, false);
            // Проверяем равна ли последовательность исходной
            if (Arrays.equals(num, degreeSumDigits))
                return degreeSum;
            else
                return -1L;
        // Если 0 присутствуют
        } else {
            long result = -1L;
            for (int i = zeroCounts; i >= 0; i--) {
                // Инициализируем массивы переменного размера, в зависимости от кол-ва 0
                byte[] test = Arrays.copyOfRange(num, i, numLength);
                for (int j = 0; j < test.length; j++) {
                    degreeSum += pows[test[j]][test.length];
                    if (degreeSum < 0) result = -1L;
                }
                byte[] degreeSumDigits = decomposeIntoDigits(degreeSum, true);
                if (Arrays.equals(test, degreeSumDigits)) {
                    result = degreeSum;
                    // Возможно, что continue тут бы было правильнее... Вероятность того, что 40007 и 400007
                    // оба будут числом Армстронга конечно существует. Но по факту - нет
                    break;
                }
                else {
                    result = -1L;
                    degreeSum = 0L;
                }
            }
            return result;
        }
    }

    // Исходная функция задачи, которую необходимо было реализовать
    public static long[] getNumbers(long N) {
        // Проверки входного параметра
        if (N <= 1)
            return new long[0];

        // Определяем длину чисел, которые необходимо генерировать
        // -1 снижает кол-во проверок, если задано число 100, то 999..100 не генерируется
        int length = getCountsOfDigits(N - 1);
        // Сюда мы будем собирать наши результаты
        HashSet<Long> armstrongNumbersSet = new HashSet<>();
        // Генерируем 9, 9, 9 ...
        byte[] num = new byte[length];
        for (int i = 0; i < length; i++) {
            num[i] = 9;
        }
        // Массив num в единственном экземпляре, чтобы не раздувать память. Поэтому проверяем его
        // после каждого изменения
        // Если это число Армстронга, то добавляем в Set
        long candidate = armstrongNumber(num);
        if (candidate > 0 && candidate < N)
            armstrongNumbersSet.add(candidate);
        /*
        9 9 9 9 ...
        8 9 9 9 ...
        7 9 9 9 ...
        ...
        1 9 9 9 ...
        0 9 9 9 ...
        8 8 9 9 ...
        7 8 9 9 ...
        6 8 9 9 ...
        ...
        0 0 0 0 ...
         */
        int i = 0;
        while (true) {
            if (num[i] > 0) {
                num[i]--;
                // Проверяем кандидатов
                candidate = armstrongNumber(num);
                if (candidate > 0 && candidate < N)
                    armstrongNumbersSet.add(candidate);
            } else if ((i + 1) < length && num[i + 1] != 0) {
                num[i + 1]--;
                for (int j = i; j >= 0 ; j--) {
                    num[j] = num[i + 1];
                }
                i = 0;
                // Проверяем кандидатов
                candidate = armstrongNumber(num);
                if (candidate > 0 && candidate < N)
                    armstrongNumbersSet.add(candidate);
            }
            else {
                i++;
                if (i >= length)
                    break;
            }
        }
        // Переносим в List, сортируем и переносим в массив long[]
        ArrayList<Long> armstrongNumbers = new ArrayList<>(armstrongNumbersSet);
        Collections.sort(armstrongNumbers);
        long[] result = new long[armstrongNumbers.size()];
        for (int k = 0; k < armstrongNumbers.size(); k++)
            result[k] = armstrongNumbers.get(k);
        return result;
    }

    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        System.out.println(Arrays.toString(getNumbers(1)));
        long b = System.currentTimeMillis();
        System.out.println("memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (8 * 1024));
        System.out.println("time = " + (b - a) / 1000);
    }
}
