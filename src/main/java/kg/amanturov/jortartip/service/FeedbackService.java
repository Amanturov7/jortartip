package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.FeedbackDto;
import kg.amanturov.jortartip.model.Feedback;

public interface FeedbackService {
    Feedback saveFeedback(Feedback feedback);

    Feedback convertDtoToEntity(FeedbackDto feedbackDto);
}