package com.taller.app.core.audit.util;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para comparar dos objetos y detectar cambios en sus campos.
 */
public class ReflectionUtils {

    private static final String[] CAMPOS_IGNORADOS = {
            "id",
            "version",
            "fechaCreacion",
            "fechaModificacion"
    };

    public static Map<String, String[]> detectChanges(Object oldObj, Object newObj) {

        Map<String, String[]> changes = new HashMap<>();

        if (oldObj == null || newObj == null) {
            return changes;
        }

        Field[] fields = oldObj.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            if (isIgnoredField(field.getName())) {
                continue;
            }

            try {

                Object oldValue = field.get(oldObj);
                Object newValue = field.get(newObj);

                if ((oldValue == null && newValue != null) ||
                        (oldValue != null && !oldValue.equals(newValue))) {

                    changes.put(field.getName(), new String[]{
                            String.valueOf(oldValue),
                            String.valueOf(newValue)
                    });
                }

            } catch (IllegalAccessException ignored) {}
        }

        return changes;
    }

    public static Integer getEntityId(Object entity) {

        if (entity == null) {
            return null;
        }

        try {

            for (var method : entity.getClass().getMethods()) {

                if (method.getName().equalsIgnoreCase("getId")
                        || method.getName().startsWith("getId")) {

                    Object value = method.invoke(entity);

                    if (value instanceof Integer) {
                        return (Integer) value;
                    }

                    if (value instanceof Long) {
                        return ((Long) value).intValue();
                    }
                }
            }

        } catch (Exception ignored) {}

        return null;
    }

    // 🔥 deepCopy real para auditoría
    public static <T> T deepCopyEntity(T source) {
        if (source == null) return null;

        try {
            Class<?> clazz = source.getClass();
            T target = (T) clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {

                field.setAccessible(true);

                // Ignorar relaciones JPA
                if (field.isAnnotationPresent(OneToMany.class) ||
                        field.isAnnotationPresent(ManyToOne.class) ||
                        field.isAnnotationPresent(ManyToMany.class) ||
                        field.isAnnotationPresent(OneToOne.class)) {
                    continue;
                }

                // Ignorar campos de Hibernate
                if (field.getName().equals("handler") ||
                        field.getName().equals("hibernateLazyInitializer")) {
                    continue;
                }

                Object value = field.get(source);

                // Copiar solo tipos simples
                if (value == null ||
                        value instanceof String ||
                        value instanceof Number ||
                        value instanceof Boolean ||
                        value instanceof java.time.temporal.Temporal) {

                    field.set(target, value);
                }
            }

            return target;

        } catch (Exception e) {
            throw new RuntimeException("Error clonando entidad JPA", e);
        }
    }

    private static boolean isIgnoredField(String fieldName) {

        for (String ignored : CAMPOS_IGNORADOS) {
            if (ignored.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }

        return false;
    }
}


