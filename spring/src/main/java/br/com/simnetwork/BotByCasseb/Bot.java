package br.com.simnetwork.BotByCasseb;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

//acessar o bot father
//crir um novo bot
//pegar o token dado pelo bot father e inserir na linha 22

@Component
public class Bot {

	// Cria��o do objeto bot com as informa��es de acesso
	TelegramBot bot = TelegramBotAdapter.build("440580164:AAHuhN2WOh2ldrxcpnw_L8uiliN9AQ2NFKU");

	// objeto respons�vel por receber as mensagens
	GetUpdatesResponse updatesResponse;
	// objeto respons�vel por gerenciar o envio de respostas
	SendResponse sendResponse;
	// objeto respons�vel por gerenciar o envio de a��es do chat
	BaseResponse baseResponse;

	@PostConstruct
	public void ouvirUpdates() throws InterruptedException {

		System.out.println("Em operação");
		
		// controle de off-set, isto �, a partir deste ID ser� lido as mensagens
		// pendentes na fila
		int m = 0;

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {

			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();

			// an�lise de cada a��o da mensagem
			for (Update update : updates) {

				// atualiza��o do off-set
				m = update.updateId() + 1;

				if (update.callbackQuery() != null) {

					sendResponse = bot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
							update.callbackQuery().data()));
				} else {
					System.out.println("Recebendo mensagem:" + update.message().text());

					// envio de "Escrevendo" antes de enviar a resposta
					baseResponse = bot
							.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					// verifica��o de a��o de chat foi enviada com sucesso
					System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());

					// enviando número de contato recebido
					if (update.message().contact() != null) {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Número " + update.message().contact().phoneNumber() + " enviado"));
					} else if (update.message().location() != null) {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Latitude " + update.message().location().latitude() + " enviada"));
					}

					if (update.message().text() != null) {

						// Criação de Keyboard
						if (update.message().text().equals("/keyboard")) {
							sendResponse = bot.execute(new SendMessage(update.message().chat()
									.id(), "Inserindo keyboard").replyMarkup(new ReplyKeyboardMarkup(
											new String[] { "Primeira linha button1", "Primeira linha button2" },
											new String[] { "Segunda linha button1", "Sergunda linha button2" })));

							// Remover keyboard do bot
						} else if (update.message().text().equals("/limparkeyboard")) {
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "limpando keyboard")
											.replyMarkup(new ReplyKeyboardRemove()));

							// Pedir contato
						} else if (update.message().text().equals("/pedircontato")) {
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "pedindo contato")
									.replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton[] {
											new KeyboardButton("Fornecer contato").requestContact(true) })));

							// Pedir localização
						} else if (update.message().text().equals("/pedirlocalizacao")) {
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "pedindo localização")
											.replyMarkup(new ReplyKeyboardMarkup(
													new KeyboardButton[] { new KeyboardButton("Fornecer localização")
															.requestLocation(true) })));
							// keyboard inline - URL
						} else if (update.message().text().equals("/keyboardInlineUrl")) {
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "Inserindo keyboard inline")
											.replyMarkup(new InlineKeyboardMarkup(
													new InlineKeyboardButton[] { new InlineKeyboardButton("url")
															.url("http://www.google.com.br") })));
							// keyboard inline - callBackData
						} else if (update.message().text().equals("/keyboardInlineCallBack")) {
							sendResponse = bot
									.execute(new SendMessage(update.message().chat().id(), "Inserindo keyboard inline")
											.replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton[] {
													new InlineKeyboardButton("Texto de apresentação")
															.callbackData("Texto enviado pelo callback")

							})));
						} else if (update.message().text().equals("/updateNormalMessage")) {
							sendResponse = bot.execute(
									new SendMessage(update.message().chat().id(), "Mensgem original aguarde..."));
							new Thread();
							Thread.sleep(2000);
							baseResponse = bot.execute(new EditMessageText(update.message().chat().id(),
									sendResponse.message().messageId(), "Mensagem editada"));

						} else {
							// envio da mensagem de opções
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Digite uma das seguintes opções:" + "\n /keyboard" + "\n /limparkeyboard"
											+ "\n /pedircontato" + "\n /pedirlocalizacao" + "\n /keyboardInlineUrl"
											+ "\n /keyboardInlineCallBack" + "\n /updateNormalMessage"));
						}

						// verifica��o de mensagem enviada com sucesso
						System.out.println("Mensagem Enviada?" + sendResponse.isOk());

					}

				}

			}

		}

	}

}
