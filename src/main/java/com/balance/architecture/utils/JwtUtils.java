package com.balance.architecture.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.balance.entity.sys.Subscriber;
import com.balance.entity.user.User;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    /**
     * 过期时间15分钟
     */
    public static final long EXPIRE_TIME = 15*60*1000;

    /**
     * token私钥
     */
    public static final String TOKEN_SECRET = "680dd9891cda8ffa6a6259e5cbd8ab8e";

    /**
     * tokenName
     */
    public static final String ACCESS_TOKEN_NAME = "accessToken";

    /**
     * token claim userName
     */
    private static final String TOKEN_CLAIM_USERNAME = "userName";
    private static final String TOKEN_CLAIM_USERID = "userId";

    /**
     * 创建token
     * @param subscriber
     * @return
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    public static String createToken(Subscriber subscriber) throws IllegalArgumentException, UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        String userName = subscriber.getUserName();
        String userId = subscriber.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create().withHeader(map)
                .withClaim(TOKEN_CLAIM_USERNAME, userName)
                .withClaim(TOKEN_CLAIM_USERID, userId)
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .sign(algorithm);
        return token;
    }

    /**
     * 创建token
     * @param user
     * @return
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    public static String createToken(User user) throws IllegalArgumentException, UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        String userName = user.getUserName();
        String userId = user.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create().withHeader(map)
                .withClaim(TOKEN_CLAIM_USERNAME, userName)
                .withClaim(TOKEN_CLAIM_USERID, userId)
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .sign(algorithm);
        return token;
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static Boolean verifyJwt(String token){
        DecodedJWT decodedJWT = null;
        try{
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            decodedJWT = jwtVerifier.verify(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 获取app端 token信息
     */
    public static User getUserByToken(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT= jwtVerifier.verify(token);

        User user = new User();
        user.setId(decodedJWT.getClaim(TOKEN_CLAIM_USERID).asString());
        user.setUserName(decodedJWT.getClaim(TOKEN_CLAIM_USERNAME).asString());
        return user;
    }

}
