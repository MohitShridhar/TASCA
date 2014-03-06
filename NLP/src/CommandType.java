
public enum CommandType {
 /*   
	ADD_DESCRIPTION, ADD_TIME, ADD_DUE_DATE, ADD_LOCATION, ADD_PRIORITY, ADD_TO_FOLDER,
	MODIFY_DESCRIPTION, MODIFY_TIME, MODIFY_DUE_DATE, MODIFY_LOCATION, MODIFY_PRIORITY, MODIFY_FOLDER,
*/	
    
    	ADD, MODIFY,
    	DISPLAY_TODAY, DISPLAY_TOMORROW, DISPLAY_WEEK, DISPLAY_MONTH,
	
	//CONTEXT_TASK, // Must add/modify
	
	DELETE, MARK, SEARCH, 
	
	QUIT, CLEAR, UNDO, REDO,
	
	INVALID
	
}
