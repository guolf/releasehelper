package com.guolf.releasehelper.service.app;

import com.guolf.releasehelper.repository.VersionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import com.guolf.releasehelper.entity.Version;

import java.util.List;
import java.util.Map;

/**
 * Created by guolf on 16/7/13.
 */
@Component
@Transactional
public class VersionService {

    @Autowired
    private VersionDao versionDao;

    public Version save(Version version) {
        return versionDao.save(version);
    }

    public List<Version> getAll() {
        return (List<Version>) versionDao.findAll();
    }

    public void delete(Long id){
        versionDao.delete(id);
    }

    public Version findByAppId(Long id) {
        return versionDao.findByAppId(id) == null && versionDao.findByAppId(id).size()>0 ?
                null :
                versionDao.findByAppId(id).get(versionDao.findByAppId(id).size()-1);
    }

    public Page<Version> getAppVersion(Long appId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                       String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Version> spec = buildSpecification(appId, searchParams);

        return versionDao.findAll(spec, pageRequest);
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("version_code".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "version_code");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Version> buildSpecification(Long appId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("app.id", new SearchFilter("app.id", SearchFilter.Operator.EQ, appId));
        Specification<Version> spec = DynamicSpecifications.bySearchFilter(filters.values(), Version.class);
        return spec;
    }


    public Version getById(Long id){
        return versionDao.findOne(id);
    }
}
