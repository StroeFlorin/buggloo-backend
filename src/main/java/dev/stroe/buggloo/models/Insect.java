package dev.stroe.buggloo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Represents an insect with detailed biological and ecological information.
 * This model is used for structured responses from the OpenAI API.
 */
public class Insect {

    @JsonPropertyDescription("Indicates if the organism is an insect.")
    @NotNull
    public Boolean isInsect;

    @JsonPropertyDescription("URL to the insect's Wikipedia page.")
    @Pattern(regexp = "^https?://.*", message = "Must be a valid URL")
    public String urlWikipedia;

    // Scientific classification
    @JsonPropertyDescription("The common name of the insect.")
    @Size(max = 100, message = "Common name must not exceed 100 characters")
    public String commonName;

    @JsonPropertyDescription("The scientific (Latin) name of the insect.")
    @Size(max = 100, message = "Scientific name must not exceed 100 characters")
    public String scientificName;

    @JsonPropertyDescription("Alternative names or aliases for the insect.")
    public List<String> alternativeNames;

    @JsonPropertyDescription("The conservation status of the insect (e.g., endangered, least concern).")
    public String conservationStatus;

    // Taxonomy
    @JsonPropertyDescription("The biological domain of the insect (e.g., Eukaryota).")
    public String domain;

    @JsonPropertyDescription("The biological kingdom of the insect (e.g., Animalia).")
    public String kingdom;

    @JsonPropertyDescription("The biological phylum of the insect (e.g., Arthropoda).")
    public String phylum;

    @JsonProperty("class")
    @JsonPropertyDescription("The biological class of the insect (e.g., Insecta).")
    public String clazz;

    @JsonPropertyDescription("The biological order of the insect (e.g., Coleoptera).")
    public String order;

    @JsonPropertyDescription("The biological family of the insect.")
    public String family;

    @JsonPropertyDescription("The biological genus of the insect.")
    public String genus;

    @JsonPropertyDescription("The biological species of the insect. Answer should be in the format Genus species (e.g., Apis mellifera).")
    public String species;

    // Habitat & Range
    @JsonPropertyDescription("The geographic range where the insect is found.")
    public String geographicRange;

    @JsonPropertyDescription("The type of habitat where the insect lives.")
    public String habitatType;

    @JsonPropertyDescription("The seasonal appearance or activity period of the insect.")
    public String seasonalAppearance;

    // Physical Characteristics
    @JsonPropertyDescription("The typical size of the insect (e.g., length or wingspan).")
    public String size;

    @JsonPropertyDescription("List of colors commonly found on the insect, represented as hex codes (e.g., #FF0000 for red).")
    public List<String> colors;

    @JsonPropertyDescription("Indicates if the insect has wings.")
    public Boolean hasWings;

    @JsonPropertyDescription("The number of legs the insect has.")
    public Integer legCount;

    @JsonPropertyDescription("Distinctive markings or features of the insect.")
    public String distinctiveMarkings;

    // Ecology & Behaviour
    @JsonPropertyDescription("The typical diet of the insect.")
    public String diet;

    @JsonPropertyDescription("The time of day the insect is most active (e.g., diurnal, nocturnal).")
    public String activityTime;

    @JsonPropertyDescription("The typical lifespan of the insect.")
    public String lifespan;

    @JsonPropertyDescription("List of known predators of the insect.")
    public List<String> predators;

    @JsonPropertyDescription("List of defense mechanisms used by the insect.")
    public List<String> defenseMechanisms;

    @JsonPropertyDescription("The insect's role in its ecosystem (e.g., pollinator, decomposer).")
    public String roleInEcosystem;

    @JsonPropertyDescription("List of interesting facts about the insect.")
    public List<String> interestingFacts;

    @JsonPropertyDescription("List of species similar to this insect.")
    public List<String> similarSpecies;

    // Conservation & Human Interactions
    @JsonPropertyDescription("Population trend of the insect (e.g., increasing, stable, declining).")
    public String populationTrend;

    @JsonPropertyDescription("Threats to the insect's population (e.g., habitat loss, pesticides, climate change).")
    public List<String> threats;

    @JsonPropertyDescription("Pest status of the insect (e.g., major agricultural pest, minor garden pest, none).")
    public String pestStatus;

    @JsonPropertyDescription("Beneficial status of the insect (e.g., pollinator, natural enemy of pests).")
    public String beneficialStatus;

    @JsonPropertyDescription("Indicates if the insect is venomous (can sting or bite harmful to humans).")
    public Boolean venomous;

    @JsonPropertyDescription("Indicates if the insect is toxic to humans (contains toxins if ingested or on contact).")
    public Boolean toxicToHumans;
}
