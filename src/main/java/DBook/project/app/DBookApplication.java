package DBook.project.app;

import DBook.project.app.users.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class DBookApplication {
	private ArrayList<User> userArrayList;

	public DBookApplication(){
		this.userArrayList = new ArrayList<>();
	}

	public ArrayList<User> getUserArrayList() {
		return userArrayList;
	}

	public static void main(String[] args) {

		DBookApplication dBookApplication = new DBookApplication();

		SpringApplication.run(DBookApplication.class, args);
	}

}
