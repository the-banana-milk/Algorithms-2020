package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    //трудоёмкость О(N^2)
//память O(N^2)
    static public String longestCommonSubstring(String first, String second) {
        int [][] table = new int[first.length()][second.length()];
        StringBuilder result = new StringBuilder();
        int saveI = 0;
        int saveJ = 0;
        int lenStr = 0;
        for (int i = 0; i < first.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    table[i][j] += 1;
                    if(i != 0 && j != 0) {
                        table[i][j] += table[i-1][j-1];
                    }
                    if (lenStr < table[i][j] && table[i][j] != 1) {
                        saveI = i;
                        saveJ = j;
                        lenStr = table[i][j];
                    }
                } else {
                    table[i][j] = 0;
                }
            }
        }
        while (saveI >= 0 && saveJ >= 0 && table[saveI][saveJ] != 0) {
            result.append(first.charAt(saveI));
            saveI--;
            saveJ--;
        }
        return result.reverse().toString();
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    //Трудоёмкость О(NlogN)
//Память О(N^2)
    static public int calcPrimesNumber(int limit) {
        int count = 0;
        boolean primeOrNot[] = new boolean[limit + 1];
        for (int ind = 2; ind < limit + 1; ind++) primeOrNot[ind] = true;
        for (int i = 2; i < limit + 1; i++) {
            for (int j = 2*i; j < limit + 1 && j > 0; j+= i) {
                primeOrNot[j] = false;
            }
        }
        for (int prime = 2; prime < limit + 1; prime++) {
            if (primeOrNot[prime]) count++;
        }
        return count;
    }
}
