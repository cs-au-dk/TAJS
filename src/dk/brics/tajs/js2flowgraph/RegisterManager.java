package dk.brics.tajs.js2flowgraph;

/**
 * Simple class for producing unique register values.
 */
class RegisterManager {

    private int register;

    /**
     * Constructs a new register manager.
     */
    RegisterManager(int initialRegister) {
        register = initialRegister;
    }

    /**
     * Returns the current register (without incrementing it).
     */
    int getRegister() {
        return register;
    }

    /**
     * Returns the current register and then increments the counter.
     */
    int nextRegister() {
        return register++;
    }
}
