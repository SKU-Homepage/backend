package org.example.skuhomepage.domain.firebase.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.skuhomepage.domain.firebase.dto.TopicRequestDTO;
import org.example.skuhomepage.domain.firebase.entity.UserKeyword;
import org.example.skuhomepage.domain.firebase.exception.FirebaseErrorStatus;
import org.example.skuhomepage.domain.firebase.repository.UserKeywordRepository;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.mypage.exception.MyPageErrorStatus;
import org.example.skuhomepage.domain.mypage.repository.UserRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeywordService {

  private final UserRepository userRepository;
  private final UserKeywordRepository keywordRepository;

  public void registerKeyword(TopicRequestDTO request, UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    boolean exists = keywordRepository.existsByUserAndKeyword(user, request.getKeyword());
    if (exists) {
      throw new GeneralException(FirebaseErrorStatus.KEYWORD_NOT_VALID);
    }

    UserKeyword keyword = UserKeyword.builder().user(user).keyword(request.getKeyword()).build();

    keywordRepository.save(keyword);
  }

  public List<String> getUserKeywords(UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    List<UserKeyword> keywords = keywordRepository.findAllByUser(user);
    return keywords.stream().map(UserKeyword::getKeyword).collect(Collectors.toList());
  }

  public void deleteKeyword(String keywordToDelete, UserDetails userDetails) {
    User user =
        userRepository
            .findByAccount(userDetails.getUsername())
            .orElseThrow(() -> new GeneralException(MyPageErrorStatus.USER_NOT_FOUND));

    UserKeyword keyword =
        keywordRepository
            .findByUserAndKeyword(user, keywordToDelete)
            .orElseThrow(() -> new GeneralException(FirebaseErrorStatus.KEYWORD_NOT_FOUND));

    keywordRepository.delete(keyword);
  }
}
