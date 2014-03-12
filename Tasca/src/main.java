import controller.*;


public class main {
	public static void main (String[] args) {
		boolean quit = false ;
		Controller controller = new Controller();
		
		String MESSAGE_WELCOME_V0_1 =  "Welcome to Tasca V0.1!\n";
		
		System.out.printf("%s", MESSAGE_WELCOME_V0_1);
		while (!quit) {
			quit = controller.executeCommands();
		}
		
		return;
	}

}
