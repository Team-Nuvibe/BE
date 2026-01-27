package com.umc.nuvibe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource; // 추가됨

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FcmConfig {

    @Value("${firebase.credentials-path}")
    private String firebaseCredentialsPath; // 변수명을 의미에 맞게 수정 (선택사항)

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // 수정된 부분: 문자열을 바로 스트림으로 만드는 게 아니라, 리소스를 읽어옵니다.
                ClassPathResource resource = new ClassPathResource(firebaseCredentialsPath);

                try (InputStream credentialsStream = resource.getInputStream()) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                            .build();

                    FirebaseApp.initializeApp(options);
                    log.info("Firebase 초기화 완료");
                }
            }
        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage());
            // 어떤 경로의 파일을 못 찾았는지 로그에 남기면 디버깅에 좋습니다.
            log.error("확인된 경로: {}", firebaseCredentialsPath);
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}
