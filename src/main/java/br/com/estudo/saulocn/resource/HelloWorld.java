package br.com.estudo.saulocn.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("hello")
public class HelloWorld {

    @Path("world")
    @GET
    public String helloWorld(){
        return "Hello World";
    }
}
