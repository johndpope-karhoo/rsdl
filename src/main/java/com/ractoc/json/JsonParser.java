package com.ractoc.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class JsonParser implements Closeable {
    private final Reader reader;

    public JsonParser(Reader json) {
        reader = json;
    }

    public <O> O readInto(Class<O> type) {
        O marshaled = null;
        try {
            marshaled = type.newInstance();
            int c;
            boolean inBlock = false;
            boolean inQuotedBlock = false;
            boolean inListBlock = false;
            int level = 0;

            StringBuilder field = new StringBuilder();
            while ((c = reader.read()) >= 0) {
                if (!inQuotedBlock && c == '{') {
                    if (level == 0) {
                        inBlock = true;
                    } else {
                        field.append((char) c);
                    }
                    level ++;
                } else if (!inQuotedBlock && c == '}') {
                    level --;
                    if (level == 0) {
                        inBlock = false;
                        processField(type, marshaled, field);
                        field = new StringBuilder();
                    } else {
                        field.append((char) c);
                    }
                } else if (level == 1 && !inQuotedBlock && !inListBlock && c == ',') {
                    processField(type, marshaled, field);
                    field = new StringBuilder();
                } else if (level == 1 && inBlock && !inQuotedBlock && c =='\"') {
                    inQuotedBlock = true;
                } else if (level == 1 && inBlock && inQuotedBlock && c =='\"') {
                    inQuotedBlock = false;
                } else if (level == 1 && inBlock && !inQuotedBlock && !inListBlock && c =='[') {
                    inListBlock = true;
                } else if (level == 1 && inBlock && !inQuotedBlock && inListBlock && c ==']') {
                    inListBlock = false;
                } else if (inBlock) {
                    field.append((char) c);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return marshaled;
    }

    private <O> void processField(Class<O> type, O marshaled, StringBuilder field) throws NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String name = field.substring(0, field.indexOf(":")).trim();
        String value = field.substring(field.indexOf(":") + 1).trim();
        System.out.println(name + "=" + value);
        Field typeField = type.getDeclaredField(trimQuotes(name));
        if (typeField != null) {
            Class<?> typeFieldClass = typeField.getType();
            Object typeFieldValue;
            if(typeFieldClass.isAssignableFrom(List.class)) {
                List<String> listParts = splitEntries(value);
                ParameterizedType parameterizedType = (ParameterizedType) typeField.getGenericType();
                Class<?> listClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                List<Object> list = new ArrayList<Object>();
                Object listPartValue;
                for(String listPart : listParts) {
                    AtomicReference<Constructor<?>> constructor = new AtomicReference<Constructor<?>>(null);
                    try {
                        constructor.set(listClass.getConstructor(String.class));
                        listPartValue = constructor.get().newInstance(trimQuotes(listPart));
                    } catch (NoSuchMethodException e) {
                        JsonParser jsonParser = new JsonParser(new StringReader(listPart));
                        listPartValue = jsonParser.readInto(listClass);
                    }
                    list.add(listPartValue);
                }
                typeFieldValue = list;
            } else {
                AtomicReference<Constructor<?>> constructor = new AtomicReference<Constructor<?>>(null);
                try {
                    constructor.set(typeFieldClass.getConstructor(String.class));
                    typeFieldValue = constructor.get().newInstance(trimQuotes(value));
                } catch (NoSuchMethodException e) {
                    JsonParser jsonParser = new JsonParser(new StringReader(value));
                    typeFieldValue = jsonParser.readInto(typeFieldClass);
                }
            }
            typeField.setAccessible(true);
            typeField.set(marshaled, typeFieldValue);
        }
    }

    private List<String> splitEntries(String listValue) {
        int level = 0;
        StringBuilder entry = new StringBuilder();
        List<String> entries = new ArrayList<String>();
        for (char c : listValue.toCharArray()) {
            if (c == '{') {
                level ++;
                entry.append(c);
            } else if (c == '}') {
                level --;
                entry.append(c);
                if (level == 0) {
                    entries.add(entry.toString().trim());
                    entry = new StringBuilder();
                }
            } else if (level == 0 && entry.toString().trim().length() > 0 && c == ',') {
                entries.add(entry.toString().trim());
                entry = new StringBuilder();
            } else {
                entry.append(c);
            }
        }
        return entries;
    }

    private String trimQuotes(String value) {
        String trimmedValue = value.trim();
        if (trimmedValue.startsWith("\"")) {
            trimmedValue = trimmedValue.substring(1);
        }
        if (trimmedValue.endsWith("\"")) {
            trimmedValue = trimmedValue.substring(0, trimmedValue.length() - 1);
        }
        return trimmedValue;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
