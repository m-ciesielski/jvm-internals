import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Mateusz on 08.03.2017.
 */
class JsonSerializerTest {

    class MiniDummyParent {
        public String str = "foo";
    }

    class MiniDummy extends MiniDummyParent {
        public int x = 1;
        public int y = 2;
    }

    class Dummy {
        public int x;
        public Integer y;
        public int[] intArray = {1,2,3};
        public String str = "foo";
        public List<String> strList = Arrays.asList("foo", "bar");
        public MiniDummy nestedObject = new MiniDummy();
        public List<MiniDummy> nestedObjects = Arrays.asList(new MiniDummy(), new MiniDummy());
        public boolean isDummy = true;

        private int privateZ;

        public void dummyMethod() {};
        public int getPrivateZ(){return privateZ;}
    }

    @Test
    void checkJsonSerialization() {
        try {
            String dummyJson = JsonSerializer.objectToJson(new Dummy());
            System.out.println(dummyJson);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Dummy deserializedDummy = gson.fromJson(dummyJson, Dummy.class);
            assertNotNull(deserializedDummy);
        }
        catch (IllegalAccessException|InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void compareSerializationTimeWithGson() {
        try {
            Dummy dummy = new Dummy();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            long startTime = System.nanoTime();
            JsonSerializer.objectToJson(dummy);
            long endTime = System.nanoTime();
            long jsonSerializerDuration = (endTime - startTime);

            startTime = System.nanoTime();
            gson.toJson(dummy);
            endTime = System.nanoTime();
            long gsonDuration = (endTime - startTime);

            System.out.println("Time diff between JsonSerializer and Gson serialization: "
                    + (jsonSerializerDuration - gsonDuration));

            // Check if execution time does not differ by an order of magnitude
            assertTrue(Math.abs(jsonSerializerDuration - gsonDuration) < 1000000000);
        }
        catch (IllegalAccessException|InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

}