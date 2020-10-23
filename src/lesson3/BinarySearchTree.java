package lesson3;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        T value;
        Node<T> left = null;
        Node<T> right = null;
        Node<T> par;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
            root.par = null;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = new Node(t);
            closest.left.par = closest;
        }
        else {
            assert closest.right == null;
            closest.right = new Node(t);
            closest.right.par = closest;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    //трудоёмкость О(n)
    //память 0(n)
    private void toTransplant( Node<T> start, Node<T> toChange) {
        Node<T> save = start.par;
        if (start.par == null) {
            root = toChange;
        } else if (start == start.par.left) {
            start.par.left = toChange;
        } else {
            start.par.right = toChange;
        }
        if (toChange != null) toChange.par = save;
    }

    private Node<T> treeMin(Node<T> start) {
        if (start == null) return null;
        while (start.left != null) {
            start = start.left;
        }
        return start;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> oNode = find((T)o);
        if (oNode.value != (T) o || oNode.value == null) return false;
        if (oNode.left == null && oNode.right != null) {
            toTransplant(oNode, oNode.right);
            size--;
            return true;
        } else if (oNode.right == null && oNode.left != null) {
            toTransplant(oNode, oNode.left);
            size--;
            return true;
        } else if (oNode.right == null) {
            toTransplant(oNode, null);
            size--;
            return true;
        } else {
            Node<T> y = treeMin(oNode.right);
            if (y.par != oNode) {
                toTransplant(y, y.right);
                y.right = oNode.right;
                y.right.par = y;
            }
            toTransplant(oNode, y);
            y.left = oNode.left;
            y.left.par = y;
            size--;
            return true;
        }
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        private int cursor; // идекс для следующего, что выводим
        private Node<T> next = treeMin(root);
        private Node<T> current;
        private final int startSize = size;
        private boolean flag = false;

        private BinarySearchTreeIterator() {
            // Добавьте сюда инициализацию, если она необходима.
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        //трудоёмкость - О(1)
        //память - О(1)
        @Override
        public boolean hasNext() {
            return cursor != startSize;
        }

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */

        @Override
        //трудоёмкость-О(logn)
        //память - O(n)
        public T next() {
            flag = true;
            if (cursor >= startSize || cursor < 0) {
                throw new NoSuchElementException();
            }
            current = next;
            next = findNext(next);
            cursor++;
            return current.value;
        }
        private Node<T> findNext(Node<T> next) {
            if (next.right != null) {
                next = next.right;
                if (next != null) next = treeMin(next);
                return next;
            } else {
                if (next == next.par.left) {
                    next = next.par;
                }else if (next == next.par.right) {
                    T value = next.par.value;
                    do {
                        next = next.par;
                    } while (next.par != null && next.value.compareTo(value) <= 0);
                }
                return next;
            }
        }
        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */
        @Override
        //трудоёмкость О(n)
        //память 0(n)
        public void remove() {
            if (!flag) throw new IllegalStateException();
            Node<T> oNode = current;
            if (oNode.left == null && oNode.right != null) {
                toTransplant(oNode, oNode.right);
                size--;
            } else if (oNode.right == null && oNode.left != null) {
                toTransplant(oNode, oNode.left);
                size--;
            } else if (oNode.right == null) {
                toTransplant(oNode, null);
                size--;
            } else {
                Node<T> y = treeMin(oNode.right);
                if (y.par != oNode) {
                    toTransplant(y, y.right);
                    y.right = oNode.right;
                    y.right.par = y;
                }
                toTransplant(oNode, y);
                y.left = oNode.left;
                y.left.par = y;
                size--;
            }
            flag = false;
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}