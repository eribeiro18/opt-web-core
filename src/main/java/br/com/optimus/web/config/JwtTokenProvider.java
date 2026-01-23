package br.com.optimus.web.config;

import br.com.optimus.web.record.UserJwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    public static final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("OptimusERPManagementSystemSecretsGeneratorEnhancedControlsStartHereWeWillBeginSalesSoon."));
    public static final String SECRET = Encoders.BASE64.encode(KEY.getEncoded());
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    static Authentication getAuthentication(HttpServletRequest request) throws ValidationException {
    	try {
    		if(validateSpringDocs(request.getRequestURI())) {
    			return null;
    		}
    		String token = request.getHeader(HEADER_STRING);
            if (token == null) {
                throw new ValidationException("Token not found");
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            UserJwtDto userContext = new UserJwtDto(claims.getSubject(),
                                                    claims.get("idUnitOrg", String.class),
                                                    claims.get("privileges", List.class));
            var authorities = userContext.roles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return userContext != null ? new UsernamePasswordAuthenticationToken(userContext.username(), null, authorities) : null;
		} catch (Exception e) {
			throw new ValidationException("Token invalid or expired");
		}
    }
    
    private static boolean validateSpringDocs(String uri) {
    	return uri.contains("/swagger-ui/") || uri.contains("/v3/api-docs/");
    }
	
}
