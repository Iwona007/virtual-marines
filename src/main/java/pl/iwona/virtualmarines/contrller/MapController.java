package pl.iwona.virtualmarines.contrller;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.iwona.virtualmarines.service.TrackReadingService;

/**
 * Controller
 */
@Controller
public class MapController {

    private final TrackReadingService trackReadingService;

    /**
     * Constructor with one parameter which is autowired by constructor
     * @param trackReadingService trackReadingService
     */
    public MapController(TrackReadingService trackReadingService) {
        this.trackReadingService = trackReadingService;
    }

    /**
     * Method Get which get all Tracks and put them into map
     * @param model for Track
     * @return map.html template with map
     */
    @GetMapping
    public String getMap(Model model) {
        model.addAttribute("tracks", trackReadingService.getPoints());
        return "map";
    }

    /**
     * Save all Tracks into database.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void saveTracks() {
        trackReadingService.savePoint();
    }
}
