package com.passcom.PassCom.service.travel;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.dto.TravelDTO;
import com.passcom.PassCom.exceptions.TravelNotFoundException;
import com.passcom.PassCom.repostories.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public List<Travel> getAllTravels() {
        List<Travel> travels = travelRepository.findAllWithAccents();
        return travels;
    }

    public Travel getTravelById(String id) {
        Travel travels = travelRepository.findWithAccent(id).orElseThrow(()-> new TravelNotFoundException("Travel not found"));
        return travels;
    }

    public Travel createTravel(TravelDTO travelDTO) {
        Travel travel = new Travel();
        travel.setPrice(travelDTO.price());
        travel.setCityDestiny(travelDTO.cityDestiny());
        travel.setCityOrigin(travelDTO.cityOrigin());
        travel.setDescription(travelDTO.description());
        List<Accent> accents = new ArrayList<>();
        for (int i = 1; i <= travelDTO.numberOfAccents(); i++) {
            Accent accent = new Accent();
            accent.setNumber(i);
            accent.setTravel(travel);
            accents.add(accent);
        }

        travel.setAccents(accents);

        return travelRepository.save(travel);
    }

    public Travel updateTravel(String id, Travel travelDetails) {
        Travel existingTravel = getTravelById(id);

        existingTravel.setCityOrigin(travelDetails.getCityOrigin());
        existingTravel.setCityDestiny(travelDetails.getCityDestiny());
        existingTravel.setDescription(travelDetails.getDescription());
        existingTravel.setPrice(travelDetails.getPrice());

        return travelRepository.save(existingTravel);
    }

    public void deleteTravel(String id) {
        Travel travel = getTravelById(id);
        travelRepository.delete(travel);
    }
}
