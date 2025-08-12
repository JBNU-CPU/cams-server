package com.cpu.cams.member.service;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        Member userData = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        System.out.println(userData);
        System.out.println(username);

        if (userData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
