package gui;

import java.util.List;
import java.util.ArrayList;
class InputHistory {
    private String state;
 
    public void set(String state) {
        this.state = state;
    }
 
    public Memento saveToMemento() {
        return new Memento(state);
    }
 
    public String restoreFromMemento(Memento memento) {
        state = memento.getSavedInput();
        return state;
    }
 
    public static class Memento {
        private final String state;
 
        public Memento(String stateToSave) {
            state = stateToSave;
        }
 
        public String getSavedInput() {
            return state;
        }
    }
}