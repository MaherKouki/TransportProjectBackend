package com.example.Transport.Config;


import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class BusPositionDeserializer extends JsonDeserializer<BusPosition> {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public BusPosition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        double latitude = node.get("latitude").asDouble();
        double longitude = node.get("longitude").asDouble();
        long time = node.get("time").asLong();
        //long busId = node.get("busId").asLong(); // required if id not auto-injected

        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        BusPosition busPosition = new BusPosition();
        busPosition.setPosition(point);

        // Set embedded ID
        //BusPositionId id = new BusPositionId(busId, time);
        BusPositionId id = new BusPositionId(null, time);
        busPosition.setId(id);

        return busPosition;
    }
}
