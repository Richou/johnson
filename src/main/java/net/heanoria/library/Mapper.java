package net.heanoria.library;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
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

    private <T> void browseObject(T convertedJson) throws IllegalAccessException, NoSuchFieldException {
        browseInnerObject(convertedJson);
    }

    private <T> void browseInnerObject(T convertedJson) throws IllegalAccessException, NoSuchFieldException {
        Field[] declaredFields = convertedJson.getClass().getDeclaredFields();
        for(Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object object = declaredField.get(convertedJson);
            if(object instanceof String) {
                Matcher matcher = pattern.matcher((String) object);
                String replacedValue = (String) object;
                Map<String, String> innerValueMap = matchesProcessing(matcher);
                for (String innerValueKey : innerValueMap.keySet()) {
                    if(innerValueKey.contains(".")) {
                        String fetchedValue = processWithDotPath(innerValueMap.get(innerValueKey), convertedJson);
                        replacedValue = replacedValue.replace(innerValueKey, fetchedValue);
                    } else {
                        Field innerField = convertedJson.getClass().getDeclaredField(innerValueMap.get(innerValueKey));
                        innerField.setAccessible(true);
                        replacedValue = replacedValue.replace(innerValueKey, String.valueOf(innerField.get(convertedJson)));
                    }
                }
                declaredField.set(convertedJson, replacedValue);
            } else if(!(object instanceof Double) && !(object instanceof List) && !(object instanceof Map) && !(object instanceof Integer)) {
                browseInnerObject(object);
            }
        }
    }

    private String processWithDotPath(String innerValueKey, Object convertedJson) throws NoSuchFieldException, IllegalAccessException {
        StringTokenizer stringTokenizer = new StringTokenizer(innerValueKey, ".");
        return processInnerPath(stringTokenizer, convertedJson);
    }

    private String processInnerPath(StringTokenizer path, Object innerObject) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = innerObject.getClass().getDeclaredField(path.nextToken());
        declaredField.setAccessible(true);
        Object objectGetWithPath = declaredField.get(innerObject);
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
