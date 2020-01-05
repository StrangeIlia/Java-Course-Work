package bgty.vt_41.bi.util.json_serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class PathFilesSerializer extends JsonSerializer<String> {
    private static String address = null;

    private void findAddress()
    {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements())
            {
                for(InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if(f.getAddress().isSiteLocalAddress() && !f.getAddress().isLoopbackAddress())
                    {
                        address = f.getAddress().getHostAddress();
                        return;
                    }
            }
        }
        catch (SocketException e)
        {

        }
        address = "";
    }

    @Override
    public void serialize(String path, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        findAddress();
        jsonGenerator.writeObject("https://" + address + ":50001/" + path );
    }
}