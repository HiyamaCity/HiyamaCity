package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Locale {

    @Expose
    private String language;
    @Expose
    private String country;

    public Locale(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public java.util.Locale getJavaUtilLocale() {
        return new java.util.Locale(this.getLanguage(), this.getCountry());
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}