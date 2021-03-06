package bgty.vt_41.bi.util.json_serializer;

import bgty.vt_41.bi.entity.enums.EStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ORSerializer extends JsonSerializer<EStatus> {
    @Override
    public void serialize(EStatus status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(status.toString().toLowerCase());
    }
}