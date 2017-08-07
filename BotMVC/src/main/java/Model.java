import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{
	
	private List<Observer> observers = new LinkedList<Observer>();
	
	private List<Student> students = new LinkedList<Student>();
	private List<Teacher> teachers = new LinkedList<Teacher>();
	
	private static Model uniqueInstance;
	
	private Model(){}
	
	public static Model getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}
	
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	
	public void notifyObservers(long chatId, String studentsData){
		for(Observer observer:observers){
			observer.update(chatId, studentsData);
		}
	}
	
	public void addStudent(Student student){
		this.students.add(student);
	}
	
	public void addTeacher(Teacher teacher){
		this.teachers.add(teacher);
	}
	
	public void searchStudent(Update update){
		String studentsData = null;
		for(Student student: students){
			if(student.getName().equals(update.message().text())){
				studentsData = student.getAcademicNumber();
			}
		}
		
		if(studentsData != null){
			this.notifyObservers(update.message().chat().id(), studentsData);
		} else {
			this.notifyObservers(update.message().chat().id(), "Student not found");
		}
		
	}
	
	public void searchTeacher(Update update){
		String teachersData = null;
		for(Teacher teacher:teachers){
			if(teacher.getName().equals(update.message().text())) teachersData = teacher.getField();
		}
		
		if(teachersData != null){
			this.notifyObservers(update.message().chat().id(), teachersData);
		} else {
			this.notifyObservers(update.message().chat().id(), "Teacher not found");
		}
		
	}

}
