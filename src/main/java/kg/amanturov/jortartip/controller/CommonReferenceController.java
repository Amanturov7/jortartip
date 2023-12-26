package kg.amanturov.jortartip.controller;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.CommonReferenceType;
import kg.amanturov.jortartip.service.CommonReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/common-reference")
public class CommonReferenceController {

    private final CommonReferenceService commonReferenceService;

    @Autowired
    public CommonReferenceController(CommonReferenceService commonReferenceService) {
        this.commonReferenceService = commonReferenceService;
    }

    @GetMapping("/all")
    public List<CommonReference> getAllCommonReferences() {
        return commonReferenceService.findAll();
    }

    @GetMapping("/by-type/{typeCode}")
    public List<CommonReference> getCommonReferencesByType(@PathVariable String typeCode) {
        return commonReferenceService.findAllByType(typeCode);
    }

    @GetMapping(value = "/{district}")
    public List<CommonReference> findById(@PathVariable String district) {
        return commonReferenceService.findAllByCode(district);
    }


    @GetMapping("/{id}")
    public CommonReference getCommonReferenceById(@PathVariable Long id) {
        return commonReferenceService.findById(id);
    }

    @GetMapping("/types")
    public List<CommonReferenceType> getAllReferenceTypes() {
        return commonReferenceService.findAllTyeps();
    }

}
