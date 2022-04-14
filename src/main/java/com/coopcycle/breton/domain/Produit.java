package com.coopcycle.breton.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Produit.
 */
@Table("produit")
public class Produit implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Column("price")
    private Float price;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @Column("description")
    private String description;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "utilisateur", "livreur", "produits" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    @Transient
    private Paiement paiement;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "cooperatives", "utilisateur" }, allowSetters = true)
    private Commerce commerce;

    @Column("commerce_id")
    private String commerceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Produit id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getPrice() {
        return this.price;
    }

    public Produit price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getType() {
        return this.type;
    }

    public Produit type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Produit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Produit setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Produit commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Produit addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getProduits().add(this);
        return this;
    }

    public Produit removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getProduits().remove(this);
        return this;
    }

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        if (this.paiement != null) {
            this.paiement.setProduit(null);
        }
        if (paiement != null) {
            paiement.setProduit(this);
        }
        this.paiement = paiement;
    }

    public Produit paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Commerce getCommerce() {
        return this.commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
        this.commerceId = commerce != null ? commerce.getId() : null;
    }

    public Produit commerce(Commerce commerce) {
        this.setCommerce(commerce);
        return this;
    }

    public String getCommerceId() {
        return this.commerceId;
    }

    public void setCommerceId(String commerce) {
        this.commerceId = commerce;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
