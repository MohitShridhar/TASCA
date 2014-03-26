package interpreter;

public enum CommandType {
    
    ADD, MODIFY,
    DISPLAY_NOW, DISPLAY_TODAY, DISPLAY_TOMORROW, DISPLAY_WEEK, DISPLAY_MONTH, DISPLAY_ALL, DISPLAY_IN_TIME, DISPLAY_ALL_FLOAT,
    
    DELETE, DELETE_ALL_COMPLETED,  
    SEARCH, MARK, 
    
    QUIT, CLEAR, UNDO, REDO,
    
    EXPORT, IMPORT,
    
    INVALID
    
}
