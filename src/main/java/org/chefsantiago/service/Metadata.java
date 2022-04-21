package org.chefsantiago.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Metadata(String postTitle, LocalDate postDate, String excerpt, String banner) {
}
