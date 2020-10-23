package lesson4;

import java.util.*;
import java.util.function.Consumer;

import kotlin.NotImplementedError;
import lesson3.BinarySearchTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
        Character nameOfNode;
        boolean leaf;
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(element) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        int a = 0;
        Character pevLetter = null;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                if (a != withZero(element).length() - 1) child.leaf = false;
                else child.leaf = true;
                pevLetter = character;
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                if (a != withZero(element).length() - 1) newChild.leaf = false;
                else newChild.leaf = true;
                newChild.nameOfNode = pevLetter;
                current.children.put(character, newChild);
                pevLetter = character;
                current = newChild;
            }
            a++;
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String elem = (String) o;
        Node start = root;
        Node saveNodeToDel = null;
        Character remLet = null;
        for (Character a: withZero(elem).toCharArray()) {
            if(start.children.containsKey(a)) {
                if(saveNodeToDel == null) {
                    saveNodeToDel = start;
                    remLet = a;
                }
                start = start.children.get(a);
            } else break;
            if (start.children.size() > 1) {
                saveNodeToDel = null;
            }
        }
        if (saveNodeToDel != null) {
            saveNodeToDel.children.remove(remLet);
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator {
        private int cursor; // идекс для следующего, что выводим
        private int lastIter = -1;// индекс удаляемого
        private final ArrayList<StringBuilder> findAllStrings = getAllStrings(root);//список строк, хранящихся в дереве
        private final int startSize = findAllStrings.size();
        private final ArrayList<String> wasRemoved = new ArrayList<>();

        private ArrayList<StringBuilder> getAllStrings(Node tree) {
            if (size == 0) return new ArrayList<StringBuilder>();
            ArrayList<StringBuilder> a = new ArrayList<StringBuilder>();
            ArrayList<StringBuilder> c = new ArrayList<StringBuilder>();
            if (tree.children.isEmpty()) {
                StringBuilder b = new StringBuilder();
                Character check = tree.nameOfNode;
                if (check != null) {
                    b.append(check);
                    a.add(new StringBuilder(b));
                    return a;
                }
            } else {
                for (Node i: tree.children.values()) {
                    a.addAll(getAllStrings(i));
                }
                if (tree.leaf && tree.children.isEmpty()) a.add(new StringBuilder());
                Character check = tree.nameOfNode;
                if (check != null) {
                    for (StringBuilder i : a) {
                        StringBuilder d = new StringBuilder();
                        d.append(check);
                        c.add(d.append(i));
                    }
                } else return a;
            }
            return c;
        }

        @Override
        //Ресурсоёмкость О(1)
        //Трудоёмкость O(1)
        public boolean hasNext() {
            return cursor != startSize;
        }
        //Ресурсоёмкость O(n)
        //Трудоёмкость O(n)
        @Override
        public void remove() {
            if (lastIter < 0) throw new IllegalStateException();
            String elem = findAllStrings.get(lastIter).toString();
            if (wasRemoved.size() == 0 || !wasRemoved.contains(elem)) {
                Trie.this.remove(elem);
                wasRemoved.add(elem);
                lastIter = -1;
            }
        }
        //Ресурсоёмкость O(n)
        //Трудоёмкость O(n)
        @Override
        public String next() {
            if (cursor >= startSize || cursor < 0) {
                throw new NoSuchElementException();
            }
            cursor++;
            lastIter = cursor - 1;
            return findAllStrings.get(lastIter).toString();
        }
    }

}