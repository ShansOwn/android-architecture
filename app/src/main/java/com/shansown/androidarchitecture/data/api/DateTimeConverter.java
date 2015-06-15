package com.shansown.androidarchitecture.data.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.shansown.androidarchitecture.data.mapper.DateTimeMapper;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Singleton
public final class DateTimeConverter
    implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

  private final DateTimeMapper mapper;

  @Inject
  public DateTimeConverter(DateTimeMapper mapper) {
    this.mapper = mapper;
  }

  @Override public JsonElement serialize(DateTime src, Type typeOfSrc,
      JsonSerializationContext context) {
    return new JsonPrimitive(mapper.toString(src));
  }

  @Override public DateTime deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return mapper.toDateTime(json.getAsString());
  }
}
