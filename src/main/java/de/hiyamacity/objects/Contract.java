package de.hiyamacity.objects;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Contract extends Request<Contract> {

    @Expose
    private String conditions;

    public Contract(UUID creator, UUID contracted, String conditions) {
        super(creator, contracted, System.currentTimeMillis(), RequestType.CONTRACT);
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create().toJson(this);
    }
}
