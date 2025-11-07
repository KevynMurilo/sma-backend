package com.monitoramento.asset.api.mapper;

import com.monitoramento.asset.api.dto.SimpleDriverDTO;
import com.monitoramento.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleDriverMapper {
    SimpleDriverDTO userToSimpleDriverDTO(User user);
}