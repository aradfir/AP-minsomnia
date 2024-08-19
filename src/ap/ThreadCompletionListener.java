package ap;

/**
 * The interface Thread completion listener.
 */
public interface ThreadCompletionListener {
    /**
     * Finished successfully and without exception.
     *
     * @param thread the thread that finished
     */
    void finishedSuccessfully(final Thread thread);

    /**
     * Thread finished by throwing an exception.
     *
     * @param thread                 the thread
     * @param ex                     the exception thrown
     * @param errorSegmentDescriptor An internal description for error
     */
    void finishedException(final Thread thread, Exception ex, String errorSegmentDescriptor);

    /**
     * Thread finished event, successful or not.
     *
     * @param thread the thread
     */
    void finished(final Thread thread);

}
