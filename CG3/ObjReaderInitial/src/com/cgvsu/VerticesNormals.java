package com.cgvsu;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class VerticesNormals {
    private static final double SCALE = Math.pow(10, 3);

    public static ArrayList<Vector3f> searchingVerticesNormals(Model model) {
        ArrayList<Polygon> polygons = model.polygons;
        ArrayList<Vector3f> vertices = model.vertices;
        ArrayList<Vector3f> normals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            normals.add(new Vector3f(0, 0, 0));
        }


        for (Polygon polygon: polygons) {
            ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
            double[] vertex1 = vertices.get(vertexIndices.get(0)).getVector();
            double[] vertex2 = vertices.get(vertexIndices.get(1)).getVector();
            double[] vertex3 = vertices.get(vertexIndices.get(2)).getVector();
            Vector3f vector1 = new Vector3f(vertex2[0] - vertex1[0], vertex2[1] - vertex1[1], vertex2[2] - vertex1[2]);
            Vector3f vector2 = new Vector3f(vertex3[0] - vertex1[0], vertex3[1] - vertex1[1], vertex3[2] - vertex1[2]);

            double cos = vectorScalarProduct(vector1, vector2) / getVectorLength(vector1) * getVectorLength(vector2);
            double sin = Math.sqrt(1 - Math.pow(cos, 2));

            Vector3f polygonsNormal = vectorProduct(vector1, vector2);
            if (roundUpToScale(getVectorLength(polygonsNormal)) != roundUpToScale(getVectorLength(vector1) * getVectorLength(vector2) * sin)) {
                polygonsNormal = vectorDivision(polygonsNormal, -1);
            }
            polygonsNormal = vectorNormalization(polygonsNormal);

            for (Integer i: vertexIndices) {
                normals.set(i, vectorsAdding(normals.get(i), polygonsNormal));
            }
        }
        return normals;
    }

    public static Vector3f vectorsAdding(Vector3f vector1, Vector3f vector2) {
        double[] mas1 = vector1.getVector();
        double[] mas2 = vector2.getVector();
        return new Vector3f(mas1[0] + mas2[0], mas1[1] + mas2[1], mas1[2] + mas2[2]);
    }

    public static double getVectorLength(Vector3f vector) {
        double[] mas = vector.getVector();
        double sum = 0;
        for (double ma : mas) {
            sum += Math.pow(ma, 2);
        }
        return Math.sqrt(sum);
    }

    public static Vector3f vectorDivision(Vector3f vector1, double div) {
        if (div == 0) {
            throw new RuntimeException("division by zero");
        }
        double[] mas1 = vector1.getVector();
        return new Vector3f(mas1[0] / div, mas1[1] / div, mas1[2] / div);
    }

    public static Vector3f vectorProduct(Vector3f vector1, Vector3f vector2) {
        double[] v1 = vector1.getVector();
        double[] v2 = vector2.getVector();
        return new Vector3f(
                v1[1] * v2[2] - v1[2] * v2[1],
                - v1[0] * v2[2] + v1[2] * v2[0],
                v1[0] * v2[1] - v1[1] * v2[0]
        );
    }

    public static double vectorScalarProduct(Vector3f vector1, Vector3f vector2) {
        double[] mas1 = vector1.getVector();
        double[] mas2 = vector2.getVector();
        return mas1[0] * mas2[0] + mas1[1] * mas2[1] + mas1[2] * mas2[2];
    }

    public static Vector3f vectorNormalization(Vector3f vector) {
        double length = getVectorLength(vector);
        return vectorDivision(vector, length);
    }

    public static double roundUpToScale(double val) {
        return Math.ceil(val * SCALE) / SCALE;
    }
}
