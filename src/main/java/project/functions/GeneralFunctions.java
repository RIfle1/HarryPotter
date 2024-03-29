package project.functions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static project.functions.SaveFunctions.*;

public class GeneralFunctions {

    public static double generateDoubleBetween(double min, double max) {
        if (min == max) {
            return min;
        } else if (max < min) {
            return Math.round(new Random().nextDouble(min - max) + max);
        } else {
            return Math.round(new Random().nextDouble(max - min) + min);
        }
    }

    public static double chooseRandomDouble(double[] array) {
        return array[new Random().nextInt(array.length)];
    }

    public static String generateRandomString(int length) {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("_", "").substring(0,length);
    }

//    public static boolean isGetter(Method method) {
//        if (Modifier.isPublic(method.getModifiers()) &&
//                method.getParameterTypes().length == 0) {
//            return (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
//                    ||
//                    (method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class));
//
//        }
//        return false;
//    }

//    public static boolean isSetter(Method method) {
//        return Modifier.isPublic(method.getModifiers()) &&
//                method.getReturnType().equals(void.class) &&
//                method.getParameterTypes().length == 1 &&
//                method.getName().matches("^set[A-Z].*");
//    }

//    public static List<Method> findGettersSetters(Class<?> c) {
//        List<Method> list = new ArrayList<>();
//        Method[] methods = c.getDeclaredMethods();
//        if(c.getSuperclass() != null)
//            methods = Stream.concat(Arrays.stream(methods), Arrays.stream(c.getSuperclass().getDeclaredMethods())).toArray(Method[]::new);
//
//        for (Method method : methods)
//            if (isGetter(method) || isSetter(method))
//                list.add(method);
//        return list;
//    }

//    public static List<String> returnStringGettersList(Class<?> c) {
//        List<String> list = new ArrayList<>();
//        Method[] methods = c.getDeclaredMethods();
//
//        for (Method method : methods) {
//            if (isGetter(method))
//                list.add(method.getName());
//        }
//        return list;
//    }

//    public static List<String> returnStringSettersList(Class<?> c) {
//        List<String> list = new ArrayList<>();
//        Method[] methods = c.getDeclaredMethods();
//
//        if(c.getSuperclass() != null)
//            methods = Stream.concat(Arrays.stream(methods), Arrays.stream(c.getSuperclass().getDeclaredMethods())).toArray(Method[]::new);
//
//        for (Method method : methods) {
//            if (isSetter(method))
//                list.add(method.getName());
//        }
//        return list;
//    }

//    public static Method getMethod(Class<?> c, String methodName) {
//        for (Method method : findGettersSetters(c)) {
//            if (method.getName().equals(methodName)) {
//                return method;
//            }
//        }
//        return null;
//    }

//    public static void runSetterMethod(Class<?> c, Object object, String methodString, Object value) {
//        Method method = Objects.requireNonNull(getMethod(c, methodString));
//        Class<?> methodParameterType = method.getParameterTypes()[0];
//
//        if(methodParameterType.equals(String.class)) {
//            runSetter(c, object, methodString, value);
//        }
//        else if(methodParameterType.equals(double.class)) {
//            runSetter(c, object, methodString, value);
//        }
//        else if(methodParameterType.equals(int.class)) {
//            Long valueLong = (Long) value;
//            int valueInt = valueLong.intValue();
//
//            runSetter(c, object, methodString, valueInt);
//        }
//        else if(methodParameterType.equals(double[].class)) {
//            JSONArray jsonArray = (JSONArray) value;
//            double[] doubleArray = jsonArray.stream().mapToDouble(o -> (double) o).toArray();
//            runSetter(c, object, methodString, doubleArray);
//        }
//        else if (methodParameterType.equals(List.class)) {
//            Class<?> listParameterClass = returnParametersClass(method).get(0);
//
//            List<?> objectsList = new ArrayList<>();
//            JSONArray jsonArray = (JSONArray) value;
//
//
//            if(jsonArray != null) {
//                objectsList = jsonArray.stream().toList();
//            }
//
//            if(listParameterClass.equals(String.class)) {
//                runSetter(c, object, methodString, objectsList);
//            }
//            else if(findAllClasses("project/classes").contains(listParameterClass)) {
//                List<Object> objectList = returnSetterObject(listParameterClass, objectsList);
//                runSetter(c, object, methodString, objectList);
//            }
//        }
//        else if (methodParameterType.equals(HashMap.class)) {
//            List<Class<?>> hashMapParameterClasses = returnParametersClass(method);
//            Class<?> firstParameterClass = hashMapParameterClasses.get(0);
//            Class<?> secondParameterClass = hashMapParameterClasses.get(1);
//
//            if(firstParameterClass.equals(String.class) && secondParameterClass.equals(Spell.class)) {
//                List<Object> objectsList = new ArrayList<>();
//                HashMap<String, Object> spellsHashMap = new HashMap<>();
//
//                JSONObject jsonObject = (JSONObject) value;
//                jsonObject.forEach((k, v) -> {
//                    objectsList.add(v);
//                });
//
//                List<Object> objectList = returnSetterObject(secondParameterClass, objectsList);
//                objectList.forEach(o -> {
//                    spellsHashMap.put(((Spell) o).getSpellName(), o);
//                });
//                runSetter(c, object, methodString, spellsHashMap);
//            }
//
//        }
//        else if (findAllClasses("project/classes").contains(methodParameterType)) {
//
//
//
//            List<Object> objectsList = new ArrayList<>(Collections.singletonList((JSONObject) value));
//            runSetter(c, object, methodString, returnSetterObject(methodParameterType, objectsList).get(0));
//        }
//        else if(findAllClasses("project/enums").contains(c)) {
//            if(c.equals(Level.class)) {
//                ((JSONObject) value).forEach((k, v) -> {
//                    Enum<?> level = Enum.valueOf((Class<Enum>) c, k.toString());
//                    ((JSONObject) v).forEach((field, val) -> {
//                        runSetter(Level.class, level, methodString, val);
//                    });
//                });
//            }
//        }
//        else {
//            Enum<?> e = Enum.valueOf((Class<Enum>) methodParameterType, value.toString());
//            runSetter(c, object, methodString, e);
//        }
//    }

//    private static List<Class<?>> returnParametersClass(Method method) {
//        ParameterizedType pType = (ParameterizedType) method.getGenericParameterTypes()[0];
//        return Arrays.stream(pType.getActualTypeArguments()).map(type -> (Class<?>) type).collect(Collectors.toList());
//    }

//    @NotNull
//    private static List<Object> returnSetterObject(Class<?> objectClass, List<?> objectsList) {
//        List<Object> objectList = new ArrayList<>();
//
//        for (Object o : objectsList) {
//
//            Object obj = null;
//            if(objectClass.equals(Potion.class))
//                obj = Potion.builder().build();
//            else if(objectClass.equals(Spell.class)) {
//                obj = Spell.builder().build();
//            }
//            else if(objectClass.equals(Wand.class)) {
//                obj = Wand.builder().build();
//            }
//            else {
//                System.out.println("Error: " + objectClass + "has to be added");
//            }
//            JSONObject jsonObject = (JSONObject) o;
//
//            Object finalObj = obj;
//            returnStringSettersList(objectClass).forEach(ms -> {
//                String fieldString1 = ms.replace("set", "");
//                String fieldString2 = fieldString1.replaceFirst(fieldString1.substring(0, 1), fieldString1.substring(0, 1).toLowerCase());
//
//                runSetterMethod(objectClass, finalObj, ms, jsonObject.get(fieldString2));
//            });
//            objectList.add(obj);
//        }
//        return objectList;
//    }

//    private static void runSetter(Class<?> c, Object object, String methodString, Object value) {
//        try {
//            Objects.requireNonNull(getMethod(c, methodString)).invoke(object, value);
//        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
//            System.out.println("Error: " + methodString + " " + value);
////            e.printStackTrace();
//        }
//    }

//    public static Object runGetterMethod(Class<?> c, Object object, String methodString) {
//        Method method = Objects.requireNonNull(getMethod(c, methodString));
//        Object objectValue = null;
//
//        try {
//        objectValue = Objects.requireNonNull(getMethod(c, methodString)).invoke(object);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        return returnGetterObject(method, objectValue);
//    }

//    private static Object returnGetterObject(Method method, Object objectValue) {
//        if (method.getReturnType().equals(double.class) ||
//                method.getReturnType().equals(int.class) ||
//                method.getReturnType().equals(String.class) ||
//                method.getReturnType().equals(boolean.class)) {
//            return objectValue;
//        } else if(method.getReturnType().equals(double[].class)) {
//            List<Object> doubleList = new ArrayList<>();
//            Arrays.stream((double[]) objectValue).forEach(doubleList::add);
//            return doubleList;
//        } else if (method.getReturnType().equals(List.class)) {
//            return returnSerializableList(objectValue);
//        } else if (method.getReturnType().equals(HashMap.class)) {
//            return returnSerializableHashMap(objectValue);
//        } else if (findAllClasses("project/classes").contains(method.getReturnType())) {
//            return getJSONObject(method.getReturnType(), objectValue);
//        } else {
//            assert objectValue != null;
//            return objectValue.toString();
//        }
//    }

//    private static Serializable returnSerializableHashMap(Object objectValue) {
//        List<Object> objectList = new ArrayList<>();
//
//        assert objectValue != null;
//        ((HashMap<?, ?>) objectValue).forEach((k, v) -> objectList.add(v));
//        boolean containsClass = objectList.stream().anyMatch(o -> findAllClasses("project/classes").contains(o.getClass()));
//
//        if (containsClass) {
//            HashMap<Object, JSONObject> newHashMap = new HashMap<>();
//            ((HashMap<?, ?>) objectValue).forEach((k, v) -> newHashMap.put(k, getJSONObject(v.getClass(), v)));
//            return newHashMap;
//        } else {
//            return objectValue.toString();
//        }
//    }

//    @NotNull
//    private static Object returnSerializableList(Object objectValue) {
//        List<?> objectList = ((List<?>) objectValue);
//
//        assert objectList != null;
//        boolean containsClass = objectList.stream().anyMatch(o -> findAllClasses("project/classes").contains(o.getClass()));
//        boolean containsEnums = objectList.stream().anyMatch(o -> findAllClasses("project/enums").contains(o.getClass()));
//
//        if (containsClass) {
//            List<Object> newList = new ArrayList<>();
//
//            objectList.forEach(o -> newList.add(getJSONObject(o.getClass(), o)));
////            objectList.forEach(o -> newList.add(o.toString()));
//
//
//            return newList;
//        }
//        else if(containsEnums) {
//            System.out.println(objectValue);
//
//            List<Object> newList = new ArrayList<>();
//            objectList.forEach(o -> newList.add(o.toString()));
//
//            return newList;
//        }
//        else {
//            return objectValue;
//        }
//    }


//    public static List<Class<?>> findAllClasses(String packageName) {
////        String path = Objects.requireNonNull(GuiLauncherMain.class.getClassLoader().getResource(packageName)).toString();
////        System.out.println(path);
////        String path2 = Objects.requireNonNull(GuiLauncherMain.class.getClassLoader().getResource(packageName)).getFile();
////        System.out.println(returnFiles(path2));
////        File[] file = new File("target/classes/project/classes").listFiles();
////        System.out.println(Arrays.toString(file));
//
//        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
////        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path.replaceAll("[.]", "/"));
//        String packageNameMod = packageName.replace("/", ".");
//
//        assert stream != null;
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//
//        return reader.lines()
//                .filter(line -> line.endsWith(".class"))
//                .map(line -> getClass(line, packageNameMod))
//                .collect(Collectors.toList());
//    }

//    private static Class<?> getClass(String className, String packageName) {
//        try {
//            return Class.forName(packageName + "."
//                    + className.substring(0, className.lastIndexOf('.')));
//        } catch (ClassNotFoundException e) {
//            // handle the exception
//        }
//        return null;
//    }

    public static boolean checkString(String input) {
        return input.length() != 0;
    }

    public static boolean checkPositiveInt(String input) {
        if(checkInt(input)) {
            return Integer.parseInt(input) > 0;
        }
        else {
            return false;
        }
    }

    public static boolean checkInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String returnFileAttribute(String dir, String filenameCompressed, String attribute) {
        List<String> saves = returnSaves(filenameCompressed);

        File file = new File(dir + "/" + saves.get(0));

        FileTime fileTime = null;
        try {
            fileTime = (FileTime) Files.getAttribute(file.toPath(), attribute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Date fileDate = new Date(fileTime.toMillis());
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(fileDate);
    }

}
