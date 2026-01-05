package Enotes_API_Service.endpoint;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Caching", description = "Caching APIs")
@RequestMapping("/api/v1/cache")
public interface CacheEndpoint {

    @GetMapping("/")
    public ResponseEntity<?> getAllCache();

    @GetMapping("/{cache_name}")
    public ResponseEntity<?> getCache(@PathVariable String cache_name);

    @DeleteMapping("/")
    public ResponseEntity<?> removeAllCache();
}
