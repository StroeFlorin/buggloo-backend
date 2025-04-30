package dev.stroe.buggloo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsectInfoDto {
    public String common_name;
    public String scientific_name;
    public List<String> alternative_names;
    public String domain;
    public String kingdom;
    public String phylum;
    @JsonProperty("class")
    public String clazz; // 'class' is a reserved word in Java
    public String order;
    public String family;
    public String genus;
    public String species;
    public String geographic_range;
    public String habitat_type;
    public String seasonal_appearance;
    public String size;
    public List<String> colors;
    public boolean has_wings;
    public int leg_count;
    public String distinctive_markings;
    public String diet;
    public String activity_time;
    public String lifespan;
    public List<String> predators;
    public List<String> defense_mechanisms;
    public String role_in_ecosystem;
    public List<String> interesting_facts;
    public List<String> similar_species;
    public String conservation_status;
    public String url_wikipedia;
}
