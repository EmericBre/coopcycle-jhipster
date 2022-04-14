package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Utilisateur;
import com.coopcycle.breton.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {}
