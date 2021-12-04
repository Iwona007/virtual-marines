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
"eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2Mzg2NTA1ODYsImV4cCI6MTYzODY1NDE4NiwiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhcGkiLCJjbGllbnRfaWQiOiJSZWVzZWFyY2g5NUBnbWFpbC5jb206SXdvbmEiLCJzY29wZSI6WyJhcGkiXX0.nIsWOXAqe-N81Ad_B46qhSe4vOv4XSknaN-AQfF6oE3zUCGTTTEEDI5LjTDoy123zOuLuGVTmi2z6vl1dAeRNdJZmg6C4d1cDSoYuih8iKTOkJS0fsS7TkiSa0HO4XPHSmwMdx3gbcI_S5Ch6DhMQw53xKKmIPwRKlz6C1hshMgEVQE2nOtMYt4pRrdWtZgSuc0JFj-vhHPZwxA-7MQjiPPVx4aU_vCNJMuk-VYif0guLrdavmw_Op_y7rObGGkOonQA6yJ5dQKxk9c8F8stJI_x2zPVVAtGXasiW72M62F3UxXhfCiNU5jJVRze8vYWDnepB2HHZB55jeXXN_8Ahjd8nuFJEUlZ8FrJC00WkafsrAz885k7LfvAMjaS1QP5jX8adurMgPTwIt6LiA0hSikL36xB_NF3OpsqyYp5C9t_kYxweki6DXHWmuNEv6Sla9QG4US5wIyhqoZ50xKlNM5-bIwC39Bct_EVLclIJ0IbbI0TDmAkBE_1HdqDNF_lLD5-SHsPhTJ_2_q6e1Zytzg1GxVGCTyRsS4kwaB9KWoFH3sGqu42rCJunR5L0eCd8tPvRlp_qjYC2aEJ8Plb0Rnw86hpdUwUFO4_nxKc98K1mC5p62gxSqzNjCndbZ0BjRrSc6dGTREy1FdTrjFnkBt-4Dm2zQ7BfjAccJkq0gw");

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
