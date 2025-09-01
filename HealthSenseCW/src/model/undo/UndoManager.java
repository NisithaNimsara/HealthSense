package model.undo;

/* Simple undo manager with a fixed capacity stack (max 3 operations).
 * Keeps the most recent operations, if more than 3 are pushed, oldest is dropped.*/

public class UndoManager {
    private Operation[] stack = new Operation[3]; // holds up to 3 operations
    private int size = 0; // current count of stored operations

    // Push a new operation onto the undo stack.
    public void push(Operation op) {
        if (size == 3) {
            // shift everything left: discard oldest, make space for new at top
            stack[0] = stack[1];
            stack[1] = stack[2];
            stack[2] = op;
        } else {
            stack[size++] = op; // place at next free slot
        }
    }

    // if there's anything to undo.
    public boolean canUndo() {
        return size > 0;
    }

    //Undo the most recent operation.
    public void undo() {
        if (size == 0) return; // nothing to undo
        Operation top = stack[size - 1]; // get last pushed
        top.undo(); // run its undo action
        stack[--size] = null; // remove it from stack
    }
}
