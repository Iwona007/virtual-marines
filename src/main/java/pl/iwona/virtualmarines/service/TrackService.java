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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrackService {

    RestTemplate restTemplate = new RestTemplate();

    public List<PointDatabase> getTracks() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " +
   "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2Mzg2MjkxNTksImV4cCI6MTYzODYzMjc1OSwiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhcGkiLCJjbGllbnRfaWQiOiJSZWVzZWFyY2g5NUBnbWFpbC5jb206SXdvbmEiLCJzY29wZSI6WyJhcGkiXX0.ZQqPQjaq3KOgcpx5fjJrdawOfIZPzSPTVW_I1h-KHeBrBgztUyOKJfKQb5Rb6X_gC8RXSPEZANgGptfQg1tr_jwNNmG7KBYf0wxadXPlJUmt1xohbr_pVcix38-Ybj_zr_v06KFsT3JS3M3AuZGIrc9LkpRQmncAdsXUjXh6ZQN-hFIwQ_akPds_BxlzUhu6Cs-KyqNKoY6ft6T0MRKoYM7N4vpE_nAG53NEYMs9Gsad_NYsQBYminaFsOynoFnVDzjKyhX7ZDFxPDXhGZAObEMpUSk6_JYsqP_FYti9OQ84rgOmOy0-wVJE2lVdH1ENDIU72B_-YqSfLZeKnZfjbv9dcv0uuR_sqxny8OoCVv2wthOhPjYxFgyEH9W1txGne34Ym_XVdoGL0zmMh-aBT_YjQeq5FwwmQTFaBN5tqGRPo2EOccmxR2dLq3o0fDxMUlt5N0c5byvtV8ZkTWiOUutsQxS_YpWBu-FKjMTXr_avYmFLGUNFzJLjlC_ztmGvNmgPUwuwZDVRB76cgBI4fL2sc8Ix9ShnfNqenFZPLNdHRdVTHLikp_Mydc33t20B6Xy_iELydPeDCeWFmji6IwXrLMnv9Awkti4C6djL8AluOmEo1yn0HDuO1tFKy3tW5Bk0-12qfDM6EckHpfTiWn2h-l7loFtkBDIuw4w766g");



        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<Track[]> exchange = restTemplate.exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=10.09094&Xmax=10.67047&Ymin=63.3989&Ymax=63.58645",
                HttpMethod.GET,
                httpEntity,
                Track[].class);

        List<PointDatabase> collect = Stream.of(exchange.getBody()).map(
                track -> new PointDatabase(
                        track.getGeometry().getCoordinates().get(0),
                        track.getGeometry().getCoordinates().get(1),
                        track.getName(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLatitude(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLongitude()
                )).collect(Collectors.toList());
        return collect;
    }


    public Datum getDestination(String destinantionName, List<Double> coordinates) {
        try {
            String url = "http://api.positionstack.com/v1/forward?access_key=a033619bd22a4994324bfb4e02173a26&query=" + destinantionName;
            JsonNode datum = restTemplate.getForObject(url, JsonNode.class).get("data").get(0);
            double latitude = datum.get("latitude").asDouble();
            double longitude = datum.get("longitude").asDouble();
            return new Datum(latitude, longitude);

        } catch (Exception ex) {
            return new Datum(coordinates.get(1), coordinates.get(0));
        }
    }
}
