package storage;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class UndoRedo {
	private static UndoRedo instance= null;
	private static Stack <AllTasks> undo;
	private static Stack <AllTasks> redo;
	
	private UndoRedo () {
		instance = this;
		undo =  new Stack<AllTasks>();
		redo = new Stack<AllTasks>();
	}
	
	public static UndoRedo getInstance () {
		if (instance == null){
			return new UndoRedo();
		} else {
			return instance;
		}
	}
	
	public void addUndo ( AllTasks node) {
		AllTasks newNode = new AllTasks();
		try{
		node.saveData();
		newNode.loadData();
		} catch (FileNotFoundException e){
			
		}
		undo.push(newNode);
		redo = new Stack<AllTasks>();
		return;
	}
	
	public AllTasks undo (AllTasks node){
		if(!undo.empty()){
		redo.push(node);
		return undo.pop();
		} else {
			return null;
		}
	}
	
	public AllTasks redo (AllTasks node) {
		if(redo.empty()){
			return null;
		}else {
			undo.push(node);
			return redo.pop();
		}
	}
	public boolean isUndoEmpty () {
		if (undo.empty()){
			return true;
		}else {
			return false;
		}
	}
	public boolean isRedoEmpty () {
		if (redo.empty()){
			return true;
		}else {
			return false;
		}
	}

}
