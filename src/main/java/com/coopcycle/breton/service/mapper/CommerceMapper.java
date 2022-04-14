package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Commerce;
import com.coopcycle.breton.domain.Cooperative;
import com.coopcycle.breton.domain.Utilisateur;
import com.coopcycle.breton.service.dto.CommerceDTO;
import com.coopcycle.breton.service.dto.CooperativeDTO;
import com.coopcycle.breton.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commerce} and its DTO {@link CommerceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommerceMapper extends EntityMapper<CommerceDTO, Commerce> {
    @Mapping(target = "cooperatives", source = "cooperatives", qualifiedByName = "cooperativeIdSet")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    CommerceDTO toDto(Commerce s);

    @Mapping(target = "removeCooperative", ignore = true)
    Commerce toEntity(CommerceDTO commerceDTO);

    @Named("cooperativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeDTO toDtoCooperativeId(Cooperative cooperative);

    @Named("cooperativeIdSet")
    default Set<CooperativeDTO> toDtoCooperativeIdSet(Set<Cooperative> cooperative) {
        return cooperative.stream().map(this::toDtoCooperativeId).collect(Collectors.toSet());
    }

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
