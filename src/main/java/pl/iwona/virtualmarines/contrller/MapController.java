package pl.iwona.virtualmarines.contrller;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.iwona.virtualmarines.service.TrackReadingService;
import pl.iwona.virtualmarines.service.TrackService;


@Controller
public class MapController {

    private final TrackService trackService;
    private final TrackReadingService trackReadingService;

    public MapController(TrackService trackService, TrackReadingService trackReadingService) {
        this.trackService = trackService;
        this.trackReadingService = trackReadingService;
    }


    @GetMapping
    public String getMap(Model model) {
        model.addAttribute("tracks", trackService.getTracks());
        return "map";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveTime() {
        trackReadingService.savePoint(trackService.getTracks());

    }

}
