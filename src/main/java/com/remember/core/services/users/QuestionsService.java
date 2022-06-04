package com.remember.core.services.users;

import com.remember.core.searchParams.users.QuestionsParams;
import com.remember.core.tools.AuthenticatedUserTool;
import com.remember.core.assemblers.user.UsersMeQuestionsAssembler;
import com.remember.core.domainMappers.QuestionDomainMapper;
import com.remember.core.domains.Question;
import com.remember.core.repositories.question.QuestionRepository;
import com.remember.core.requestDtos.QuestionsRO;
import com.remember.core.responseDtos.question.QuestionVO;
import com.remember.core.responseDtos.question.QuestionsVO;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class QuestionsService {
    private final QuestionRepository repository;
    private final QuestionDomainMapper deassembler;
    private final UsersMeQuestionsAssembler listAssembler;
    private final PagedResourcesAssembler<Question> pageAssembler;
    private final EntityManager entityManager;

    private final AuthenticatedUserTool userTool;

    public PagedModel<QuestionsVO> findAll(Pageable pageable, QuestionsParams params) {
        String baseUri = requestURL();


        Long user = userTool.getUserId();
        return null;
        //return pageAssembler.toModel(repository.findAll(pageable, user, params), listAssembler);
    }

    public QuestionVO findById(Long id) {
        Question question = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));

        return new QuestionVO(question);
    }

    @Transactional
    public QuestionVO create(Long userId, QuestionsRO ro) {
        Question question = repository.save(deassembler.toEntity(userId, ro));
        Long new_id = question.getId();

        question = repository.findById(new_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + new_id));

        return new QuestionVO(question);
    }

    @Transactional
    public QuestionVO update(Long userId, Long id, QuestionsRO ro) {
        Question question = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        Question updatedQuestion = deassembler.toEntity(userId, id, ro);

        question = entityManager.merge(updatedQuestion);

        return new QuestionVO(question);
    }

    @Transactional
    public QuestionVO partial_update(Long userId, Long id, QuestionsRO ro) {
        Question question = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id= " + id));
        Question updatedQuestion = deassembler.toEntity(userId, id, ro);

        question.partial_update(updatedQuestion);
        System.out.println(updatedQuestion.getPracticeStatus().getStatus());
        System.out.println("-------");
        System.out.println(question.getPracticeStatus().getStatus());
        return new QuestionVO(question);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /*
     * helpers
     */
    private String requestURL() {
        return BasicLinkBuilder.linkToCurrentMapping().toString();
    }
}