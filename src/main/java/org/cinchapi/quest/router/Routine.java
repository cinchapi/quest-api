package org.cinchapi.quest.router;

/**
 * A {@link Routine} is a {@link Route} that does not return a payload or render
 * a view. It is generally used to check for some common preconditions before
 * executing other routes.
 * <p>
 * A Routine matches every other route for the Router in which it was defined.
 * For example, a Routine defined in the {@code HelloWorldRouter} will match all
 * requests to {@code /hello/world/*}. Routines are meant to be catch-alls so
 * there is no way to further specify the paths a Routine should match.
 * <p>
 * 
 * @author jnelson
 */
public abstract class Routine extends AbstractRewritableRoute {

    /**
     * Construct a new instance.
     */
    protected Routine() {
        super("/*");
    }

    @Override
    protected final Object handle() {
        run();
        return "";
    }

    /**
     * Run the routine. If, for some reason, the routine fails, you may call
     * {@link #halt()}, or redirect to another route or throw an exception.
     */
    protected abstract void run();

}
