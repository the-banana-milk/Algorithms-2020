package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    // Трудоёмкость О(N)
//Память О(N)
    static private int changeStringToNumber(String line) {
        int pm = 43200;
        int seconds = 0;
        String[] splitedOne = line.split(" ");
        String[] splitedTwo = splitedOne[0].split( ":" ) ;
        seconds = (Integer.parseInt(splitedTwo[0]) * 60 + Integer.parseInt(splitedTwo[1])) * 60 + Integer.parseInt(splitedTwo[2]);
        if (splitedOne[1].equals ("PM") && Integer.parseInt(splitedTwo[0]) != 12) {
            seconds += pm;
        }
        if (Integer.parseInt(splitedTwo[0]) == 12 && splitedOne[1].equals("AM")) {
            seconds -= pm;
        }
        return seconds;
    }
    static private String changeNumderToString(int i) {
        Integer hour = i / 3600;
        Integer save = i / 3600;
        Integer min = (i % 3600)/60;
        Integer seconds = i % 60;
        StringBuilder result = new StringBuilder();
        if (hour > 12) {
            hour -= 12;
        }
        if (hour == 0) {
            hour += 12;
        }
        String res = String.format("%02d:%02d:%02d", hour, min, seconds);
        result.append(res);
        if (save >= 12 && save != 24) {
            result.append(" PM");
        } else {
            result.append(" AM");
        }
        return result.toString();
    }
    static public void sortTimes (String inputName, String outputName ) {
        try(PrintWriter fileToWrite = new PrintWriter(outputName);
            BufferedReader newFile = Files.newBufferedReader(new File(inputName).toPath())) {
            ArrayList<Integer> listOfSec = new ArrayList<>();
            for (String line; (line = newFile.readLine()) != null; ) {
                listOfSec.add(changeStringToNumber(line));
            }int[] need = new int[listOfSec.size()];
            int ind = 0;
            for (Integer i: listOfSec) {
                need[ind] = i;
                ind ++;
            }
            Sorts.insertionSort(need);
            for (Integer j: need) {
                fileToWrite.println(changeNumderToString(j));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    //Трудоёмкость О(N^2)
//память О(N)
    public static class Addresses implements Comparable {
        private String address;
        private Integer numberOfstreet;
        private String owner;
    public Addresses ( String str, Integer num, String owner) {
        this.address = str;
        this.owner = owner;
        this.numberOfstreet = num;
    }

    public String getAddress() {
        return address;
    }

    public Integer getNumberOfstreet() {
        return numberOfstreet;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Addresses a = (Addresses) o;
        int whoIsMore = address.compareTo(a.address);

        if (whoIsMore > 0) {
            return 1;
        } else if (whoIsMore == 0) {
            if (numberOfstreet > a.numberOfstreet) {
                return 1;
            } else if (numberOfstreet < a.numberOfstreet) {
                return -1;
            } else return 0;

        } else return -1;
    }
}

    static public void sortAddresses ( String inputName, String outputName ) {
        try (BufferedReader newFile = Files.newBufferedReader (new File(inputName).toPath());
             PrintWriter fileToWrite = new PrintWriter(new File(outputName), StandardCharsets.UTF_8)) {
            ArrayList<String> streets = new ArrayList<String>();
            ArrayList<ArrayList<String>> owners = new ArrayList<>();
            ArrayList<Addresses> toSort= new ArrayList<>();
            for (String line; (line = newFile.readLine()) != null; ) {
                String[] toTake = line.split(" - ");
                if (!streets.contains(toTake[1])) {
                    String[] numAndStr = toTake[1].split(" ");
                    toSort.add(new Addresses(numAndStr[0], Integer.parseInt(numAndStr[1]), toTake[0]));
                    streets.add(toTake[1]);
                    ArrayList<String> toAdd = new ArrayList<>();
                    toAdd.add(toTake[0]);
                    owners.add(toAdd);
                } else {
                    int ind = streets.indexOf(toTake[1]);
                    owners.get(ind).add(toTake[0]);
                }
            }

            Addresses[] array = new Addresses[toSort.size()];
            int indOfClass = 0;
            for (Addresses i : toSort) {
                array[indOfClass] = i;
                indOfClass++;
            }

            Sorts.insertionSort(array);
            for (ArrayList<String> person : owners) {
                Collections.sort(person);
            }

            for (int i = 0; i< array.length; i++) {
                StringBuilder line = new StringBuilder();
                line.append(array[i].address);
                line.append(" ");
                line.append(array[i].numberOfstreet);
                int indToTakeOwn = streets.indexOf(line.toString());
                line.append(" - ");
                int checking = 0 ;
                for ( String person : owners.get(indToTakeOwn)) {
                    checking += 1 ;
                    line.append(person) ;
                    if ( checking != owners.get(indToTakeOwn).size()) {
                        line.append( ", " ) ;
                    }
                }
                fileToWrite.println(line.toString());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    private static double[] sorting(@NotNull ArrayList<Double> list, int lim) {
        int[] count = new int[lim * 10 + 1];
        for (Double element: list) {
            int savePt = (int)Math.abs(element * 10);
            count[savePt]++;
        }
        for (int j = 1; j <= lim * 10; j++) {
            count[j] += count[j - 1];
        }
        double[] out = new double[list.size()];
        for (int j = list.size() - 1; j >= 0; j--) {
            int savePt = (int)Math.abs((list.get(j) * 10));
            out[count[savePt] - 1] = list.get(j);
            count[savePt]--;
        }
        return out;
    }
    static public void sortTemperatures(String inputName, String outputName) {
        try (BufferedReader newFile = Files.newBufferedReader(new File(inputName).toPath());
             PrintWriter fileToWrite = new PrintWriter(new File(outputName), StandardCharsets.UTF_8)) {

            ArrayList<Double> savePositive = new ArrayList<>();
            ArrayList<Double> saveNegative = new ArrayList<>();
            for (String line; (line = newFile.readLine()) != null; ) {
                Double a = Double.parseDouble(line);
                if (a >= 0.0 && a <= 500.0) {
                    savePositive.add(a);
                } else saveNegative.add(a);
            }

            double[] resultPosit = sorting(savePositive, 500);
            double[] resulteNegat = sorting(saveNegative, 273);

            for (int iNegat = resulteNegat.length -1; iNegat >= 0; iNegat--) {
                fileToWrite.println(resulteNegat[iNegat]);
            }

            for (int j = 0; j < resultPosit.length ; j++) {
                fileToWrite.println(resultPosit[j]);
            }

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
