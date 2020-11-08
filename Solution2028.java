package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
*/

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    int treeSize;


    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }
        // insert
        public void setChild(Entry<T> child) {
            if (this.availableToAddLeftChildren) {
                this.leftChild = child;
                this.availableToAddLeftChildren = false;
            }
            else {
                this.rightChild = child;
                this.availableToAddRightChildren = false;
            }
        }
        // insert
        public void setParent(Entry<T> parent) {
            this.parent = parent;
        }
        // insert
        public String getElementName() {
            return elementName;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }

        public boolean hasChild() {
            return !availableToAddLeftChildren;
        }
    }

    public CustomTree() {
        this.root = new Entry<>("0");
        this.root.parent = null;
    }

    // insert
    private Entry<String> findEntryByName(String s) {
        if (!root.hasChild()) return null;

        Stack<Entry<String>> stack = new Stack<>();
        Entry<String> entry;
        stack.push(root);
        while (!stack.empty()) {
            entry = stack.pop();
            // если совпало значение
            if (entry.getElementName().equals(s)) {
                return entry;
            } else if (entry.hasChild()) {
                if (!entry.availableToAddRightChildren)
                    stack.push(entry.rightChild);
                stack.push(entry.leftChild);
            }
        }
        return null;
    }

    private Entry<String> getFreeParent() {
        Entry<String> entry = root;
        LinkedList<Entry<String>> freeParentsList = new LinkedList<>();
        freeParentsList.add(entry);
        while (freeParentsList.size() > 0) {
            entry = freeParentsList.pollFirst();
            if (entry.isAvailableToAddChildren()) {
                return entry;
            } else {
                freeParentsList.add(entry.leftChild);
                freeParentsList.add(entry.rightChild);
            }
        }
        return null;
    }


    public String getParent(String s) {
        Entry<String> entry = findEntryByName(s);
        if (entry != null)
            if (entry.parent != null)
                return entry.parent.getElementName();
        return null;
    }



    @Override
    public boolean add(String s) {
        if (this.treeSize == Integer.MAX_VALUE) return false;
        // создаем новый элемент, назначаем родителя и потомка
        Entry<String> element = new Entry<>(s);
        Entry<String> parent = getFreeParent();
        if (parent == null)
            return false;
        element.setParent(parent);
        parent.setChild(element);
        this.treeSize++;
        return true;
    }

    @Override
    public int size() {
        return this.treeSize;
    }

    public boolean remove(Object o) {
        if (o instanceof String) {
            Entry<String> entry = findEntryByName((String)o);
            if (entry != null) {
                removeAllEntriesFrom(entry);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return true;
    }

    private void removeAllEntriesFrom(Entry<String> entry) {
        // освобождаем родителя от удаляемого потомка
        Entry<String> entryParent = entry.parent;
        if (entryParent.leftChild == entry) {
            entryParent.leftChild = null;
            entryParent.availableToAddLeftChildren = true;
        } else if (entryParent.rightChild == entry) {
            entryParent.rightChild = null;
            entryParent.availableToAddRightChildren = true;
        }
        // перебираем потомков удаляемой записи
        // можно и не перебирать, но тогда они останутся в памяти до завершения программы => Memory Leak
        Stack<Entry<String>> stack = new Stack<>();
        stack.push(entry);
        while (!stack.empty()) {
            entry = stack.pop();
            if (entry.hasChild()) {
                if (!entry.availableToAddRightChildren)
                    stack.push(entry.rightChild);
                stack.push(entry.leftChild);
            }
            entry = null;
            treeSize--;
        }

    }

    // UNSUPPORTED METHODS ===================================================================

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }
}
