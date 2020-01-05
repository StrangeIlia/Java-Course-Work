package bgty.vt_41.bi.entity.json_serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.net.InetAddress;

public class PathFilesSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String path, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String addr = InetAddress.getLocalHost().getHostAddress();
        //Object port = System.getProperties().get("server.port");
        jsonGenerator.writeObject(addr + "/" + path );
    }
}