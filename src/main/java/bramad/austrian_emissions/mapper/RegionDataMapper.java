package bramad.austrian_emissions.mapper;

import bramad.austrian_emissions.annotations.ToDto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RegionDataMapper {
    public static Map<String, Object> toDto(Object o){
        Map<String,Object> result =new HashMap<>();

        for(Field field : o.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ToDto.class)) {
                try {
                    field.setAccessible(true);

                    ToDto annotation = field.getAnnotation(ToDto.class);
                    String key = annotation.key();

                    if(key.isEmpty()) {
                        key = field.getName();
                    }

                    Object value = field.get(o);
                    result.put(key, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}