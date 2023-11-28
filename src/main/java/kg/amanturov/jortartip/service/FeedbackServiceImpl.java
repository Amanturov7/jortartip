package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.FeedbackDto;
import kg.amanturov.jortartip.model.Feedback;
import kg.amanturov.jortartip.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository repository;
    private final UserService userService;
    private final CommonReferenceService commonReferenceService;


    public FeedbackServiceImpl(FeedbackRepository repository, UserService userService, CommonReferenceService commonReferenceService) {
        this.repository = repository;
        this.userService = userService;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return repository.save(feedback);
    }


    @Override
    public Feedback convertDtoToEntity(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setMessage(feedbackDto.getMessage());
        feedback.setCreatedDate(feedbackDto.getCreatedDate());

        if(feedbackDto.getFeedbackTypeId() != null) {
            feedback.setFeedbackType(commonReferenceService.findById(feedbackDto.getFeedbackTypeId()));
        }
        if(feedbackDto.getUserId() != null){
            feedback.setUser(userService.findById(feedbackDto.getUserId()));
        }
        return feedback;
    }
}