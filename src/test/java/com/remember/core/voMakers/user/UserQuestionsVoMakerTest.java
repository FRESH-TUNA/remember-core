package com.remember.core.voMakers.user;

import com.remember.core.domains.Platform;
import com.remember.core.domains.PracticeStatus;
import com.remember.core.domains.Question;
import com.remember.core.vos.user.UserQuestionsVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserQuestionsVoMakerTest {
    private UserQuestionsVoMaker assembler = new UserQuestionsVoMaker();

    @Test
    @DisplayName("문제들의 리스트 결과값을 위해 직렬화에 필요한 Dto 테스트")
    void toModel_test() {
        /*
         * given
         */
        Question question = Question.builder()
                .id(1L)
                .title("title")
                .link("link")
                .user(1L)
                .level(1)
                .platform(Platform.builder().name("name").build())
                .practiceStatus(PracticeStatus.builder().status("status").build())
                .build();

//        /*
//         * then
//         */
//        UserQuestionsVO dto = assembler.toModel(question);
//
//        /*
//         * when
//         */
//        assertThat(dto.getLink("self").get().getHref()).isEqualTo("/users/1/questions/1");
    }
}
