package lesson4;

import java.util.*;
import java.util.function.Consumer;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
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
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        int a = 0;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                if (a != element.length() - 1)child.leaf = false;
                else child.leaf = true;
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                if (a != element.length() - 1)newChild.leaf = false;
                else newChild.leaf = true;
                current.children.put(character, newChild);
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
        int lenOfWord = elem.length();
        int lenCheck = 0;
        boolean isDeleted = false;
        for (Character a: elem.toCharArray()) {
            if(start.children.containsKey(a)) {
                if(saveNodeToDel == null) {
                    saveNodeToDel = start;
                    remLet = a;
                }
                lenCheck++;
                start = start.children.get(a);
            } else break;
            if (start.leaf && lenCheck == lenOfWord -1) {
                isDeleted = true;
                start.leaf = false;
                break;
            }
            if (start.children.size() > 1) {
                saveNodeToDel = null;
            }
        }
        if (!isDeleted && saveNodeToDel != null) {
            saveNodeToDel.children.remove(remLet);
            return true;
        }
        /*Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;*/
        return false;
    }
    public static void main(String[] args) {
        /*ArrayList<Integer> a = new ArrayList();
        System.out.println(a.iterator().hasNext());*/
        Trie a = new Trie();
        a.add("Yana");
        a.add("Ruby");
       /* a.add("Java");
        a.add("Jafar");
        a.add("Jerry");
        a.add("Jer");
        a.add("Jel");*/
        a.iterator();
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

        private ArrayList<StringBuilder> getAllStrings(Node tree) {
            if (size == 0) return new ArrayList<StringBuilder>();
            ArrayList<StringBuilder> a = new ArrayList<StringBuilder>();
            ArrayList<StringBuilder> c = new ArrayList<StringBuilder>();
            if (tree.children.isEmpty()) {
                StringBuilder b = new StringBuilder();
                ArrayList<Character> check = getChar(tree);
                if (check.size() == 1 && check.get(0) != null) {
                    b.append(check.get(0));
                    a.add(new StringBuilder(b));
                    return a;
                }
            } else {
                for (Node i: tree.children.values()) {
                    a.addAll(getAllStrings(i));
                }
                if (tree.leaf && !tree.children.isEmpty()) a.add(new StringBuilder());
                for (StringBuilder i: a) {
                    ArrayList<Character> check = getChar(tree);
                    if (check.size() == 1 && check.get(0) != null) {
                        StringBuilder d = new StringBuilder();
                        d.append(check.get(0));
                        if (i != null) {
                            c.add(d.append(i));
                        }
                    } else if (check.size() > 1) {
                        StringBuilder d = new StringBuilder();
                        d.append(check.get(0));
                        if (i != null) {
                            c.add(d.append(i));
                        }
                    }
                }
            }
            return c;
        }

        private ArrayList<Character> getChar(Node tree) {
            ArrayList<Character> result = new ArrayList<>();
            for (Map.Entry<Character, Node> forKey: tree.children.entrySet()) {
                result.add(forKey.getKey());
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public void remove() {
            Node start = root;
            Node saveNodeToDel = null;
            if (lastIter < 0) throw new IllegalStateException();
            String elem = findAllStrings.get(lastIter).toString();
            Character remLet = null;
            int lenOfWord = elem.length();
            int lenCheck = 0;
            boolean isDeleted = false;
            for (Character a: elem.toCharArray()) {
                if(start.children.containsKey(a)) {
                    if(saveNodeToDel == null) {
                        saveNodeToDel = start;
                        remLet = a;
                    }
                    lenCheck++;
                    start = start.children.get(a);
                } else break;
                if (start.leaf && lenCheck == lenOfWord -1) {
                    isDeleted = true;
                    start.leaf = false;
                    break;
                }
                if (start.children.size() > 1) {
                    saveNodeToDel = null;
                }
            }
            if (!isDeleted && saveNodeToDel != null) {
                saveNodeToDel.children.remove(remLet);
            }
            size--;
            cursor = lastIter;
            lastIter = lastIter - 1;
        }

        @Override
        public String next() {
            cursor++;
            if (cursor > size || cursor < 0) throw new IllegalArgumentException();
            return findAllStrings.get(lastIter = cursor - 1).toString();
        }
    }

}