package br.com.optimus.web.config.commons;

import br.com.optimus.web.record.UserJwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.com.optimus.web.config.JwtTokenProvider.*;

@Component
public class JsonUserProvider {
  
    public static UserJwtDto getContextDto(String authorization) {
    	return getUserContext(authorization);
    }

    private static UserJwtDto getUserContext(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(KEY)
					.build()
					.parseSignedClaims(token.replace(TOKEN_PREFIX, ""))
					.getPayload();
			return new UserJwtDto(claims.getSubject(),
					              claims.get("idUser", String.class),
								  claims.get("idUnitOrg", String.class),
					              claims.get("idCompanyDefault", String.class),
					              claims.get("privileges", List.class));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
