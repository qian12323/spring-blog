package qian.blog_api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 从配置文件读取密钥（生产环境需复杂密钥，避免硬编码）
    @Value("${jwt.secret}")
    private String secretKey;

    // 从配置文件读取过期时间（例如 3600 秒 = 1小时）
    @Value("${jwt.expiration}")
    private long expiration;

    // 生成签名密钥（基于 secretKey）
    private Key getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 Token
     * @param username 用户名（作为 Token 中的身份标识）
     * @return 生成的 JWT 字符串
     */
    public String generateToken(String username) {
        // 自定义负载（可添加用户ID、角色等信息）
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims) // 设置负载
                .setSubject(username) // 主题（通常为用户名）
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // 过期时间
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 签名算法
                .compact();
    }

    /**
     * 从 Token 中解析用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 验证 Token 是否有效（用户名匹配 + 未过期）
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 工具方法：提取 Token 中的声明
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 判断 Token 是否过期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取过期时间
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}