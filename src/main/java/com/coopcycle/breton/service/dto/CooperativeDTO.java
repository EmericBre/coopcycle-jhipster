package com.coopcycle.breton.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.breton.domain.Cooperative} entity.
 */
public class CooperativeDTO implements Serializable {

    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    private String name;

    private String adress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativeDTO)) {
            return false;
        }

        CooperativeDTO cooperativeDTO = (CooperativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cooperativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
