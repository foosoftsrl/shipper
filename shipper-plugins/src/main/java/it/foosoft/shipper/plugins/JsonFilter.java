package it.foosoft.shipper.plugins;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.foosoft.shipper.api.ConfigurationParm;
import it.foosoft.shipper.api.Event;
import it.foosoft.shipper.api.FilterPlugin;

import javax.validation.constraints.NotNull;

public class JsonFilter implements FilterPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(JsonFilter.class);

    @NotNull
    @ConfigurationParm
    public String source;

    @ConfigurationParm
    String[] tag_on_failure = new String[]{"_jsonparsefailure"};

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean process(Event evt) {
        Object attr = evt.getField(source);
        if(attr == null) {
            LOG.warn("Field {} is missing in {}", source, evt.toString());
            evt.addTags(tag_on_failure);
            return false;
        }

        if(!(attr instanceof String attrString)) {
            LOG.warn("Unsupported field type for {}", source);
            return false;
        }
        try {
            JsonNode root = objectMapper.readTree((String) attr);
            if (!root.isObject()) {
                LOG.warn("JSON root for {} is not an object", source);
                evt.addTags(tag_on_failure);
                return false;
            }

            // iterate each field in the object node
            var fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String name = field.getKey();
                JsonNode valueNode = field.getValue();

                if (valueNode.isTextual()) {
                    evt.setField(name, valueNode.textValue());
                } else if (valueNode.isNumber()) {
                    evt.setField(name, valueNode.numberValue());
                } else if (valueNode.isBoolean()) {
                    evt.setField(name, valueNode.booleanValue());
                } else {
                    evt.setField(name, valueNode.toString());
                }
            }
            return true;
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse JSON for {}: {}", source, e.getMessage());
            evt.addTags(tag_on_failure);
            return false;
        }
    }
}
