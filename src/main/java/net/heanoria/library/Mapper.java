package net.heanoria.library;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapper {

    private ObjectMapper objectMapper = null;
    private Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z\\.]+)\\}");

    public Mapper() {
        objectMapper = new ObjectMapper();
    }

    public <T> T readValue(String content, Class<T> type) throws IOException, IllegalAccessException, NoSuchFieldException {
        T convertedJson = objectMapper.readValue(content, type);
        browseObject(convertedJson);
        return convertedJson;
    }

    public <T> T injectValue(T object) throws NoSuchFieldException, IllegalAccessException {
        browseObject(object);
        return object;
    }

    private <T> void browseObject(T convertedJson) throws IllegalAccessException, NoSuchFieldException {
        browseInnerObject(convertedJson, convertedJson);
    }

    private <T> void browseInnerObject(T convertedJson, T origin) throws IllegalAccessException, NoSuchFieldException {
        List<Field> fullFields = new ArrayList<Field>();
        fullFields.addAll(Arrays.asList(convertedJson.getClass().getDeclaredFields()));
        fullFields.addAll(Arrays.asList(convertedJson.getClass().getSuperclass().getDeclaredFields()));
        for(Field declaredField : fullFields) {
            declaredField.setAccessible(true);
            Object object = declaredField.get(convertedJson);
            if(object instanceof String) {
                Matcher matcher = pattern.matcher((String) object);
                String replacedValue = (String) object;
                Map<String, String> innerValueMap = matchesProcessing(matcher);
                for (String innerValueKey : innerValueMap.keySet()) {
                    if (innerValueKey.contains(".")) {
                        String fetchedValue = processWithDotPath(innerValueMap.get(innerValueKey), origin);
                        replacedValue = replacedValue.replace(innerValueKey, fetchedValue);
                    } else {
                        Field innerField = convertedJson.getClass().getDeclaredField(innerValueMap.get(innerValueKey));
                        innerField.setAccessible(true);
                        replacedValue = replacedValue.replace(innerValueKey, String.valueOf(innerField.get(convertedJson)));
                    }
                }
                declaredField.set(convertedJson, replacedValue);
            } else if(object != null && object instanceof Map) {
                for(Object key : ((Map)object).keySet()) {
                    browseInnerObject(((Map)object).get(key), origin);
                }
            } else if(object != null && !(object instanceof Double) && !(object instanceof List) && !(object instanceof Integer)) {
                browseInnerObject(object, origin);
            }
        }
    }

    private String processWithDotPath(String innerValueKey, Object origin) throws NoSuchFieldException, IllegalAccessException {
        StringTokenizer stringTokenizer = new StringTokenizer(innerValueKey, ".");
        return processInnerPath(stringTokenizer, origin);
    }

    private String processInnerPath(StringTokenizer path, Object origin) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField;
        String token = path.nextToken();
        try {
            declaredField = origin.getClass().getDeclaredField(token);
        }catch (NoSuchFieldException ex) {
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
