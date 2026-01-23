package br.com.optimus.web.record;

import java.util.List;

public record UserJwtDto(

        String username,
        String idUnitOrg,
        List<String> roles
){}
