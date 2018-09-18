package ru.otus.shw3;

/*
ДЗ 03. MyArrayList
Написать свою реализацию ArrayList на основе массива. Проверить, что на ней работают методы java.util.Collections
 */

import java.util.*;

public class MyArrayList <T> implements List<T> {

    private Object [] data = new Object[3];

    private int size = 0;

    private void increaseDataSize(int newSize){
        Object [] newData = new Object[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
        System.out.printf("Size increased. New size = %d, new data  = %s\n", data.length, Arrays.toString(data));
    }

    private int makeSpaseAtPosition(int pos, int size){
        Object [] newData = new Object[data.length + size];
        System.arraycopy(data, 0, newData, 0, pos);
        System.arraycopy(data, pos, newData, pos+size, data.length-pos);
        data = newData;
        return pos;
    }

    @Override
    public boolean add(T t) {
        if( data.length < (size + 1) ){
            System.out.println("Increasing array...");
            increaseDataSize(data.length*2);
        }
        data[size++] = t;
        return true;
    }

    @Override
    public void add(int index, T element) {
        makeSpaseAtPosition(index, 1);
        data[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        increaseDataSize(data.length + c.size());
        int i = 0;
        Object [] ca = c.toArray();
        System.arraycopy(ca, 0, data, size, ca.length);
        size += ca.length;
        return ca.length != 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        makeSpaseAtPosition(index, c.size());
        Object [] ca = c.toArray();
        System.arraycopy(ca, 0, data, index, ca.length);
        size += ca.length;
        return c.size() != 0;
    }

    @Override
    public String toString(){
        return Arrays.toString(data) + "\n";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if(index < size) {
            return (T) data[index];
        } else{
            throw new IndexOutOfBoundsException(index + " > " + size);
        }
    }

    @Override
    public boolean isEmpty() {
        return size==0?true:false;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size(); i++){
            if(data[i].equals(o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size()-1; i >=0; i--){
            if(data[i] == o) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o)>-1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object el : c){
            if(indexOf(el) == -1) return false;
        }
        return true;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size());
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(data, size, a.getClass());
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public T remove(int index) {

        if(index > size) throw new IndexOutOfBoundsException("index=" + index + " > size=" +size);

        T removedValue = (T) data[index];

        System.arraycopy(data, index+1, data, index, size-index-1);

        data[--size] = null;

        return removedValue;
    }

    @Override
    public boolean remove(Object o) {
        int index2bRemoved = indexOf(o);
        if(index2bRemoved == -1) return false;
        else remove(index2bRemoved);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int originalSize = size();
        for(Object el: c){
            do;while(remove(el));
        }
        return size() < originalSize;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int originalSize = size();
        int i =0;
        while(i < size()){
            if(!c.contains(data[i])) remove(i);
            else i++;
        }
        return size() < originalSize;
    }

    @Override
    public void clear() {
        for(int i = 0; i < size(); i++){
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public T set(int index, T element) {
        T oldValue = (T) data[index];
        if(index > size()) throw new IndexOutOfBoundsException(index + " > " + size());
        data[index] = element;
        return oldValue;
    }

    public class MyIterator implements Iterator<T>{

        int next = 0;
        int prev = -1;

        @Override
        public boolean hasNext() {
            return next < size();
        }

        @Override
        public T next() {
            prev = next;
            return get(next++);
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(prev);
            next--;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public class MyListIterator extends MyIterator implements ListIterator<T>{

        MyListIterator(int index) {
            next = index;
        }

        @Override
        public boolean hasPrevious() {
            return prev >= 0;
        }

        @Override
        public T previous() {
            return get(prev);
        }

        @Override
        public int nextIndex() {
            return next;
        }

        @Override
        public int previousIndex() {
            return prev;
        }

        @Override
        public void set(T t) {
//            System.out.printf("Set %s at %d\n", t.toString(), prev);
            MyArrayList.this.set(prev, t);
        }

        @Override
        public void add(T t) {
            MyArrayList.this.add(prev, t);
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new MyListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}
