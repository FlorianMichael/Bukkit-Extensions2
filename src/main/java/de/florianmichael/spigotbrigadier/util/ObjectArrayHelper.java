package de.florianmichael.spigotbrigadier.util;

public class ObjectArrayHelper {
    private final Object[] array;

    public ObjectArrayHelper(final Object[] array) {
        this.array = array;
    }

    public static ObjectArrayHelper empty() {
        return new ObjectArrayHelper(new Object[]{});
    }

    public int getLength() {
        return this.array.length;
    }

    public boolean isLength(final int length) {
        return this.getLength() == length;
    }

    public boolean isSmaller(final int length) {
        return this.getLength() < length;
    }

    public boolean isLarger(final int length) {
        return this.getLength() > length;
    }

    public boolean isEmpty() {
        return this.getLength() == 0;
    }

    public boolean isIndexValid(final int index) {
        return index >= 0 && index < this.getLength();
    }

    public Object get(final int index) {
        if (!this.isIndexValid(index)) return null;

        return this.array[index];
    }

    public boolean isString(final int index) {
        if (!this.isIndexValid(index)) return false;

        return this.get(index) instanceof String;
    }

    public boolean isBoolean(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Boolean.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isChar(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            @SuppressWarnings("unused") char c = (char) this.get(index);
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isShort(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Short.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isInteger(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Integer.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isLong(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Long.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isFloat(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Float.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isDouble(final int index) {
        if (!this.isIndexValid(index)) return false;

        try {
            Double.valueOf(this.get(index).toString());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public String getString(final int index, final String defaultValue) {
        if (!this.isIndexValid(index) || !this.isString(index)) {
            return defaultValue;
        }

        return this.get(index).toString();
    }

    public boolean getBoolean(final int index, final boolean defaultValue) {
        if (!this.isIndexValid(index) || !this.isBoolean(index)) {
            return defaultValue;
        }

        return Boolean.parseBoolean(this.getString(index));
    }

    public char getChar(final int index, final char defaultValue) {
        if (!this.isIndexValid(index) || !this.isChar(index)) {
            return defaultValue;
        }

        return (char) this.get(index);
    }

    public short getShort(final int index, final short defaultValue) {
        if (!this.isIndexValid(index) || !this.isShort(index)) {
            return defaultValue;
        }

        return Short.parseShort(this.get(index).toString());
    }

    public int getInteger(final int index, final int defaultValue) {
        if (!this.isIndexValid(index) || !this.isInteger(index)) {
            return defaultValue;
        }

        return Integer.parseInt(this.get(index).toString());
    }

    public long getLong(final int index, final long defaultValue) {
        if (!this.isIndexValid(index) || !this.isLong(index)) {
            return defaultValue;
        }

        return Long.parseLong(this.get(index).toString());
    }

    public float getFloat(final int index, final float defaultValue) {
        if (!this.isIndexValid(index) || !this.isFloat(index)) {
            return defaultValue;
        }

        return Float.parseFloat(this.get(index).toString());
    }

    public double getDouble(final int index, final double defaultValue) {
        if (!this.isIndexValid(index) || !this.isDouble(index)) {
            return defaultValue;
        }

        return Double.parseDouble(this.get(index).toString());
    }

    public String getString(final int index) {
        return this.getString(index, "");
    }

    public Object[] getArray() {
        return array;
    }
}
