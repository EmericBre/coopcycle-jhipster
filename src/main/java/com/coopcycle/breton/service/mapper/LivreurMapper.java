package com.coopcycle.breton.service.mapper;

import com.coopcycle.breton.domain.Livreur;
import com.coopcycle.breton.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {}
