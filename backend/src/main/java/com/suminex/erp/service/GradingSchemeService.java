package com.suminex.erp.service;

import com.suminex.erp.dto.*;
import com.suminex.erp.entity.GradeBand;
import com.suminex.erp.entity.GradingScheme;
import com.suminex.erp.exception.ConflictException;
import com.suminex.erp.exception.ResourceNotFoundException;
import com.suminex.erp.repository.GradeBandRepository;
import com.suminex.erp.repository.GradingSchemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradingSchemeService {

    private final GradingSchemeRepository gradingSchemeRepository;
    private final GradeBandRepository gradeBandRepository;

    public GradingSchemeService(GradingSchemeRepository gradingSchemeRepository,
                                GradeBandRepository gradeBandRepository) {
        this.gradingSchemeRepository = gradingSchemeRepository;
        this.gradeBandRepository = gradeBandRepository;
    }

    @Transactional
    public GradingSchemeResponse createScheme(CreateGradingSchemeRequest request) {
        if (gradingSchemeRepository.existsByName(request.getName())) {
            throw new ConflictException("A grading scheme with this name already exists");
        }

        GradingScheme scheme = new GradingScheme();
        scheme.setName(request.getName());
        scheme.setActive(false); // must be explicitly activated separately

        GradingScheme savedScheme = gradingSchemeRepository.save(scheme);

        List<GradeBand> bands = request.getGradeBands().stream().map(bandReq -> {
            GradeBand band = new GradeBand();
            band.setGradingScheme(savedScheme);
            band.setMinMarks(bandReq.getMinMarks());
            band.setMaxMarks(bandReq.getMaxMarks());
            band.setGrade(bandReq.getGrade());
            band.setGradePoint(bandReq.getGradePoint());
            band.setPass(bandReq.isPass());
            return band;
        }).collect(Collectors.toList());

        gradeBandRepository.saveAll(bands);

        return toResponse(savedScheme, bands);
    }

    @Transactional
    public GradingSchemeResponse activateScheme(Long id) {
        GradingScheme scheme = gradingSchemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grading scheme not found"));

        // Deactivate any currently active scheme first — only one can be active at a time.
        gradingSchemeRepository.findByActiveTrue().ifPresent(current -> {
            current.setActive(false);
            gradingSchemeRepository.save(current);
        });

        scheme.setActive(true);
        GradingScheme saved = gradingSchemeRepository.save(scheme);
        List<GradeBand> bands = gradeBandRepository.findByGradingSchemeId(saved.getId());

        return toResponse(saved, bands);
    }

    public GradeBand resolveGrade(int marks) {
        GradingScheme activeScheme = gradingSchemeRepository.findByActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active grading scheme configured"));

        return gradeBandRepository.findMatchingBand(activeScheme.getId(), marks)
                .orElseThrow(() -> new ResourceNotFoundException("No grade band matches marks: " + marks));
    }

    private GradingSchemeResponse toResponse(GradingScheme scheme, List<GradeBand> bands) {
        List<GradeBandResponse> bandResponses = bands.stream()
                .map(b -> new GradeBandResponse(b.getId(), b.getMinMarks(), b.getMaxMarks(),
                        b.getGrade(), b.getGradePoint(), b.isPass()))
                .collect(Collectors.toList());

        return new GradingSchemeResponse(scheme.getId(), scheme.getName(), scheme.isActive(), bandResponses);
    }
}