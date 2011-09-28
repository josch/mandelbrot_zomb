package deepZoom.animation;

import deepZoom.parameters.Parameters;

/**
 * Generic animation for fractals. Implementations define which parameters are
 * animated, though animation for the following quantities is supported: center,
 * magnitude, maxiter and bailout. Before the animation can be calculated, the
 * number of frames need also be known beforehand. After changing animation
 * parameters or number of frames, the animation needs to be initialized by
 * calling init().
 * 
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public abstract class Animation extends Parameters {

    protected int numFrames;

    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    /**
     * Initialize the animation sequence. Call after each change to
     * initial/final parameters or number of frames.
     */
    public abstract void init();

    public abstract void setFrame(int frameNr);
}
