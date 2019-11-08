package webServerV3;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.event.Event;

public class SimpleHTTPServerWithSSL implements Runnable {
	static BLFacadePublic bl = new BLFacadePublic();
	static final File WEB_ROOT = new File("./resources/templates2/");
	static final String DEFAULT_FILE = "/landingPage";
	static final String FILE_NOT_FOUND = "/404.html";
	static final String METHOD_NOT_SUPPORTED = "/404.html";
	static Boolean MAINTENANCE_MODE = false;
	// port to listen connection
	static final int PORT = 443;

	// verbose mode
	static final boolean verbose = true;
	// business Logic
	// Client Connection via Socket Class
	private SSLSocket connect;
	private static SSLServerSocket serverConnect;

	public SSLSocket getConnect() {
		return connect;
	}

	public SimpleHTTPServerWithSSL(SSLSocket c) {
		connect = c;
	}

	public static void main(String[] args) {

		try {
			KeyStore keyStore = null;
			try {
				keyStore = KeyStore.getInstance("PKCS12");
				char[] keyStorePassword = "patata".toCharArray();
				InputStream keyStoreData = new FileInputStream("./resources/certificates/origin2.pfx");
				keyStore.load(keyStoreData, keyStorePassword);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Create key manager
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			keyManagerFactory.init(keyStore, "patata".toCharArray());
			KeyManager[] km = keyManagerFactory.getKeyManagers();

			// Create trust manager
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(keyStore);
			TrustManager[] tm = trustManagerFactory.getTrustManagers();

			// Initialize SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLSv1");
			sslContext.init(km, tm, null);

			SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
			serverConnect = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			// we listen until user halts server execution
			while (true) {
				SimpleHTTPServerWithSSL myServer = new SimpleHTTPServerWithSSL((SSLSocket) serverConnect.accept());
				String ip = myServer.getConnect().getRemoteSocketAddress().toString();
				System.out.println(ip);
				if (verbose) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}

				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				System.out.println(thread.getName());
				thread.start();
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Socket error");
		}
	}

	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null;
		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
		String fileRequested = null;

		try {
			// we read characters from the client via input stream on the socket
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			// we get character output stream to client (for headers)
			out = new PrintWriter(connect.getOutputStream());
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());

			// get first line of the request from the client
			String input = in.readLine();
			if (input == null) {
				return;
			}
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			// we get file requested
			fileRequested = parse.nextToken();
			System.out.println(input);
			// we support only GET and HEAD methods, we check
			if (!method.equals("GET") && !method.equals("POST")) {
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");
				}
				// we return the not supported file to the client
				File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
				int fileLength = (int) file.length();
				String contentMimeType = "text/html";
				// read content to return to client
				byte[] fileData = readFileData(file, fileLength);

				// we send HTTP Headers with data to client
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: Java HTTP Server from Carlos : 1.0");
				out.println("Date: " + new Date());
				out.println("Content-type: " + contentMimeType);
				out.println("Content-length: " + fileLength);
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				// file
				dataOut.write(fileData, 0, fileLength);
				dataOut.flush();

			} else {
				if (method.equals("GET")) {
					String type = "";
					if (fileRequested.endsWith(".jpg")) {
						type = "image/jpeg";
					} else if (fileRequested.endsWith(".png")) {
						type = "image/png";
					} else if (fileRequested.endsWith(".pdf")) {
						type = "application/pdf";
					} else if (fileRequested.endsWith(".js")) {
						type = "application/javascript";
					}
					if (!type.equals("")) {
						File file2 = new File(WEB_ROOT, fileRequested);
						int fileLength2 = (int) file2.length();
						byte[] fileData2 = readFileData(file2, fileLength2); // send HTTP Headers
						out.println("HTTP/1.1 200 OK");
						out.println("Server: Java HTTP Server from Carlos : 1.0");
						out.println("Date: " + new Date());
						out.println("Content-type: " + type);
						out.println("Content-length: " + fileLength2);
						out.println("Content-Disposition: inline; filename=\"" + fileRequested);
						out.println(); // blank line between headers and content, very important !
						out.flush(); // flush character output stream buffer
						dataOut.write(fileData2, 0, fileLength2);
						dataOut.flush();
						return;
					}
					getMethod(fileRequested, in, out, dataOut);
				} else {
					postMethod(fileRequested, in, out, dataOut);
				}

			}

		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}

			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}

	}

	/**
	 * This functions reads a file a convert it to bytes
	 * 
	 * @param file       file to read
	 * @param fileLength length of the file
	 * @return file in bytes
	 * @throws IOException exception
	 */
	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];

		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}

		return fileData;
	}

	/**
	 * This function returns the file in string format in bytes
	 * 
	 * @param file to be read
	 * @return file in bytes
	 * @throws IOException exception
	 */
	private byte[] readFileData(String file) throws IOException {
		byte[] fileData = file.getBytes();
		return fileData;
	}

	/**
	 * If the file is not found
	 * 
	 * @param out           to send HTTP header through the socket
	 * @param dataOut       to send some information though the socket
	 * @param fileRequested file that the user request
	 * @throws IOException exception
	 */
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {

		File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);

		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from Carlos : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer

		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();

		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}
	// --------------------------- GET -------------------------------

	/**
	 * This function parse the GET request
	 * 
	 * @param fileRequested the file that the user request
	 * @param in            the information that comes through the socket
	 * @param out           to send the HTTP header through the socker
	 * @param dataOut       to send some information throug the socket
	 */
	private void getMethod(String fileRequested, BufferedReader in, PrintWriter out, BufferedOutputStream dataOut) {
		String username = "";
		String token = "";
		String content = "";
		String defaultLanguage = "EN";
		boolean setLanguageCookie = true;
		Hashtable<String, String> cookies = new Hashtable<String, String>();
		String code = "";
		try {
			if (!MAINTENANCE_MODE || fileRequested.contains("/administration")) {

				// -------- READING COOKIE AND BODY OF THE HTTP (THE FORM) -----------
				String line;
				String cookie = "";
				while ((line = in.readLine()) != null) {
					if (line.contains("cookie:")) {
						cookie = line;
					}
					if (line.length() == 0) {
						break;
					}
				}
				// add cookies values to the hashtable
				if (!cookie.equals("")) {
					String[] cookiehelper = cookie.split(" ");
					for (int i = 1; i < cookiehelper.length; i++) {
						String[] cookiesplit = cookiehelper[i].split("=");
						try {
							if (cookiehelper[i].endsWith(";")) {
								cookies.put(cookiesplit[0], cookiesplit[1].split(";")[0]);
							} else {
								cookies.put(cookiesplit[0], cookiesplit[1]);
							}
						} catch (Exception e) {

						}
					}
				}
				System.out.println("Cookies");
				System.out.println(cookie);
				// set up properly the username and token
				if (cookies.containsKey("username")) {
					username = cookies.get("username");
				}

				if (cookies.containsKey("token")) {
					token = cookies.get("token");
				}

				if (cookies.containsKey("language")) {
					defaultLanguage = cookies.get("language");
					setLanguageCookie = false;
				}else {
					cookies.put("language", "EN");
				}


				// --------- END READING COOKIE AND BODY OF THE HTTP (THE FORM) ---------
				// check if the user is active else send back him to the login page or admin
				// page depending in the path he is
				if (bl.getUsers().activeUser(username, token)) {
					System.out.println("GET: Logged user");
					if (fileRequested.equals("/administration/adminlogin")) {
						code = RewriteAdminHTML.adminMain(bl.getPrivateAPI(username, token));
					}
					if (fileRequested.startsWith("/administration")) {
						if (bl.getUsers().checkUserRank(username, token) == 3) {
							code = AdminPostGetVector.adminGetRequest(fileRequested, cookies, bl);
						}
					} else if (fileRequested.equals("/changeLanguage/ES")) {
						setLanguageCookie = true;
						defaultLanguage = "ES";
						UserPostGetVector.currentPage.update(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Last Available Events", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"),
										UserPostGetVector.EVENTES_PER_PAGE),
								new LinkedList<Event>(), new LinkedList<Event>(), "", "", "ES");
						code = RewriteUserHTML.rewriteMainpage(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Ultimos Eventos disponibles", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"), UserPostGetVector.EVENTES_PER_PAGE), new LinkedList<Event>(), new LinkedList<Event>(), "", "",
								"ES");
					} else if (fileRequested.equals("/changeLanguage/EUS")) {
						setLanguageCookie = true;
						defaultLanguage = "EUS";
						UserPostGetVector.currentPage.update(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Last Available Events", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"),
										UserPostGetVector.EVENTES_PER_PAGE),
								new LinkedList<Event>(), new LinkedList<Event>(), "", "", "EUS");
						code = RewriteUserHTML.rewriteMainpage(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Azken Eventuak ezkuragai", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"), UserPostGetVector.EVENTES_PER_PAGE), new LinkedList<Event>(), new LinkedList<Event>(), "", "",
								"EUS");
					} else if (fileRequested.equals("/changeLanguage/EN")) {
						setLanguageCookie = true;
						UserPostGetVector.currentPage.update(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Last Available Events", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"),
										UserPostGetVector.EVENTES_PER_PAGE),
								new LinkedList<Event>(), new LinkedList<Event>(), "", "", "EN");
						code = RewriteUserHTML.rewriteMainpage(
								bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), "Sports",
								"Last Available Events", "",
								bl.getEvents().getNEventsByCanBet(cookies.get("username"), UserPostGetVector.EVENTES_PER_PAGE), new LinkedList<Event>(), new LinkedList<Event>(), "", "",
								"EN");
						defaultLanguage = "EN";
					} else {
						code = UserPostGetVector.userGetRequest(fileRequested, cookies, bl);
					}

				} else {
					// sending to the login page depending where they are
					if (fileRequested.startsWith("/administration")) {
						code = RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminLogin.html");
					} else if (fileRequested.startsWith("/ES/passwordrecovery")) {
						code = RewriteUserHTML.rewriteUserPasswordRecovery(fileRequested.split("/")[3], "ES", bl, "");
					} else if (fileRequested.startsWith("/EN/passwordrecovery")) {
						code = RewriteUserHTML.rewriteUserPasswordRecovery(fileRequested.split("/")[3], "EN", bl, "");
					} else if (fileRequested.startsWith("/EUS/passwordrecovery")) {
						code = RewriteUserHTML.rewriteUserPasswordRecovery(fileRequested.split("/")[3], "EUS", bl, "");
					} else if (fileRequested.startsWith("/changeLanguage/ES")) {
						code = RewriteUserHTML.RewriteLandingPage("ES", bl);
						setLanguageCookie = true;
						defaultLanguage = "ES";
					} else if (fileRequested.startsWith("/changeLanguage/EUS")) {
						code = RewriteUserHTML.RewriteLandingPage("EUS", bl);
						setLanguageCookie = true;
						defaultLanguage = "EUS";
					} else if (fileRequested.startsWith("/changeLanguage/EN")) {
						code = RewriteUserHTML.RewriteLandingPage("EN", bl);
						setLanguageCookie = true;
						defaultLanguage = "EN";
					} else if (fileRequested.equals("/PasswordRecovery")) {
						code = RewriteUserHTML.rewritePasswordRecoveryAsk(defaultLanguage, bl);
					} else if (fileRequested.equals("/signUp")) {
						code = RewriteUserHTML.rewriteSignUp(defaultLanguage, bl, "");
					} else if (fileRequested.equals("/terms_and_conditions.html")) {
						code = RewriteUserHTML.readFile(WEB_ROOT, "/terms_and_conditions.html");
					} else if (fileRequested.endsWith("/")) {
						code = RewriteUserHTML.RewriteLandingPage(defaultLanguage, bl);
					} else {
						code = RewriteUserHTML.RewriteLandingPage(defaultLanguage, bl);
					}
				}
			} else {
				code = RewriteUserHTML.readFile(WEB_ROOT, fileRequested = "./maintenance.html");
			}

			if (code.equals("")) {
				code = RewriteUserHTML.readFile(WEB_ROOT, "/404.html");
			}

			int fileLength = 0;
			byte[] fileData;

			fileData = readFileData(code);
			fileLength = (int) code.getBytes().length;

			content = "text/html";

			// GET method so we return content

			// send HTTP Headers
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from Carlos: 1.0");
			out.println("Date: " + new Date());
			out.println("Content-Type: text/html; charset=utf-8");
			out.println("Content-length: " + fileLength);
			if (setLanguageCookie) {
				out.println("Set-Cookie: language=" + defaultLanguage + "; Path=/; Secure; HttpOnly;");
			}
			out.println(); // blank line between headers and content, very important !
			out.flush(); // flush character output stream buffer

			dataOut.write(fileData, 0, fileLength);
			dataOut.flush();

			if (verbose) {
				System.out.println("File " + fileRequested + " of type " + content + " returned");
			}

		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --------------------------- END GET HERE ----------------------

	// --------------------------- POST ------------------------------
	/**
	 * This function parse the POST request
	 * 
	 * @param fileRequested the file that the user request
	 * @param in            the information that comes through the socket
	 * @param out           to send the HTTP header through the socker
	 * @param dataOut       to send some information throug the socket
	 */
	private void postMethod(String fileRequested, BufferedReader in, PrintWriter out, BufferedOutputStream dataOut) {
		String change = "";
		try {
			// if (fileRequested.endsWith("/")) {
			// change = RewriteUserHTML.RewriteLandingPage("EN", bl);
			// }

			// -------- READING COOKIE AND BODY OF THE HTTP (THE FORM) -----------
			String username = "";
			String token = "";
			String cookie = "";
			String line;
			String defaultLanguage = "EN";
			Hashtable<String, String> cookies = new Hashtable<String, String>();
			int length = 0;
			while ((line = in.readLine()) != null) {
				if (line.contains("Content-Length:")) {
					length = Integer.valueOf(line.split(" ")[1]);
				}
				if (line.contains("cookie:")) {
					cookie = line;
				}
				if (line.length() == 0) {
					break;
				}
			}
			// add cookies values to the hashtable
			if (!cookie.equals("")) {
				String[] cookiehelper = cookie.split(" ");
				for (int i = 1; i < cookiehelper.length; i++) {
					String[] cookiesplit = cookiehelper[i].split("=");
					try {
						if (cookiehelper[i].endsWith(";")) {
							cookies.put(cookiesplit[0], cookiesplit[1].split(";")[0]);
						} else {
							cookies.put(cookiesplit[0], cookiesplit[1]);
						}
					} catch (Exception e) {

					}
				}
			}
			System.out.println("Cookies");
			System.out.println(cookie);
			char[] body = new char[length];
			in.read(body, 0, length);

			// set up properly the username and token
			if (cookies.containsKey("username")) {
				username = cookies.get("username");
			}
			if (cookies.containsKey("token")) {
				token = cookies.get("token");
			}

			if (cookies.containsKey("language")) {
				defaultLanguage = cookies.get("language");
			}else {
				cookies.put("language", "EN");
			}


			// --------- END READING COOKIE AND BODY OF THE HTTP (THE FORM) ---------

			// string help is used is used to send cookie information and retrieve
			// information when login
			List<String> stringHelp = new LinkedList<String>();
			stringHelp.add(connect.getRemoteSocketAddress().toString());
			change = postRequest(body, fileRequested, stringHelp, cookies);

			boolean setUpCookie = false;
			boolean adminaccess = false;
			// ------ In case that everything goes as expected change should be true -------
			if (!change.equals("")) {
				// IN CASE THAT YOU DO NOT HAVE COOKIES
				if (!(stringHelp.size() == 0)) {
					username = stringHelp.get(0).split("=")[1].split(";")[0];
					token = stringHelp.get(1).split("=")[1].split(";")[0];
				}
				// OTHERWISE
				if (bl.getUsers().activeUser(username, token)) {
					if (fileRequested.startsWith("/administration")) {
						adminaccess = bl.getUsers().checkUserRank(username, token) == 3;
					}
					if (adminaccess) {
						switch (fileRequested) {
						// SPECIAL CASE
						case "/administration/adminlogin":
							setUpCookie = true;
							break;
						}
					} else {
						// SPECIAL CASE
						switch (fileRequested) {
						case "/userlogin":
							setUpCookie = true;
							break;
						}
					}
				}
			} else {
				if (fileRequested.endsWith("/")) {
					change = RewriteUserHTML.RewriteLandingPage(defaultLanguage, bl);
				} else {
					change = RewriteUserHTML.readFile(WEB_ROOT, "/404.html");
				}
			}

			int fileLength = 0;
			byte[] fileData;

			fileData = readFileData(change);
			fileLength = (int) change.getBytes().length;

			String content = "text/html";

			// send HTTP Headers
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from Carlos : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-Type: text/html; charset=utf-8");
			out.println("Content-length: " + fileLength);
			// cookie set-up only if you login
			if (setUpCookie) {
				out.println("Set-Cookie: " + stringHelp.get(0)
						+ " Path=/; Expires=Fri, 4 Oct 2025 14:28:00 GMT; Secure; HttpOnly;");
				out.println("Set-Cookie: " + stringHelp.get(1)
						+ " Path=/; Expires=Fri, 4 Oct 2025 14:28:00 GMT; Secure; HttpOnly;");
			}
			out.println(); // blank line between headers and content, very important !
			out.flush(); // flush character output stream buffer

			dataOut.write(fileData, 0, fileLength);
			dataOut.flush();

			if (verbose) {
				System.out.println("File " + fileRequested + " of type " + content + " returned");
			}

		} catch (

		FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function for the POST requests
	 * 
	 * @param body    of the HTTP request
	 * @param request the file that the user wants
	 * @param help    list to connect between the vector and the server
	 * @param cookies of the user
	 * @return HTML page requested
	 */
	private String postRequest(char[] body, String request, List<String> help, Hashtable<String, String> cookies) {
		// administration request and user requests
		if (request.startsWith("/administration")) {
			return AdminPostGetVector.adminPostRequest(body, request, help, cookies, bl);
		} else {
			return UserPostGetVector.userPostRequest(body, request, help, cookies, bl);
		}
	}

}
