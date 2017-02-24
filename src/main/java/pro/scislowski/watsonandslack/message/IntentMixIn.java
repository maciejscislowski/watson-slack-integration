package pro.scislowski.watsonandslack.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Maciej.Scislowski@gmail.com
 */
abstract class IntentMixIn {

    IntentMixIn(@JsonProperty("intent") String intent, @JsonProperty("confidence") Double confidence) {
    }

    @JsonProperty("intent")
    abstract String getIntent();

    @JsonProperty("confidence")
    abstract int getConfidence();

}