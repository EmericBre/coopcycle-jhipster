package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Commande;
import com.coopcycle.breton.domain.Commerce;
import com.coopcycle.breton.domain.Produit;
import com.coopcycle.breton.service.dto.CommandeDTO;
import com.coopcycle.breton.service.dto.CommerceDTO;
import com.coopcycle.breton.service.dto.ProduitDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produit} and its DTO {@link ProduitDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper extends EntityMapper<ProduitDTO, Produit> {
    @Mapping(target = "commandes", source = "commandes", qualifiedByName = "commandeIdSet")
    @Mapping(target = "commerce", source = "commerce", qualifiedByName = "commerceId")
    ProduitDTO toDto(Produit s);

    @Mapping(target = "removeCommande", ignore = true)
    Produit toEntity(ProduitDTO produitDTO);

    @Named("commandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeDTO toDtoCommandeId(Commande commande);

    @Named("commandeIdSet")
    default Set<CommandeDTO> toDtoCommandeIdSet(Set<Commande> commande) {
        return commande.stream().map(this::toDtoCommandeId).collect(Collectors.toSet());
    }

    @Named("commerceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommerceDTO toDtoCommerceId(Commerce commerce);
}
