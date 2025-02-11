package com.laun.dove.service;

import com.laun.dove.controller.dto.AuthenticateDto;
import com.laun.dove.controller.response.AuthenticationResponse;
import com.laun.dove.domain.InvalidToken;
import com.laun.dove.domain.User;
import com.laun.dove.exception.AppException;
import com.laun.dove.exception.ErrorCode;
import com.laun.dove.repository.InvalidTokenRepository;
import com.laun.dove.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Value("${token.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${token.access_token_key}")
    private String accessTokenKey;

    @Value("${token.refresh_token_expiration}")
    private long refreshTokenExpiration;

    @Value("${token.refresh_token_key}")
    private String refreshTokenKey;

    private final InvalidTokenRepository invalidTokenRepository;
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;  // Bean đã được khai báo trong SecurityConfig

    public Map<String, String> authenticated(AuthenticateDto authenticateDto) {
        User user = userRepository.findByEmail(authenticateDto.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(authenticateDto.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return buildPairOfToken(user);
    }

    public Map<String, String> refreshToken(String refresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(refresh, refreshTokenKey);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        return buildPairOfToken(user);
    }


    public void logout(String token) throws ParseException, JOSEException {

        SignedJWT signedJWT = verifyToken(token, accessTokenKey);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        InvalidToken invalidToken = InvalidToken.builder()
                .id(jti)
                .expirationDate(expiryTime)
                .build();
        invalidTokenRepository.save(invalidToken);

    }

    public boolean introspect(String token) throws JOSEException, ParseException {

        SignedJWT signedJWT = verifyToken(token, accessTokenKey);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        String jti = signedJWT.getJWTClaimsSet().getJWTID();

        if (invalidTokenRepository.existsById(jti)) {
            return false;
        }

        return expiryTime.after(new Date());
    }

    private String generateToken(User user, long expirationToken, String tokenKey) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("laun.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(System.currentTimeMillis() + expirationToken * 1000L))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(tokenKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing token", e);
            throw new RuntimeException("Error signing token", e);
        }

    }

    private SignedJWT verifyToken(String token, String tokenKey) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(tokenKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        if (!signedJWT.verify(jwsVerifier)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner roles = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(roles::add);
        }
        return roles.toString();
    }

    private Map<String, String> buildPairOfToken(User user){
        String accessToken = generateToken(user, accessTokenExpiration, accessTokenKey);
        String refreshToken = generateToken(user, refreshTokenExpiration, refreshTokenKey);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
}
