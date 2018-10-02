package edu.csupomona.cs480.controller;

import java.util.List;
import java.util.Random;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
 
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.csupomona.cs480.App;
import edu.csupomona.cs480.data.GpsProduct;
import edu.csupomona.cs480.data.User;
import edu.csupomona.cs480.data.provider.GpsProductManager;
import edu.csupomona.cs480.data.provider.UserManager;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 * This is the controller used by Spring framework.
 * <p>
 * The basic function of this controller is to map
 * each HTTP API Path to the correspondent method.
 *
 */

@RestController
public class WebController {

	/**
	 * When the class instance is annotated with
	 * {@link Autowired}, it will be looking for the actual
	 * instance from the defined beans.
	 * <p>
	 * In our project, all the beans are defined in
	 * the {@link App} class.
	 */
	@Autowired
	private UserManager userManager;
	@Autowired
	private GpsProductManager gpsProductManager;

	/**
	 * This is a simple example of how the HTTP API works.
	 * It returns a String "OK" in the HTTP response.
	 * To try it, run the web application locally,
	 * in your web browser, type the link:
	 * 	http://localhost:8080/cs480/ping
	 */
	@RequestMapping(value = "/cs480/ping", method = RequestMethod.GET)
	String healthCheck() {
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		return "OK-CS480-Demo";
	}

	/**
	 * This is the HTTP API created by Bryan Ayala
	 * from team Future Vision. It returns the string
	 * "My name is Brian with a Y!!!" in the HTTP response.
	 * To try it, run the App.java file as a Java Application
	 * locally. Check the URL: http://localhost:8080/cs480/bryanayala
	 * @return "My name is Brian with a Y"
	 */
	@RequestMapping(value = "/cs480/bryanayala", method = RequestMethod.GET)
	String bryanAyala() {
		//I created a HTTP API that returns my name. Check
		// with the URL: http://localhost:8080/bryanayala

		//Asignment 4. I used Jsoup, and external library, to parse HTML into JAVA and display it with my HTTP response
		String htmlString = "<html><head><title>My name is Brian with a Y.</title></head>"  + "<body>I used Jsoup to parse HTML into JAVA</body></html>";

		Document doc = Jsoup.parse(htmlString);
		String title = doc.title();
		String body = doc.body().text();

		String parsedString = "" + title + "\n" + body;

		return parsedString;
	}

	/*********** Assignment 3: My HTTP API **********/
	/**
	 * This method simple returns boolean depending on the random number
	 * is even (true) or odd (false). Created by Nathaniel Dao. It returns boolean
	 * true if the random generated number is even and false otherwise.
	 * @return boolean
	 */
	@RequestMapping(value = "/cs480/ndao", method = RequestMethod.GET)
	boolean returnTrueOrFalse() {
		Random rand = new Random();

		int  n = rand.nextInt(50) + 1;
		// You can replace this with other string,
		// and run the application locally to check your changes
		// with the URL: http://localhost:8080/
		if (n % 2 == 0)
			return true;
		else
			return false;
	}

	/**
	 * This API returns a String "Hello" in the HTTP response.
	 */
	@RequestMapping(value = "/cs480/hello", method = RequestMethod.GET)
	String hello() {
		return "Hello";
	}

	/**
	 * This API returns a String "Hello World" in the HTTP response.
	 */
	@RequestMapping(value = "/cs480/hWorld", method = RequestMethod.GET)
	String helloWorld() {
		return "Hello World";
	}

	/**
	 * This is a simple example of how to use a data manager
	 * to retrieve the data and return it as an HTTP response.
	 * <p>
	 * Note, when it returns from the Spring, it will be
	 * automatically converted to JSON format.
	 * <p>
	 * Try it in your web browser:
	 * 	http://localhost:8080/cs480/user/user101
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.GET)
	User getUser(@PathVariable("userId") String userId) {
		User user = userManager.getUser(userId);
		return user;
	}

	/**
	 * This is an example of sending an HTTP POST request to
	 * update a user's information (or create the user if not
	 * exists before).
	 *
	 * You can test this with a HTTP client by sending
	 *  http://localhost:8080/cs480/user/user101
	 *  	name=John major=CS
	 *
	 * Note, the URL will not work directly in browser, because
	 * it is not a GET request. You need to use a tool such as
	 * curl.
	 *
	 * @param id
	 * @param name
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.POST)
	User updateUser(
			@PathVariable("userId") String id,
			@RequestParam("name") String name,
			@RequestParam(value = "major", required = false) String major) {
		User user = new User();
		user.setId(id);
		user.setMajor(major);
		user.setName(name);
		userManager.updateUser(user);
		return user;
	}

	/**
	 * This API deletes the user. It uses HTTP DELETE method.
	 *
	 * @param userId
	 */
	@RequestMapping(value = "/cs480/user/{userId}", method = RequestMethod.DELETE)
	void deleteUser(
			@PathVariable("userId") String userId) {
		userManager.deleteUser(userId);
	}

	/**
	 * This API lists all the users in the current database.
	 *
	 * @return
	 */
	@RequestMapping(value = "/cs480/users/list", method = RequestMethod.GET)
	List<User> listAllUsers() {
		return userManager.listAllUsers();
	}

	@RequestMapping(value = "/cs480/gps/list", method = RequestMethod.GET)
	List<GpsProduct> listGpsProducts() {
		return gpsProductManager.listAllGpsProducts();
	}

	/*********** Web UI Test Utility **********/
	/**
	 * This method provide a simple web UI for you to test the different
	 * functionalities used in this web service.
	 */
	@RequestMapping(value = "/cs480/home", method = RequestMethod.GET)
	ModelAndView getUserHomepage() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("users", listAllUsers());
		return modelAndView;
	}

	/*********** Assignment 4: Add a 3rd party Java library and function to use that library  **********/
	/**
	 * This method simple returns boolean depending on the random number is even (true) or odd (false)
	 * @return
	 */
	@RequestMapping(value = "/cs480/gson", method = RequestMethod.GET)
	public String activate(){
		Myself me = new Myself();
		me.name = "Nathaniel Dao";
		me.hobbies = "Playing Game, Reading Manga, Coding Game";
		me.major = "Computer Science";
		me.unitTaken = 20;
		me.zodiacSign = "Leo";
		GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    return gson.toJson(me);
	}

	/**
	 * This method creates a list of items and filters the list by a
	 * specific pattern.
	 *
	 * @return str
	 */
	@RequestMapping(value = "/cs480/guava", method = RequestMethod.GET)
	String filter() {
		List<String> items = Lists.newArrayList("avocado", "banana",
				"coconut", "durian", "eggplant");
		Collection<String> result = Collections2.filter(items,
				Predicates.containsPattern("a"));
		String str = "";
		for (String item: result) {
			str += (item + "<br>");
		}
		return str;
	}

	/**
	 * This method is a text stamp remover for PDF files
	 */
	@RequestMapping(value = "/cs480/commonsIO", method = RequestMethod.GET)

	public static Iterator<File> getFiles(
	        @NotNull String idn,
	        @NotNull boolean recursive) {
	    File dirI = new File(idn);
	    if (dirI.exists() && dirI.isDirectory()) {
	        return FileUtils.iterateFiles(dirI, new String[]{"pdf"}, recursive);
	    } else
	        return null;
	}
	@RequestMapping(value = "/cs480/commonIO", method = RequestMethod.GET)
	@SuppressWarnings("deprecation")
	public static void readURL() throws MalformedURLException, IOException {
	       InputStream in = new URL("http://commons.apache.org").openStream();
	       try {
	           System.out.println(IOUtils.toString(in));
	       } finally {
	           IOUtils.closeQuietly(in);
	       }
	   }

}
