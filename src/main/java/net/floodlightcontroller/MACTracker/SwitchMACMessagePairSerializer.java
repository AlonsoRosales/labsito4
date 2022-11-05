package net.floodlightcontroller.MACTracker;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.floodlightcontroller.core.types.SwitchMACMessagePair;
import net.floodlightcontroller.util.OFMessageUtils;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;

import java.io.IOException;

public class SwitchMACMessagePairSerializer extends JsonSerializer<SwitchMACMessagePair> {
    @Override
    public void serialize(SwitchMACMessagePair m, JsonGenerator jGen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        jGen.writeStartObject();

        jGen.writeFieldName("switch");
        jGen.writeStartObject();
        jGen.writeStringField("dpid", m.getSwitch().getId().toString());
        jGen.writeEndObject();
        System.out.println(m.getSwitch().getId().toString());

        jGen.writeFieldName("MAC");
        jGen.writeStartObject();
        jGen.writeStringField("mac",m.getMac().toString());
        jGen.writeEndObject();
        System.out.println(m.getMac().toString());

        jGen.writeFieldName("message");
        jGen.writeStartObject();
        jGen.writeStringField("type", m.getMessage().getType().toString());
        jGen.writeEndObject();
        System.out.println(m.getMessage().getType().toString());

        jGen.writeEndObject();
    }
}
