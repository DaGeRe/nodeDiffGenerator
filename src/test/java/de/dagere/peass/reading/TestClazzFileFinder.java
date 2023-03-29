package de.dagere.peass.reading;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import de.dagere.nodeDiffDetector.clazzFinding.ClazzFileFinder;
import de.dagere.nodeDiffDetector.data.MethodCall;
import de.dagere.nodeDiffDetector.diffDetection.FileComparisonUtil;
import de.dagere.nodeDiffDetector.testUtils.TestConstants;

public class TestClazzFileFinder {
   
   private static final File SOURCE = new File("src/test/resources/clazzFinderExample/");
   
   @Test
   public void testClasses() {
      List<String> clazzes = new ClazzFileFinder(TestConstants.DEFAULT_FOLDERS).getClasses(SOURCE);
      
      System.out.println(clazzes);
      
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.TestMe1"));
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.TestMe2"));
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.Second"));
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.TestMe2$Inner"));
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.TestMe2$Inner$InnerInner"));
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.LocalClass"));
      
      MatcherAssert.assertThat(clazzes, Matchers.hasItem("de.dagere.TestMe2$InnerEnum"));
   }
   
   @Test
   public void testGetSourceFile() throws FileNotFoundException {
      File sourceFileClass = new ClazzFileFinder(TestConstants.DEFAULT_FOLDERS).getSourceFile(SOURCE, new MethodCall("de.dagere.LocalClass"));
      Assert.assertNotNull(sourceFileClass);
      
      File sourceFileEnum = new ClazzFileFinder(TestConstants.DEFAULT_FOLDERS).getSourceFile(SOURCE, new MethodCall("de.dagere.LocalEnum"));
      Assert.assertNotNull(sourceFileEnum);
      
      File sourceFileInterface = new ClazzFileFinder(TestConstants.DEFAULT_FOLDERS).getSourceFile(SOURCE, new MethodCall("de.dagere.LocalInterface"));
      Assert.assertNotNull(sourceFileInterface);
      
      MethodCall exampleEntity = new MethodCall("de.dagere.LocalClass#myMethod(int)");
      String text = FileComparisonUtil.getMethodSource(SOURCE, exampleEntity, exampleEntity.getMethod(), TestConstants.DEFAULT_FOLDERS);
      MatcherAssert.assertThat(text, Matchers.containsString("this.i = i;"));
   }
}
