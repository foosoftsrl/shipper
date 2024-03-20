package it.foosoft.shipper.plugins;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.core.EventImpl;
import it.foosoft.shipper.plugins.elastic.BulkIndexHeader;
import it.foosoft.shipper.plugins.elastic.FastJsonEventWriter;
import it.foosoft.shipper.plugins.elastic.JacksonEventWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestElasticSearchOutput {

    @Test
    public void testJackson() throws IOException {
        Event event = createEvent();
        JacksonEventWriter writer = new JacksonEventWriter(evt->"test", new ObjectMapper());
        try(var outputStream = new ByteArrayOutputStream()) {
            writer.writeEvents(outputStream, Arrays.asList(event, event));
            Assertions.assertEquals("""
                    {"index":{"_index":"test"}}
                    {"@timestamp":0,"test":"testValue"}
                    {"index":{"_index":"test"}}
                    {"@timestamp":0,"test":"testValue"}""", new String(outputStream.toByteArray()));
        }
    }



    @Test
    public void testFastJson() throws IOException {
        Event event = createEvent();
        FastJsonEventWriter writer = new FastJsonEventWriter(evt->"test");
        try(var outputStream = new ByteArrayOutputStream()) {
            writer.writeEvents(outputStream, Arrays.asList(event, event));
            Assertions.assertEquals("""
                    {"index":{"_index":"test"}}
                    {"@timestamp":0,"test":"testValue"}
                    {"index":{"_index":"test"}}
                    {"@timestamp":0,"test":"testValue"}
                    """, new String(outputStream.toByteArray()));
        }
    }

    private static void fastJsonWriteEvents(List<Event> events, ByteArrayOutputStream outputStream) throws IOException {
        for (Event evt : events) {
            BulkIndexHeader bulkIndexHeader = new BulkIndexHeader("test");
            outputStream.write(JSON.toJSONBytes(bulkIndexHeader));
            outputStream.write('\n');
            outputStream.write(JSON.toJSONBytes(evt));
            outputStream.write('\n');
        }
    }

    private static List<Event> createEventList() {
        List<Event> events = new ArrayList<>();
        Event event = createComplexEvent();
        for(int i = 0; i < 100000; i++) {
            events.add(event);
        }
        return events;
    }

    private static Event createEvent() {
        Event event = new EventImpl();
        event.setTimestamp(new Date(0));
        event.setField("test","testValue");
        event.setMetadata("meta", "metaValue");
        return event;
    }

    private static Event createComplexEvent() {
        Event event = new EventImpl();
        event.setTimestamp(new Date(0));
        for(int i = 0; i < 50; i++) {
            event.setField("test" + i,"testValue" + i);
        }
        event.setMetadata("meta", "metaValue");
        return event;
    }

    public interface Runnable {
        void run() throws Exception;
    }
    public static long elapsed(Runnable r) throws Exception {
        long a = System.currentTimeMillis();
        r.run();
        return System.currentTimeMillis() - a;
    }

    public static void main(String[] args) throws Exception {
        var events = createEventList();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withRootValueSeparator("\n");
        JacksonEventWriter jacksonEventWriter = new JacksonEventWriter(evt->"test", new ObjectMapper());
        FastJsonEventWriter fastJsonEventWriter = new FastJsonEventWriter(evt->"test");
        long fastJsonTotal = 0;
        long jacksonTotal = 0;
        for(int i = 0; i < 100; i++) {
            long fastjsonTiming = elapsed(() -> fastJsonEventWriter.writeEvents(new ByteArrayOutputStream(), events));
            long jacksonTiming = elapsed(() -> jacksonEventWriter.writeEvents(new ByteArrayOutputStream(), events));
            System.err.println(fastjsonTiming + " - " + jacksonTiming);
            fastJsonTotal += fastjsonTiming;
            jacksonTotal += jacksonTiming;
        }
        System.err.println(fastJsonTotal + " - " + jacksonTotal);
    }
}
