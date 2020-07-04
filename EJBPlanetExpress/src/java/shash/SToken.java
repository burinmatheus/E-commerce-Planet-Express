package shash;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;

/**
 *
 * @author Matheus M. Burin
 */
public class SToken {

    public String gerador(String id) {
        try {
            Date dataexp = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataexp);
            cal.add(Calendar.DATE, 1);
            dataexp = cal.getTime();

            Algorithm algorithm = Algorithm.HMAC256("ELre2XF3YAYdMgvF");
            String token = JWT.create().withIssuer("PlanetExpressSecurity").withExpiresAt(dataexp).withSubject(id).sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            return "Falha ao gerar token";
        }
    }

    public boolean veficando(String token, String id) {
        try {
            Date dataexp = new Date();

            Algorithm algorithm = Algorithm.HMAC256("ELre2XF3YAYdMgvF");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("PlanetExpressSecurity").acceptLeeway(7200).withSubject(id).build();
            DecodedJWT jwt = verifier.verify(token);
            
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }

    }
}
