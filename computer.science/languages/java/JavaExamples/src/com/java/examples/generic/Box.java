package com.java.examples.generic;

/**
 * Created with IntelliJ IDEA.
 * User: jiangrui
 * Date: 13-8-11
 * Time: 上午9:02
 * To change this template use File | Settings | File Templates.
 */
public class Box<T> {
    private T t;

    public Box(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Box box = (Box) o;

        if (t != null ? !t.equals(box.t) : box.t != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t != null ? t.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Box{" +
                "t=" + t +
                '}';
    }
}
