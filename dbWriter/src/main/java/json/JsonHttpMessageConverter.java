package json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHttpMessageConverter {

   private static final class InstanceHolder {
      private static final JsonHttpMessageConverter converter = new JsonHttpMessageConverter();
   }

   private static Gson createGsonBuild() {
      GsonBuilder builder = new GsonBuilder();
      builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

      builder.addDeserializationExclusionStrategy(new ExclusionStrategy() {
         @Override
         public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().contains("errors");
         }

         @Override
         public boolean shouldSkipClass(Class<?> clazz) {
            return false;
         }
      });

      return builder.create();
   }

   private final Gson gson = createGsonBuild();

   private JsonHttpMessageConverter() { }

   public <T> String convert(T from) {
      return gson.toJson(from);
   }

   public static JsonHttpMessageConverter instanceOf() {
      return InstanceHolder.converter;
   }

}
