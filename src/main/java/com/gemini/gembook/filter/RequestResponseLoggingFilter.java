//package com.gemini.gembook.filter;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.google.api.client.http.HttpStatusCodes;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class RequestResponseLoggingFilter extends OncePerRequestFilter {
//
//    private final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
//
//    @Value("${CLIENT_ID}")
//    private String clientId;
//
//    @Value("${spring.profiles.active}")
//    private String activeProfile;
//
//    private final List<String> excludedUrlsList = Arrays.asList("/v2/api-docs", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html/**", "/csrf", "/");
//
//    @Override
//    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) {
//
//        String requestToken = servletRequest.getHeader("x-auth-token");
//        if ((activeProfile.equals("beta") || activeProfile.equals("dev")) && "TOKEN".equals(requestToken)) {
//            try {
//                filterChain.doFilter(servletRequest, servletResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            NetHttpTransport transport = new NetHttpTransport();
//            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//                    .setAudience(Collections.singletonList(clientId))
//                    .build();
//            GoogleIdToken idToken;
//            try {
//                if (requestToken == null) {
//                    requestToken = servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//                }
//                idToken = verifier.verify(requestToken);
//            } catch (Exception e) {
//                servletResponse.setStatus(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
//                logger.error("Invalid token with exception : {} for endpoint : {}", e.getMessage(),
//                        servletRequest.getRequestURI());
//                return;
//            }
//
//            if (idToken != null) {
//                GoogleIdToken.Payload payLoad = idToken.getPayload();
//                String email = payLoad.getEmail();
//
//                try {
//                    logger.info("Logging Request by {} with {} : {}", email, servletRequest.getMethod(), servletRequest.getRequestURI());
//
//                    if (email != null) {
//                        filterChain.doFilter(servletRequest, servletResponse);
//                        logger.info("Access granted for user {} for {}", email, servletRequest.getRequestURI());
//                        logger.info("Logging Response :{}", servletResponse.getContentType());
//                    } else {
//                        servletResponse.setStatus(HttpStatusCodes.STATUS_CODE_FORBIDDEN);
//                        logger.error("Access denied for user : {} for {}", email, servletRequest.getRequestURI());
//                    }
//                } catch (Exception e) {
//                    logger.error("Exception with message : {} for {}", e.getMessage(), servletRequest.getRequestURI());
//                    servletResponse.setStatus(HttpStatusCodes.STATUS_CODE_SERVER_ERROR);
//                }
//            } else {
//                logger.error("Could not verify id_token for {}", servletRequest.getRequestURI());
//                servletResponse.setStatus(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
//            }
//        }
//    }
//
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        return excludedUrlsList.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
//    }
//}
//
