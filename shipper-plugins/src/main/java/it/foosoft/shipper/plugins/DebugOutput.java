package it.foosoft.shipper.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.Output;

public class DebugOutput implements Output {

	static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
	private File file;
	private PrintStream output;

	public DebugOutput() {
		this(null);
	}

	/**
	 * Build an output to a possibly null file null = stderr
	 * @param f
	 */
	public DebugOutput(File f) {
		this.file = f;
	}

	@Override
	public void start() {
		if(file == null)
			output = System.out;
		else {
			try {
				output = new PrintStream(new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public void stop() {
		if(file != null && output != null) {
			output.close();
		}
	}

	@Override
	public void process(Event e) {
		try {
			HashMap map = new HashMap<>();
			map.putAll(e.fields());
			map.put("@metadata", e.metadata());
			map.put("tags", e.tags());
			map.put("@timestamp", e.getTimestamp());
			
			output.println(writer.writeValueAsString(map));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
	}

}
