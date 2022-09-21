package com.training.apparatus.data.service;

import com.training.apparatus.data.entity.TheoreticalText;
import com.training.apparatus.data.entity.TheoreticalTopic;
import com.training.apparatus.data.repo.TheoreticalTextRepository;
import com.training.apparatus.data.repo.TheoreticalTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TheoreticalTopicService {
    @Autowired
    TheoreticalTopicRepository theoreticalTopicRepository;
    @Autowired
    TheoreticalTextRepository theoreticalTextRepository;

    @Autowired
    EntityManager em;

    public List<TheoreticalTopic> getTheoreticalTopics() {
        return theoreticalTopicRepository.findAll();
    }

    public Integer getCountPageByIdTopic(Long id) {
        return theoreticalTextRepository.getCountPageById(id);
    }

    public String viewPage(Long topicId, Integer numberPage) {
        if(theoreticalTextRepository.existsByNumberPageAndTopicId(topicId, numberPage) == 1)
            return theoreticalTextRepository.findTextByTopicIdAndNumberPage(topicId, numberPage);
        else
            return "Тут ещё ничего не написано";
    }

    public TheoreticalTopic findTopicByName(String topicName) {
        return theoreticalTopicRepository.findByNameTopic(topicName);
    }

    @Transactional
    public void saveTextByTopicsAndPage(Long id, Integer numberPage, String text) {
        TheoreticalText theoreticalText = theoreticalTextRepository.findByTopicAndNumberPage(id, numberPage);
        if(theoreticalText != null) {
            theoreticalText.setText(text);
            theoreticalTextRepository.save(theoreticalText);
            return;
        }
        theoreticalText = new TheoreticalText(text, numberPage);
        TheoreticalTopic topic = theoreticalTopicRepository.findById(id).get();
        theoreticalText.setTopic(topic);
        theoreticalTextRepository.save(theoreticalText);
    }
}
