package DBook.project.app;

import DBook.project.app.users.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.driver.Driver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class DBookApplication {

	private ArrayList<User> userArrayList;
	private Driver driver;

	public DBookApplication(){
		this.userArrayList = new ArrayList<>();
		this.driver = this.initializeDriver();
	}

	private Driver initializeDriver(){
		JSONParser parser = new JSONParser();
		JSONObject auth = new JSONObject();

		try{
			auth = (JSONObject) parser.parse("../../../../../../auth.json");
		}
		catch (ParseException e){
			e.printStackTrace();
		}

		String username = (String) auth.get("username");
		String password = (String) auth.get("password");
		String uri = ((String) auth.get("host")) + ((String) auth.get("port"));

		DBDriver dbd = new DBDriver(uri, username, password);

		return dbd.getDriver();

	}

	public ArrayList<User> getUserArrayList() {
		return userArrayList;
	}

	public static void main(String[] args) {

		DBookApplication dBookApplication = new DBookApplication();

		SpringApplication.run(DBookApplication.class, args);


	}

}
