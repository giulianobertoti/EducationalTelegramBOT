package hello;
import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.request.SendMessage;

import org.bson.Document;
import org.json.JSONObject;
import java.io.*;



public class TelegramResponse {
    
	TelegramBot bot = TelegramBotAdapter.build("226960242:AAG869_fYHCBf59oPW74ET7KirEDKiHT7wo");//passo 1 - token do Bot criado no usuario BotFather - dei /newbot - e setei nome de usuario (educational_bot) e de exibicao (Educational Bot)

	Model model;
	
    public TelegramResponse(Model model){
    	this.model = model;
    }
    
    protected void readMessage(byte[] body) throws IOException  {

    	

    	
        try {

        	
        	
            String responseJSON = new String(body, "UTF-8");
            JSONObject objectJSON = new JSONObject(responseJSON);
            String usersMessage = objectJSON.getJSONObject("message").getString("text");
            String chatID =  Integer.toString(objectJSON.getJSONObject("message").getJSONObject("chat").getInt("id"));

            String response = model.searchResponse(new Document().append("request", usersMessage));
            
            if(!response.equals("null")){
            	bot.execute(new SendMessage(chatID, response));
            } else {
            	bot.execute(new SendMessage(chatID, usersMessage));
            }
            
            

        } catch (Exception ex) {
        	bot.execute(new SendMessage("203367786",ex.getMessage()));//passo 2 - se der alguma merda, me avisa no meu id giulianobertoti 203367786 - descobri este id no postman enviando a url api.telegram.org/bot226960242:AAG869_fYHCBf59oPW74ET7KirEDKiHT7wo/getUpdates - lembrando que o getUpdates soh funciona neste momento porque eu ainda nao indiquei ao telegram o server que vai tratar as mensagens - depois que eu indicar, estes metodos do telegram para responder mensagens do bot nao funcionarao mais
        }
    }

    //passo 3 - setar no postman via ***POST*** o servidor que vai responder as mensagens do bot
    // api.telegram.org/bot226960242:AAG869_fYHCBf59oPW74ET7KirEDKiHT7wo/setWebHook?url=https://educationalbot.herokuapp.com/readMessages
}
