package academy.prog;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String toUser = null;
		try(Scanner scanner = new Scanner(System.in)) {
			System.out.println("Enter your login: ");
			String login = scanner.nextLine();

			Thread thCommon = new Thread(new GetThread());
			thCommon.setDaemon(true);
			thCommon.start();

			Thread thPrivate = new Thread(new GetThreadPrivate(login));
			thPrivate.setDaemon(true);
			thPrivate.start();

			System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;

				String[] words = text.split(" ");
				if (words[0].startsWith("@")) {
					toUser = words[0].substring(1);
					text = text.replaceAll(words[0], "" );
				} else if (text.equals("/users")) {
					GetThread.printUsersList();
				}
				Message m = new Message(login, toUser, text);
				int res = m.send(Utils.getURL() + "/add");
				toUser = null;

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occurred: " + res);
					return;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}

