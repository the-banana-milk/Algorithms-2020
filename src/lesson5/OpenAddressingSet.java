package lesson5;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    public static void main(String[] args) {
        new ArrayList<Integer>();
        OpenAddressingSet a = new OpenAddressingSet(7);
        a.add("fgh");
        a.add("ksh");
        Iterator<Object> b = a.iterator();
        System.out.println(b.next());
        System.out.println(b.next());
        System.out.println(b.next());
        System.out.println(b.hasNext());
    }
    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     * && !current.equals(new Deleted())
     */
    private class Deleted extends Object {
        private final Object a = null;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Deleted deleted = (Deleted) o;
            return Objects.equals(a, deleted.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && !current.equals(new Deleted())) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        //return super.remove(o);
        T elem = (T) o;
        int startingIndex = startingIndex(elem);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && !current.equals(elem)) {
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                return false;
            }
            current = storage[index];
        }
        if (current != null) {
            storage[index] = new Deleted();
            size--;
            return true;
        }
        return false;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OASIter();
        /*// 
        throw new NotImplementedError();*/
    }

    private class OASIter implements Iterator {
        private int curs; //следующий для возвращения
        private int lastRet = -1;// последний возращённый
        private int toStop;
        private final int startSize = size;
        @Override
        public void remove() {
            /*if (lastRet < 0) {
                throw new IllegalStateException();
            }
            storage[lastRet] = new Deleted();
            size--;
            //OpenAddressingSet.this.remove(storage[lastRet]);
            curs = lastRet;
            lastRet = -1;*/
        }
        //трудоёмкость О(n)
        //память O(1)
        @Override
        public Object next() {
            if (lastRet == curs || curs == capacity) {
                throw new IllegalStateException();
            }
            if (toStop == 0 || lastRet < 0) findIndOfNext();
            lastRet = curs;
            if (toStop != startSize)findIndOfNext();
            return storage[lastRet];
        }

        private void findIndOfNext() {
            int ind = curs;
            if (toStop == 0 && curs == 0 && lastRet == -1) {
                while (storage[ind] == null && ind < capacity) {
                    ind++;
                }
            } else {
                do {
                    ind++;
                } while (ind < capacity && (storage[ind] == null || storage[ind].equals(new Deleted())));
            }
            if (lastRet < 0) toStop--;
            toStop++;
            curs = ind;
        }
        //трудоёмкость О(1)
        //память O(1)
        @Override
        public boolean hasNext() {
            return toStop != startSize;
        }
    }
}
