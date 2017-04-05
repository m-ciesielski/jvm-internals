package rest_api;

import rest_api.annotations.Measure;

import static spark.Spark.*;


public class Api {


    @Measure
    private static String hello(){
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return "Hello world";
    }

    private static String gutenTag(){
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return "Guten Tag";
    }


    public static void main(String[] args){
        get("/hello", (req, res) -> hello());
        get("/guten", (req, res) -> gutenTag());
    }
}
