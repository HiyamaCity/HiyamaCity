package de.hiyamacity.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;

public final class AutoValueAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        int m = rawType.getModifiers();
        if (!Modifier.isAbstract(m)) {
            return null;
        }

        String packageName = rawType.getPackage().getName();
        String className = rawType.getName().substring(packageName.length() + 1).replace('$', '_');
        String autoValueName = packageName + ".AutoValue_" + className;

        try {
            Class<?> autoValueType = Class.forName(autoValueName);
            return (TypeAdapter<T>) gson.getAdapter(autoValueType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load AutoValue type " + autoValueName, e);
        }
    }
}