package de.dagere.nodeDiffDetector.data.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import de.dagere.nodeDiffDetector.data.MethodCall;

public class MethodCallDeserializer extends KeyDeserializer {

   public MethodCallDeserializer() {
      }

   @Override
   public MethodCall deserializeKey(final String key, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
      String value = key;
      final MethodCall entity;

      String method = null;
      if (value.contains(MethodCall.METHOD_SEPARATOR)) {
         method = value.substring(value.indexOf(MethodCall.METHOD_SEPARATOR) + 1);
         value = value.substring(0, value.indexOf(MethodCall.METHOD_SEPARATOR));
      }

      if (value.contains(MethodCall.MODULE_SEPARATOR)) {
         final String clazz = value.substring(value.indexOf(MethodCall.MODULE_SEPARATOR) + 1);
         final String module = value.substring(0, value.indexOf(MethodCall.MODULE_SEPARATOR));
         entity = new MethodCall(clazz, module, method);
      } else {
         entity = new MethodCall(value, "", method);
      }

      return entity;
   }
}
