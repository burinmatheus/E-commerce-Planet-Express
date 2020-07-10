package utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

/**
 *
 * @author Matheus M. Burin
 */
public class JWTGen {

    private static String chave = "chave_secreta";

    public static final String INFO_HEADER = "Authorization";

    public static String gerador(String user, String id) {

        JwtBuilder builder = Jwts.builder()
                .setSubject(user)
                .setId(id)
                .signWith(SignatureAlgorithm.HS512, chave);

        long nowMillis = System.currentTimeMillis();
        long ttlMillis = 7200000;
        long expMillis = nowMillis + ttlMillis;
        Date exp = new Date(expMillis);

        builder.setExpiration(exp);

        return builder.compact();

    }
}
