/*
 * Copyright 2009 Igor Azarnyi, Denys Pavlov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.yes.cart.domain.entity.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.yes.cart.constants.Constants;
import org.yes.cart.domain.entity.SkuPrice;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 *
 * Bridge to product sku price.
 *
* User: Igor Azarny iazarny@yahoo.com
 * Date: 07-May-2011
 * Time: 16:13:01
 * */
public class SkuPriceBridge implements FieldBridge {

    private final DecimalFormat formatter = new DecimalFormat(Constants.MONEY_FORMAT_TOINDEX);

    /** {@inheritDoc} */
    public void set(final String proposedFiledName, final Object value, final Document document, final LuceneOptions luceneOptions) {
        if (value instanceof Collection) {
            for (Object obj : (Collection)value) {
                SkuPrice skuPrice = (SkuPrice) obj;
                if (skuPrice.getQuantity().intValue() == 1) {
                    String rez = objectToString(skuPrice.getShop().getShopId(), skuPrice.getCurrency(), skuPrice.getRegularPrice());
                    Field field = new Field(
                            proposedFiledName,
                            rez,
                            luceneOptions.getStore(),
                            Field.Index.NOT_ANALYZED,
                            luceneOptions.getTermVector()
                    );
                    document.add(field);
                }
            }
        }
    }


    /**
     * Create index value for given shop currency and price.
     * @param shopId shop id
     * @param currency currency code
     * @param regularPrice regular price
     * @return index value in following format shopid_currency_price. All digital value will be left padded according to formatter.
     */
    public String objectToString(final long shopId, final String currency, final BigDecimal regularPrice) {
        StringBuilder stringBuilder = new StringBuilder();
        long price = regularPrice.movePointRight(2).longValue();
        stringBuilder.append(formatter.format(shopId));
        stringBuilder.append('_');
        stringBuilder.append(currency);
        stringBuilder.append('_');
        stringBuilder.append(formatter.format(price));        
        return  stringBuilder.toString();
    }

}
