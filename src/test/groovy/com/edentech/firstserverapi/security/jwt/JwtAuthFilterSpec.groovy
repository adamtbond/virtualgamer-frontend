package com.edentech.firstserverapi.security.jwt

import com.edentech.firstserverapi.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class JwtAuthFilterSpec extends Specification {

    JwtAuthFilter jwtAuthFilter
    JwtService jwtService
    HttpServletRequest request
    HttpServletResponse response
    FilterChain filterChain

    def setup() {
        jwtAuthFilter = new JwtAuthFilter()
        jwtService = Mock(JwtService)
        jwtAuthFilter.jwtService = jwtService

        request = Mock(HttpServletRequest)
        response = Mock(HttpServletResponse)
        filterChain = Mock(FilterChain)

        SecurityContextHolder.clearContext()
    }

    def "should allow OPTIONS requests without authentication"() {
        given:
            request.getMethod() >> "OPTIONS"

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
    }

    def "should allow requests without Authorization header"() {
        given:
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> null

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
            SecurityContextHolder.getContext().getAuthentication() == null
    }

    def "should reject requests with invalid Bearer token format"() {
        given:
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> "Basic dXNlcjpwYXNz"

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
            SecurityContextHolder.getContext().getAuthentication() == null
    }

    def "should reject invalid tokens"() {
        given:
            String invalidToken = "invalid.token.here"
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> "Bearer " + invalidToken
            jwtService.isTokenValid(invalidToken) >> false

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * jwtService.isTokenValid(invalidToken)
            1 * filterChain.doFilter(request, response)
            SecurityContextHolder.getContext().getAuthentication() == null
    }

    def "should set authentication for valid token"() {
        given:
            String validToken = "valid.jwt.token"
            String username = "testuser"
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> "Bearer " + validToken
            request.getRemoteAddr() >> "127.0.0.1"
            jwtService.isTokenValid(validToken) >> true
            jwtService.extractUsername(validToken) >> username

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
            SecurityContextHolder.getContext().getAuthentication() != null
            SecurityContextHolder.getContext().getAuthentication().getPrincipal() == username
    }

    def "should extract token from Bearer header"() {
        given:
            String token = "my.extracted.token"
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> "Bearer " + token
            request.getRemoteAddr() >> "192.168.1.1"
            jwtService.isTokenValid(token) >> true
            jwtService.extractUsername(token) >> "alice"

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            SecurityContextHolder.getContext().getAuthentication() != null
            SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "alice"
    }

    def "should handle case-insensitive OPTIONS method"() {
        given:
            request.getMethod() >> "options"

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
    }

    def "should continue filter chain regardless of authentication"() {
        given:
            request.getMethod() >> "POST"
            request.getHeader("Authorization") >> null

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            1 * filterChain.doFilter(request, response)
    }

    def "should preserve request details in authentication token"() {
        given:
            String token = "valid.token.123"
            String username = "john"
            request.getMethod() >> "GET"
            request.getHeader("Authorization") >> "Bearer " + token
            request.getRemoteAddr() >> "10.0.0.1"
            jwtService.isTokenValid(token) >> true
            jwtService.extractUsername(token) >> username

        when:
            jwtAuthFilter.doFilterInternal(request, response, filterChain)

        then:
            SecurityContextHolder.getContext().getAuthentication() != null
            SecurityContextHolder.getContext().getAuthentication().getPrincipal() == username
    }
}

