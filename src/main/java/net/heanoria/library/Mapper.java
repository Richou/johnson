package net.heanoria.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapper {

    private ObjectMapper objectMapper = null;
    private Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z\\.]+)\\}");

    public Mapper() {
        objectMapper = new ObjectMapper();
    }

    public void configure(DeserializationFeature feature, boolean active) {
        objectMapper.configure(feature, active);
    }

    public void configure(SerializationFeature feature, boolean active) {
        objectMapper.configure(feature, active);
    }

    public <T> T readValue(String content, TypeReference<T> type) throws IOException, NoSuchFieldException, IllegalAccessException {
        T convertedJson = objectMapper.readValue(content, type);
        browseObject(convertedJson);
        return convertedJson;
    }

    /**
     * Map a json string value to object and perform the injection
     * @param content json string value
     * @param type type of object
     * @param <T> type
     * @return injected object
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public <T> T readValue(String content, Class<T> type) throws IOException, IllegalAccessException, NoSuchFieldException {
        T convertedJson = objectMapper.readValue(content, type);
        browseObject(convertedJson);
        return convertedJson;
    }

    /**
     * Bypass the mapping and perform the injection directly
     * @param object object for injection
     * @param <T> type
     * @return injected object
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public <T> T injectValue(T object) throws NoSuchFieldException, IllegalAccessException {
        browseObject(object);
        return object;
    }

    /**
     * Start point to browse object, for replacement
     * @param convertedJson object for injection
     * @param <T> type
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private <T> void browseObject(T convertedJson) throws IllegalAccessException, NoSuchFieldException {
        browseInnerObject(convertedJson, convertedJson);
    }

    /**
     * Make this method inner, to be able to perform recursively
     * @param convertedJson object for injection
     * @param origin full object
     * @param <T> type
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private <T> void browseInnerObject(T convertedJson, T origin) throws IllegalAccessException, NoSuchFieldException {
        List<Field> fullFields = new ArrayList<Field>();
        // Had to add in a full list the declared field for an object and parent if the object inherit an other object
        fullFields.addAll(Arrays.asList(convertedJson.getClass().getDeclaredFields()));
        fullFields.addAll(Arrays.asList(convertedJson.getClass().getSuperclass().getDeclaredFields()));
        for(Field declaredField : fullFields) {
            if(!Modifier.isTransient(declaredField.getModifiers())) {
                declaredField.setAccessible(true);
                Object object = declaredField.get(convertedJson);
                if (object instanceof String) {
                    String replacedValue = processReplacement(convertedJson, origin, (String) object);
                    declaredField.set(convertedJson, replacedValue);
                } else if (object != null && object instanceof Map) {
                    // If value is a map, we have to browse the map and call this method with the new value
                    for (Object key : ((Map) object).keySet()) {
                        if (!(((Map) object).get(key) instanceof String)) {
                            browseInnerObject(((Map) object).get(key), origin);
                        } else {
                            String replacement = processReplacement(convertedJson, origin, (String) ((Map) object).get(key));
                            ((Map) object).put(key, replacement);
                        }
                    }
                } else if (object != null && object instanceof List) {
                    for(Object item : (List)object) {
                        browseInnerObject(item, origin);
                    }
                } else if (object != null && !(object instanceof Double) && !(object instanceof Integer) && !(object instanceof Boolean) && !(object instanceof Short) && !(object instanceof Long) && !(object instanceof Float) && !(object instanceof Character) && !(object.getClass().isEnum())) {
                    // The value is not a string or a map so we don't need to process
                    browseInnerObject(object, origin);
                }
            }
        }
    }

    private <T> String processReplacement(T convertedJson, T origin, String object) throws NoSuchFieldException, IllegalAccessException {
        // If the object is instance of string we do the replacement if needed
        Matcher matcher = pattern.matcher(object);
        // The pattern is found with a regex it will search for ${xx.aa} pattern
        String replacedValue = object;
        // The matched values will be collected in a map
        Map<String, String> innerValueMap = matchesProcessing(matcher);
        for (String innerValueKey : innerValueMap.keySet()) {
            if (innerValueKey.contains(".")) {
                // If the value contains a dot, that means we have a deep object inside, so we fetch the correct value
                // with dot path and do the replacement
                String fetchedValue = processWithDotPath(innerValueMap.get(innerValueKey), origin);
                replacedValue = replacedValue.replace(innerValueKey, fetchedValue);
            } else {
                // Else we do the replacement directly
                Field innerField = convertedJson.getClass().getDeclaredField(innerValueMap.get(innerValueKey));
                innerField.setAccessible(true);
                replacedValue = replacedValue.replace(innerValueKey, String.valueOf(innerField.get(convertedJson)));
            }
        }
        return replacedValue;
    }

    /**
     * Starting point for deep object processing. <br />
     * For example : <br />
     * <code>
     *     ${path.url}
     * </code>
     *  will fetch url field in path object
     * @param innerValueKey the path without ${} keys
     * @param origin full object
     * @return correct value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String processWithDotPath(String innerValueKey, Object origin) throws NoSuchFieldException, IllegalAccessException {
        StringTokenizer stringTokenizer = new StringTokenizer(innerValueKey, ".");
        return processInnerPath(stringTokenizer, origin);
    }

    /**
     * Make an inner method to be able to perform recursively
     * @param path splitted path
     * @param origin full object
     * @return correct value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String processInnerPath(StringTokenizer path, Object origin) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField;
        String token = path.nextToken();
        try {
            declaredField = origin.getClass().getDeclaredField(token);
        }catch (NoSuchFieldException ex) {
            // No field found, try to get it from the super class
            declaredField = origin.getClass().getSuperclass().getDeclaredField(token);
        }
        declaredField.setAccessible(true);
        Object objectGetWithPath = declaredField.get(origin);
        if(objectGetWithPath instanceof String) {
            return (String) objectGetWithPath;
        } else {
            return processInnerPath(path, objectGetWithPath);
        }
    }

    private Map<String, String> matchesProcessing(Matcher matcher) {
        Map<String, String> valuesPaths = new HashMap<String, String>();
        while(matcher.find()) {
            valuesPaths.put(matcher.group(), matcher.group(1));
        }
        return valuesPaths;
    }
}
