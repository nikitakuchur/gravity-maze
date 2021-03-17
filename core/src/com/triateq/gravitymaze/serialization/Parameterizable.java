package com.triateq.gravitymaze.serialization;

/**
 * Every serializable class must implement this interface if it needs to save and load something.
 *
 * This interface is also used in the level editor. User can edit fields obtained from parameters.
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
