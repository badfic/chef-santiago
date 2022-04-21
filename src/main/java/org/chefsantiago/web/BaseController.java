package org.chefsantiago.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected Map<String, Object> commonProps() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageTitle", "Chef Santiago"); // default
        map.put("currentYear", LocalDateTime.now().getYear());
        return map;
    }

}
