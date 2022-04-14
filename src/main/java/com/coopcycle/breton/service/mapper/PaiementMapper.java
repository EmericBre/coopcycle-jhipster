package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Paiement;
import com.coopcycle.breton.domain.Produit;
import com.coopcycle.breton.service.dto.PaiementDTO;
import com.coopcycle.breton.service.dto.ProduitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "produit", source = "produit", qualifiedByName = "produitId")
    PaiementDTO toDto(Paiement s);

    @Named("produitId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProduitDTO toDtoProduitId(Produit produit);
}
