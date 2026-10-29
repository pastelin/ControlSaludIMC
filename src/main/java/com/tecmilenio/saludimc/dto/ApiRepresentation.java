package com.tecmilenio.saludimc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/** Hipermedia simple: los enlaces permiten descubrir las operaciones disponibles. */
public record ApiRepresentation<T>(T data, @JsonProperty("_links") Map<String, ApiLink> links) {
    public record ApiLink(String href, String method) {}
    public static ApiLink link(String href,String method){return new ApiLink(href,method);}
}
