package io.digitalreactor.core.api;

/**
 * Created by flaidzeres on 30.04.16.
 */

//todo may be change to abstract class
public interface Request {

    String toQuery();

    String prefix();
}
