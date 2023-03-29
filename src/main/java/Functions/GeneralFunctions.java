package Functions;

import AbstractClasses.AbstractItem;
import Classes.Potion;
import Enums.Pet;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Functions.SaveFunctions.getJSONObject;

public class GeneralFunctions {

    public static double generateDoubleBetween(double min, double max) {
        if (min == max) {
            return min;
        } else if (max < min) {
            return new Random().nextDouble(min - max) + max;
        } else {
            return new Random().nextDouble(max - min) + min + 1;
        }
    }

    public static String generateRandomString(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            return (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
                    ||
                    (method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class));

        }
        return false;
    }

    public static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

    public static List<Method> findGettersSetters(Class<?> c) {
        List<Method> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        if(c.getSuperclass() != null)
            methods = Stream.concat(Arrays.stream(methods), Arrays.stream(c.getSuperclass().getDeclaredMethods())).toArray(Method[]::new);

        for (Method method : methods)
            if (isGetter(method) || isSetter(method))
                list.add(method);
        return list;
    }

    public static List<String> returnStringGettersList(Class<?> c) {
        List<String> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();

        for (Method method : methods) {
            if (isGetter(method))
                list.add(method.getName());
        }
        return list;
    }

    public static List<String> returnStringSettersList(Class<?> c) {
        List<String> list = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();

        if(c.getSuperclass() != null)
            methods = Stream.concat(Arrays.stream(methods), Arrays.stream(c.getSuperclass().getDeclaredMethods())).toArray(Method[]::new);

        for (Method method : methods) {
            if (isSetter(method))
                list.add(method.getName());
        }
        return list;
    }

    public static Method getMethod(Class<?> c, String methodName) {
        for (Method method : findGettersSetters(c)) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static void runSetterMethod(Class<?> c, Object object, String methodString, Object value) {
        Method method = Objects.requireNonNull(getMethod(c, methodString));
        Object methodParameterType = method.getParameterTypes()[0];
        Object methodParameter = method.getParameters()[0];

        if(methodParameterType.equals(double.class)
                || methodParameterType.equals(int.class)
                || methodParameterType.equals(String.class)) {
//            System.out.println(method.getName() + "-" + methodParameterType + "-up");
            runSetter(c, object, methodString, value);
        }
        else if (methodParameterType.equals(List.class)) {
            ParameterizedType pType = (ParameterizedType) method.getGenericParameterTypes()[0];
            Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];

            // todo- here


        }
        else if (methodParameterType.equals(HashMap.class)) {
//            runSetter(c, object, methodString, returnSerializableHashMap(value));
        }
        else if (findAllClasses("Classes").contains(methodParameterType)) {
//            runSetter(c, object, methodString, getJSONObject(methodParameterType.getClass(), value));
        }
        else {
            Enum<?> e = Enum.valueOf((Class<Enum>) methodParameterType, value.toString());
            runSetter(c, object, methodString, e);
        }


    }

    private static void runSetter(Class<?> c, Object object, String methodString, Object value) {
        try {
            Objects.requireNonNull(getMethod(c, methodString)).invoke(object, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object runGetterMethod(Class<?> c, Object object, String methodString) {
        Method method = Objects.requireNonNull(getMethod(c, methodString));
        Object objectValue = null;

        try {
        objectValue = Objects.requireNonNull(getMethod(c, methodString)).invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return returnObject(method, objectValue);
    }

    private static Object returnObject(Method method, Object objectValue) {
        if (method.getReturnType().equals(double.class) ||
                method.getReturnType().equals(int.class) ||
                method.getReturnType().equals(String.class)) {
            return objectValue;
        } else if (method.getReturnType().equals(List.class)) {
            return returnSerializableList(objectValue);
        } else if (method.getReturnType().equals(HashMap.class)) {
            return returnSerializableHashMap(objectValue);
        } else if (findAllClasses("Classes").contains(method.getReturnType())) {
            return getJSONObject(method.getReturnType(), objectValue);
        } else {
            assert objectValue != null;
            return objectValue.toString();
        }
    }



    private static Serializable returnSerializableHashMap(Object objectValue) {
        List<Object> objectList = new ArrayList<>();

        assert objectValue != null;
        ((HashMap<?, ?>) objectValue).forEach((k, v) -> objectList.add(v));
        boolean containsClass = objectList.stream().anyMatch(o -> findAllClasses("Classes").contains(o.getClass()));

        if (containsClass) {
            HashMap<Object, JSONObject> newHashMap = new HashMap<>();
            ((HashMap<?, ?>) objectValue).forEach((k, v) -> newHashMap.put(k, getJSONObject(v.getClass(), v)));
            return newHashMap;
        } else {
            return objectValue.toString();
        }
    }

    @NotNull
    private static Object returnSerializableList(Object objectValue) {
        List<?> objectList = ((List<?>) objectValue);

        assert objectList != null;
        boolean containsClass = objectList.stream().anyMatch(o -> findAllClasses("Classes").contains(o.getClass()));

        if (containsClass) {
            List<JSONObject> newList = new ArrayList<>();
            objectList.forEach(o -> newList.add(getJSONObject(o.getClass(), o)));
            return newList;
        } else {
            return objectValue;
        }
    }


    public static List<Class<?>> findAllClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        assert stream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toList());
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
