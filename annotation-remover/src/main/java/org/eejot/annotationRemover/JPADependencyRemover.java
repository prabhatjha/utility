package org.eejot.annotationRemover;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JPADependencyRemover
{

   public static void main(String[] args) throws Exception {

      String destFolder = "/tmp/non-jpa-aware-client-model/";
      String originFolder = "/home/prabhat/eejot/workspace/jpa-stuff/src/main/java/org/eejot/model/";
      String destPackageName ="com.ideawheel.webmanager.client.model";

      if (!new File(originFolder).exists())
         throw new RuntimeException("WTF! Not even a valid folder where source files are?");
      if (!new File(destFolder).exists()) {
         boolean success = new File (destFolder).mkdirs();
         if (success)
            System.out.println ("Created non existent destination directory");
      }



      List<String> fileNames = getFileNames (originFolder);

      for (String fileName: fileNames) {

         FileInputStream in = new FileInputStream(originFolder+ fileName);
         CompilationUnit cu;
         try {
            // parse the file
            cu = JavaParser.parse(in);
         } finally {
            in.close();
         }
         //overwrite the destination package name
         cu.setPackage(new PackageDeclaration(new NameExpr(destPackageName)));
         //clean hibernate and jpa dependency from imports
         removeJPAImports(cu);

         //it currently remove all class level annotation
         removeAnnotationFromClass(cu);      

         //it currently removes all annotations from methods 
         removeAnnotationFromMethods(cu);

         FileWriter writer = new FileWriter (destFolder+ fileName);
         writer.write(cu.toString());
         writer.close();
         System.out.println ("Just massaged : " + destFolder+fileName);
      }

      /* FileOutputStream out = new FileOutputStream(folder+ "/"+"ClientLog.java");
      out.write(cu.toString().toCharArray());
       */
      // prints the changed compilation unit
      //System.out.println(cu.toString());
   }

   private static void removeAnnotationFromClass(CompilationUnit cu) {

      List<TypeDeclaration> types = cu.getTypes();
      for (TypeDeclaration type : types) {
         if (type instanceof ClassOrInterfaceDeclaration) {
            type.setAnnotations(null);
         }
      }

   }

   private static void removeJPAImports(CompilationUnit cu)  {
      List<ImportDeclaration> imports = cu.getImports();
      if (imports == null)
         return; //nothing to do
      List<ImportDeclaration> newImports = new ArrayList <ImportDeclaration> ();
      for (ImportDeclaration imp: imports) {
         if (!(imp.getName().toString().contains("persistence")
               || imp.getName().toString().contains("hibernate")))
            newImports.add (imp);

      }
      cu.setImports(newImports);

   }

   private static void removeAnnotationFromMethods(CompilationUnit cu) {
      List<TypeDeclaration> types = cu.getTypes();
      for (TypeDeclaration type : types) {
         List<BodyDeclaration> members = type.getMembers();
         for (BodyDeclaration member : members) {
            if (member instanceof MethodDeclaration) {
               MethodDeclaration method = (MethodDeclaration) member;
               removeAnnotationFromMethod(method);
            }
         }
      }
   }

   private static void removeAnnotationFromMethod(MethodDeclaration n) {
      n.setAnnotations(null);
      // change the name of the method to upper case
      /*n.setName(n.getName().toUpperCase());

      // create the new parameter
      Parameter newArg = ASTHelper.createParameter(ASTHelper.INT_TYPE, "value");

      // add the parameter to the method
      ASTHelper.addParameter(n, newArg);
       */  }

   private static List<String> getFileNames (String originFolder) {
      List<String> fileNames = new ArrayList<String>();
      File folder = new File(originFolder);
      File[] listOfFiles = folder.listFiles();     
      for (int i = 0; i < listOfFiles.length; i++) {
         if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".java")) {         
            fileNames.add(listOfFiles[i].getName());

         }
      }
      return fileNames;

   }


}
