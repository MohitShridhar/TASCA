package interpreter;
public enum CommandFeedback {
    
    INVALID_START_TIME, INVALID_END_TIME, INVALID_REMIND_TIME, 
    INVALID_PRIORITY, INVALID_FOLDER_REF, INVALID_TASK_ID,
    
    EMPTY_DESCRIPTION, EMPTY_LOCATION,
    
    
//  INVALID_DELETE_REF, CANNOT_UNDO, CANNOT_REDO,
//  INVALID_MARK_REF, SEARCH_NOT_FOUND, 
//  
            
//  INVALID_COMMAND_FORMAT, INVALID_COMMAND, INVALID_ALREADY_EXISTS,
    
    INVALID_DATABASE_DUPLICATES, EMPTY_KEYWORD, MULTIPLE_WORD_KEYWORD,
    
    SUCCESSFUL_OPERATION;
}

