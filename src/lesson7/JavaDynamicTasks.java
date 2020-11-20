package lesson7;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //трудоёмкость О(n^2)
    //память O(n^2)
    private static int[][] retMatrix(String first, String second) {
        int[][] matrix = new int[first.length() + 1][second.length() + 1];
        for (int x = 0; x < first.length() + 1; x++) {
            for (int y = 0; y < second.length() + 1; y++) {
                if (y != 0 && x != 0) {
                    if (first.charAt(x - 1) == second.charAt(y - 1)) {
                        matrix[x][y] = matrix[x - 1][y - 1] + 1;
                    } else {
                        matrix[x][y] = Math.max(matrix[x][y - 1], matrix[x - 1][y]);
                    }
                } else {
                    matrix[x][y] = 0;
                }
            }
        }
        return matrix;
    }
    public static String longestCommonSubSequence(String first, String second) {
        int[][] matrix = retMatrix(first, second);
        StringBuilder result = new StringBuilder();
        int x = first.length();
        int y = second.length();
        while (x > 0 && y > 0) {
            if (first.charAt(x - 1) == second.charAt(y - 1)) {
                result.append(first.charAt(x - 1));
                x--;
                y--;
            } else if (matrix[x - 1][y] > matrix[x][y - 1]) {
                x--;
            } else y--;
        }
        result.reverse();
        return result.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    //трудоёмкость О(nlogn)
    //память O(n)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.size() <= 1){
            return list;
        }
        int[] helpingList = new int[list.size()];
        int[] indexForNormolizing = new int[list.size()];

        helpingList[0] = list.get(0);
        indexForNormolizing[0] = 0;

        for (int i = 1; i < list.size(); i++) {
            helpingList[i] = Integer.MAX_VALUE;
        }

        for (int i = 1; i < list.size(); i++) {
            indexForNormolizing[i] = whereToPutNum(helpingList, 0, i, list.get(i));
        }

        ArrayList<Integer> result = new ArrayList<>();

        int ind = getMaxIndOfSeq(indexForNormolizing);
        int curInd = ind;
        int indInSeq = indexForNormolizing[ind];

        while (curInd >= 0) {
            if(result.isEmpty()) {
               result.add(0, list.get(curInd));
               indInSeq = indexForNormolizing[curInd];
            } else if (ind - 1 >= 0 && (indInSeq - indexForNormolizing[curInd] == 1)) {
                if(curInd - 1 >= 0 && indexForNormolizing[curInd] == indexForNormolizing[curInd - 1]
                && list.get(curInd - 1) < result.get(0)) {
                    curInd--;
                    continue;
                }
                result.add(0, list.get(curInd));
                indInSeq = indexForNormolizing[curInd];
            } else if (curInd == 0 && indexForNormolizing[curInd] == 0 && indexForNormolizing[curInd + 1] != 0) {
                result.add(0, list.get(curInd));
                indInSeq = indexForNormolizing[curInd];
            } else if (indexForNormolizing[ind] - indexForNormolizing[ind - 1] < 0) {

            }
            curInd--;
        }

        return result;
    }
    private static int getMaxIndOfSeq(int[] listOfInd) {
        int ret = 0;
        for (int i =0; i < listOfInd.length; i++) {
            if (listOfInd[i] > listOfInd[ret]) {
                ret = i;
            }
        }
        return ret;
    }
    private static int whereToPutNum(int[] helpinglist, int left, int right, int num) {
        int mid = 0;
        int r = right;
        int l = left;
        int ind = 0;
        boolean flag = false;
        while (l <= r && !flag) {
            mid = (l + r)/2;
            if (helpinglist[mid] > num) {
                r = mid - 1;
            }
            else if (helpinglist[mid] == num) {
                ind = mid;
                flag = true;
            }
            else if (mid + 1 <= r && helpinglist[mid + 1] >= num) {
                helpinglist[mid + 1] = num;
                ind = mid + 1;
                flag = true;
            } else l = mid + 1;
        }
        if (!flag) {
            if (mid == l) {
                helpinglist[mid] = num;
                ind = mid;
            } else {
                helpinglist[mid + 1] = num;
                ind = mid + 1;
            }
        }
        return ind;
    }
    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
