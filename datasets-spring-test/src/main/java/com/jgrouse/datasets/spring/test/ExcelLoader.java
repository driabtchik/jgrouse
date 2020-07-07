package com.jgrouse.datasets.spring.test;

import com.jgrouse.datasets.input.poi.PoiInputDataSetGroupFactory;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

public class ExcelLoader {
    private final ApplicationContext applicationContext;
    private final PoiInputDataSetGroupFactory datasetGroupFactory;


    public ExcelLoader(final ApplicationContext applicationContext, final PoiInputDataSetGroupFactory poiInputDataSetGroupFactory) {
        this.applicationContext = applicationContext;
        this.datasetGroupFactory = poiInputDataSetGroupFactory;
    }

    void loadExcel(final ExcelLoadingRequest request) {
        throw new UnsupportedOperationException("fix me");
        /*
        PoiInputDataSetGroup dataSetGroup = datasetGroupFactory.create(() -> applicationContext.getResource(request.getWorkBookResourceName()).getInputStream());
        List<String> dataSetNames = dataSetGroup.getDataSetNames();

        List<PoiSheetInputDataSet> datasets = dataSetNames.stream().map(dataSetGroup::get).collect(Collectors.toList());
        DataSource dataSource = getDataSource(request);
        JdbcOutputDataSetGroup jdbcOutputDataSetGroup = new JdbcOutputDataSetGroup(dataSource);
        jdbcOutputDataSetGroup.save(datasets);

         */
    }

    private DataSource getDataSource(final ExcelLoadingRequest request) {
        throw new UnsupportedOperationException("fix me");
//        String datasourceName = request.getMergedSqlConfig() == null || StringUtils.isEmpty(request.getMergedSqlConfig().getDatasourceName())? null : request.getMergedSqlConfig().getDatasourceName();
//        return TestContextTransactionUtils.retrieveDataSource(context, datasourceName);
    }


}
