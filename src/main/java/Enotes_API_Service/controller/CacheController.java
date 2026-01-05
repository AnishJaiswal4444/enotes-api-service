package Enotes_API_Service.controller;

import Enotes_API_Service.endpoint.CacheEndpoint;
import Enotes_API_Service.service.CacheManagerService;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class CacheController implements CacheEndpoint {

    @Autowired
    private CacheManagerService cacheService;

    @Override
    public ResponseEntity<?> getAllCache() {
        Collection<String> cache = cacheService.getCache();
        return CommonUtil.createBuildResponse(cache, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCache(String cache_name) {
        Cache cacheName = cacheService.getCacheName(cache_name);
        return CommonUtil.createBuildResponse(cacheName, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeAllCache() {
        cacheService.removeAllCache();
        return CommonUtil.createBuildResponseMessage("Removed all cache", HttpStatus.OK);
    }
}
