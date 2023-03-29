package de.dagere.nodeDiffDetector.clazzFinding;

import java.io.File;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;

import de.dagere.nodeDiffDetector.testUtils.TestConstants;

public class TestPackageFinder {

   @Test
   public void testDependencyModule() {
      final List<String> lowestPackage = new ClazzFileFinder(TestConstants.DEFAULT_FOLDERS).getClasses(new File("."));
      System.out.println(lowestPackage);
      MatcherAssert.assertThat(lowestPackage, IsIterableContaining.hasItem("de.dagere.peass.nodeDiffGenerator.config.FolderConfig"));
      MatcherAssert.assertThat(lowestPackage, Matchers.not(IsIterableContaining.hasItem("de.dagere.peass.nodeDiffGenerator.config.FolderConfig.FolderConfig")));
      MatcherAssert.assertThat(lowestPackage, IsIterableContaining.hasItem("de.dagere.peass.nodeDiffGenerator.config.SourceCodeFolders"));
   }
}
