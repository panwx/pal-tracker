package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {
    private final String port;
    private final String memoryLimit;
    private final String cfInstanceIndex;
    private final String cfInstanceAddr;


    public EnvController(@Value("${PORT:NOT SET}") String port,@Value("${MEMORY.LIMIT:NOT SET}") String memoryLimit,@Value("${CF.INSTANCE.INDEX:NOT SET}") String cfInstanceIndex,@Value("${CF.INSTANCE.ADDR:NOT SET}") String cfInstanceAddr){
        this.port = port;
        this.memoryLimit = memoryLimit;
        this.cfInstanceIndex = cfInstanceIndex;
        this.cfInstanceAddr = cfInstanceAddr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){
        Map<String, String> map = new HashMap<String, String>();
                map.put("PORT",port);
        map.put("MEMORY_LIMIT",memoryLimit);
        map.put("CF_INSTANCE_INDEX",cfInstanceIndex);
        map.put("CF_INSTANCE_ADDR",cfInstanceAddr);

        return map;
    }
}
