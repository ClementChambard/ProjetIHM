package com.example.demo1;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * util class for 3D visualization and calculations
 */
public class Util3D {

    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final double TEXTURE_OFFSET = 1.01;

    /**
     * Generate a mesh for a line.
     * @author From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
     * @param origin the origin of the line
     * @param target the target of the line
     * @return the mesh
     */
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    /**
     * get 3D coordinates from a 2D position on the globe.
     * @param lat latitude
     * @param lon longitude
     * @return the 3D coordinates
     */
    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        return geoCoordTo3dCoord(lat, lon, 1);
    }

    /**
     * get 3D coordinates from a 2D position on the globe. (with a given height)
     * @param lat latitude
     * @param lon longitude
     * @param h height
     * @return the 3D coordinates
     */
    public static Point3D geoCoordTo3dCoord(float lat, float lon, float h) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                h * -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                h * -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                h * java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }

    /**
     * get 2D coordinates on a globe from a 3D position.
     * @param p the 3D position
     * @return the 2D coordinates
     */
    public static Point2D SpaceCoordToGeoCoord(Point3D p) {
        float lat = (float) (Math.asin(-p.getY()/ TEXTURE_OFFSET) * (180/Math.PI) - TEXTURE_LAT_OFFSET);
        float lon = (float)(Math.asin(-p.getX()/(TEXTURE_OFFSET * Math.cos((Math.PI/180) * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
        if (p.getZ() < 0) lon = 180 - lon;
        return new Point2D(lat, lon);
    }

    /**
     * will display a sphere on the globe as a marker for a town.
     * @param parent the parent group
     * @param name the name of the town
     * @param latitude the latitude of the town
     * @param longitude the longitude of the town
     */
    public static void displayTown(Group parent, String name, float latitude, float longitude)
    {
        PhongMaterial materialTown = new PhongMaterial(Color.GREEN);
        System.out.println("added city" + name);
        Sphere sphere = new Sphere(0.01);
        sphere.setMaterial(materialTown);
        Point3D p = geoCoordTo3dCoord(latitude, longitude);
        sphere.setTranslateX(p.getX());
        sphere.setTranslateY(p.getY());
        sphere.setTranslateZ(p.getZ());
        parent.getChildren().add(sphere);
    }

    /**
     * will display a quadrilateral in 3D space.
     *
     * @param parent      the parent group
     * @param topRight    the top right point of the quadrilateral
     * @param topLeft     the top left point of the quadrilateral
     * @param bottomLeft  the bottom left point of the quadrilateral
     * @param bottomRight the bottom right point of the quadrilateral
     * @param material    the material of the quadrilateral
     */
    public static void addQuadrilateral(Group parent, Point3D topRight, Point3D topLeft, Point3D bottomLeft, Point3D bottomRight, PhongMaterial material)
    {
        final TriangleMesh triangleMesh = new TriangleMesh();

        final float[] points = {
                (float) topRight.getX(), (float) topRight.getY(), (float) topRight.getZ(),
                (float) topLeft.getX(), (float) topLeft.getY(), (float) topLeft.getZ(),
                (float) bottomLeft.getX(), (float) bottomLeft.getY(), (float) bottomLeft.getZ(),
                (float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomRight.getZ()
        };
        final float[] texCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };
        final int[] faces = {
                0, 0, 2, 2, 1, 1,
                0, 0, 3, 3, 2, 2
        };

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().add(meshView);
    }

}
