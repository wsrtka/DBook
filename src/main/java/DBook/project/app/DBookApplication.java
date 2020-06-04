package DBook.project.app;

import DBook.project.app.book.Book;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Offer;
import DBook.project.app.users.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.driver.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class DBookApplication {

	private ArrayList<User> userArrayList;
	private static Driver driver;

	public DBookApplication(){
		this.userArrayList = new ArrayList<>();
		driver = this.initializeDriver();
		this.setupIds();
	}

	private Driver initializeDriver(){
		JSONParser parser = new JSONParser();
		JSONObject auth = new JSONObject();

		try(FileReader reader = new FileReader("auth.json")){
			auth = (JSONObject) parser.parse(reader);
		}
		catch (ParseException e){
			e.printStackTrace();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}

		String username = (String) auth.get("username");
		String password = (String) auth.get("password");
		String uri = "bolt://" + ((String) auth.get("host")) + ":" + ((String) auth.get("port"));

		DBDriver dbd = new DBDriver(uri, username, password);

		return dbd.getDriver();

	}

	private void setupIds(){

		try(Session s = driver.session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
			s.readTransaction(
					tx -> {
						new Book("", 0).setupIdGenerator(tx);
						new Invoice().setupIdGenerator(tx);
						new Offer().setupIdGenerator(tx);
						new User("","","").setupIdGenerator(tx);
						return 0;
					}
			);
			s.writeTransaction(
					tx -> {
						this.setupIndex("Book", tx);
						this.setupIndex("Invoice", tx);
						this.setupIndex("Offer", tx);
						this.setupIndex("User", tx);
						return 0;
					}
			);
		}

	}

	private void setupIndex(String className, Transaction tx){

		String query = "CREATE INDEX ON :" + className + "(" + className.toLowerCase() + "id)";

		tx.run(query);

	}

	public ArrayList<User> getUserArrayList() {
		return userArrayList;
	}

	public Driver getDriver(){
		return driver;
	}

	public static void main(String[] args) {

		DBookApplication dBookApplication = new DBookApplication();

		SpringApplication.run(DBookApplication.class, args);

		//miejsce na dopisanie user interface kiedyś


	}

}
