package ma.ensetm.project.security.config;

public class SecurityConstant {
    public static String SECRET = "secret"; // needs to be changed !!!!
    public static final int ACCESS_EXPIRATION_TIME = 30*60*1000 ; // 30 minutes
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 150*60*1000 ; // 150 minutes
    public static final String REFRESH_PATH = "/api/refresh-token";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String HEADER_STRING = "Authorization";
}
