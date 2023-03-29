package de.dagere.nodeDiffGenerator.data;

public class MethodCallHelper {

   public static String getParameterString(final String[] parameterTypes) {
      if (parameterTypes.length > 0) {
         String parameterString = "(";
         for (String parameter : parameterTypes) {
            parameterString += parameter + ",";
         }
         parameterString = parameterString.substring(0, parameterString.length() - 1) + ")";
         return parameterString;
      } else {
         return "";
      }
   }

}
