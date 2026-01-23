package br.com.optimus.web.record;

import java.util.List;

public record UserJwtDto (

        String username,
        String idUser,
        String idUnitOrg,
        String idCompanyDefault,
        List<String> roles
){}
