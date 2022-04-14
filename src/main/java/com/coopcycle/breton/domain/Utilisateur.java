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
 * A Utilisateur.
 */
@Table("utilisateur")
public class Utilisateur implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("firstname")
    private String firstname;

    @NotNull(message = "must not be null")
    @Size(max = 30)
    @Pattern(regexp = "^[A-Z][a-z]+$")
    @Column("lastname")
    private String lastname;

    @Column("mail")
    private String mail;

    @Column("phone")
    private String phone;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("address")
    private String address;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "cooperatives", "utilisateur" }, allowSetters = true)
    private Set<Commerce> commerce = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "utilisateur", "livreur", "produits" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Utilisateur id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Utilisateur firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Utilisateur lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return this.mail;
    }

    public Utilisateur mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return this.phone;
    }

    public Utilisateur phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Utilisateur address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Utilisateur setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Commerce> getCommerce() {
        return this.commerce;
    }

    public void setCommerce(Set<Commerce> commerce) {
        if (this.commerce != null) {
            this.commerce.forEach(i -> i.setUtilisateur(null));
        }
        if (commerce != null) {
            commerce.forEach(i -> i.setUtilisateur(this));
        }
        this.commerce = commerce;
    }

    public Utilisateur commerce(Set<Commerce> commerce) {
        this.setCommerce(commerce);
        return this;
    }

    public Utilisateur addCommerce(Commerce commerce) {
        this.commerce.add(commerce);
        commerce.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeCommerce(Commerce commerce) {
        this.commerce.remove(commerce);
        commerce.setUtilisateur(null);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setUtilisateur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setUtilisateur(this));
        }
        this.commandes = commandes;
    }

    public Utilisateur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Utilisateur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setUtilisateur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", mail='" + getMail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
