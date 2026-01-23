package br.com.optimus.web.config.commons;

import br.com.optimus.web.record.UserJwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.com.optimus.web.config.JwtTokenProvider.SECRET;
import static br.com.optimus.web.config.JwtTokenProvider.TOKEN_PREFIX;

@Component
public class JsonUserProvider {
  
    public static UserJwtDto getContextDto(String authorization) {
    	return getUserContext(authorization);
    }

    private static UserJwtDto getUserContext(String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(SECRET)
					.build()
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody();
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
