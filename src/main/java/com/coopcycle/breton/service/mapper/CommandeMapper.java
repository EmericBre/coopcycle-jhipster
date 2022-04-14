package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Commande;
import com.coopcycle.breton.domain.Livreur;
import com.coopcycle.breton.domain.Utilisateur;
import com.coopcycle.breton.service.dto.CommandeDTO;
import com.coopcycle.breton.service.dto.LivreurDTO;
import com.coopcycle.breton.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurId")
    CommandeDTO toDto(Commande s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("livreurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivreurDTO toDtoLivreurId(Livreur livreur);
}
