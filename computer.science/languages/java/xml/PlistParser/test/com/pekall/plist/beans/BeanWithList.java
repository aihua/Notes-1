package com.pekall.plist.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jiangrui
 * Date: 8/6/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeanWithList {
    private List<Boolean> booleans;
    private List<Integer> integers;
    private List<Long> longs;
    private List<Double> doubles;
    private List<String> strings;
    private List<Date> dates;
    private List<BeanBasicType> objects;
    // TODO:
    // private List<byte[]> byteArrays = new ArrayList<byte[]>();
    // TODO: nest array, List<List<E>>

    public BeanWithList() {
    }

    public List<BeanBasicType> getObjects() {
        return objects;
    }

    public void setObjects(List<BeanBasicType> objects) {
        this.objects = objects;
    }

    public List<Boolean> getBooleans() {
        return booleans;
    }

    public void setBooleans(List<Boolean> booleans) {
        this.booleans = booleans;
    }

    public List<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public void setLongs(List<Long> longs) {
        this.longs = longs;
    }

    public List<Double> getDoubles() {
        return doubles;
    }

    public void setDoubles(List<Double> doubles) {
        this.doubles = doubles;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    /* TODO:
    public List<byte[]> getByteArrays() {
        return byteArrays;
    }

    public void setByteArrays(List<byte[]> byteArrays) {
        this.byteArrays = byteArrays;
    } */

    public void addBoolean(Boolean b) {
        if (booleans == null) {
            booleans = new ArrayList<Boolean>();
        }
        booleans.add(b);
    }

    public void addInteger(Integer integer) {
        if (integers == null) {
            integers = new ArrayList<Integer>();
        }
        integers.add(integer);
    }

    public void addLong(Long l) {
        if (longs == null) {
            longs = new ArrayList<Long>();
        }
        longs.add(l);
    }

    public void addDouble(Double d) {
        if (doubles == null) {
            doubles = new ArrayList<Double>();
        }
        doubles.add(d);
    }

    public void addString(String s) {
        if (strings == null) {
            strings = new ArrayList<String>();
        }
        strings.add(s);
    }

    public void addDate(Date date) {
        if (dates == null) {
            dates = new ArrayList<Date>();
        }
        dates.add(date);
    }

    /* TODO:
    public void addByteArray(byte[] bs) {
        byteArrays.add(bs);
    } */

    public void addObject(BeanBasicType item) {
        if (objects == null) {
            objects = new ArrayList<BeanBasicType>();
        }
        this.objects.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeanWithList)) return false;

        BeanWithList that = (BeanWithList) o;

        if (this.hashCode() != that.hashCode()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Boolean b : booleans) {
            result += b.hashCode();
        }
        for (Integer i : integers) {
            result += i.hashCode();
        }
        for (Long l : longs) {
            result += l.hashCode();
        }
        for (Double d : doubles) {
            result += d.hashCode();
        }
        for (String s : strings) {
            result += s.hashCode();
        }
        for (Date d : dates) {
            // String equality is enough for date
            result += d.toString().hashCode();
        }
        /* for (byte[] bs : byteArrays) {
            result += Arrays.hashCode(bs);
        } */
        for (BeanBasicType beanBasicType : objects) {
            result += beanBasicType.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (BeanBasicType beanBasicType : objects) {
            sb.append(";" + beanBasicType.toString());
        }
        sb.append("}");
        // TODO: add left fields
        return "BeanWithList{" +
                "objects=" + objects +
                '}';
    }
}
