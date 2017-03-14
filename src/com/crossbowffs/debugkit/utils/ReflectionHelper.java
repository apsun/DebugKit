package com.crossbowffs.debugkit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public final class ReflectionHelper {
    private ReflectionHelper() { }

    public static String dumpFields(Object self, boolean dumpValues) {
        return dumpFields(self.getClass(), self, dumpValues);
    }

    public static String dumpFields(Class<?> cls) {
        return dumpFields(cls, null, false);
    }

    private static String dumpFields(Class<?> cls, Object self, boolean dumpValues) {
        StringBuilder sb = new StringBuilder();
        for (; cls != null; cls = cls.getSuperclass()) {
            Field[] fields = cls.getDeclaredFields();
            ArrayList<Field> shownFields = new ArrayList<Field>(fields.length);
            for (Field field : fields) {
                if (field.isSynthetic()) continue;
                if (field.getName().startsWith("shadow$_")) continue;
                shownFields.add(field);
            }

            if (shownFields.isEmpty()) {
                continue;
            }

            sb.append("----------------------------------------\n");
            sb.append("Fields for: ");
            sb.append(cls.getName());
            sb.append("\n----------------------------------------\n");

            for (Field field : shownFields) {
                sb.append(field.getName());
                if (dumpValues) {
                    sb.append(" = ");
                    field.setAccessible(true);
                    Object fieldValue;
                    try {
                        fieldValue = field.get(self);
                    } catch (IllegalAccessException e) {
                        throw new AssertionError(e);
                    }
                    sb.append(fieldValue);
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public static String dumpMethods(Object self) {
        return dumpMethods(self.getClass());
    }

    public static String dumpMethods(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        for (; cls != null; cls = cls.getSuperclass()) {
            Method[] methods = cls.getDeclaredMethods();
            if (methods.length == 0) {
                continue;
            }

            sb.append("----------------------------------------\n");
            sb.append("Methods for: ");
            sb.append(cls.getName());
            sb.append("\n----------------------------------------\n");
            for (Method method : methods) {
                sb.append(method.getName());
                sb.append('(');

                Class<?>[] argTypes = method.getParameterTypes();

                if (argTypes.length == 1) {
                    sb.append(argTypes[0].getName());
                } else if (argTypes.length > 1) {
                    for (int i = 0; i < argTypes.length; i++) {
                        Class<?> argType = argTypes[i];
                        sb.append("\n  ");
                        sb.append(argType.getName());
                        if (i < argTypes.length - 1) {
                            sb.append(',');
                        }
                    }
                }

                sb.append(")\n");
            }
        }
        return sb.toString();
    }
}
