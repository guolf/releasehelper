package releasehelper.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Clock;
import releasehelper.entity.App;
import releasehelper.repository.AppDao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by guolf on 16/7/13.
 */
@Component
@Transactional
public class AppService {

    private AppDao appDao;
    private Clock clock = Clock.DEFAULT;


    @Autowired
    public void setAppDao(AppDao appDao) {
        this.appDao = appDao;
    }

    public List<App> getAllApp() {
        return (List<App>) appDao.findAll();
    }

    public App saveApp(App app) {
        System.out.printf("app:" + app.getId());
        if (app.getId() != null) {
            app.setUpdateDate(clock.getCurrentDate());
        } else {
            app.setUuid(UUID.randomUUID().toString());
            app.setCreateDate(clock.getCurrentDate());
        }
        return appDao.save(app);
    }

    public App getOne(Long id) {
        return appDao.findOne(id);
    }

    public Page<App> getPageApp(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<App> spec = buildSpecification(searchParams);

        return appDao.findAll(spec, pageRequest);
    }

    public void deleteById(Long id) {
        appDao.delete(id);
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("title".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "title");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<App> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//        filters.put("user.id", new SearchFilter("user.id", SearchFilter.Operator.EQ, userId));
        Specification<App> spec = DynamicSpecifications.bySearchFilter(filters.values(), App.class);
        return spec;
    }


    public App findByUuid(String uuid) {
        return appDao.findByUuid(uuid);
    }

}
