package it.foosoft.shipper.plugins.elastic;

import com.alibaba.fastjson2.JSON;
import it.foosoft.shipper.api.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FastJsonEventWriter implements EventWriter {
    private final EventIndexResolver resolver;

    public FastJsonEventWriter(EventIndexResolver resolver) {
        this.resolver = resolver;
    }
    public void writeEvents(OutputStream outputStream, List<Event> events) throws IOException {
        for(Event evt: events) {
            BulkIndexHeader bulkIndexHeader = new BulkIndexHeader(resolver.resolve(evt));
            outputStream.write(JSON.toJSONBytes(bulkIndexHeader));
            outputStream.write('\n');
            outputStream.write(JSON.toJSONBytes(evt));
            outputStream.write('\n');
        }
    }
}
