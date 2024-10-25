package com.cgvsu;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        Path fileName = Path.of("ObjReaderInitial/3DModels/Faceform/WrapSkull.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());
        System.out.println("------------------------------");
        System.out.println("finding vertex normals ...");
        ArrayList<Vector3f> normals = VerticesNormals.searchingVerticesNormals(model);
        System.out.println("Calculated normals: " + normals.size());
//        System.out.println("example: " + Arrays.toString(normals.get(0).getVector()));
    }
}
