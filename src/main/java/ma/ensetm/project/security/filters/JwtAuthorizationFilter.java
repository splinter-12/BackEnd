package ma.ensetm.project.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ma.ensetm.project.security.config.SecurityConstant;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class JwtAuthorizationFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if( request.getServletPath().equals( SecurityConstant.REFRESH_PATH)){ // if the request is for refresh token
            filterChain.doFilter(request, response);
            return ;
        }
        String jwt_token = request.getHeader(SecurityConstant.HEADER_STRING);
        if( jwt_token!=null && jwt_token.startsWith(SecurityConstant.TOKEN_PREFIX) ){ // is it a valid token ?
            try{
                String jwt = jwt_token.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.SECRET);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(jwt);

                String username= decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()) ;
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(token);

                filterChain.doFilter(request, response);
            }catch (Exception e){
                response.setHeader("error",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
