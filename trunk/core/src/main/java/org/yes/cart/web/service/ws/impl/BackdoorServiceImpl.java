package org.yes.cart.web.service.ws.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yes.cart.dao.GenericDAO;
import org.yes.cart.domain.query.impl.AsIsAnalyzer;
import org.yes.cart.service.domain.ProductService;
import org.yes.cart.util.ShopCodeContext;
import org.yes.cart.utils.impl.ObjectUtil;
import org.yes.cart.web.service.ws.BackdoorService;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 1/28/12
 * Time: 10:02 AM
 */
@WebService(endpointInterface = "org.yes.cart.web.service.ws.BackdoorService",
        serviceName = "BackdoorService")
public class BackdoorServiceImpl implements BackdoorService {

    private static final long serialVersionUID = 20120129L;

    private static final Logger LOG = LoggerFactory.getLogger(ShopCodeContext.getShopCode());

    private ProductService productService;



    /**
     * {@inheritDoc}
     */
    public int reindexAllProducts() {
        return  productService.reindexProducts();
    }

    /**
     * {@inheritDoc}
     */
    public int reindexProduct(long productPk) {
        return productService.reindexProduct(productPk);
    }

    /**
     * {@inheritDoc}
     */
    public int reindexProducts( long[] productPks) {
        int rez = 0;
        for (long pk : productPks) {
            rez += productService.reindexProduct(pk);
        }
        return rez;
    }

    /**
     * {@inheritDoc}
     */
    public List<Object[]> sqlQuery(final String query) {

        if (StringUtils.isNotBlank(query)) {

            if (query.toLowerCase().contains("select ")) {

                return getGenericDao().executeNativeQuery(query);

            } else {

                return Collections.singletonList(new Object[] {getGenericDao().executeNativeUpdate(query)});

            }
        }

        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public List<Object[]> hsqlQuery(final String query) {

        if (StringUtils.isNotBlank(query)) {

            if (query.toLowerCase().contains("select ")) {

                final List queryRez = getGenericDao().executeHsqlQuery(query);

                return transformTypedResultListToArrayList(queryRez);

            } else {

                return Collections.singletonList(new Object[] {getGenericDao().executeHsqlQuery(query)});

            }
        }

        return Collections.EMPTY_LIST;

    }

    private List<Object[]> transformTypedResultListToArrayList(List queryRez) {

        final List<Object[]> rezList = new ArrayList<Object[]>(queryRez.size());

        for( Object obj : queryRez) {

            rezList.add(ObjectUtil.toObjectArray(obj));

        }
        return rezList;
    }



    /**
     * {@inheritDoc}
     */
    public List<Object[]> luceneQuery(final String luceneQuery) {

        final QueryParser queryParser = new QueryParser(Version.LUCENE_31, "", new AsIsAnalyzer());

        try {

            final Query query = queryParser.parse(luceneQuery);

            return transformTypedResultListToArrayList(getGenericDao().fullTextSearch(query));

        } catch (ParseException e) {

            final String msg = "Cant parse query : " + luceneQuery +  " Error : " + e.getMessage();

            LOG.warn(msg);

            return Collections.singletonList(new Object[] {msg});

        }

    }

    /**
     * IoC. Set product service.
     *
     * @param productService product service to use.
     */
    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }




    private GenericDAO<Object, Long> getGenericDao() {
        return productService.getGenericDao();
    }




}
