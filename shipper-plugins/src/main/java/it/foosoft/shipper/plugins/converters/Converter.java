package it.foosoft.shipper.plugins.converters;

import it.foosoft.shipper.api.Event;

public interface Converter {
    void process(Event e);

    String targetType();
}
