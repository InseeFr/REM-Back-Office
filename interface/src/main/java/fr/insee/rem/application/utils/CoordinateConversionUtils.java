package fr.insee.rem.application.utils;

import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import fr.insee.rem.domain.dtos.GPSLocation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CoordinateConversionUtils {

    private static final String GPS_SYSTEM = "EPSG:4326";

    private CoordinateConversionUtils() {
        throw new UnsupportedOperationException("Utility class and cannot be instantiated");
    }

    private static Coordinate transformCoordonneesToGPSSystem(double x, double y, TypeConversion type) throws Exception {
        try {
            var coordinate = new Coordinate(x, y);
            var transform = CRS.findMathTransform(CRS.decode(type.epsg), CRS.decode(GPS_SYSTEM), false);
            return JTS.transform(coordinate, new Coordinate(), transform);
        }
        catch (FactoryException | TransformException e) {
            var error = String.format("Cannot transform coordinates [%f,%f]", x, y);
            log.error(error, e);
            throw e;
        }
    }

    private enum TypeConversion {
        GUYANE("EPSG:2972"), ANTILLES("EPSG:4559"), REUNION("EPSG:2975"), LAMBERT("EPSG:2154");

        TypeConversion(String epsg) {
            this.epsg = epsg;
        }

        private String epsg;

    }

    public static GPSLocation convertCoordinates(Double x, Double y, String cityCode) {
        if (x != null && y != null && StringUtils.isNotBlank(cityCode) && cityCode.length() >= 3) {
            try {
                String dep = cityCode.substring(0, 3);
                CoordinateConversionUtils.TypeConversion type;
                if ("973".equals(dep)) {
                    type = CoordinateConversionUtils.TypeConversion.GUYANE;
                }
                else if ("974".equals(dep)) {
                    type = CoordinateConversionUtils.TypeConversion.REUNION;
                }
                else if ("971".equals(dep) || "972".equals(dep)) {
                    type = CoordinateConversionUtils.TypeConversion.ANTILLES;
                }
                else {
                    type = CoordinateConversionUtils.TypeConversion.LAMBERT;
                }
                Coordinate c = transformCoordonneesToGPSSystem(x, y, type);
                return GPSLocation.builder().latitude(c.x).longitude(c.y).build();
            }
            catch (Exception e) {
                return GPSLocation.builder().latitude(0d).longitude(0d).build();
            }
        }
        return GPSLocation.builder().latitude(0d).longitude(0d).build();
    }
}
