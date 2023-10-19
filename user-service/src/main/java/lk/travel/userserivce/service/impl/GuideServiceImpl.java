package lk.travel.userserivce.service.impl;

import lk.travel.userserivce.dto.GuideDTO;
import lk.travel.userserivce.entity.Guide;
import lk.travel.userserivce.repo.GuideRepo;
import lk.travel.userserivce.service.GuideService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuideServiceImpl implements GuideService {
    @Autowired
    GuideRepo guideRepo;
    @Autowired
    ModelMapper mapper;

    @Override
    public GuideDTO saveGuide(GuideDTO guideDTO) {
        if(guideRepo.existsById(guideDTO.getGuideID())){
            throw  new RuntimeException("Guide Already Exist...!!");
        }
        guideRepo.save(mapper.map(guideDTO, Guide.class));
        return guideDTO;
    }

    @Override
    public GuideDTO updateGuide(GuideDTO guideDTO) {
        if(!guideRepo.existsById(guideDTO.getGuideID())){
            throw  new RuntimeException("Guide Not Exist...!!");
        }
        guideRepo.save(mapper.map(guideDTO, Guide.class));
        return guideDTO;
    }

    @Override
    public GuideDTO searchGuide(int guideID) {
        if(!guideRepo.existsById(guideID)){
            throw  new RuntimeException("Guide Not Exist...!!");
        }
        return mapper.map(guideRepo.findById(guideID),GuideDTO.class);
    }

    @Override
    public void deleteGuide(int guideID) {
        if(!guideRepo.existsById(guideID)){
            throw  new RuntimeException("Guide Not Exist...!!");
        }
        guideRepo.deleteById(guideID);
    }

    @Override
    public List<GuideDTO> getAllGuide() {
        return mapper.map(guideRepo.findAll(),new TypeToken<List<GuideDTO>>(){}.getType());
    }
}
