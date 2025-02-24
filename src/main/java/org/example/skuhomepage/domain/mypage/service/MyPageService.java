package org.example.skuhomepage.domain.mypage.service;

import lombok.RequiredArgsConstructor;
import org.example.skuhomepage.domain.mypage.dto.MyPageResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    public MyPageResponseDTO.MyPageInfoDTO getMyInfo(UserDetails userDetails) {
        return null;
    }

    public MyPageResponseDTO.SignupResultDTO signUp(UserDetails userDetails) {
        return null;
    }

    public MyPageResponseDTO.LoginResultDTO googleLogin(String code) {
        return null;
    }

}
