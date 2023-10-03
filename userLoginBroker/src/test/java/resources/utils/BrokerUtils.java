package resources.utils;

import com.google.gson.JsonObject;

public class BrokerUtils {

    public static JsonObject criarPostBroker(){
        JsonObject broker = new JsonObject();

        broker.addProperty("user_id", "a3d5f1a7-8a9b-4c6c-9d2e-26a3e7256bf3");
        broker.addProperty("cleansession", true);
        broker.addProperty("name", "John Doe");
        broker.addProperty("description", "Description for John Doe");
        broker.addProperty("broker_port", 1883);
        broker.addProperty("broker_host", "mqtt.example.com");
        broker.addProperty("username", "john_doe");
        broker.addProperty("password", "mySecretPassword");
        broker.addProperty("lastwilltopic", "lastwill_topic");
        JsonObject lastwillqos = new JsonObject();
        lastwillqos.addProperty("type", 1);
        lastwillqos.addProperty("enum", 0);
        lastwillqos.addProperty("names","AT_MOST_ONCE");
        broker.add("lastwillqos", lastwillqos);
        broker.addProperty("lastwillmessage", "Last Will Message");
        broker.addProperty("lastwillretain", true);
        broker.addProperty("keepalive", 300);

        return broker;
    }
    
    public static JsonObject criarPostBrokerInvalido(){
    	JsonObject broker = new JsonObject();

        broker.addProperty("user_id", "");
        broker.addProperty("cleansession", true);
        broker.addProperty("name", "");
        broker.addProperty("description", "");
        broker.addProperty("broker_port", 0);
        broker.addProperty("broker_host", "");
        broker.addProperty("username", "");
        broker.addProperty("password", "");
        broker.addProperty("lastwilltopic", "");
        JsonObject lastwillqos = new JsonObject();
        lastwillqos.addProperty("", 1);
        lastwillqos.addProperty("", "AT_LEAST_ONCE");
        broker.add("lastwillqos", lastwillqos);
        broker.addProperty("lastwillmessage", "");
        broker.addProperty("lastwillretain", true);
        broker.addProperty("keepalive", 300);

        return broker;
    }

}
