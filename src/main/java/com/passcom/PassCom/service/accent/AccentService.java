package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.exceptions.AccentAlreadySoldException;
import com.passcom.PassCom.exceptions.AccentNotFoundException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.AccentRepository;
import com.passcom.PassCom.repostories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccentService {

    private final AccentRepository accentRepository;
    private final UserRepository userRepository;
    private final Lock lock = new ReentrantLock();

    public AccentService(AccentRepository accentRepository, UserRepository userRepository) {
        this.accentRepository = accentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Accent sellAccent(String userId, String accentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Accent accent = accentRepository.findByIdAndLock(accentId).orElseThrow(() -> new AccentNotFoundException("Accent not found"));

        if (accent.getUser() != null && accent.getStatusConfirmation() == Accent.Status.SOLD) {
            throw new AccentAlreadySoldException("Accent already sold");
        } else if (accent.getUser() != null && accent.getStatusConfirmation() == Accent.Status.RESERVED
                && accent.getExpire().isAfter(LocalDateTime.now())) {
            throw new AccentAlreadySoldException("Accent reserved and not expired");
        }

        accent.setUser(user);
        accent.setExpire(LocalDateTime.now().plusSeconds(30));
        accent.setStatusConfirmation(Accent.Status.RESERVED);
        return accentRepository.save(accent);
    }

    @Transactional
    public Accent confirmAccent(String accentId, boolean confirm) {
        Accent accent = accentRepository.findByIdAndLock(accentId).orElseThrow(() -> new AccentNotFoundException("Accent not found"));

        if (accent.getUser() == null) {
            throw new UserNotFoundException("User not found");
        } else if (accent.getStatusConfirmation() == Accent.Status.SOLD) {
            throw new AccentAlreadySoldException("Accent already sold");
        } else if (accent.getStatusConfirmation() == Accent.Status.RESERVED
                && accent.getExpire().isAfter(LocalDateTime.now())) {
            throw new AccentAlreadySoldException("Reservation expired");
        }

        if (confirm) {
            accent.setStatusConfirmation(Accent.Status.SOLD);
        } else {
            accent.setStatusConfirmation(Accent.Status.AVAILABLE);
            accent.setUser(null);
        }

        return accentRepository.save(accent);
    }


}
