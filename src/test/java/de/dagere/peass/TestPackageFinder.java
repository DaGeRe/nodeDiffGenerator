package de.dagere.peass;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.dagere.nodeDiffDetector.clazzFinding.ClazzFileFinder;
import de.dagere.nodeDiffDetector.config.FolderConfig;

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
