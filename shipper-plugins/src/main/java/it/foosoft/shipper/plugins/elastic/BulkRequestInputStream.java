package it.foosoft.shipper.plugins.elastic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;

import it.foosoft.shipper.api.Event;

/**
 * An implementation of InputStream which gets its data from the JSON serialization of an array of events  
 * 
 * This implementation uses a (non growable) circular Buffer where data is stored.
 * Data is fed as needed (i.e. when the InputStream requests data and the CircularBuffer is empty) into the JSON serializer
 * Although a bit more complicate than a complete event list serialization to byte buffer, this solution avoids allocation of huge
 * chunks of memory even for gigantic documents
 * 
 * @author luca
 *
 */
public class BulkRequestInputStream extends InputStream {

	int nextEvent = 0;
	private ObjectWriter objectMapper;
	private List<Event> events;
	private BulkIndexHeader bulkIndexHeader;
	private OutputStream outputStream;
	private SequenceWriter objectWriter;
	private CircularByteBuffer buffer = new CircularByteBuffer(32768);
	private boolean eof;
	public BulkRequestInputStream(String indexName, List<Event> events, boolean compress) throws IOException {
		this.bulkIndexHeader = new BulkIndexHeader(indexName);
		this.events = events;
		this.objectMapper = new ObjectMapper().writer();
		this.outputStream = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				buffer.add((byte)b);
			}
			@Override
			public void write(byte[] b, int off, int len) {
				buffer.add(b, off, len);
			}
			
		};
		if(compress) {
			this.outputStream = new GZIPOutputStream(this.outputStream);
		}
		objectWriter = objectMapper.writeValues(this.outputStream);
	}

	@Override
	public int read() throws IOException {
		byte[] b = new byte[1];
		int r = read(b, 0, 1);
		if(r <= 0) {
			return -1;
		}
		return b[0] & 255;
	}

    public int read(byte b[], int off, int len) throws IOException {
    	if(eof)
    		return -1;

    	int readBytes = 0;
    	
    	// First try read from non-read part of current buffer
    	int avail = Math.min(len, buffer.getCurrentNumberOfBytes());
    	buffer.read(b, off, avail);
		off += avail;
		len -= avail;
		readBytes += avail;
    	
    	// Then create new buffers, store them into 
    	while(len > 0 && nextEvent < events.size()) {
			objectWriter.write(bulkIndexHeader);
			outputStream.write('\n');
			objectWriter.write(events.get(nextEvent));
			outputStream.write('\n');
			nextEvent++;

	    	avail = Math.min(len, buffer.getCurrentNumberOfBytes());
	    	buffer.read(b, off, avail);
			off += avail;
			len -= avail;
			readBytes += avail;
    	}
    	
    	if(len > 0) {
    		outputStream.close();
    		avail = Math.min(len, buffer.getCurrentNumberOfBytes());
	    	buffer.read(b, off, avail);
			off += avail;
			len -= avail;
			readBytes += avail;
    	}
    	if(readBytes == 0) {
    		eof = true;
    	}
    	return readBytes;
	}
    
}
