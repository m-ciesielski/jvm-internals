package oom;

/**
 * Created by Mateusz on 05.04.2017.
 */
public class OomError {

    public static void main(String[] args){
        byte[][] bytes = new byte[(int) Runtime.getRuntime().maxMemory()][1024];
        System.out.println(bytes.length);
    }
}
