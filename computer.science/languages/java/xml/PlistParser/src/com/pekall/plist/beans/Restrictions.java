package com.pekall.plist.beans;

/**
 * Containing restrictions.
 */
@SuppressWarnings({"UnusedDeclaration", "SimplifiableIfStatement"})
public class Restrictions {
    /**
     * boolean restrictions
     */
    private RestrictedBool restrictedBool;

    /**
     * numeric restrictions
     */
    private RestrictedValue restrictedValue;

    public Restrictions() {
    }

    public Restrictions(RestrictedBool restrictedBool, RestrictedValue restrictedValue) {
        this.restrictedBool = restrictedBool;
        this.restrictedValue = restrictedValue;
    }

    public RestrictedBool getRestrictedBool() {
        return restrictedBool;
    }

    public void setRestrictedBool(RestrictedBool restrictedBool) {
        this.restrictedBool = restrictedBool;
    }

    public RestrictedValue getRestrictedValue() {
        return restrictedValue;
    }

    public void setRestrictedValue(RestrictedValue restrictedValue) {
        this.restrictedValue = restrictedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restrictions)) return false;

        Restrictions that = (Restrictions) o;

        if (restrictedBool != null ? !restrictedBool.equals(that.restrictedBool) : that.restrictedBool != null)
            return false;
        return !(restrictedValue != null ? !restrictedValue.equals(that.restrictedValue) : that.restrictedValue != null);

    }

    @Override
    public int hashCode() {
        int result = restrictedBool != null ? restrictedBool.hashCode() : 0;
        result = 31 * result + (restrictedValue != null ? restrictedValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Restrictions{" +
                "restrictedBool=" + restrictedBool.toString() +
                ", restrictedValue=" + restrictedValue.toString() +
                '}';
    }
}
