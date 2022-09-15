package com.picture.diary.common.jwt;

import com.picture.diary.login.data.LoginType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private String key = "projectKey";

    private long tokenValidTime = 30 * 60 * 100L;

    private final JwtRepository jwtRepository;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public boolean resolve(String accessToken) {
        //String accessToken = this.resolveToken((HttpServletRequest) request);
        if(accessToken != null) {
            JwtEntity validateResponseToken = this.getValidateResponseToken(accessToken);
            //유효성 검사에 성공한 토큰이면
            if(validateResponseToken.isValidate()) {


            }

            if(validateResponseToken.isNeedRefresh()) {
                //TODO 401 ERROR RETURN + 헤더에 토큰을 재발급하라는 메시지를 담아 리턴
                //     클라이언트는 메시지 확인 후 바로 재발급 요청
                //     서버는 재발급
                //     클라이언트는 재발급받은 토큰으로 재요청
                //    -> 이렇게 되면 Refresh Token 은 필요없어짐
                //    -> JwtEntity 존재 이유 사라짐
                //this.setRefreshToken((HttpServletResponse) response, validateResponseToken);
            }
        }

        return false;
    }

    // 토큰의 유효성 + 만료일자 확인
    public JwtEntity getValidateResponseToken(String accessToken) {
        JwtEntity response = new JwtEntity();

        try {
            //accessToken 검사
            final Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken);
            if(claims.getBody().getExpiration().before(new Date())) {
                response.setValidate(true);
            }

            //accessToken 이 만료됐다면 refreshToken 검사
            String refreshToken = jwtRepository.findRefreshTokenByAccessToken(accessToken);
            final Jws<Claims> refreshClams = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);
            if(refreshClams.getBody().getExpiration().before(new Date())) {
                response.setNeedRefresh(true);
                response.setValidate(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Create JWT Token
     * @param loginType
     * @param userId
     * @param roles
     * @return Jwt Access Token
     */
    public String createToken(LoginType loginType, String userId, List<String> roles) {
        String userPk = this.createUserPk(loginType, userId);
        String accessToken = createAccessToken(userPk, roles);
        String refreshToken = createRefreshToken(accessToken);

        JwtEntity jwtEntity = new JwtEntity(accessToken, refreshToken);

        jwtRepository.save(jwtEntity);

        return jwtEntity.getAccessToken();
    }

    private String createAccessToken(String userPk, List<String> roles) {
        //JWT payload 에 저장되는 정보 단위, 여기서 사용자를 식별하는 값을 넣는다.
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))    //set Expire Time
                .signWith(SignatureAlgorithm.HS256, userPk)                 //Hashing user id
                .compact();
    }


    //TODO 메서드 수정 필요 임시로 만들어 놓음
    private String createUserPk(LoginType loginType, String userId) {
        String loginTypeStr = loginType.toString();

        return loginTypeStr + ":" + userId;
    }

    //token 에서 사용자 인증 정보 조회
    public String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //TODO 필요할까?
    private String createRefreshToken(String accessToken) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))    //set Expire Time
                .signWith(SignatureAlgorithm.HS256, accessToken)            //Hashing user id
                .compact();
    }

    // Request의 Header에서 token 값을 가져온다. "Authorization" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("ACCESS-TOKEN");
    }
}
