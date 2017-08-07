import com.pengrad.telegrambot.model.Update;

public class ControllerSearchSudent implements ControllerSearch{
	
	
	private Model model;
	private View view;
	
	public ControllerSearchSudent(Model model, View view){
		this.model = model; //connection Controller -> Model
		this.view = view; //connection Controller -> View
	}
	
	public void search(Update update){
		view.sendTypingMessage(update);
		model.searchStudent(update);
	}

}
