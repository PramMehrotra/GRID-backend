package com.pram.riskintel.controller;

import com.pram.riskintel.entity.Incident;
import com.pram.riskintel.repository.IncidentRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/incidents")
@CrossOrigin(origins = "http://localhost:5173") // React frontend origin
public class IncidentController {

    private final IncidentRepository incidentRepository;

    public IncidentController(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    // -------------------------------------------------------
    // 1️⃣ Paginated All Incidents
    // -------------------------------------------------------
    @GetMapping
    public List<Incident> getAllIncidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return incidentRepository.findAll(
                PageRequest.of(page, size)
        ).getContent();
    }

    // -------------------------------------------------------
    // 2️⃣ Filter By Country
    // -------------------------------------------------------
    @GetMapping("/country/{country}")
    public List<Incident> getByCountry(@PathVariable String country) {
        return incidentRepository.findByCountryTxt(country);
    }

    // -------------------------------------------------------
    // 3️⃣ Filter By Region
    // -------------------------------------------------------
    @GetMapping("/region/{region}")
    public List<Incident> getByRegion(@PathVariable String region) {
        return incidentRepository.findByRegionTxt(region);
    }

    // -------------------------------------------------------
    // 4️⃣ Filter By Year
    // -------------------------------------------------------
    @GetMapping("/year/{year}")
    public List<Incident> getByYear(@PathVariable Integer year) {
        return incidentRepository.findByIyear(year);
    }

    // -------------------------------------------------------
    // 5️⃣ Country Incident Count Stats
    // -------------------------------------------------------
    @GetMapping("/stats/country")
    public List<Map<String, Object>> getCountryStats() {

        List<Object[]> results = incidentRepository.countIncidentsByCountry();

        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("country", result[0]);
            map.put("count", result[1]);
            return map;
        }).toList();
    }

    // -------------------------------------------------------
    // 6️⃣ Country Risk Score (Deaths + Wounded)
    // -------------------------------------------------------
    @GetMapping("/stats/risk")
    public List<Map<String, Object>> getRiskStats() {

        List<Object[]> results = incidentRepository.calculateRiskScoreByCountry();

        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("country", result[0]);
            map.put("riskScore", result[1] == null ? 0 : result[1]);
            return map;
        }).toList();
    }

    // -------------------------------------------------------
    // 7️⃣ High-Lethality (Deaths Only)
    // -------------------------------------------------------
    @GetMapping("/high-lethality")
    public List<Incident> getHighLethalityIncidents(
            @RequestParam(defaultValue = "10") Integer minDeaths,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by("nkill").descending()
        );

        return incidentRepository
                .findByNkillGreaterThan(minDeaths, pageable)
                .getContent();
    }

    // -------------------------------------------------------
    // 8️⃣ High-Impact (Deaths + Wounded)
    // -------------------------------------------------------
    @GetMapping("/high-impact")
    public List<Incident> getHighImpactIncidents(
            @RequestParam(defaultValue = "20") Integer threshold) {

        return incidentRepository.findHighImpactIncidents(threshold);
    }

    // -------------------------------------------------------
    // 9️⃣ Heatmap Endpoint (Lat, Lng, Intensity)
    // -------------------------------------------------------
    @GetMapping("/heatmap/{year}")
    public List<Map<String, Object>> getHeatmapByYear(@PathVariable Integer year) {

        List<Object[]> results = incidentRepository.getHeatmapDataByYear(year);

        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("lat", result[0]);
            map.put("lng", result[1]);
            map.put("intensity", result[2] == null ? 0 : result[2]);
            return map;
        }).toList();
    }
}