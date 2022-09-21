package com.training.apparatus.data.service;

import com.training.apparatus.data.entity.Result;
import com.training.apparatus.data.entity.Task;
import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.ResultRepository;
import com.training.apparatus.data.repo.TaskRepository;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.secutiy.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TaskRepository taskRepository;

    public ResultService() {

    }

    public Result create(long count, long length, double min) {
        Result result = new Result();
        result.setMistakes((double)length / count * 100);
        result.setSpeed((int)(count / min));
        result.setTime(LocalDateTime.now());
        return result;
    }

    @Transactional
    public Result save(long count, long length, double min, String type, long number) {
        Result result = new Result();
        String mistakes = new DecimalFormat("#0.00")
                .format((double)length / count * 100).replace(",", ".");
        result.setMistakes(Double.valueOf(mistakes));
        result.setSpeed((int)(count / min * 60));
        result.setTime(LocalDateTime.now());
        UserDetails userAuth = securityService.getAuthenticatedUser();
        User user = userRepository.findByEmail(userAuth.getUsername());
        user.getResults().size();
        user.addResult(result);
        Optional<Task> task = taskRepository.findByNumberAndType(type, number);
        if(task.isPresent()) {
            task.get().addResult(result);
            userRepository.save(user);
        }
        return result;
    }
}
