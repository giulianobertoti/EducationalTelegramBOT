package hello;


import static spark.Spark.*;

import org.bson.Document;


public class MainServer {

	static Model model = new Model();
	
    public static void main(String[] args) {
       
        
        TelegramResponse api = new TelegramResponse(model);
        initialize();

        ProcessBuilder process = new ProcessBuilder();
        Integer myPort;
        if (process.environment().get("PORT") != null) {
            myPort = Integer.parseInt(process.environment().get("PORT"));
        } else {
            myPort = 8080;
        }
        port(myPort);



        
        post("/readMessages", (req, res) -> {
            api.readMessage(req.bodyAsBytes());
            return "Sucesso";
        });


    }
    
    public static void initialize(){
    	model.addResponse(new Document().append("request", "Ola").append("response", "Ola pra vc tambem"));
    }
}
