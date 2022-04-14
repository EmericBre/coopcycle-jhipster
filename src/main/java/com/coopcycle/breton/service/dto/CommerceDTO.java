package com.coopcycle.breton.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.breton.domain.Commerce} entity.
 */
public class CommerceDTO implements Serializable {

    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String adress;

    private Set<CooperativeDTO> cooperatives = new HashSet<>();

    private UtilisateurDTO utilisateur;

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

    public Set<CooperativeDTO> getCooperatives() {
        return cooperatives;
    }

    public void setCooperatives(Set<CooperativeDTO> cooperatives) {
        this.cooperatives = cooperatives;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommerceDTO)) {
            return false;
        }

        CommerceDTO commerceDTO = (CommerceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commerceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommerceDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            ", cooperatives=" + getCooperatives() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
