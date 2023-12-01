package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.dto.FeedbackDto;
import kg.amanturov.jortartip.model.Feedback;
import kg.amanturov.jortartip.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = feedbackService.convertDtoToEntity(feedbackDto);
        feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok("Feedback submitted successfully");
    }
}
