package it.foosoft.shipper.plugins.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import it.foosoft.shipper.api.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class JacksonEventWriter implements EventWriter {
    ObjectMapper objectMapper;

    private final EventIndexResolver resolver;

    public JacksonEventWriter(EventIndexResolver resolver, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.resolver = resolver;
    }
    public void writeEvents(OutputStream outputStream, List<Event> events) throws IOException {
        SequenceWriter sequenceWriter = objectMapper.writer().withRootValueSeparator("\n").writeValues(outputStream);
        for(Event evt: events) {
            BulkIndexHeader bulkIndexHeader = new BulkIndexHeader(resolver.resolve(evt));
            sequenceWriter.write(bulkIndexHeader);
            sequenceWriter.write(evt);
        }
    }
}
