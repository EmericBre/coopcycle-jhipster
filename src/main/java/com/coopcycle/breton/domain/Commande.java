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
 * A Commande.
 */
@Table("commande")
public class Commande implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "commerce", "commandes" }, allowSetters = true)
    private Utilisateur utilisateur;

    @Transient
    @JsonIgnoreProperties(value = { "commandes" }, allowSetters = true)
    private Livreur livreur;

    @Transient
    @JsonIgnoreProperties(value = { "commandes", "paiement", "commerce" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @Column("utilisateur_id")
    private String utilisateurId;

    @Column("livreur_id")
    private String livreurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Commande id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Commande setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.utilisateurId = utilisateur != null ? utilisateur.getId() : null;
    }

    public Commande utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.livreurId = livreur != null ? livreur.getId() : null;
    }

    public Commande livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.removeCommande(this));
        }
        if (produits != null) {
            produits.forEach(i -> i.addCommande(this));
        }
        this.produits = produits;
    }

    public Commande produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Commande addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getCommandes().add(this);
        return this;
    }

    public Commande removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getCommandes().remove(this);
        return this;
    }

    public String getUtilisateurId() {
        return this.utilisateurId;
    }

    public void setUtilisateurId(String utilisateur) {
        this.utilisateurId = utilisateur;
    }

    public String getLivreurId() {
        return this.livreurId;
    }

    public void setLivreurId(String livreur) {
        this.livreurId = livreur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            "}";
    }
}
