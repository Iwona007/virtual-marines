package pl.iwona.virtualmarines.service;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.iwona.virtualmarines.model.Datum;
import pl.iwona.virtualmarines.model.PointDatabase;
import pl.iwona.virtualmarines.model.Track;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service where application connect with open Api
 */
@Service
public class TrackService {

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Getter for restTemplate
     * @return restTemplate
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * Get all crafts with data such as: name, starting point and destination. Authorization token is valid only by 1 hour.
     *
     * @return collection Of Crafts.
     */
    public List<PointDatabase> getTracks() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " +
"eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2Mzg2Mzk1MzYsImV4cCI6MTYzODY0MzEzNiwiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhcGkiLCJjbGllbnRfaWQiOiJSZWVzZWFyY2g5NUBnbWFpbC5jb206SXdvbmEiLCJzY29wZSI6WyJhcGkiXX0.Ed6lROKPEgmUc_7JgmTbjshDRo2DDWSyvFUyKdBsSl3F2SFuBXMrZ1IBFyPSCRZvOHnag3jmLD6UafTuLvV6SqKEkA1hGUgCQyPO3CE9zTCXIq1XEiQKh9l96OaLFoV5kALqSqvoupDdYSuvHirq3M5tcHcJINwOIBfkhAvg9RAywHr8CzLvw3hRZIILPR_-tJuGgOvZqB9dbJ61VXScCQ6ukchUv9kOvsLDFkyLI9Mhr_WVZG5I-1AZ_0V2yI6V2Dl-oQCwaGSpgqHz3AhILzer2qpfl7aLicSbC268hhKH-0VgESffKZj7TsO67Ww88qZqX7u5BqchJj9exT-hdO1mmvU6WTAhBhgeFip9XtP_63kGy8z_5pOs1ikIRQRJmayOoTSA3-z6moBwxH1nFCvea95FaDkLs0jpC1u1UGPpwMsHA8vzM0RoiQQPRSWlPKn76GxtO6wxfiyWbazZyPR8dvlYAnLvuK1YPbltLXmywVpXA2silre9VeUPi-Wgw-R8UdylWLVHvEdfJyNhoy2_WcuEeNlIDIiuOjQqo8jw57iwq4mTZ3Eha1UVEbP61HWIINWGCRGXgUJdLZ45qTN3eVraOZSDhJkU7kxRtoCZ90O-GmdPOGngbjNLoasux0YNtcaAee0UN-0fox1XVrucvrf8s87ZmjXy7_JDADs");

        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Track[]> exchange = getRestTemplate().exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=10.09094&Xmax=10.67047&Ymin=63.3989&Ymax=63.58645",
                HttpMethod.GET,
                httpEntity,
                Track[].class);

        return Stream.of(Objects.requireNonNull(exchange.getBody())).map(
                track -> new PointDatabase(
                        track.getGeometry().getCoordinates().get(0),
                        track.getGeometry().getCoordinates().get(1),
                        track.getName(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLatitude(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLongitude()
                )).collect(Collectors.toList());
    }


    private Datum getDestination(String destinantionName, List<Double> coordinates) {
        try {
            String url = "http://api.positionstack.com/v1/forward?access_key=a033619bd22a4994324bfb4e02173a26&query=" + destinantionName;
            JsonNode datum = Objects.requireNonNull(getRestTemplate().getForObject(url, JsonNode.class)).get("data").get(0);
            double latitude = datum.get("latitude").asDouble();
            double longitude = datum.get("longitude").asDouble();
            return new Datum(latitude, longitude);

        } catch (Exception ex) {
            return new Datum(coordinates.get(1), coordinates.get(0));
        }
    }
}
