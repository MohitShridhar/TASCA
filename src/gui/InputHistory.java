package gui;

class InputHistory {
    private String state;
 
    public void set(String state) {
        this.state = state;
    }
 
    public StateMemory saveToHistory() {
        return new StateMemory(state);
    }
 
    public String restoreFromHistory(StateMemory stateMemory) {
        state = stateMemory.getSavedInput();
        return state;
    }
 
    public static class StateMemory {
        private final String state;
 
        public StateMemory(String stateToSave) {
            state = stateToSave;
        }
 
        public String getSavedInput() {
            return state;
        }
    }
}