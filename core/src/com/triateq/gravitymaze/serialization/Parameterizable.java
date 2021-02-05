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
    Parameters getParameters();

    /**
     * Sets the given parameters to the object.
     *
     * @param parameters the parameters
     */
    void setParameters(Parameters parameters);
}
