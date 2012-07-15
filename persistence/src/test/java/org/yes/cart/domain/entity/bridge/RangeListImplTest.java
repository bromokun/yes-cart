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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;
import org.yes.cart.domain.misc.navigation.range.RangeList;
import org.yes.cart.domain.misc.navigation.range.impl.RangeListImpl;
import org.yes.cart.domain.misc.navigation.range.impl.RangeNodeImpl;

import static org.junit.Assert.assertNotNull;

/**
 * This test just prove, that we can serialize / deserialize {@link RangeListImpl}.
 * <p/>
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 08-May-2011
 * Time: 11:12:54
 */
public class RangeListImplTest {

    private XStream getXStream() {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias(RangeList.RANGE_NODE_ALIAS, RangeNodeImpl.class);
        xStream.alias(RangeList.RANGE_LIST_ALIAS, RangeListImpl.class);
        return xStream;
    }

    /**
     * <rangeList serialization="custom">
     * <unserializable-parents/>
     * <list>
     * <default>
     * <size>3</size>
     * </default>
     * <int>10</int>
     * <range>
     * <range>
     * <first class="string">a</first>
     * <second class="string">c</second>
     * </range>
     * </range>
     * <range>
     * <range>
     * <first class="string">d</first>
     * <second class="string">f</second>
     * </range>
     * </range>
     * <range>
     * <range>
     * <first class="string">g</first>
     * <second class="string">z</second>
     * </range>
     * </range>
     * </list>
     * </rangeList>
     */
    @Test
    public void testXmlSerialization() {
        RangeList rangeList = new RangeListImpl();
        rangeList.add(new RangeNodeImpl("0.001", "0.999"));
        rangeList.add(new RangeNodeImpl("1.000", "1.999"));
        rangeList.add(new RangeNodeImpl("2.000", "2.999"));
        rangeList.add(new RangeNodeImpl("3.000", "3.999"));
        rangeList.add(new RangeNodeImpl("4.000", "4.999"));
        rangeList.add(new RangeNodeImpl("5.000", "5.999"));
        rangeList.add(new RangeNodeImpl("6.000", "6.999"));
        rangeList.add(new RangeNodeImpl("7.000", "7.999"));
        String result = getXStream().toXML(rangeList);
        assertNotNull(result);
        rangeList = (RangeList) getXStream().fromXML(result);
    }
}
