package torage;

import java.util.*;


public class UndoRedo {
	private Stack <UndoRedoNode> undo;
	private Stack <UndoRedoNode> redo;
	
	public UndoRedo () {
		undo =  new Stack<UndoRedoNode>();
		redo = new Stack<UndoRedoNode>();
	}
	
	public void addUndo ( Task oldTask, Reminder oldReminder, Task newTask, Reminder newReminder, String command) {
		UndoRedoNode node = new UndoRedoNode(oldTask, oldReminder, newTask, newReminder,command);
		undo.push(node);
		redo = new Stack<UndoRedoNode>();
		return;
	}
	
	public UndoRedoNode undo (){
		if(!undo.empty()){
		redo.push(undo.pop());
		return redo.peek();
		} else {
			return null;
		}
	}
	
	public UndoRedoNode redo () {
		if(redo.empty()){
			return null;
		}else {
			undo.push(redo.pop());
			return undo.peek();
		}
	}

}
