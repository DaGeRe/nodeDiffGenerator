package de.dagere.nodeDiffDetector.sourceReading;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import de.dagere.nodeDiffDetector.data.MethodCall;
import de.dagere.peass.dependency.traces.ParameterComparator;

class MethodReader {

   private static final Logger LOG = LogManager.getLogger(MethodReader.class);

   private final ClassOrInterfaceDeclaration clazz;

   public MethodReader(final ClassOrInterfaceDeclaration clazz) {
      this.clazz = clazz;
   }

   CallableDeclaration<?> getMethod(final Node node, final MethodCall currentTraceElement) {
      if (node != null && node.getParentNode().isPresent()) {
         final Node parent = node.getParentNode().get();
         if (node instanceof MethodDeclaration) {
            final MethodDeclaration method = (MethodDeclaration) node;
            String methodName = method.getNameAsString();
            if (methodName.equals(currentTraceElement.getMethod())) {
               //TODO LOG.trace
               LOG.trace("Parameter: {} Trace-Parameter: {}", method.getParameters().size(), currentTraceElement.getParameterTypes().length);
               LOG.trace(method.getParameters()); //TODO delete
               LOG.trace(Arrays.toString(currentTraceElement.getParameterTypes()));
               if (new ParameterComparator(this.clazz).parametersEqual(currentTraceElement, method)) {
                  if (parent instanceof TypeDeclaration<?>) {
                     final TypeDeclaration<?> clazz = (TypeDeclaration<?>) parent;
                     final String clazzName = clazz.getNameAsString();
                     if (clazzName.equals(currentTraceElement.getSimplestClazzName())) {
                        return method;
                     }
                  } else {
                     return method;
                  }
               }
            }
         } else if (node instanceof ConstructorDeclaration) {
            if ("<init>".equals(currentTraceElement.getMethod())) {
               if (parent instanceof TypeDeclaration<?>) {
                  final ConstructorDeclaration constructor = (ConstructorDeclaration) node;
                  final TypeDeclaration<?> clazz = (TypeDeclaration<?>) parent;
                  LOG.trace(clazz.getNameAsString() + " " + currentTraceElement.getClazz());
                  if (clazz.getNameAsString().equals(currentTraceElement.getSimplestClazzName())) {
                     if (new ParameterComparator(this.clazz).parametersEqual(currentTraceElement, constructor)) {
                        return (CallableDeclaration<?>) node;
                     }
                  }
               }
               LOG.trace(parent);
            }
         }

         for (final Node child : node.getChildNodes()) {
            final CallableDeclaration<?> possibleMethod = getMethod(child, currentTraceElement);
            if (possibleMethod != null) {
               return possibleMethod;
            }

         }
      }

      return null;
   }
}
