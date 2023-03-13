package test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Vehicle {
    public static final Vehicle a = new Vehicle();
    public static final Vehicle b = new Vehicle();
    public static Vehicle c = new Vehicle();

    public static final List<Vehicle> VEHICLES;

    static {
        VEHICLES = new ArrayList<>();
        Field[] declaredFields = Vehicle.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Vehicle.class.isAssignableFrom(field.getType())) {
                    try {
                        VEHICLES.add((Vehicle) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

public class test {
    public static void main(String[] args) {
        System.out.println(Vehicle.VEHICLES);
    }
}