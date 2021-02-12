package org.logstash.dissect;

import it.foosoft.shipper.api.Event;

interface Converter {
    void convert(Event e, String src);
    default boolean isInvalid() {
        return false;
    }
}
