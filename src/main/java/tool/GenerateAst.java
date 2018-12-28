package tool;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(1);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expression", Arrays.asList(
                "Binary     : Expression left, Token operator, Expression right",
                "Grouping   : Expression expression",
                "Literal    : Object value",
                "Unary      : Token operator, Expression right"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws FileNotFoundException, UnsupportedEncodingException {
        String path = outputDir + "/Expression" + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package core;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("public abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        // The AST Classes
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println();
        writer.println("  abstract <T> T accept(Visitor<T> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
            PrintWriter writer, String baseName,
            List<String> types) {
        writer.println("\tpublic interface Visitor<T> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("\tT visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }

    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println(" static class " + className + " extends " + baseName + " {");

        // Ctor
        writer.println("\t" + className + "(" + fieldList + ") {");

        // Store params in fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("\t\tthis." + name + " = " + name + ";");
        }

        writer.println("\t}");

        writer.println();
        writer.println("\t<T> T accept(Visitor<T> visitor) {");
        writer.println("\t\t return visitor.visit" +
                className + baseName + "(this);");
        writer.println("\t}");

        // Fields
        writer.println();
        for (String field : fields) {
            writer.println("\tfinal " + field + ";");
        }

        writer.println("  }");
    }
}
