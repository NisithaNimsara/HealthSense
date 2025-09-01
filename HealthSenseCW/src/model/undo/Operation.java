package model.undo;

/* Represents one action that can be undone and redone.
 *   - a type/name (for debugging or display),
 *   - what to do to undo it,
 *   - what to do to redo it.
 */
public class Operation {
    String type;            // identifier for the operation
    Runnable undoAction;    // code to run to reverse the operation
    Runnable redoAction;    // code to run to apply it again

    // Constructor
    public Operation(String type, Runnable undoAction, Runnable redoAction) {
        this.type = type;
        this.undoAction = undoAction;
        this.redoAction = redoAction;
    }

    // Perform the undo logic
    public void undo() {
        undoAction.run();
    }

    // Perform the redo logic
    public void redo() {
        redoAction.run();
    }
}
