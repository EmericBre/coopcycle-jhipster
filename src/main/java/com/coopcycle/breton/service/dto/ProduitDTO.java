package com.coopcycle.breton.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.breton.domain.Produit} entity.
 */
public class ProduitDTO implements Serializable {

    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private Float price;

    @NotNull(message = "must not be null")
    private String type;

    private String description;

    private Set<CommandeDTO> commandes = new HashSet<>();

    private CommerceDTO commerce;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CommandeDTO> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<CommandeDTO> commandes) {
        this.commandes = commandes;
    }

    public CommerceDTO getCommerce() {
        return commerce;
    }

    public void setCommerce(CommerceDTO commerce) {
        this.commerce = commerce;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProduitDTO)) {
            return false;
        }

        ProduitDTO produitDTO = (ProduitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, produitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProduitDTO{" +
            "id='" + getId() + "'" +
            ", price=" + getPrice() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", commandes=" + getCommandes() +
            ", commerce=" + getCommerce() +
            "}";
    }
}
