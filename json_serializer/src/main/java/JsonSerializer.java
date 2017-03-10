import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.ReflectionUtils;

import java.beans.Introspector;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class JsonSerializer {

    // TODO: boolean, char
    // TODO: wrappers
    private static final Class [] NUMERIC_PRIMITIVES = {
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class
    };

    private static final Class [] NUMERIC_WRAPPERS = {
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
    };

    //private static boolean isFieldClassInClassArra

    private static boolean isClassNumeric(Class c){
        return ArrayUtils.contains(NUMERIC_PRIMITIVES, c) || ArrayUtils.contains(NUMERIC_WRAPPERS, c);
    }

    private static boolean isClassArray(Class c){
        return c.isArray();
    }

    private static boolean isClassList(Class c){
        return List.class.isAssignableFrom(c);
    }

    private static boolean isClassStringOrChar(Class c){
        return c.equals(String.class) || c.equals(char.class) || c.equals(Character.class);
    }

    private static boolean isClassBoolean(Class c){
        return c.equals(boolean.class) || c.equals(Boolean.class);
    }

    private static String primitiveToJson(Object primitive){
        Class primitiveClass = primitive.getClass();
        if (isClassNumeric(primitiveClass) || isClassBoolean(primitiveClass)) {
            return primitive.toString();
        }
        else {
            return '\"' + primitive.toString() + '\"';
        }
    }

    private static String collectionItemToJson(Object item) throws IllegalAccessException, InvocationTargetException{
        if (ClassUtils.isPrimitiveOrWrapper(item.getClass()) || item.getClass().equals(String.class)){
            return primitiveToJson(item);
        }
        else{
            return objectToJson(item);
        }
    }

    private static String arrayToJson(Object array) throws IllegalAccessException, InvocationTargetException{
        StringBuilder arrayJsonBuilder = new StringBuilder();
        arrayJsonBuilder.append('[');
        if (Array.getLength(array) > 0){
            arrayJsonBuilder.append(collectionItemToJson(Array.get(array, 0)));
            for (int i = 1; i < Array.getLength(array); ++i){
                Object item = Array.get(array, i);
                arrayJsonBuilder.append(", ");
                arrayJsonBuilder.append(collectionItemToJson(item));
            }
        }
        arrayJsonBuilder.append(']');
        return arrayJsonBuilder.toString();
    }

    private static String listToJson(List list) throws IllegalAccessException, InvocationTargetException{
        StringBuilder listJsonBuilder = new StringBuilder();
        listJsonBuilder.append('[');
        if (list.size() > 0){
            listJsonBuilder.append(collectionItemToJson(list.get(0)));
            for (int i = 1; i < list.size(); ++i){
                Object item = list.get(i);
                listJsonBuilder.append(", ");
                listJsonBuilder.append(collectionItemToJson(item));
            }
        }
        listJsonBuilder.append(']');
        return listJsonBuilder.toString();
    }

    private static String fieldToJson(JsonField field) throws IllegalAccessException, InvocationTargetException{
        String name = field.getName();
        Object value = field.getValue();
        if (value == null){
            return "'" + name + "': " + value;
        }

        Class fieldClass = value.getClass();
        if (isClassNumeric(fieldClass) || isClassBoolean(fieldClass)){
            return "'" + name + "': " + value;
        }
        if (isClassArray(fieldClass)){
            return "'" + name + "': " + arrayToJson(value);
        }
        if (isClassList(fieldClass)){
            return "'" + name + "': " + listToJson((List) value);
        }
        if (isClassStringOrChar(fieldClass)){
            return '\'' + field.getName() + "': " + '\"' + value.toString() + '\"';
        }
        else {
            return '\'' + field.getName() + "': " + objectToJson(value);
        }
    }

    private static Field[] getDeclaredFieldsWithoutThis(Object object){
        Class objectClass = object.getClass();
        return Arrays.stream(objectClass.getFields())
                .filter(f -> ! f.getName().contains("this$0"))
                .toArray(Field[]::new);
    }

    private static List<JsonField> getPublicFields(Object object) throws IllegalAccessException{
        Class objectClass = object.getClass();
        List<JsonField> jsonFields = new ArrayList<>();
        for(Field field : objectClass.getFields()){
            if(! field.getName().equals("this$0")){
                jsonFields.add(new JsonField(field.getName(), field.get(object)));
            }
        }
        return jsonFields;
    }

    private static String getFieldNameFromGetter(Method getter){
        return Introspector.decapitalize(getter.getName().replace("get", ""));
    }

    private static List<JsonField> getGetterFields(Object object) throws InvocationTargetException, IllegalAccessException{
        Class objectClass = object.getClass();
        Set<Method> getters = ReflectionUtils.getAllMethods(objectClass,
                ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        List<JsonField> jsonFields = new ArrayList<>();
        for (Method getter: getters){
            jsonFields.add(new JsonField(getFieldNameFromGetter(getter), getter.invoke(object)));
        }
        return jsonFields;
    }

    public static String objectToJson(Object object) throws IllegalAccessException, InvocationTargetException {
        List<JsonField> getterFields = getGetterFields(object);
        List<JsonField> publicFields = getPublicFields(object);
        List<JsonField> fields = new ArrayList<>();
        Stream.of(getterFields, publicFields).forEach(fields::addAll);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append('{');
        if (fields.size() > 0){
            jsonBuilder.append(fieldToJson(fields.get(0)));
        }
        for(int i = 1; i < fields.size(); ++i){
            jsonBuilder.append(", ");
            jsonBuilder.append(fieldToJson(fields.get(i)));
        }
        jsonBuilder.append('}');

        return jsonBuilder.toString();
    }
}
