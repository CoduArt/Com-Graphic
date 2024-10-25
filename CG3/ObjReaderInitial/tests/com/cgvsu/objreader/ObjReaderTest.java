package com.cgvsu.objreader;

import com.cgvsu.VerticesNormals;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class ObjReaderTest {

    @Test
    public void testParseVertex01() {
        final ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));
        final Vector3f result = ObjReader.parseVertex(wordsInLineWithoutToken, 5);
        final Vector3f expectedResult = new Vector3f(1.01f, 1.02f, 1.03f);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testParseVertex02() {
        final ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("ab", "o", "ba"));
        try {
            ObjReader.parseVertex(wordsInLineWithoutToken, 10);
            Assertions.fail();

        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Failed to parse float value.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testParseVertex03() {
        final ArrayList<String> wordsInLineWithoutToken = new ArrayList<>(Arrays.asList("1.0", "2.0"));
        try {
            ObjReader.parseVertex(wordsInLineWithoutToken, 10);
            Assertions.fail();

        } catch (ObjReaderException exception) {
            String expectedError = "Error parsing OBJ file on line: 10. Too few vertex arguments.";
            Assertions.assertEquals(expectedError, exception.getMessage());
        }
    }

    @Test
    public void testGetVectorLength() {
        final Vector3f vector1 = new Vector3f(4, 2, 2);
        final double result = VerticesNormals.getVectorLength(vector1);
        final double expectedResult = 4.89897948556;
        Assertions.assertEquals(VerticesNormals.roundUpToScale(expectedResult), VerticesNormals.roundUpToScale(result));
    }

    @Test
    public void testNormalization() {
        final Vector3f vector1 = new Vector3f(-5, 2, 7);
        final Vector3f result = VerticesNormals.vectorNormalization(vector1);
        final Vector3f expectedResult = new Vector3f(-0.56613857	, 0.22645542, 0.79259396);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testScalarProduct() {
        final Vector3f vector1 = new Vector3f(10, 2, 8);
        final Vector3f vector2 = new Vector3f(4, -50, -8);
        final double result = VerticesNormals.vectorScalarProduct(vector1, vector2);
        final double expectedResult = -124;
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testDivision() {
        final Vector3f vector1 = new Vector3f(-10, 2, -19);
        final double val = 2;
        final Vector3f result = VerticesNormals.vectorDivision(vector1, val);
        final Vector3f expectedResult = new Vector3f(-5, 1, -9.5);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testVectorDivisionException() {
        final Vector3f vector1 = new Vector3f(10, 2, 10);
        final double val = 0;
        try {
            VerticesNormals.vectorDivision(vector1, val);
            Assertions.fail();
        } catch (RuntimeException exception) {
            String Message = "division by zero";
            Assertions.assertEquals(Message, exception.getMessage());
        }
    }

    @Test
    public void testVectorsAdding() {
        final Vector3f vector1 = new Vector3f(1, 38, 8);
        final Vector3f vector2 = new Vector3f(4, -50, -8);
        final Vector3f result = VerticesNormals.vectorsAdding(vector1, vector2);
        final Vector3f expectedResult = new Vector3f(5, -12, 0);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testVectorProduct() {
        final Vector3f vector1 = new Vector3f(5, 9, 12);
        final Vector3f vector2 = new Vector3f(14, 5.6, 7.7);
        final Vector3f result = VerticesNormals.vectorProduct(vector1, vector2);
        final Vector3f expectedResult = new Vector3f(2.1, 129.5,-98);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testNormalsSize() {
        final Model model = new Model();
        model.vertices = new ArrayList<>(Arrays.asList(new Vector3f(0, 0, 0),
                new Vector3f(0, 10, 0),
                new Vector3f(0, 5, 10),
                new Vector3f(5, 5, 0)
                ));
        model.polygons = new ArrayList<>(Arrays.asList(new Polygon(), new Polygon()));
        model.polygons.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.polygons.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3)));
        final double result = VerticesNormals.searchingVerticesNormals(model).size();
        final double expectedResult = 4;
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testNormalsV01() {
        final Model model = new Model();
        model.vertices = new ArrayList<>(Arrays.asList(new Vector3f(0, 0, 0),
                new Vector3f(0, 10, 0),
                new Vector3f(0, 5, 10),
                new Vector3f(5, 5, 0)
        ));
        model.polygons = new ArrayList<>(Arrays.asList(new Polygon(), new Polygon()));
        model.polygons.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.polygons.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3)));
        final Vector3f result = VerticesNormals.searchingVerticesNormals(model).get(2);
        final Vector3f expectedResult = new Vector3f(-1, 0, 0);
        Assertions.assertTrue(expectedResult.equals(result));
    }

    @Test
    public void testNormalsV02() {
        final Model model = new Model();
        model.vertices = new ArrayList<>(Arrays.asList(new Vector3f(0, 0, 0),
                new Vector3f(0, 10, 0),
                new Vector3f(0, 5, 10),
                new Vector3f(5, 5, 0)
        ));
        model.polygons = new ArrayList<>(Arrays.asList(new Polygon(), new Polygon()));
        model.polygons.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.polygons.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3)));
        final Vector3f result = VerticesNormals.searchingVerticesNormals(model).get(0);
        final Vector3f expectedResult = new Vector3f(-1, 0, 1);
        Assertions.assertTrue(expectedResult.equals(result));
    }
}