package org.example.skuhomepage.domain.skunotice.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.skuhomepage.domain.firebase.entity.Alarm;
import org.example.skuhomepage.domain.firebase.entity.NotificationType;
import org.example.skuhomepage.domain.firebase.entity.UserDeviceToken;
import org.example.skuhomepage.domain.firebase.entity.UserKeyword;
import org.example.skuhomepage.domain.firebase.repository.AlarmRepository;
import org.example.skuhomepage.domain.firebase.repository.UserDeviceTokenRepository;
import org.example.skuhomepage.domain.firebase.repository.UserKeywordRepository;
import org.example.skuhomepage.domain.firebase.service.NotificationService;
import org.example.skuhomepage.domain.mypage.entity.User;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeApiResponseDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeApiResponseDTO.SkuNoticeApiResponse;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO.SkuNoticeDTO;
import org.example.skuhomepage.domain.skunotice.dto.SkuNoticeResponseDTO.SkuNoticeListDTO;
import org.example.skuhomepage.domain.skunotice.entity.SkuNotice;
import org.example.skuhomepage.domain.skunotice.enums.ECNoticeType;
import org.example.skuhomepage.domain.skunotice.exception.SkuEcNoticeErrorStatus;
import org.example.skuhomepage.domain.skunotice.repository.SkuNoticeRepository;
import org.example.skuhomepage.global.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuNoticeScrapService {

  private final RestTemplate restTemplate;
  private final SkuNoticeRepository skuNoticeRepository;
  private final NotificationService notificationService;
  private final UserKeywordRepository userKeywordRepository;
  private final UserDeviceTokenRepository userDeviceTokenRepository;
  private final AlarmRepository alarmRepository;

  @Value("${sku.notice.url}")
  private String skuNoticeApiUrl;

  public SkuNoticeApiResponseDTO scrap(int page, ECNoticeType searchKeyword) {
    String url =
        UriComponentsBuilder.fromUriString(skuNoticeApiUrl)
            .queryParam("page", page)
            .queryParam(
                "search_keyword",
                URLEncoder.encode(
                    searchKeyword == null ? "" : searchKeyword.getValue(), StandardCharsets.UTF_8))
            .queryParam("search_target", "user_name")
            .build(true)
            .toUriString();

    log.info("[GET] 공지 API url: {}", url);

    ResponseEntity<List<SkuNoticeApiResponse>> response =
        restTemplate.exchange(
            URI.create(url),
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<>() {});

    if (response.getStatusCode().isError()) {
      log.warn("서경대 공지 스크랩 API 호출 중 에러 발생: {}", response.getBody());
      throw new GeneralException(SkuEcNoticeErrorStatus.SKU_NOTICE_API_CALL_FAILURE);
    }

    return SkuNoticeApiResponseDTO.builder().responseList(response.getBody()).build();
  }

  public SkuNoticeListDTO save(int page) {
    AtomicInteger newData = new AtomicInteger();
    AtomicInteger updateData = new AtomicInteger();
    List<SkuNoticeDTO> newNotices = new ArrayList<>();
    List<SkuNotice> pageNotices = new ArrayList<>();

    scrap(page, null)
        .getResponseList()
        .forEach(
            skuNotice ->
                skuNoticeRepository
                    .findById(Long.parseLong(skuNotice.getId()))
                    .ifPresentOrElse(
                        notice -> {
                          updateData.getAndIncrement();
                          pageNotices.add(notice);
                        },
                        () -> {
                          newData.getAndIncrement();
                          updateData.getAndIncrement();
                          newNotices.add(skuNotice.toDTO());
                          pageNotices.add(
                              new SkuNotice(
                                  Long.parseLong(skuNotice.getId()),
                                  skuNotice.getCategory(),
                                  parseDate(skuNotice.getDate()),
                                  skuNotice.getTitle(),
                                  skuNotice.getUrl(),
                                  skuNotice.getAuthor(),
                                  0,
                                  skuNotice.getImage(),
                                  new ArrayList<>()));
                        }));
    skuNoticeRepository.saveAll(pageNotices);

    log.info("{} 페이지 데이터 저장 완료, 새 데이터 수: {}, 이전 데이터 수: {}", page, newData.get(), updateData.get());

    return SkuNoticeListDTO.builder().skuNoticeList(newNotices).build();
  }

  public void saveAll(int startPage, int endPage) {

    for (int i = startPage; i <= endPage; i++) {
      save(i);
    }
    log.info("모든 페이지 데이터 저장 완료");
  }

  //  @Scheduled(cron = "0 0/10 * * * ?") // 매 10분마다 실행
  //  public void saveNoticeTask() {
  //    SkuNoticeListDTO newNotices = save(1);
  //
  //    if (newNotices.getSkuNoticeList().isEmpty()) return;
  //
  //    List<String> ecNoticeTopics =
  //        topicRepository.findTopicsByTopicGroup(TopicGroup.SKU_EC_NOTICE.getValue());
  //    List<String> noticeTopics =
  //        topicRepository.findTopicsByTopicGroup(TopicGroup.SKU_NOTICE.getValue());
  //
  //    for (SkuNoticeDTO notice : newNotices.getSkuNoticeList()) {
  //      TopicGroup topicGroup = TopicGroup.SKU_NOTICE;
  //      if (notice.getAuthor().equals(ECNoticeType.GYOSU_HAKSEUB.getValue())
  //          || notice.getAuthor().equals(ECNoticeType.DAEHAK_HYEOKSIN.getValue())
  //          || notice.getAuthor().equals(ECNoticeType.JINLO_CHWIEOB.getValue())) {
  //        for (String topic : ecNoticeTopics) {
  //          log.info("토픽 확인: {}, department: {}", topic, notice.getAuthor());
  //          if (notice.getTitle().contains(topic)) {
  //            fcmService.sendTopicMessage(
  //                notice.toMessageRequest(),
  //                TopicRequestDTO.builder().topicGroup(topicGroup).keyword(topic).build());
  //          }
  //        }
  //      } else {
  //        for (String topic : noticeTopics) {
  //          log.info("토픽 확인: {}, department: {}", topic, notice.getAuthor());
  //          if (notice.getTitle().contains(topic)) {
  //            fcmService.sendTopicMessage(
  //                notice.toMessageRequest(),
  //                TopicRequestDTO.builder().topicGroup(topicGroup).keyword(topic).build());
  //          }
  //        }
  //      }
  //    }
  //  }
  @Scheduled(cron = "0 0/10 * * * ?") // 10분마다 실행
  public void saveNoticeTask() {
    System.out.println("[INFO] 공지사항 스크래핑 작업 시작");

    SkuNoticeListDTO newNotices = save(1);
    if (newNotices.getSkuNoticeList().isEmpty()) {
      System.out.println("[INFO] 새 공지사항 없음");
      return;
    }

    System.out.println("[INFO] 새 공지사항 수: " + newNotices.getSkuNoticeList().size());

    List<UserKeyword> allKeywords = userKeywordRepository.findAll();

    for (SkuNoticeDTO notice : newNotices.getSkuNoticeList()) {
      Set<User> matchedUsers = new HashSet<>();

      for (UserKeyword keyword : allKeywords) {
        if (notice.getTitle().contains(keyword.getKeyword())) {
          matchedUsers.add(keyword.getUser());
        }
      }

      if (!matchedUsers.isEmpty()) {
        System.out.println(
            "[INFO] '" + notice.getTitle() + "' 키워드 알림 대상자 수: " + matchedUsers.size());
      }

      for (User user : matchedUsers) {
        List<UserDeviceToken> tokens = userDeviceTokenRepository.findAllByUser(user);
        for (UserDeviceToken token : tokens) {
          System.out.println("[INFO] 사용자 " + user.getId() + " 에게 푸시 전송: " + token.getFcmToken());
          notificationService.sendPush(
              token.getFcmToken(), notice.getTitle(), "등록되었습니다", "/notice");

          Alarm alarm =
              Alarm.builder()
                  .user(user)
                  .title(notice.getTitle())
                  .content("새 공지사항이 등록되었습니다.")
                  .notificationType(NotificationType.NOTICE)
                  .build();

          alarmRepository.save(alarm);
        }
      }
    }

    System.out.println("[INFO] 공지사항 스크래핑 작업 종료");
  }

  private LocalDateTime parseDate(String dateString) {
    try {
      return LocalDateTime.parse(dateString);
    } catch (Exception e) {
      log.warn("날짜 파싱 오류: {}", dateString);
      return LocalDateTime.now();
    }
  }
}
