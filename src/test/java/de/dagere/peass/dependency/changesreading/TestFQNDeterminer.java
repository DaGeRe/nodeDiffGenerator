package de.dagere.peass.dependency.changesreading;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.github.javaparser.ast.CompilationUnit;

public class TestFQNDeterminer {

   @Test
   public void testInnerClass() throws FileNotFoundException {
      File file = new File("src/test/resources/clazzFinderExample/src/main/java/de/dagere/TestMe2.java");
      CompilationUnit unit = JavaParserProvider.parse(file);

      String fqnTest = FQNDeterminer.getParameterFQN(unit, "TestMe2");
      Assert.assertEquals("de.TestMe2", fqnTest);

      String fqnInner = FQNDeterminer.getParameterFQN(unit, "Inner");
      Assert.assertEquals("de.TestMe2$Inner", fqnInner);

      String fqnInnerInner = FQNDeterminer.getParameterFQN(unit, "InnerInner");
      Assert.assertEquals("de.TestMe2$Inner$InnerInner", fqnInnerInner);

      String fqnEnum = FQNDeterminer.getParameterFQN(unit, "InnerEnum");
      Assert.assertEquals("de.TestMe2$InnerEnum", fqnEnum);

      String fqnSecond = FQNDeterminer.getParameterFQN(unit, "Second");
      Assert.assertEquals("de.Second", fqnSecond);
   }
}
