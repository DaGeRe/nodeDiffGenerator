package de.dagere.peass;

import java.io.File;
import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import de.dagere.peass.dependency.analysis.data.ChangedEntity;
import de.dagere.peass.dependency.changesreading.JavaParserProvider;
import de.dagere.peass.dependency.changesreading.SourceReadUtils;

public class TestSourceDetectionInnerClass {
   @Test
   public void testInner() throws ParseException, IOException {
      final File file = new File(TestSourceDetection.SOURCE, "Test3_Inner.java");
      final CompilationUnit cu = JavaParserProvider.parse(file);

      final ChangedEntity currentTraceElement = new ChangedEntity("Test3_Inner$InnerStuff", null, "<init>");
      currentTraceElement.addParameters("de.peass.InnerParameter1", "InnerParameter2");
      final Node method = SourceReadUtils.getMethod(currentTraceElement, cu);

      System.out.println(method);

      Assert.assertNotNull(method);

      final ChangedEntity currentTraceElement2 = new ChangedEntity("Test3_Inner$InnerStuff$InnerInner", null, "doubleInnerMethod");
      final Node method2 = SourceReadUtils.getMethod(currentTraceElement2, cu);

      System.out.println(method2);

      Assert.assertNotNull(method2);
   }

   @Test
   public void testInnerWithSelfReference() throws ParseException, IOException {
      final File file = new File(TestSourceDetection.SOURCE, "Test3_Inner.java");
      final CompilationUnit cu = JavaParserProvider.parse(file);

      final ChangedEntity currentTraceElement = new ChangedEntity("Test3_Inner$InnerStuff", "", "<init>");
      currentTraceElement.addParameters("Test3_Inner", "de.peass.InnerParameter1", "InnerParameter2");
      final Node method = SourceReadUtils.getMethod(currentTraceElement, cu);

      System.out.println(method);

      Assert.assertNotNull(method);

      final ChangedEntity currentTraceElement2 = new ChangedEntity("Test3_Inner$InnerStuff$InnerInner", "", "doubleInnerMethod");
      final Node method2 = SourceReadUtils.getMethod(currentTraceElement2, cu);

      System.out.println(method2);

      Assert.assertNotNull(method2);
   }

   @Test
   public void testInnerWithWrongReference() throws ParseException, IOException {
      final File file = new File(TestSourceDetection.SOURCE, "Test3_Inner.java");
      final CompilationUnit cu = JavaParserProvider.parse(file);

      final ChangedEntity currentTraceElement = new ChangedEntity("Test3_Inner$InnerStuff", "", "<init>");
      currentTraceElement.addParameters("SomeReferemce", "de.peass.InnerParameter1", "InnerParameter2");
      final Node method = SourceReadUtils.getMethod(currentTraceElement, cu);

      Assert.assertNull(method);
   }

   @Test
   public void testAnonymousClazzes() throws ParseException, IOException {
      final File file = new File(TestSourceDetection.SOURCE, "Test1_Anonym.java");
      final CompilationUnit cu = JavaParserProvider.parse(file);

      final ChangedEntity currentTraceElement = new ChangedEntity("Test1_Anonym$1", "", "<init>");
      final Node method = SourceReadUtils.getMethod(currentTraceElement, cu);

      System.out.println(method);

      Assert.assertNull(method);

      final ChangedEntity traceElementRun1 = new ChangedEntity("Test1_Anonym$1", "", "run");
      final Node methodRun = SourceReadUtils.getMethod(traceElementRun1, cu);

      System.out.println(methodRun);

      Assert.assertNotNull(methodRun);
      MatcherAssert.assertThat(methodRun.toString(), Matchers.containsString("Run R3"));

      final ChangedEntity traceElementRun2 = new ChangedEntity("Test1_Anonym$2", "", "run");
      final Node methodRun2 = SourceReadUtils.getMethod(traceElementRun2, cu);

      System.out.println(methodRun2);

      Assert.assertNotNull(methodRun2);
      MatcherAssert.assertThat(methodRun2.toString(), Matchers.containsString("Run R1"));

      final ChangedEntity traceElementRun3 = new ChangedEntity("Test1_Anonym$3", "", "run");
      final Node methodRun3 = SourceReadUtils.getMethod(traceElementRun3, cu);

      System.out.println(methodRun3);

      Assert.assertNotNull(methodRun3);
      MatcherAssert.assertThat(methodRun3.toString(), Matchers.containsString("Run R2"));
   }

   @Test
   public void testNamedClazzes() throws ParseException, IOException {
      final File file = new File(TestSourceDetection.SOURCE, "Test2_Named.java");
      final CompilationUnit cu = JavaParserProvider.parse(file);

      final ChangedEntity currentTraceElement = new ChangedEntity("Test2_Named$MyStuff", null, "doMyStuff1");
      final Node methodRun = SourceReadUtils.getMethod(currentTraceElement, cu);

      System.out.println(methodRun);

      Assert.assertNotNull(methodRun);
      MatcherAssert.assertThat(methodRun.toString(), Matchers.containsString("stuff 1"));

      final ChangedEntity currentTraceElement2 = new ChangedEntity("Test2_Named$MyStuff2", "", "doMyStuff2");
      final Node methodRun2 = SourceReadUtils.getMethod(currentTraceElement2, cu);

      System.out.println(methodRun2);

      Assert.assertNotNull(methodRun2);
      MatcherAssert.assertThat(methodRun2.toString(), Matchers.containsString("stuff 2"));
   }
}
