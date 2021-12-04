package pl.iwona.virtualmarines.service;


import org.springframework.stereotype.Service;
import pl.iwona.virtualmarines.model.PointDatabase;
import pl.iwona.virtualmarines.repository.PointRepository;


;import java.util.List;

@Service
public class TrackReadingService {

    private PointRepository pointRepository;
    private TrackService trackService;

    public TrackReadingService(PointRepository pointRepository, TrackService trackService) {
        this.pointRepository = pointRepository;
        this.trackService = trackService;
    }

    public void savePoint(List<PointDatabase> pointDatabase) {
        pointRepository.saveAll(trackService.getTracks());
    }

    public List<PointDatabase> getPoints() {
      return  trackService.getTracks();
    }
}
