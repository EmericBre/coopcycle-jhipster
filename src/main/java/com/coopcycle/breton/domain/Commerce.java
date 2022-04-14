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
 * A Commerce.
 */
@Table("commerce")
public class Commerce implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("adress")
    private String adress;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "commandes", "paiement", "commerce" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commerce" }, allowSetters = true)
    private Set<Cooperative> cooperatives = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commerce", "commandes" }, allowSetters = true)
    private Utilisateur utilisateur;

    @Column("utilisateur_id")
    private String utilisateurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Commerce id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Commerce name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return this.adress;
    }

    public Commerce adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Commerce setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.setCommerce(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setCommerce(this));
        }
        this.produits = produits;
    }

    public Commerce produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Commerce addProduit(Produit produit) {
        this.produits.add(produit);
        produit.setCommerce(this);
        return this;
    }

    public Commerce removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.setCommerce(null);
        return this;
    }

    public Set<Cooperative> getCooperatives() {
        return this.cooperatives;
    }

    public void setCooperatives(Set<Cooperative> cooperatives) {
        this.cooperatives = cooperatives;
    }

    public Commerce cooperatives(Set<Cooperative> cooperatives) {
        this.setCooperatives(cooperatives);
        return this;
    }

    public Commerce addCooperative(Cooperative cooperative) {
        this.cooperatives.add(cooperative);
        cooperative.getCommerce().add(this);
        return this;
    }

    public Commerce removeCooperative(Cooperative cooperative) {
        this.cooperatives.remove(cooperative);
        cooperative.getCommerce().remove(this);
        return this;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.utilisateurId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Commerce utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public String getUtilisateurId() {
        return this.utilisateurId;
    }

    public void setUtilisateurId(String utilisateur) {
        this.utilisateurId = utilisateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commerce)) {
            return false;
        }
        return id != null && id.equals(((Commerce) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commerce{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
