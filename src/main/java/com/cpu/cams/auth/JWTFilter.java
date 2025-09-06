package com.cpu.cams.auth;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 Authorization키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없거나 잘못된 형식이면 다음 필터로 넘김
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            System.out.println("토큰 없음");
            filterChain.doFilter(request, response);
            return;
        }

        // 접두어 제거
        accessToken = accessToken.substring(7);


        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            if (jwtUtil.isExpired(accessToken)) {
                System.out.println("token expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Access-Token-Expired", "true");
                return;
            }
        } catch (ExpiredJwtException e) {
            System.out.println("token expired");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Access-Token-Expired", "true");
            return;
        }

        // 토큰이 access인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Invalid-Access-Token", "true");
            return;
        }


        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //userEntity를 생성하여 값 set
        Member userEntity = Member.create(username, role);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
