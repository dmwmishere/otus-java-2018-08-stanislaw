package ru.otus.shw2;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class SizeOf {

    private final static int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));

    private final static int padding = 64;

    static {
        System.out.println(String.format("bitness = %d(%d bytes), padding = %d(%d bytes)", bitness, bitness/8, padding, padding/8));
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Boolean
                || o instanceof Short || o instanceof Byte || o instanceof Character || o instanceof Double;
    }

    private static int getPrimitiveSize(Object o){
        if(o instanceof Byte) return Byte.SIZE;
        if(o instanceof Short) return Short.SIZE;
        if(o instanceof Integer) return Integer.SIZE;
        if(o instanceof Long) return Long.SIZE;
        if(o instanceof Float) return Float.SIZE;
        if(o instanceof Double) return Double.SIZE;
        if(o instanceof Character) return Character.SIZE;
        if(o instanceof Boolean) return 8; // The size
        return -1;
    }

    public static int getAlignedFields(int fields_size){
        int closestMultiple = (int)(fields_size/(padding/8f) + 0.5f) * (padding/8);
        return closestMultiple < fields_size ? closestMultiple + 8 : closestMultiple;
    }

    public static int sizeof_bits(Object obj, String indent){
        if(obj == null){
            System.out.println(indent + "Object is null return " + bitness/8);
            return bitness/8; // return only reference size
        }
        else {
            Class<? extends Object> cls = obj.getClass();

            String type = "";
            int actual_size = -1;
            if (isPrimitive(obj)) {
                type = "primitive";
                actual_size =  getPrimitiveSize(obj) / 8;
            } else {
                int reference_size = bitness; // Class header or array reference (2^31-8) size;
                if (cls.isArray()) {
                    int array_size = 0;
                    int array_len = Array.getLength(obj);
                    type = "array[" + array_len + "]";
                    for (int i = 0; i < array_len; i++) {
                        array_size += sizeof_bits(Array.get(obj, i), indent + '\t');
                    }
                    actual_size = reference_size / 8 + array_size;
                } else {
                    type = "complex";
                    int fields_size = 0;
                    for (Field field : cls.getDeclaredFields()) {
                        field.setAccessible(true);
                        System.out.println(indent + "Field name = " + field.getName());
                        try {
                            Object subObj = field.get(obj);
                            fields_size += sizeof_bits(subObj, indent + '\t');
                        } catch (IllegalAccessException iae) {
                            System.err.println(indent + "Failed to access field " + field.getName() + ": " + iae.getMessage());
                        }
                    }
                    actual_size = reference_size / 8 + getAlignedFields(fields_size);
                }
            }
            System.out.println(String.format("%sobject '%s' is %s, its size = %d bytes(s)", indent, cls.getSimpleName(), type, actual_size));
            return actual_size;
        }
    }

}
