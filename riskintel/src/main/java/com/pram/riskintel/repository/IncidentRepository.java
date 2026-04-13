package com.pram.riskintel.repository;

import com.pram.riskintel.entity.Incident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // -------------------------------------------------------
    // 1️⃣ Basic Filters
    // -------------------------------------------------------
    List<Incident> findByCountryTxt(String countryTxt);

    List<Incident> findByRegionTxt(String regionTxt);

    List<Incident> findByIyear(Integer iyear);


    // -------------------------------------------------------
    // 2️⃣ Country Incident Count (Frequency)
    // -------------------------------------------------------
    @Query("""
           SELECT i.countryTxt, COUNT(i)
           FROM Incident i
           GROUP BY i.countryTxt
           ORDER BY COUNT(i) DESC
           """)
    List<Object[]> countIncidentsByCountry();


    // -------------------------------------------------------
    // 3️⃣ Country Risk Score (Deaths + Wounded)
    // -------------------------------------------------------
    @Query("""
           SELECT i.countryTxt,
                  SUM(COALESCE(i.nkill,0) + COALESCE(i.nwound,0))
           FROM Incident i
           GROUP BY i.countryTxt
           ORDER BY SUM(COALESCE(i.nkill,0) + COALESCE(i.nwound,0)) DESC
           """)
    List<Object[]> calculateRiskScoreByCountry();


    // -------------------------------------------------------
    // 4️⃣ Risk Trend By Year
    // -------------------------------------------------------
    @Query("""
           SELECT i.iyear,
                  SUM(COALESCE(i.nkill,0) + COALESCE(i.nwound,0))
           FROM Incident i
           GROUP BY i.iyear
           ORDER BY i.iyear ASC
           """)
    List<Object[]> calculateRiskByYear();


    // -------------------------------------------------------
    // 5️⃣ Risk By Region
    // -------------------------------------------------------
    @Query("""
           SELECT i.regionTxt,
                  SUM(COALESCE(i.nkill,0) + COALESCE(i.nwound,0))
           FROM Incident i
           GROUP BY i.regionTxt
           ORDER BY SUM(COALESCE(i.nkill,0) + COALESCE(i.nwound,0)) DESC
           """)
    List<Object[]> calculateRiskByRegion();


    // -------------------------------------------------------
    // 6️⃣ High-Lethality (Deaths Only, Paginated)
    // -------------------------------------------------------
    Page<Incident> findByNkillGreaterThan(Integer nkill, Pageable pageable);


    // -------------------------------------------------------
    // 7️⃣ High-Impact (Deaths + Wounded)
    // -------------------------------------------------------
    @Query("""
           SELECT i FROM Incident i
           WHERE (COALESCE(i.nkill,0) + COALESCE(i.nwound,0)) > :threshold
           ORDER BY (COALESCE(i.nkill,0) + COALESCE(i.nwound,0)) DESC
           """)
    List<Incident> findHighImpactIncidents(@Param("threshold") Integer threshold);

    @Query("""
       SELECT i.latitude,
              i.longitude,
              (COALESCE(i.nkill,0) + COALESCE(i.nwound,0))
       FROM Incident i
       WHERE i.iyear = :year
         AND i.latitude IS NOT NULL
         AND i.longitude IS NOT NULL
       """)
    List<Object[]> getHeatmapDataByYear(@Param("year") Integer year);


}
