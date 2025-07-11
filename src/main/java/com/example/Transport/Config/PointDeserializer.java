package com.example.Transport.Config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class PointDeserializer extends JsonDeserializer<Point> {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        double lon = node.get("latitude").asDouble();  // Convert JSON node to double
        double lat = node.get("longitude").asDouble();  // Convert JSON node to double

        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }




    public static class PointSerializer extends JsonSerializer<Point> {
        @Override
        public void serialize(Point value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("latitude", value.getY());   // Y = latitude
            gen.writeNumberField("longitude", value.getX());  // X = longitude
            gen.writeEndObject();
        }
    }

    // Register serializer and deserializer
    private final ObjectMapper objectMapper;

    public PointDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void setupModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Point.class, this);
        module.addSerializer(Point.class, new PointSerializer());
        objectMapper.registerModule(module);
    }





}
