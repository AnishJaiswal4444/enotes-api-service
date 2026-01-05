package Enotes_API_Service.service;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.List;

public interface CacheManagerService {

    public Collection<String> getCache();

    public Cache getCacheName(String cacheName);

    public void removeAllCache();

    public void removeCacheByName(List<String> cacheNames);
}
