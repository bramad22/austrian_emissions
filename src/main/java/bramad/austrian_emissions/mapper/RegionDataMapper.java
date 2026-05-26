package bramad.austrian_emissions.mapper;

import bramad.austrian_emissions.annotations.ToDto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RegionDataMapper {
    public static Map<String, Object> toDto(Object o) {
        Map<String, Object> result = new HashMap<>();

        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ToDto.class)) {
                try {
                    field.setAccessible(true);
                    ToDto annotation = field.getAnnotation(ToDto.class);
                    String key = annotation.key().isEmpty() ? field.getName() : annotation.key();

                    Object value = field.get(o);

                    if (value != null && !value.getClass().isPrimitive() && !value.getClass().equals(String.class)
                            && !value.getClass().equals(Double.class) && !value.getClass().equals(Integer.class)
                            && !value.getClass().equals(Long.class)) {
                        value = toDto(value);
                    }

                    result.put(key, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}