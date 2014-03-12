import controller.*;


public class main {
	public static void main (String[] args) {
		boolean quit = false ;
		Controller controller = new Controller();
		
		
		while (!quit) {
			quit = controller.executeCommands();
		}
		
		return;
	}

}
