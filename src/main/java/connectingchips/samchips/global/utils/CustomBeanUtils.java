package connectingchips.samchips.global.utils;

import jakarta.persistence.ManyToOne;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

@Component
public class CustomBeanUtils<T> {
    public T copyNonNullProperties(T source, T destination) {
        if (source == null || destination == null || source.getClass() != destination.getClass()) {
            return null;
        }

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper dest = new BeanWrapperImpl(destination);


        for (final Field property : source.getClass().getDeclaredFields()) {
            Object sourceProperty = src.getPropertyValue(property.getName());
            boolean isJoinEntity = Arrays.stream(property.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation instanceof ManyToOne);
            if (sourceProperty != null && !(sourceProperty instanceof Collection<?>) && !isJoinEntity) {
                dest.setPropertyValue(property.getName(), sourceProperty);
            }
        }

        return destination;
    }
}
