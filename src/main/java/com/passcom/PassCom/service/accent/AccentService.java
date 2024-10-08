package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.exceptions.AccentAlreadySoldException;
import com.passcom.PassCom.exceptions.AccentNotFoundException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.AccentRepository;
import com.passcom.PassCom.repostories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccentService {

    private final AccentRepository accentRepository;
    private final UserRepository userRepository;

    private AccentService(AccentRepository accentRepository, UserRepository userRepository) {
        this.accentRepository = accentRepository;
        this.userRepository = userRepository;
    }

    public Accent sellAccent(String userId, String accentId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));
        Accent accent = accentRepository.findById(accentId).orElseThrow(()-> new AccentNotFoundException("Accent not found"));
        if (accent.getUser() != null) {
            throw new AccentAlreadySoldException("Accent Already Sold");
        }
        accent.setUser(user);
        accentRepository.save(accent);
        return accent;
    }

}
