package com.triateq.gravitymaze.core.serialization;

/**
 * Every serializable game class must implement this interface if it needs to save and load something.
 *
 * This interface is also used in the level editor. User can edit fields obtained from parameters.
 * If your class implements this interface, you can use {@link Parameter} instead
 * of methods {@link #getParameters()} and {@link #setParameters(Parameters)}.
 */
public interface Parameterizable {

    /**
     * Creates new parameters and returns them.
     *
     * @return parameters
     */
    default Parameters getParameters() {
        return new Parameters();
    }

    /**
     * Sets the given parameters to the object.
     *
     * @param parameters the parameters
     */
    default void setParameters(Parameters parameters) {
    }
}
