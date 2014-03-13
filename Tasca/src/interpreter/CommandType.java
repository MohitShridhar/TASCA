package interpreter;

public enum CommandType {
    
    ADD, MODIFY,
    DISPLAY_TODAY, DISPLAY_TOMORROW, DISPLAY_WEEK, DISPLAY_MONTH, DISPLAY_ALL, DISPLAY_IN_TIME,
    
    DELETE, DELETE_ALL_COMPLETED,  
    SEARCH, MARK, 
    
    QUIT, CLEAR, UNDO, REDO,
    
    INVALID
    
}
