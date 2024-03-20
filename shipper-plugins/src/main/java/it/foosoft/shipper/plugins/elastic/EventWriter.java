package it.foosoft.shipper.plugins.elastic;

import it.foosoft.shipper.api.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface EventWriter {
    void writeEvents(OutputStream outputStream, List<Event> events) throws IOException;
}
