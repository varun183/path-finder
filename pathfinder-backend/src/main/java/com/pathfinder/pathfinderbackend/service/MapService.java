package com.pathfinder.pathfinderbackend.service;



import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    @Transactional(readOnly = true)
    public List<Map> getAllMaps() {
        return mapRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Map> getMapById(Long id) {
        return mapRepository.findById(id);
    }

    @Transactional
    public Map createMap(String name, String description, int width, int height) {
        Map map = Map.builder()
                .name(name)
                .description(description)
                .width(width)
                .height(height)
                .build();

        return mapRepository.save(map);
    }

    @Transactional
    public void deleteMap(Long id) {
        mapRepository.deleteById(id);
    }
}
