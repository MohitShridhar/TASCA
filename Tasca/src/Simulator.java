import controller.Controller;


import java.util.Scanner;
public class Simulator {
	public static void main (String[] args) {
		boolean quit = false;
		Controller controller = new Controller();
		Scanner myScan = new Scanner(System.in);
		
		String MESSAGE_WELCOME_V0_1 =  "Welcome to Tasca V0.1!\n";
		
		System.out.printf("%s", MESSAGE_WELCOME_V0_1);
		while (!quit) {
			String input = myScan.nextLine();
			quit = controller.executeCommands(input);
		}
		
		return;
	}

}
