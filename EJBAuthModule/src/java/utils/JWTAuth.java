/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

/**
 *
 * @author Matheus M. Burin
 */
public class JWTAuth {
    
    private static String chave = "chave_secreta";

    public static final String INFO_HEADER = "Authorization";

    public static Jws<Claims> decode(String token) {
        return Jwts.parser().setSigningKey(chave).parseClaimsJws(token);
    }
}
