package it.foosoft.shipper.plugins.elastic;

import it.foosoft.shipper.api.Event;

public interface EventIndexResolver {
    String resolve(Event evt);
}
