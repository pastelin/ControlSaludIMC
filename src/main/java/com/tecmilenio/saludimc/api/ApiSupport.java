package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.dto.ApiRepresentation;
import com.tecmilenio.saludimc.dto.ApiRepresentation.ApiLink;
import java.util.*;

public final class ApiSupport {
    private ApiSupport(){}
    public static Map<String,ApiLink> links(Object... pairs){
        Map<String,ApiLink> m=new LinkedHashMap<>();
        for(int i=0;i<pairs.length;i+=3) m.put((String)pairs[i],ApiRepresentation.link((String)pairs[i+1],(String)pairs[i+2]));
        return m;
    }
    public static <T> ApiRepresentation<T> response(T data,Object... pairs){
        return new ApiRepresentation<>(data,links(pairs));
    }
}
