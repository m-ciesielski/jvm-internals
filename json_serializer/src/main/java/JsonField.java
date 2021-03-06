/**
 * Created by Mateusz on 09.03.2017.
 */
public class JsonField {
    private String name;
    private Object value;

    public JsonField(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
