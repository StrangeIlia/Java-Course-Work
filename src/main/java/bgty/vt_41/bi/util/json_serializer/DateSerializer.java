package bgty.vt_41.bi.util.json_serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date datetime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", new Locale("rus"));
        jsonGenerator.writeObject(dateFormat.format(datetime));
    }
}