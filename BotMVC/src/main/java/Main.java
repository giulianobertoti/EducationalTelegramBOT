

public class Main {

	private static Model model;
	
	public static void main(String[] args) {

		model = Model.getInstance();
		initializeModel(model);
		View view = new View(model);
		model.registerObserver(view); //connection Model -> View
		view.receiveUsersMessages();

	}
	
	public static void initializeModel(Model model){
		model.addStudent(new Student("joao", "111"));
		model.addStudent(new Student("thomas", "222"));
		
		model.addTeacher(new Teacher("percy", "computing"));
	}

}