package org.chefsantiago.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    String postTitle;
    LocalDate postDate;
    String excerpt;
    String banner;
}
