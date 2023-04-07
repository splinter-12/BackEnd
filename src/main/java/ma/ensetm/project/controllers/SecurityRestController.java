 package ma.ensetm.project.controllers;

 import com.auth0.jwt.JWT;
 import com.auth0.jwt.JWTVerifier;
 import com.auth0.jwt.algorithms.Algorithm;
 import com.auth0.jwt.interfaces.DecodedJWT;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import lombok.AllArgsConstructor;
 import ma.ensetm.project.security.config.SecurityConstant;
 import ma.ensetm.project.security.entities.AppRole;
 import ma.ensetm.project.security.entities.AppUser;
 import ma.ensetm.project.security.service.SecurityService;
 import org.springframework.web.bind.annotation.CrossOrigin;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RestController;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.security.Principal;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class SecurityRestController {
    private SecurityService securityService;

    @GetMapping( "/api/refresh-token" )
    public void refreshToken( HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jwt_token = request.getHeader(SecurityConstant.HEADER_STRING);
        if( jwt_token!=null && jwt_token.startsWith(SecurityConstant.TOKEN_PREFIX) ){
            try{
                String jwt = jwt_token.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.SECRET);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(jwt);

                String username= decodedJWT.getSubject();
                AppUser user = securityService.loadUserByUsername(username);

                String jwtAccessToken= JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+SecurityConstant.ACCESS_EXPIRATION_TIME))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(AppRole::getRoleName).collect(Collectors.toList()))
                        .sign( algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", jwtAccessToken);
                tokens.put("refresh_token", jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);


            }catch (Exception e){
                throw e;
            }
        }else
            throw new RuntimeException(" Refresh token is required !!! ");
    }

    @GetMapping("/api/profile")
    public AppUser getUser(Principal principal){
        System.out.println("principal : "+ principal);
        return securityService.loadUserByUsername( principal.getName() );
    }
}
