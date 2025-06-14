package dev.stroe.buggloo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public class Insect {

    @JsonPropertyDescription("Indicates if the organism is an insect.")
    public Boolean is_insect;

    @JsonPropertyDescription("The common name of the insect.")
    public String common_name;

    @JsonPropertyDescription("The scientific (Latin) name of the insect.")
    public String scientific_name;

    @JsonPropertyDescription("Alternative names or aliases for the insect.")
    public List<String> alternative_names;

    @JsonPropertyDescription(
        "The biological domain of the insect (e.g., Eukaryota)."
    )
    public String domain;

    @JsonPropertyDescription(
        "The biological kingdom of the insect (e.g., Animalia)."
    )
    public String kingdom;

    @JsonPropertyDescription(
        "The biological phylum of the insect (e.g., Arthropoda)."
    )
    public String phylum;

    @JsonProperty("class")
    @JsonPropertyDescription(
        "The biological class of the insect (e.g., Insecta)."
    )
    public String clazz;

    @JsonPropertyDescription(
        "The biological order of the insect (e.g., Coleoptera)."
    )
    public String order;

    @JsonPropertyDescription("The biological family of the insect.")
    public String family;

    @JsonPropertyDescription("The biological genus of the insect.")
    public String genus;

    @JsonPropertyDescription("The biological species of the insect. Answer should be in the format Genus species (e.g., Apis mellifera).")
    public String species;

    @JsonPropertyDescription("The geographic range where the insect is found.")
    public String geographic_range;

    @JsonPropertyDescription("The type of habitat where the insect lives.")
    public String habitat_type;

    @JsonPropertyDescription(
        "The seasonal appearance or activity period of the insect."
    )
    public String seasonal_appearance;

    @JsonPropertyDescription(
        "The typical size of the insect (e.g., length or wingspan)."
    )
    public String size;

    @JsonPropertyDescription(
        "List of colors commonly found on the insect, represented as hex codes (e.g., #FF0000 for red)."
    )
    public List<String> colors;

    @JsonPropertyDescription("Indicates if the insect has wings.")
    public boolean has_wings;

    @JsonPropertyDescription("The number of legs the insect has.")
    public int leg_count;

    @JsonPropertyDescription("Distinctive markings or features of the insect.")
    public String distinctive_markings;

    @JsonPropertyDescription("The typical diet of the insect.")
    public String diet;

    @JsonPropertyDescription(
        "The time of day the insect is most active (e.g., diurnal, nocturnal)."
    )
    public String activity_time;

    @JsonPropertyDescription("The typical lifespan of the insect.")
    public String lifespan;

    @JsonPropertyDescription("List of known predators of the insect.")
    public List<String> predators;

    @JsonPropertyDescription("List of defense mechanisms used by the insect.")
    public List<String> defense_mechanisms;

    @JsonPropertyDescription(
        "The insect's role in its ecosystem (e.g., pollinator, decomposer)."
    )
    public String role_in_ecosystem;

    @JsonPropertyDescription("List of interesting facts about the insect.")
    public List<String> interesting_facts;

    @JsonPropertyDescription("List of species similar to this insect.")
    public List<String> similar_species;

    @JsonPropertyDescription(
        "The conservation status of the insect (e.g., endangered, least concern)."
    )
    public String conservation_status;

        @JsonPropertyDescription("URL to the insect's Wikipedia page.")
    public String url_wikipedia;

    @JsonPropertyDescription("Population trend of the insect (e.g., increasing, stable, declining).")
    public String population_trend;

    @JsonPropertyDescription("Threats to the insect's population (e.g., habitat loss, pesticides, climate change).")
    public List<String> threats;

    @JsonPropertyDescription("Pest status of the insect (e.g., major agricultural pest, minor garden pest, none).")
    public String pest_status;

    @JsonPropertyDescription("Beneficial status of the insect (e.g., pollinator, natural enemy of pests).")
    public String beneficial_status;

    @JsonPropertyDescription("Indicates if the insect is venomous (can sting or bite harmful to humans).")
    public Boolean venomous;

    @JsonPropertyDescription("Indicates if the insect is toxic to humans (contains toxins if ingested or on contact).")
    public Boolean toxic_to_humans;



 



}
