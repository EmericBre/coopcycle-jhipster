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
 * A Cooperative.
 */
@Table("cooperative")
public class Cooperative implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("adress")
    private String adress;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "cooperatives", "utilisateur" }, allowSetters = true)
    private Set<Commerce> commerce = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cooperative id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cooperative name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return this.adress;
    }

    public Cooperative adress(String adress) {
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

    public Cooperative setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<Commerce> getCommerce() {
        return this.commerce;
    }

    public void setCommerce(Set<Commerce> commerce) {
        if (this.commerce != null) {
            this.commerce.forEach(i -> i.removeCooperative(this));
        }
        if (commerce != null) {
            commerce.forEach(i -> i.addCooperative(this));
        }
        this.commerce = commerce;
    }

    public Cooperative commerce(Set<Commerce> commerce) {
        this.setCommerce(commerce);
        return this;
    }

    public Cooperative addCommerce(Commerce commerce) {
        this.commerce.add(commerce);
        commerce.getCooperatives().add(this);
        return this;
    }

    public Cooperative removeCommerce(Commerce commerce) {
        this.commerce.remove(commerce);
        commerce.getCooperatives().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
