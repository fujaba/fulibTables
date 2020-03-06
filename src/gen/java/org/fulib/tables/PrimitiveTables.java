package org.fulib.tables;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;

public class PrimitiveTables
{
   public static void main(String[] args) throws IOException
   {
      final STGroup group = new STGroupFile(PrimitiveTables.class.getResource("PrimitiveTables.stg"));

      final ST intST = group.getInstanceOf("primitiveTable");
      intST.add("primitive", "int");
      intST.add("wrapper", "Integer");
      intST.add("streamClass", "IntStream");
      intST.add("streamMap", "mapToInt(Integer::intValue)");
      intST.write(new File("src/main/java/org/fulib/tables/intTable.java"), group.getListener());

      final ST longST = group.getInstanceOf("primitiveTable");
      longST.add("primitive", "long");
      longST.add("wrapper", "Long");
      longST.write(new File("src/main/java/org/fulib/tables/longTable.java"), group.getListener());

      final ST doubleST = group.getInstanceOf("primitiveTable");
      doubleST.add("primitive", "double");
      doubleST.add("wrapper", "Double");
      doubleST.add("minNegative", true);
      doubleST.write(new File("src/main/java/org/fulib/tables/doubleTable.java"), group.getListener());

      final ST floatST = group.getInstanceOf("primitiveTable");
      floatST.add("primitive", "float");
      floatST.add("wrapper", "Float");
      floatST.add("streamClass", "DoubleStream");
      floatST.add("streamMethod", "doubleStream");
      floatST.add("streamMap", "mapToDouble(Float::doubleValue)");
      floatST.add("streamCast", "(float) ");
      floatST.add("minNegative", true);
      floatST.write(new File("src/main/java/org/fulib/tables/floatTable.java"), group.getListener());
   }
}
