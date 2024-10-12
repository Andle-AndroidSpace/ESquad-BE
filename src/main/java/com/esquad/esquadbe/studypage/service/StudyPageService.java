package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudyPageService {

    private final TeamSpaceRepository teamSpaceRepository;
    private final StudyPageUserRepository studyPageUserRepository;
    private final BookRepository bookRepository;
    private final StudyPageRepository studyPageRepository;
    private final UserRepository userRepository;
    private final StudyRemindRepository studyRemindRepository;
    private StudyRemindRepository studyRemindService;

    @Autowired
    public StudyPageService(TeamSpaceRepository teamSpaceRepository, StudyPageUserRepository studyPageUserRepository,
                            BookRepository bookRepository, StudyPageRepository studyPageRepository,
                            UserRepository userRepository, StudyRemindRepository studyRemindRepository) {
        this.teamSpaceRepository = teamSpaceRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.bookRepository = bookRepository;
        this.studyPageRepository = studyPageRepository;
        this.userRepository = userRepository;
        this.studyRemindRepository = studyRemindRepository;
    }

    // Create
    public Long createStudyPage(Long teamId, Long bookId, StudyInfoDto dto) {
        log.info("Creating StudyPage with teamId: {}, bookId: {}, dto: {}", teamId, bookId, dto);

        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TeamSpace ID"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Book ID"));

        StudyPage studyPage = StudyPage.builder()
                .teamSpace(teamSpace)
                .book(book)
                .studyPageName(dto.getStudyPageName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        studyPageRepository.save(studyPage);
        return studyPage.getId();
    }

    // Read
    public List<StudyPageReadDto> readStudyPages(Long teamId) {
        // teamId로 TeamSpace 찾기
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamSpace not found with ID: " + teamId));

        // teamSpace에 속한 StudyPages 조회
        List<StudyPage> studyPages = studyPageRepository.findAllByTeamSpace(teamSpace)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPages found for TeamSpace ID: " + teamId));

        // StudyPage 엔티티를 StudyPageReadDto로 변환하여 리스트 반환
        return studyPages.stream()
                .map(this::convertStudyPageToStudyPageReadDto)
                .collect(Collectors.toList());
    }

    private StudyPageReadDto convertStudyPageToStudyPageReadDto(StudyPage studyPage) {
        return new StudyPageReadDto(
                studyPage.getId(),
                studyPage.getBook().getImgPath(),
                studyPage.getStudyPageName()
        );
    }

    // Update
    public boolean updateStudyPage(Long studyPageId, UpdateStudyPageRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid StudyPage ID"));;
        StudyPage studyPage = studyPageRepository.findById(studyPageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid StudyPage ID"));

        StudyPageUser studyPageUser = studyPageUserRepository.findByMemberAndStudyPage(user, studyPage)
                .orElseThrow(() -> new IllegalArgumentException("Invalid StudyPage ID"));;

        if (!studyPageUser.isOwnerFlag()) {
            return false;
        }

        studyPage = StudyPage.builder()
                .id(studyPage.getId())
                .teamSpace(studyPage.getTeamSpace())
                .book(studyPage.getBook())
                .studyPageName(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        studyPageRepository.save(studyPage);
        return true;
    }

    // Delete
    @Transactional
    public void deleteStudyPage(Long id, String name) {
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudyPage not found with ID: " + id));

        if (!studyPage.getStudyPageName().equals(name)) {
            throw new IllegalArgumentException("StudyPage name does not match");
        }
        studyPageUserRepository.deleteAllByStudyPage(studyPage);
        studyRemindRepository.deleteByStudyPage(studyPage);
        studyPageRepository.delete(studyPage);
    }
}