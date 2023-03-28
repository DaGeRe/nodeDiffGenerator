package de.dagere.peass.dependency;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ParseException;

import de.dagere.peass.config.FolderConfig;
import de.dagere.peass.dependency.analysis.data.ChangedEntity;
import de.dagere.peass.dependency.changesreading.ClazzChangeData;
import de.dagere.peass.dependency.changesreading.FileComparisonUtil;

public class ChangeDetector {
   
   private static final Logger LOG = LogManager.getLogger(ChangeDetector.class);
   
   private final FolderConfig config;
   private final SourceCodeFolders sourceCodeFolders;
   
   public ChangeDetector(FolderConfig config, SourceCodeFolders sourceCodeFolders) {
      this.config = config;
      this.sourceCodeFolders = sourceCodeFolders;
   }

   public void compareClazz(final Map<ChangedEntity, ClazzChangeData> changedClassesMethods, final Iterator<ChangedEntity> clazzIterator) {
      final ChangedEntity clazz = clazzIterator.next();
      final ClazzChangeData changeData = new ClazzChangeData(clazz);
      try {
         ClazzFileFinder finder = new ClazzFileFinder(config);
         final File newFile = finder.getSourceFile(sourceCodeFolders.getProjectFolder(), clazz);
         final File oldFile = finder.getSourceFile(sourceCodeFolders.getOldSources(), clazz);
         LOG.info("Comparing {}", newFile, oldFile);
         if (newFile != null && newFile.exists() && oldFile != null) {
            compareFiles(changedClassesMethods, clazzIterator, clazz, changeData, newFile, oldFile);
         } else {
            LOG.info("Class did not exist before: {}", clazz);
            changeData.addClazzChange(clazz);
            changedClassesMethods.put(clazz, changeData);
         }
      } catch (final ParseException | NoSuchElementException pe) {
         LOG.info("Class is unparsable for java parser, so to be sure it is added to the changed classes: {}", clazz);
         changeData.addClazzChange(clazz);
         changedClassesMethods.put(clazz, changeData);
         pe.printStackTrace();
      } catch (final IOException e) {
         LOG.info("Class is unparsable for java parser, so to be sure it is added to the changed classes: {}", clazz);
         changeData.addClazzChange(clazz);
         changedClassesMethods.put(clazz, changeData);
         e.printStackTrace();
      }
   }
   
   private void compareFiles(final Map<ChangedEntity, ClazzChangeData> changedClassesMethods, final Iterator<ChangedEntity> clazzIterator, final ChangedEntity clazz,
         final ClazzChangeData changeData, final File newFile, final File oldFile) throws ParseException, IOException {
      FileComparisonUtil.getChangedMethods(newFile, oldFile, changeData);
      boolean isImportChange = false;
      ClazzFileFinder finder = new ClazzFileFinder(config);
      for (ChangedEntity entity : changeData.getImportChanges()) {
         final File entityFile = finder.getSourceFile(sourceCodeFolders.getProjectFolder(), entity);
         if (entityFile != null && entityFile.exists()) {
            isImportChange = true;
            changeData.setChange(true);
            changeData.setOnlyMethodChange(false);
            changeData.addClazzChange(clazz);
         }
      }
      
      if (!changeData.isChange() && !isImportChange) {
         clazzIterator.remove();
         LOG.debug("Files identical: {}", clazz);
      } else {
         changedClassesMethods.put(clazz, changeData);
      }
   }
}
