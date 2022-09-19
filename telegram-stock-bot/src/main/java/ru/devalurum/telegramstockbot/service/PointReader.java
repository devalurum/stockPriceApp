package ru.devalurum.telegramstockbot.service;

import lombok.extern.slf4j.Slf4j;
import ru.devalurum.telegramstockbot.exception.PointReaderException;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

@Component
@Slf4j
public class PointReader {

    private static final int SRID = 4326;
    private static final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);

    private final WKTReader reader;
    private final WKBReader wkbReader;
    private final GeometryFactory geometryFactory;

    private static final String ST_GEOM_FROM_TEXT = "ST_GeomFromText('%s', 4326)";

    public PointReader() {
        geometryFactory = new GeometryFactory(pm, SRID);
        wkbReader = new WKBReader(geometryFactory);
        reader = new WKTReader(geometryFactory);
    }


    public Point read(@NotNull String wkt) {
        if (wkt == null) {
            return null;
        } else {
            Geometry geometry;
            try {
                geometry = reader.read(wkt);
                return convert3Dto2D(geometry);
            } catch (ParseException e) {
                log.error("Ошибка при чтении Point в виде текста.", e);
                throw new PointReaderException(e);
            }
        }
    }

    public Point read(@NotNull byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            Geometry geometry;
            try {
                geometry = wkbReader.read(bytes);
                return convert3Dto2D(geometry);
            } catch (ParseException e) {
                log.error("Ошибка при чтении Point в виде байтов.", e);
                throw new PointReaderException(e);
            }
        }
    }

    private Point convert3Dto2D(Geometry g3D) {
        Coordinate geomCoord = g3D.getCoordinate().copy();
        CoordinateSequence seq = new PackedCoordinateSequenceFactory().create(1, 2);
        seq.setOrdinate(0, CoordinateSequence.X, geomCoord.x);
        seq.setOrdinate(0, CoordinateSequence.Y, geomCoord.y);
        return geometryFactory.createPoint(seq);
    }

    public Point createPointFromLatLon(double latitude, double longitude) {
        CoordinateSequence seq = new PackedCoordinateSequenceFactory().create(1, 2);
        seq.setOrdinate(0, CoordinateSequence.X, latitude);
        seq.setOrdinate(0, CoordinateSequence.Y, longitude);
        return geometryFactory.createPoint(seq);
    }


    public Point createPointFromTelegramLocation(Location location) {
        return Optional.ofNullable(location)
                .map(latLon -> {
                    CoordinateSequence seq = new PackedCoordinateSequenceFactory().create(1, 2);
                    seq.setOrdinate(0, CoordinateSequence.X, location.getLatitude());
                    seq.setOrdinate(0, CoordinateSequence.Y, location.getLongitude());
                    return geometryFactory.createPoint(seq);
                })
                .orElseThrow(() -> new PointReaderException("LatLng is null"));
    }

    public Location convertPointToTelegramLocation(Point point) {
        return Optional.ofNullable(point)
                .map(p -> {
                    Location location = new Location();
                    location.setLatitude(p.getX());
                    location.setLongitude(p.getY());
                    return location;
                })
                .orElseThrow(() -> new PointReaderException("Point is null"));
    }


    public String getSqlForInsert(Point point) {
        return Optional.ofNullable(point)
                .map(p -> String.format(ST_GEOM_FROM_TEXT, p))
                .orElse("NULL");
    }

    private Geometry getGeometryFromInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new PointReaderException("Пустой поток.");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            //convert the stream to a byte[] array
            //so it can be passed to the WKBReader
            Geometry dbGeometry;

            byte[] buffer = new byte[255];

            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] geometryAsBytes = baos.toByteArray();

            if (geometryAsBytes.length < 5) {
                throw new PointReaderException("Неправильная система координат WGS84, меньше 5 байт.");
            }

            //first four bytes of the geometry are the SRID,
            //followed by the actual WKB. Determine the SRID

            byte[] sridBytes = new byte[4];
            System.arraycopy(geometryAsBytes, 0, sridBytes, 0, 4);
            boolean bigEndian = (geometryAsBytes[4] == 0x00);

            int srid = 0;
            if (bigEndian) {
                for (byte sridByte : sridBytes) {
                    srid = (srid << 8) + (sridByte & 0xff);
                }
            } else {
                for (int i = 0; i < sridBytes.length; i++) {
                    srid += (sridBytes[i] & 0xff) << (8 * i);
                }
            }

            //use the JTS WKBReader for WKB parsing
            WKBReader wkbReader = new WKBReader();

            //copy the byte array, removing the first four
            //SRID bytes
            byte[] wkb = new byte[geometryAsBytes.length - 4];
            System.arraycopy(geometryAsBytes, 4, wkb, 0, wkb.length);
            dbGeometry = wkbReader.read(wkb);
            dbGeometry.setSRID(srid);
            return dbGeometry;
        } catch (Exception e) {
            log.error("Ошибка при чтении Point в виде байтов.", e);
            throw new PointReaderException(e);
        }
    }
}

